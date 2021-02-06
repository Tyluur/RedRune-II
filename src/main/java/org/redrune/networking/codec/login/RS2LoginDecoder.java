package org.redrune.networking.codec.login;

import com.alex.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.Cache;
import org.redrune.game.GameFlags;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.punishment.PunishmentType;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.codec.RS2PacketDecoder;
import org.redrune.networking.packet.outgoing.impl.LobbyConfigurationPacketBuilder;
import org.redrune.networking.packet.outgoing.impl.LoginConfigurationPacketBuilder;
import org.redrune.networking.packet.outgoing.impl.LoginResponseCodePacketBuilder;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.PlayerSaving;
import org.redrune.utility.game.session.ISAACCipher;
import org.redrune.utility.game.stream.buffer.FixedBuffer;

import java.util.Arrays;
import java.util.List;

import static org.redrune.utility.game.entity.actor.player.LoginReturnCode.*;
import static org.redrune.utility.game.entity.actor.player.ReturnCode.INVALID_LOGIN_SERVER;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public class RS2LoginDecoder extends ByteToMessageDecoder {
	
	/**
	 * The {@code NetworkSession} instance created
	 */
	private NetworkSession session;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 3) {
			return;
		}
		int opcode = in.readUnsignedByte();
		int size = in.readUnsignedShort();
		if (in.readableBytes() != size) {
			ctx.close();
			return;
		}
		if (opcode != 16 && opcode != 18 && opcode != 19) {
			System.out.println("Received unexpected world login opcode: " + opcode);
			setSession(ctx.channel());
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		int revision = in.readInt();
		if (revision != NetworkConstants.PROTOCOL_NUMBER) {
			System.out.println("Received unexpected protocol number: " + revision);
			setSession(ctx.channel());
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		setSession(ctx.channel());
		byte[] data = new byte[size - 4];
		// store the data into the buffer
		in.readBytes(data);
		// convert the buffer into a readable object
		FixedBuffer buffer = new FixedBuffer(data);
		if (opcode == 19) {
			decodeLobbyLogin(ctx, buffer, out);
		} else if (opcode == 16) {
			decodeWorldLogin(ctx, buffer, out);
		} else {
			if (GameFlags.debugMode) {
				System.out.println("Received unexpected login request from " + session + ". [opcode=" + opcode + "]");
			}
			ctx.channel().close();
		}
	}
	
	/**
	 * Decode the lobby login from buffer.
	 *
	 * @param ctx
	 * 		The context
	 * @param buffer
	 * 		the buffer with data
	 * @param out
	 * 		The outgoing response
	 */
	private void decodeLobbyLogin(ChannelHandlerContext ctx, FixedBuffer buffer, List<Object> out) {
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.read(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		int gameType = buffer.readUnsignedByte();
		int language = buffer.readUnsignedByte();
		buffer.skipBefore(24);
		buffer.readString();
		buffer.readInt();
		for (int index = 0; index < 36; index++) {
			int crc = Cache.STORE.getIndexes()[index] == null ? 0 : Cache.STORE.getIndexes()[index].getCRC();
			int receivedCrc = buffer.readInt();
			if (crc != receivedCrc && index < 32) {
				session.write(new LoginResponseCodePacketBuilder(UPDATED)).addListener(ChannelFutureListener.CLOSE);
				System.out.println("index=" + index + ", crc=" + crc + ", receivedCrc=" + receivedCrc);
				return;
			}
		}
		if (Misc.invalidAccountName(username)) {
			session.write(new LoginResponseCodePacketBuilder(INVALID_CREDENTIALS)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (World.getLobbyPlayers().size() >= GameConstants.PLAYERS_LIMIT - 10) {
			session.write(new LoginResponseCodePacketBuilder(FULL_WORLD)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (World.containsPlayer(username, false)) {
			session.write(new LoginResponseCodePacketBuilder(ALREADY_ONLINE)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		Player player;
		
		if (!PlayerSaving.playerExists(username)) {
			player = new Player(password);
		} else {
			player = PlayerSaving.fromFile(username);
			if (player == null) {
				ctx.writeAndFlush(new LoginResponseCodePacketBuilder(LOGIN_SERVER_OFFLINE).build().getBuffer());
				return;
			}
		}
		int[] inCipher = Arrays.copyOf(isaacSeed, isaacSeed.length);
		int[] outCipher = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			outCipher[i] = isaacSeed[i] + 50;
		}
		
		// builds the isaac ciphers used for packet encryption
		session.buildCiphers(new ISAACCipher(inCipher), new ISAACCipher(outCipher));
		
		player.initializeLobby(username, session);
		if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
			session.write(new LoginResponseCodePacketBuilder(ACCOUNT_DISABLED)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		// send the player to the lobby
		session.write(new LobbyConfigurationPacketBuilder(player));
		
		// change decoders
		ctx.pipeline().replace("decoder", "decoder", new RS2PacketDecoder(session));
	}
	
	/**
	 * Decode the world login from buffer.
	 *
	 * @param ctx
	 * 		the channel context.
	 * @param buffer
	 * 		the buffer with data
	 * @param out
	 * 		The outgoing response
	 */
	@SuppressWarnings("unused")
	private void decodeWorldLogin(ChannelHandlerContext ctx, FixedBuffer buffer, List<Object> out) {
		boolean reconnecting = buffer.readBoolean();
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.read(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.write(new LoginResponseCodePacketBuilder(BAD_SESSION_ID)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		buffer.readByte();
		int mode = buffer.readByte();
		int width = buffer.readShort();
		int height = buffer.readShort();
		int displayMode = buffer.readByte();
		byte[] userId = new byte[24];
		// that's the content of random.dat , which is generated depending on user's hardware and software.
		for (int i = 0; i < 24; i++) {
			userId[i] = (byte) buffer.readByte();
		}
		String settings = buffer.readString();
		int affiliateId = buffer.readInt(); // used for adverts.
		int settingsBufferLength = buffer.readByte();
		byte[] settingsBuffer = new byte[settingsBufferLength];
		for (int i = 0; i < settingsBufferLength; i++) {
			settingsBuffer[i] = (byte) buffer.readByte();
		}
		
		// hardware block
		int hwMagic = buffer.readByte();
		if (hwMagic != 5) {
			session.write(new LoginResponseCodePacketBuilder(MALFORMED_LOGIN_PACKET)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		int osId = buffer.readByte(); // [1 - windows, 2 - mac, 3 - linux, 4 - other]
		boolean is64Bit = buffer.readByte() == 1;
		int osVersion = buffer.readByte();
		int javaVendorID = buffer.readByte();
		byte[] javaVersion = new byte[3];
		for (int i = 0; i < 3; i++) {
			javaVersion[i] = (byte) buffer.readByte();
		}
		boolean webclient = buffer.readByte() == 1;
		int heapSize = buffer.readShort();
		int availableProcessors = buffer.readByte();
		
		buffer.read24BitInt(); // raw cpu info MEDINT,USHORT,UBYTE,UBYTE,UBYTE
		buffer.readShort();
		buffer.readByte();
		buffer.readByte();
		buffer.readByte();
		for (int i = 0; i < 4; i++) {
			buffer.readVString(); // those 4 are always empty and not set in client ( propably for future use )
		}
		buffer.readByte(); // same for theese two
		buffer.readShort();
		
		int specialPacketCounter = buffer.readInt();
		long userFlow = buffer.readLong();
		
		boolean additionalExists = buffer.readByte() == 1;
		String additionalInfo = "";
		if (additionalExists) {
			additionalInfo = buffer.readString();
		}
		
		boolean jagtheoraLoaded = buffer.readByte() == 1;
		boolean supportsJavaScript = buffer.readByte() == 1;
		buffer.readByte();
		
		for (int index = 0; index < 36; index++) {
			int crc = Cache.STORE.getIndexes()[index] == null ? 0 : Cache.STORE.getIndexes()[index].getCRC();
			int receivedCrc = buffer.readInt();
			if (crc != receivedCrc && index < 32) {
				session.write(new LoginResponseCodePacketBuilder(UPDATED)).addListener(ChannelFutureListener.CLOSE);
				return;
			}
		}
		if (Misc.invalidAccountName(username)) {
			session.write(new LoginResponseCodePacketBuilder(INVALID_CREDENTIALS)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (password.length() >= 30) {
			session.write(new LoginResponseCodePacketBuilder(INVALID_CREDENTIALS)).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		// build the isaac ciphers
		int[] inCipher = Arrays.copyOf(isaacSeed, isaacSeed.length);
		int[] outCipher = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			outCipher[i] = isaacSeed[i] + 50;
		}
		Player player;
		
		if (!PlayerSaving.playerExists(username)) {
			player = new Player(password);
		} else {
			player = PlayerSaving.fromFile(username);
			if (player == null) {
				ctx.writeAndFlush(new LoginResponseCodePacketBuilder(LOGIN_SERVER_OFFLINE).build().getBuffer());
				return;
			}
		}
		
		// builds the isaac ciphers used for packet encryption
		session.buildCiphers(new ISAACCipher(inCipher), new ISAACCipher(outCipher));
		
		player.setUsername(username);
		if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
			ctx.writeAndFlush(new LoginResponseCodePacketBuilder(INVALID_CREDENTIALS).build().getBuffer());
			return;
		}

		if (!password.equals(player.getPassword())) {
			ctx.writeAndFlush(new LoginResponseCodePacketBuilder(INVALID_CREDENTIALS).build().getBuffer());
			return;
		}

		// start game session
		player.initializeGameSession(username, session, mode, width, height);
		session.write(new LoginConfigurationPacketBuilder(player));
		player.start();
		
		// change decoders
		ctx.pipeline().replace("decoder", "decoder", new RS2PacketDecoder(session));
	}
	
	/**
	 * Sets the session
	 *
	 * @param channel
	 * 		The channel
	 */
	private void setSession(Channel channel) {
		session = new NetworkSession(channel);
		channel.attr(NetworkConstants.SESSION_KEY).set(session);
	}
	
}
