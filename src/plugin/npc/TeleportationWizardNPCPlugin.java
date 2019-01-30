package plugin.npc;

import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;
import org.redrune.utility.constants.AttributeKey;
import plugin.inter.TeleportationInterfacePlugin;
import plugin.inter.TeleportationInterfacePlugin.TransportationLocation;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class TeleportationWizardNPCPlugin implements NPCPlugin {
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		switch (option) {
			case "Talk-to":
				TeleportationInterfacePlugin.displaySelectionInterface(player, true);
				return true;
			case "Previous":
				TransportationLocation last = player.getAttributes().getAttribute(AttributeKey.LAST_TRANSPORTATION_LOCATION);
				if (last == null) {
					return true;
				}
				TeleportationInterfacePlugin.teleportPlayer(player, last.getDestination(), () -> last.getLocations().handlePostTeleportation(player, last.getOptionIndex()));
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		registerNPC(14332, "Talk-to");
		registerNPC(14332, "Previous");
	}
}
