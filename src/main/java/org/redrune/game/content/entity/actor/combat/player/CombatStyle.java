package org.redrune.game.content.entity.actor.combat.player;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.content.entity.actor.combat.player.style.MeleeCombatStyle;
import org.redrune.game.content.entity.actor.combat.player.style.RangeCombatStyle;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public enum CombatStyle {
	
	MELEE(new MeleeCombatStyle()) {
		@Override
		public int getDelay(Player player) {
			int weaponId = player.getEquipment().getWeaponId();
			String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			// interval 0.6
			if (weaponId == 9703) {
				return 1;
			}
			// Interval 1.8
			if (weaponName.contains("saradomin sword") || weaponName.contains(" whip") || weaponName.equals("zamorakian spear") || weaponName.equals("korasi's sword")) {
				return 3;
			}
			// Interval 3.0
			if (weaponName.contains("spear") || weaponName.contains(" sword") || weaponName.contains("longsword") || weaponName.contains("light") || weaponName.contains("hatchet") || weaponName.contains("pickaxe ") || weaponName.contains("mace") || weaponName.contains("hasta") || weaponName.contains("warspear") || weaponName.contains("flail") || weaponName.contains("hammers")) {
				return 4;
			}
			// Interval 3.6
			if (weaponName.contains("godsword") || weaponName.contains("warhammer") || weaponName.contains("battleaxe") || weaponName.contains("maul") || weaponName.equals("dominion sword")) {
				return 5;
			}
			// Interval 4.2
			if (weaponName.contains("greataxe") || weaponName.contains("halberd") || weaponName.contains("2h sword") || weaponName.contains("two handed sword") || weaponName.contains("katana") || weaponName.equals("thok's sword")) {
				return 6;
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
	RANGE(new RangeCombatStyle()) {
		@Override
		public int getDelay(Player player) {
			int weaponId = player.getEquipment().getWeaponId();
			final int attackStyle = player.getCombatDefinitions().getAttackStyle();
			int delay;
			String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("shortbow") || weaponName.contains("karil's crossbow") || weaponName.contains("sling")) {
				delay = 3;
			} else if (weaponName.contains("crossbow")) {
				delay = 5;
			} else if (weaponName.contains("dart") || weaponName.contains("knife")) {
				delay = 2;
			} else if (weaponName.contains("chinchompa") || weaponName.contains("crystal bow")) {
				delay = 4;
			} else if (weaponName.contains("toktz-xil-ul")) {
				delay = 3;
			} else {
				switch (weaponId) {
					case 15241:
						delay = 7;
						break;
					case 11235: // dark bows
					case 15701:
					case 15702:
					case 15703:
					case 15704:
						delay = 9;
						break;
					case 20171:
						delay = 4;
						break;
					default:
						delay = 6;
						break;
				}
			}
			if (attackStyle == 1) {
				delay--;
			} else if (attackStyle == 2) {
				delay++;
			}
			return delay;
		}
	},
	MAGIC(new MagicCombatStyle()) {
		@Override
		public int getDelay(Player player) {
			Optional<SpellPlugin> optional = PluginRepository.getSpellPlugin(player.getCombatDefinitions().getMagicBook(), player.getCombatDefinitions().getRealSpellId());
			return optional.map(spellPlugin -> {
				if (spellPlugin instanceof CombatSpellPlugin) {
					return ((CombatSpellPlugin) spellPlugin).delay(player);
				} else {
					return -1;
				}
			}).orElse(-1);
		}
	};
	
	/**
	 * The style
	 */
	private final AbstractCombatStyle style;
	
	/**
	 * Gets the delay for the player to swing this style
	 */
	public abstract int getDelay(Player player);
	
	CombatStyle(AbstractCombatStyle style) {
		this.style = style;
	}

    public AbstractCombatStyle getStyle() {
        return this.style;
    }
}
