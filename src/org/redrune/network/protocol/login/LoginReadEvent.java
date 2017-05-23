package org.redrune.network.protocol.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.protocol.ProtocolThrottle.Protocol;
import org.redrune.network.protocol.ProtocolThrottle.ProtocolRequest;
import org.redrune.network.protocol.login.msg.LoginRequestEvent;
import org.redrune.rs2.node.entity.player.components.managers.InterfaceManager.DisplayMode;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.BufferUtils;
import org.redrune.utility.backend.base37.Base37Utils;
import org.redrune.utility.backend.isaac.IsaacRandom;
import org.redrune.utility.backend.isaac.IsaacRandomPair;
import org.redrune.utility.backend.xtea.XTEACryption;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * LoginReadEvent.java
 *
 * @author Chryonic May 22, 2017 | RedRune
 */
@ProtocolRequest(request = Protocol.REQUEST_LOGIN)
public class LoginReadEvent extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {
			int opcode = in.readUnsignedByte();
			LoginRequest request = Optional.of(opcode == 19 ? LoginRequest.REQUEST_LOBBY : LoginRequest.REQUEST_WORLD).get();
			System.out.println(request);
			
			int size = in.readShort();
			if (in.readableBytes() != size) {
				ctx.channel().disconnect();
				return;
			}
			
			int major = in.readInt();
			
			switch (request) {
				case REQUEST_LOBBY:
					processLobbyRequest(major, ctx, in, out);
					break;
				case REQUEST_WORLD:
					processWorldRequest(major, ctx, in, out);
					break;
				default:
					break;
			}
		}
	}
	
	private void processLobbyRequest(int major, ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws InterruptedException {
		final int rsaLength = in.readUnsignedShort();
		if (in.readableBytes() < rsaLength) {
			ctx.channel().disconnect();
			return;
		}
		byte[] rsaBuffer = new byte[rsaLength];
		for (int i = 0; i < rsaLength; i++)
			rsaBuffer[i] = in.readByte();
		
		ByteBuf rsaBlock = Unpooled.wrappedBuffer(rsaBuffer);
		byte rsaMagic = rsaBlock.readByte();
		if (rsaMagic != 10) {
			ctx.channel().disconnect().sync();
			return;
		}
		int[] isaac = new int[4];
		for (int i = 0; i < 4; i++) {
			isaac[i] = rsaBlock.readInt();
		}
		long vHash = rsaBlock.readLong();
		if (vHash != 0L) {
			ctx.channel().disconnect().sync();
			return;
		}
		String password = BufferUtils.readString(rsaBlock);
		long[] serverSeeds = new long[2];
		for (int i = 0; i < 2; i++) {
			serverSeeds[i] = rsaBlock.readLong();
		}
		// after rsaBlock there goes xteaBlock , which needs to be decrypted.
		int xteaBlockLength = in.readableBytes();
		byte[] xteaBuffer = new byte[xteaBlockLength];
		for (int i = 0; i < xteaBlockLength; i++) {
			xteaBuffer[i] = in.readByte();
		}
		ByteBuf xteaPacket = Unpooled.wrappedBuffer(XTEACryption.set(isaac).decrypt(xteaBuffer, 0,xteaBlockLength));
		String username = BufferUtils.readString(xteaPacket);
		byte gameID = xteaPacket.readByte(); // game ID (0 for runescape)
		byte langID = xteaPacket.readByte(); // language id.
		
		byte[] userId = new byte[24];
		for (int i = 0; i < 24; i++) // that's the content of random.dat , which is generated depending on user's hardware and software.
		{
			userId[i] = xteaPacket.readByte();
		}
		
		String loginToken = BufferUtils.readString(xteaPacket);
		
		if (!loginToken.equals(NetworkConstants.LOGIN_TOKEN)) {
			ctx.channel().disconnect().sync();
			return;
		}
		
		int affliateID = xteaPacket.readInt(); // this is used when showing adverts.
		
		int[] cacheCRCS = new int[36];
		for (int i = 0; i < 36; i++) {
			cacheCRCS[i] = xteaPacket.readInt();
		}
		IsaacRandom input = new IsaacRandom(isaac);
		for (int i = 0; i < 4; i++) {
			isaac[i] += 50;
		}
		IsaacRandom output = new IsaacRandom(isaac);
		IsaacRandomPair pair = new IsaacRandomPair(input, output);
		out.add(new LoginRequestEvent(LoginRequest.REQUEST_LOBBY, major, username, password, pair, null));
	}
	
	private void processWorldRequest(int major, ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws InterruptedException {
		in.readUnsignedByte();
		
		byte[] rsa = new byte[in.readUnsignedShort()];
		in.readBytes(rsa);
		
		ByteBuf block = Unpooled.wrappedBuffer(new BigInteger(rsa).modPow(org.redrune.network.protocol.Protocol.WORLD_PRIVATE_KEY, org.redrune.network.protocol.Protocol.WORLD_MODULUS).toByteArray());
		
		int blockId = block.readUnsignedByte();
		
		if (blockId != 10) {
			ctx.channel().disconnect().sync();
			return;
		}
		
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = block.readInt();
		}
		
		long key = block.readLong();
		
		if (key != 0) {
			ctx.channel().disconnect();
			return;
		}
		
		String password = BufferUtils.readString(block);
		
		long[] loginSeeds = new long[2];
		for (int i = 0; i < loginSeeds.length; i++) {
			loginSeeds[i] = block.readLong();
		}
		
		byte[] xtea = new byte[in.readableBytes()];
		in.readBytes(xtea);
		XTEACryption.set(isaacSeed).decrypt(xtea, 0, xtea.length);
		
		block = Unpooled.wrappedBuffer(xtea);
		
		boolean useString = block.readByte() == 1;
		String username = useString ? BufferUtils.readString(block) : Base37Utils.decodeBase37(block.readLong());
		username = Misc.formatPlayerNameForDisplay(username);
		
		int displayId = block.readUnsignedByte();// mode
		block.readUnsignedShort();// width
		block.readUnsignedShort();// height
		block.readUnsignedByte();
		
		for (int i = 0; i < 24; i++) {
			block.readByte();
		}
		
		String loginToken = BufferUtils.readString(block);
		
		if (!loginToken.equals(NetworkConstants.LOGIN_TOKEN)) {
			ctx.channel().disconnect().sync();
			return;
		}
		
		IsaacRandom input = new IsaacRandom(isaacSeed);
		for (int i = 0; i < 4; i++) {
			isaacSeed[i] += 50;
		}
		IsaacRandom output = new IsaacRandom(isaacSeed);
		IsaacRandomPair pair = new IsaacRandomPair(input, output);
		out.add(new LoginRequestEvent(LoginRequest.REQUEST_WORLD, major, username, password, pair, Optional.of(displayId == 1 ? DisplayMode.FIXED_DISPLAY : DisplayMode.RESIZEABLE_DISPLAY).get()));
	}
	
	public enum LoginRequest {
		REQUEST_LOBBY,
		REQUEST_WORLD;
	}
	
}
