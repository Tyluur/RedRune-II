package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.outgoing.impl.CS2StringBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.InterfaceChangeBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "You never know what this will do!")
public class DebugCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int end = intParam(args, 2);
		for (int i = intParam(args, 1); i <= end; i++) {
			player.getTransmitter().send(new InterfaceChangeBuilder(667, i, false).build(player));
		}
		String completed = getCompleted(args, 3);
		String[] split = completed.split(",");
		for (String itself : split) {
			int number = Integer.parseInt(itself);
			player.getTransmitter().send(new CS2StringBuilder(number, "g" + number + "<br><br><br>").build(player));
		}
		player.getManager().getInterfaces().sendInterface(667, true);
		System.out.println("Finished with " + completed);
		
		//			player.getManager().getPrayers().setIcon(PrayerIcon.valueOf(args[1].toUpperCase()));
		//			player.getNetworkSession().getChannel().close();
		//			player.getManager().getDialogues().startDialogue(new BankerNPCDialogue(), 45);
	/*		Animation MODERN_ANIM = new Animation(8939, 0, false, Priority.HIGHEST);
			Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);
			
			player.getUpdateMasks().register(MODERN_ANIM);
			player.getUpdateMasks().register(MODERN_GRAPHIC);
			
			SystemManager.getScheduler().schedule(new ScheduledTask(3, 1, false, () -> {
				player.getUpdateMasks().register(new Animation(-1));
				player.getUpdateMasks().register(new Graphic(-1));
				player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 0));
			}));
			*/
	}
}