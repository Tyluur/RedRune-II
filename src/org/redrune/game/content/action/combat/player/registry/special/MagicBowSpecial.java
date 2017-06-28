package org.redrune.game.content.action.combat.player.registry.special;

import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.content.action.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerEquipment;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class MagicBowSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("magic *bow");
	}
	
	@Override
	public double accuracyIncrease() {
		return 1.15;
	}
	
	@Override
	public int energyRequired() {
		return 55;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		// we should always be on this mode
		if (!(swing instanceof RangeCombatSwing)) {
			return;
		}
		// bonus calculations
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, accuracyIncrease());
		// projectile delay
		final int delay = swing.getProjectileDelay(player, target);
		
		player.sendAnimation(1074);
		
		// the first hit
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.RANGE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, delay);
		// second hit
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.RANGE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, delay);
		
		int speed = (int) (27.0D + player.getLocation().getDistance(target.getLocation()) * 5.0D);
		player.getRegion().sendProjectile(new Projectile(player, target, 249, 41, 36, 20, speed, 15, 0));
		speed = (int) (20.0D + player.getLocation().getDistance(target.getLocation()) * 10.0D);
		player.getRegion().sendProjectile(new Projectile(player, target, 249, 41, 36, 40, speed, 10, 0));
		
		// the range swing type
		RangeCombatSwing range = (RangeCombatSwing) swing;
		
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS), true);
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS), true);
	}
}
