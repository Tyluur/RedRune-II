package plugin.rsinterface;

import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class MagicSpellbookInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 192) {
			if (componentId == 2) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 11) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId == 13) {
				player.getCombatDefinitions().switchShowSkillSpells();
			} else if (componentId >= 15 & componentId <= 17) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 15);
			} else {
				Magic.processNormalSpell(player, componentId);
			}
		} else if (interfaceId == 193) {
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId >= 9 && componentId <= 11) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 9);
			} else if (componentId == 18) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processAncientSpell(player, componentId);
			}
		} else if (interfaceId == 430) {
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId >= 11 & componentId <= 13) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 11);
			} else if (componentId == 20) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processLunarSpell(player, componentId);
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(192, 193, 430);
	}
}
