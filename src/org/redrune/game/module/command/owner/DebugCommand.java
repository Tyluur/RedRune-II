package org.redrune.game.module.command.owner;

import com.google.common.collect.EvictingQueue;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

import java.util.Arrays;
import java.util.Queue;

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
		Queue<int[]> fifo = EvictingQueue.create(2);
		fifo.add(new int[] { 1, 1 });
		fifo.add(new int[] { 2, 1 });
		fifo.add(new int[] { 3, 1 });
		fifo.forEach(o -> System.out.println(Arrays.toString(o)));
		
		System.out.println(Arrays.toString(player.getMovement().getLastWalkTile()));
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