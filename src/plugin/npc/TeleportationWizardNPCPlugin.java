package plugin.npc;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.NPCPlugin;
import com.rs.utility.game.ClickOption;
import plugin.inter.TeleportationInterfacePlugin;
import plugin.inter.TeleportationInterfacePlugin.TransportationLocation;

import static com.rs.utility.game.ClickOption.FIRST;
import static com.rs.utility.game.ClickOption.SECOND;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class TeleportationWizardNPCPlugin extends NPCPlugin {
	
	@Override
	public void handle(Player player, NPC npc, ClickOption option) {
		if (option == FIRST) {
			TeleportationInterfacePlugin.displaySelectionInterface(player, true);
		} else if (option == SECOND) {
			TransportationLocation last = player.getSaving().getAttribute("last_transportation_location");
			if (last == null) {
				return;
			}
			TeleportationInterfacePlugin.teleportPlayer(player, last.getDestination(), () -> last.getLocations().handlePostTeleportation(player, last.getOptionIndex()));
		}
	}
	
	@Override
	public void register() {
		register(14332, FIRST, SECOND);
	}
}
