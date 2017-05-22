package org.redrune.network.protocol.game;

import java.util.List;

import org.redrune.network.packet.PacketRepository;
import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.game.msg.GameRequestEvent;
import org.redrune.network.session.impl.GameSession.GameEvent;
import org.redrune.network.stream.IoReadEvent;
import org.redrune.utility.isaac.IsaacRandomPair;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * GameReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_GAME)
public class GameReadEvent extends ReplayingDecoder<GameEvent> {

	private final IsaacRandomPair isaacPair;

	private int packetId;

	private int length;

	public GameReadEvent(IsaacRandomPair isaacPair) {
		super(GameEvent.READ_PACKET);
		this.isaacPair = isaacPair;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {
			switch (state()) {
			case READ_PACKET:
				packetId = 0xff & in.readUnsignedByte() - isaacPair.getInput().getNextValue();
				if (packetId >= 128) {
					packetId = (packetId - 128 << 8) + (in.readUnsignedByte() - isaacPair.getInput().getNextValue());
				}
				if (packetId > PacketRepository.PACKET_LENGTHS.length || packetId < 0) {
					return;
				}
				checkpoint(GameEvent.READ_SIZE);
				break;
			case READ_SIZE:
				length = PacketRepository.PACKET_LENGTHS[packetId];
				if (length < 0) {
					switch (length) {
					case -1:
						if (in.isReadable()) {
							length = in.readByte() & 0xff;
						}
						break;
					case -2:
						if (in.readableBytes() >= 2) {
							length = in.readShort() & 0xffff;
						}
						break;
					default:
						length = in.readableBytes();
						break;
					}
				}
				checkpoint(GameEvent.FINALIZE);
				break;
			case FINALIZE:
				if (in.readableBytes() >= length) {
					if (length < 0) {
						return;
					}
					byte[] payload = new byte[length];
					in.readBytes(payload, 0, length);
					out.add(new GameRequestEvent(new IoReadEvent(packetId, Unpooled.wrappedBuffer(payload))));
				}
				checkpoint(GameEvent.READ_PACKET);
				break;
			}
		}
	}

	public IsaacRandomPair getIsaacPair() {
		return isaacPair;
	}

}
