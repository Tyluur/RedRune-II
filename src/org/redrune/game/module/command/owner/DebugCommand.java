package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

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
		player.getManager().getInterfaces().sendInterface(intParam(args, 1), intParam(args, 2));
		/*if (boolParam(args, 1)) {
			player.getManager().getInterfaces().sendPrimaryOverlay(intParam(args, 2));
		} else {
			player.getManager().getInterfaces().closePrimaryOverlay();
		}*/
/*		Entity target = null;
		for (int i = 0; i <= World.get().getPlayers().size(); i++) {
			Player p = World.get().getPlayers().get(i);
			if (p == null) {
				continue;
			}
			if (p.getIndex() == player.getIndex()) {
				continue;
			}
			target = p;
		}
		if (target == null) {
			System.out.println("Notargfound");
			return;
		}
		boolean found = EntityMovement.findBasicRoute(player, target, target.getLocation(), 25);
		System.out.println("targ=" + target + ", found=" + found);*/
		
		
	/*	int stat = 30;
		int value = (stat + intParamOrDefault(args, 1, 0))
				            | ((stat + intParamOrDefault(args, 2, 0)) << 6)
				            | ((stat + intParamOrDefault(args, 3, 0)) << 12)
				            | ((stat + intParamOrDefault(args, 4, 0)) << 18)
				            | ((stat + intParamOrDefault(args, 5, 0)) << 24);
		player.getTransmitter().send(new ConfigPacketBuilder(1583, value).build(player));*/
		//		ShopRepository.open(player, intParam(args, 1));
		//		player.getManager().getPrayers().setBook(PrayerBook.valueOf(args[1].toUpperCase()));
		//player.getNetworkSession().getChannel().close();
		//		System.out.println(player.getRegion().getPlayers());
		//			player.getManager().getPrayers().setIcon(PrayerIcon.valueOf(args[1].toUpperCase()));
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