package org.redrune.networking.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketType;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.constants.PacketConstants;

import java.util.List;

/**
 * Decodes a received packet.
 *
 * @author Cjay0091
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-05
 */
public class RS2PacketDecoder extends ReplayingDecoder<GameState> {

    /**
     * The opcode of the current packed being decoded
     */
    private int opcode;

    /**
     * The length of the current packet being decoded
     */
    private int length;

    /**
     * Constructs a new {@code RS2GameDecoder} {@code Object}.
     *
     * @param session The session.
     */
    public RS2PacketDecoder(NetworkSession session) {
        super(GameState.VERSION);
        session.getChannel().attr(NetworkConstants.SESSION_KEY).setIfAbsent(session);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case VERSION:
                opcode = in.readUnsignedByte();
                checkpoint(GameState.PAYLOAD_LENGTH);
                break;
            case PAYLOAD_LENGTH:
                length = PacketConstants.PACKET_SIZES[opcode];
                if (length == -1) {
                    length = in.readUnsignedByte();
                } else if (length == -2) {
                    length = in.readUnsignedShort();
                } else if (length == -3) {
                    length = in.readInt();
                }
                checkpoint(GameState.PAYLOAD);
                break;
            case PAYLOAD:
                try {
                    byte[] payload = new byte[length];
                    in.readBytes(payload, 0, length);
                    in.markReaderIndex();
                    out.add(new Packet(opcode, PacketType.STANDARD, Unpooled.copiedBuffer(payload)));
                } catch (Exception e) {
                    System.out.println("Packet[" + opcode + ", " + length + "]");
                    ctx.fireExceptionCaught(e);
                }
                checkpoint(GameState.VERSION);
                break;
            default:
                break;
        }
    }

}