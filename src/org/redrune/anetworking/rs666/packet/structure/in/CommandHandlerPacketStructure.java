package org.redrune.network.rs666.packet.structure.in;

import org.redrune.anetworking.rs666.packet.structure.IncomingPacketStructure;
import org.redrune.anetworking.rs666.packet.structure.out.VarpPacketBuilder;
import org.redrune.rs2.node.entity.data.Hit;
import org.redrune.rs2.node.entity.data.Hit.HitSplat;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.render.flag.impl.HitUpdate;
import org.redrune.utility.Misc;

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
		String[] split = command.toLowerCase().split(" ");
		String name = split[0];
		switch(name) {
			case "varp":
				int id = Integer.parseInt(split[1]);
				int value = Integer.parseInt(split[2]);
				player.getTransmitter().send(new VarpPacketBuilder(id, value).build(player));
				break;
			case "hits":
				player.getHitMap().getHitList().add(new Hit(player, 500, HitSplat.ABSORB_DAMAGE));
				player.getUpdateMasks().register(new HitUpdate(player));
				break;
		}
	}
}
