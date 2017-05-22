package org.redrune.network.session.impl;

import org.redrune.network.packet.PacketRepository;
import org.redrune.network.packet.read.PacketReadEvent;
import org.redrune.network.protocol.game.msg.GameRequestEvent;
import org.redrune.network.protocol.game.msg.GameResponseEvent;
import org.redrune.network.session.Session;
import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.utility.isaac.IsaacRandomPair;

import io.netty.channel.Channel;

/**
 * GameSession.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class GameSession extends Session {

	public enum GameEvent {
		READ_PACKET, READ_SIZE, FINALIZE;
	}

	private final Player player;

	private final IsaacRandomPair isaacPair;

	public GameSession(Player player, Channel channel, IsaacRandomPair isaacPair) {
		super(channel);
		this.player = player;
		this.isaacPair = isaacPair;
	}

	@Override
	public void throttleRequest(Object context) {
		if (context instanceof GameRequestEvent) {
			GameRequestEvent request = (GameRequestEvent) context;
			PacketReadEvent packet = PacketRepository.readPacket(request.getIOReadEvent().getPacketId());
			if (packet == null) {
				player.getPacketSender()
						.sendConsoleMessage("Unhandled Packet: " + request.getIOReadEvent().getPacketId());
				System.err.println("Unhandled Packet: " + request.getIOReadEvent().getPacketId());
				return;
			}
			packet.decodePacket(player, request.getIOReadEvent());
		}
	}

	public void write(Class<?> clazz, IoWriteEvent event) {
		if (channel.isRegistered()) {
			synchronized (channel) {
				channel.writeAndFlush(new GameResponseEvent(isaacPair, clazz, event));
			}
		}
	}

	@Override
	public void disconnect() throws InterruptedException {
		if (player == null) {
			return;
		}
//		player.sendLogout(false);
	}

	public IsaacRandomPair getIsaacPair() {
		return isaacPair;
	}

}
