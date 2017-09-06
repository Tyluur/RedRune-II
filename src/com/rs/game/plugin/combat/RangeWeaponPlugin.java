package com.rs.game.plugin.combat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.Plugin;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public abstract class RangeWeaponPlugin extends Plugin {
	
	/**
	 * The pattern-matching names of weapons that will use this plugin
	 */
	public abstract String[] getWeaponNames();
	
	/**
	 * Handling the firing of the weapon
	 */
	public abstract void fire(Player source, Actor target, AbstractCombatStyle style);
	
	@Override
	public void register() {
		PluginRepository.register(this, getWeaponNames());
	}
	
	/**
	 * Handles the dropping of ammo
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param quantity
	 * 		The quantity to drop, -2 meaning the ammo is deleted but not dropped. -1/-3 meaning the ammo is removed from
	 * 		the weapon slot, otherwise we handle the regular removal of ammo
	 */
	public static void dropAmmo(Player player, Actor target, int quantity) {
		if (quantity == -2) {
			final int ammoId = player.getEquipment().getAmmoId();
			player.getEquipment().removeAmmo(ammoId, 1);
		} else if (quantity == -1 || quantity == -3) {
			final int weaponId = player.getEquipment().getWeaponId();
			if (weaponId != -1) {
				if ((quantity == -3 && Misc.getRandom(10) < 2) || (quantity != -3 && Misc.getRandom(3) > 0)) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's") || capeId == 20771) {
						return; // nothing happens
					}
				} else {
					player.getEquipment().removeAmmo(weaponId, quantity);
					return;
				}
				player.getEquipment().removeAmmo(weaponId, quantity);
				RegionManager.updateGroundItem(new Item(weaponId, quantity), new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()), target.getPlane()), player);
			}
		} else {
			final int ammoId = player.getEquipment().getAmmoId();
			if (Misc.getRandom(3) > 0) {
				int capeId = player.getEquipment().getCapeId();
				if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's") || capeId == 20771) {
					return; // nothing happens
				}
			} else {
				player.getEquipment().removeAmmo(ammoId, quantity);
				return;
			}
			if (ammoId != -1) {
				player.getEquipment().removeAmmo(ammoId, quantity);
				RegionManager.updateGroundItem(new Item(ammoId, quantity), new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()), target.getPlane()), player);
			}
		}
	}
}
