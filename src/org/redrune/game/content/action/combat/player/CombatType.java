package org.redrune.game.content.action.combat.player;

import lombok.Getter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.action.combat.player.swing.MeleeCombatSwing;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public enum CombatType {
	MELEE(new MeleeCombatSwing()) {
		@Override
		public int getDelay(Player player, int weaponId) {
			if (weaponId != -1) {
				String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
				// interval 0.6
				if (weaponId == 9703) {
					return 1;
				}
				// Interval 2.4
				if (weaponName.equals("zamorakian spear") || weaponName.equals("korasi's sword")) {
					return 3;
				}
				// Interval 3.6
				if (weaponName.contains("godsword") || weaponName.contains("warhammer") || weaponName.contains("battleaxe") || weaponName.contains("maul") || weaponName.equals("dominion sword")) {
					return 5;
				}
				// Interval 4.2
				if (weaponName.contains("greataxe") || weaponName.contains("halberd") || weaponName.contains("2h sword") || weaponName.contains("two handed sword") || weaponName.contains("katana") || weaponName.equals("thok's sword")) {
					return 6;
				}
				// Interval 3.0
				if (weaponName.contains("spear") || weaponName.contains(" sword") || weaponName.contains("longsword") || weaponName.contains("light") || weaponName.contains("hatchet") || weaponName.contains("pickaxe ") || weaponName.contains("mace") || weaponName.contains("hasta") || weaponName.contains("warspear") || weaponName.contains("flail") || weaponName.contains("hammers")) {
					return 4;
				}
			}
			switch (weaponId) {
				case 6527: // tzhaar-ket-em
					return 4;
				case 10887: // barrelchest anchor
					return 5;
				case 15403: // balmung
				case 20084: // golden hammer
				case 6528: // tzhaar-ket-om
					return 6;
				default:
					return 3;
			}
		}
	},
	RANGE(null) {
		@Override
		public int getDelay(Player player, int id) {
			return 0;
		}
	},
	MAGIC(null) {
		@Override
		public int getDelay(Player player, int id) {
			return 0;
		}
	};
	
	/**
	 * The combat swing type
	 */
	@Getter
	private final CombatTypeSwing swing;
	
	/**
	 * The delay between each swing
	 *
	 * @param player
	 * 		The player
	 * @param id
	 * 		The id of the weapon used, or the id of the spell.
	 */
	public abstract int getDelay(Player player, int id);
	
	CombatType(CombatTypeSwing swing) {
		this.swing = swing;
	}
}
