package org.redrune.game.content.action.combat.player.registry;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.region.RegionManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public interface BowFireEvent extends CombatRegistryEvent {
	
	/**
	 * The names of the bows that can use this event
	 */
	String[] bowNames();
	
	/**
	 * Fires the bow
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id used
	 * @param ammoId
	 * 		The ammo id used
	 */
	void fire(Player attacker, org.redrune.game.node.entity.Entity target, CombatTypeSwing swing, int weaponId, int ammoId);
	
	/**
	 * Drops ammo on the ground
	 *
	 * @param player
	 * 		The player
	 * @param location
	 * 		The location of the target
	 * @param ammoSlot
	 * 		The slot of the ammo
	 * @param ammoId
	 * 		The id of the ammo to use
	 * @param delete
	 * 		If the ammo should just be deleted and not sent to the floor
	 */
	default void dropAmmo(Player player, Location location, int ammoSlot, int ammoId, boolean delete) {
		Item ammo = player.getEquipment().getItem(ammoSlot);
		// no ammo here, safe check [this shouldn't happen anyway]
		if (ammo == null || ammo.getId() != ammoId) {
			return;
		}
		// the new amount to set
		int newAmount = ammo.getAmount() - 1;
		// if we should remove the item from the equipment
		final boolean removed = newAmount <= 0;
		// removes the ammo from the equipment
		player.getEquipment().getItems().set(ammoSlot, removed ? null : new Item(ammoId, newAmount));
		// we aren't deleting so it must be dropped on ground...
		if (!delete) {
			RegionManager.addFloorItem(ammoId, 1, 180, location, player.getDetails().getUsername());
		}
		player.getEquipment().refresh(ammoSlot);
		// if the item is removed
		if (removed) {
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}
	
	/**
	 * Sends the block emote 1 tick before hit appears
	 *
	 * @param target
	 * 		The target
	 * @param delay
	 * 		The delay
	 */
	// TODO: npc block emote
	default void sendBlockEmote(org.redrune.game.node.entity.Entity target, int delay) {
		SystemManager.getScheduler().schedule(new ScheduledTask(delay - 1, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> target.sendAwaitedAnimation(target.isPlayer() ? StaticCombatFormulae.getDefenceEmote(target.toPlayer()) : -1);
			}
		});
	}
	
	/**
	 * Sends the damage to the target
	 *
	 * @param attacker
	 * 		The attacker
	 * @param target
	 * 		The target
	 * @param swing
	 * 		The swing
	 * @param weaponId
	 * 		The weapon id
	 */
	default void sendDamage(Player attacker, org.redrune.game.node.entity.Entity target, CombatTypeSwing swing, int weaponId) {
		final int style = attacker.getCombatDefinitions().getAttackStyle();
		final int delay = swing.getProjectileDelay(attacker, target);
		final double maxHit = swing.getMaxHit(attacker, weaponId, style, 1);
		final int damage = swing.randomizeHit(maxHit, swing.getAttackBonus(attacker, weaponId, style), swing.getDefenceBonus(target, weaponId, style));
		
		// hit, ammo, defend
		swing.applyHit(attacker, target, new Hit(attacker, damage, HitSplat.RANGE_DAMAGE).setMaxHit(maxHit), weaponId, style, delay);
		sendBlockEmote(target, delay);
	}
	
}
