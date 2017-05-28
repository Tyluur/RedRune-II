package org.redrune.network.rs666.packet.structure.in;

import org.redrune.network.rs666.packet.Packet;
import org.redrune.network.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.network.rs666.packet.structure.out.*;
import org.redrune.rs2.node.entity.data.Hit;
import org.redrune.rs2.node.entity.data.Hit.HitSplat;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.render.flag.impl.Animation;
import org.redrune.rs2.node.entity.player.render.flag.impl.Graphic;
import org.redrune.rs2.node.entity.player.render.flag.impl.HitUpdate;
import org.redrune.rs2.task.ScheduledTask;
import org.redrune.rs2.world.World;
import org.redrune.rs2.world.map.Location;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.Priority;
import org.redrune.utility.rs.GameTab;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class CommandHandlerPacketStructure implements IncomingPacketStructure {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(12);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		if (packet.getBuffer().readableBytes() < 1) {
			return;
		}
		packet.readUnsignedByte();
		packet.readUnsignedByte();
		String command = packet.readRS2String();
		String[] args = command.toLowerCase().split(" ");
		String name = args[0];
		switch(name) {
			case "item":
				player.getInventory().addItem(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				break;
			case "sendtab":
				player.getInterfaceManager().sendTab(GameTab.OPTIONS, 743);
				break;
			case "toggletab":
				player.getInterfaceManager().toggleTab(GameTab.valueOf(args[1].toUpperCase()));
				break;
			case "closeinter":
				player.getTransmitter().send(new CloseInterfaceBuilder(Integer.parseInt(args[1]), Integer.parseInt(args[2])).build(player));
				break;
			case "idbg":
				player.getTransmitter().send(new InterfaceDisplayBuilder(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), true).build(player));
				break;
			case "dbg":
				player.getTransmitter().send(new RunEnergyBuilder(100).build(player));
				break;
			case "varp":
				int id = Integer.parseInt(args[1]);
				int value = Integer.parseInt(args[2]);
				player.getTransmitter().send(new VarpPacketBuilder(id, value).build(player));
				break;
			case "hits":
				player.getHitMap().getHitList().add(new Hit(player, 500, HitSplat.ABSORB_DAMAGE));
				player.getUpdateMasks().register(new HitUpdate(player));
				break;
			case "pos":
				player.getTransmitter().send(new MessageBuilder(player.getLocation().toString()).build(player));
				break;
			case "teleport":
				Animation MODERN_ANIM = new Animation(8939, 0, false, Priority.HIGHEST);
				Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);
				
				player.getUpdateMasks().register(MODERN_ANIM);
				player.getUpdateMasks().register(MODERN_GRAPHIC);
				
				World.get().getScheduler().schedule(new ScheduledTask(3, 1,false) {
					@Override
					public void execute() {
						player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(3222, 3222, 0));
						System.out.println("pulse");
					}
				});
				break;
			case "xtele":
				try {
					Integer x = Integer.parseInt(args[1]);
					Integer y = Integer.parseInt(args[2]);
					Integer z = Integer.parseInt(args[3]);
					
					player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(x, y, z));
				} catch (NumberFormatException e) {
					player.getTransmitter().send(new MessageBuilder("Invalid parameters...").build(player));
				}
				break;
		}
	}
}
