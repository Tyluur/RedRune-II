package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-01
 */
@CommandManifest(description = "Sends all the components of an interface", types = { Integer.class})
public class DebugInterfaceCommandPlugin extends CommandPlugin  {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int interfaceId = intParam(args, 1);
		int componentLength = Misc.getInterfaceDefinitionsComponentsSize(interfaceId);
		boolean sendInterface = true;
		if (args.length == 3) {
			sendInterface = Boolean.parseBoolean(args[2]);
		}
		for (int i = 0; i < componentLength; i++) {
			player.getPackets().sendIComponentText(interfaceId, i, "" + i);
		}
		for (int i = 0; i <= 354; i++) {
			player.getPackets().sendGlobalString(i, "g" + i);
		}
		if (sendInterface)
			player.getInterfaceManager().sendInterface(interfaceId);
		
		System.out.println("Component length: " + componentLength);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbi");
	}
}
