package com.rs.networking.codec.decode;

import com.rs.cache.Cache;
import com.rs.cores.CoresManager;
import com.rs.game.GameConstants;
import com.rs.game.GameFlags;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;
import com.rs.game.world.punishment.PunishmentRepository;
import com.rs.game.world.punishment.PunishmentType;
import com.rs.networking.NetworkConstants;
import com.rs.networking.Session;
import com.rs.networking.codec.Decoder;
import com.rs.networking.io.InputStream;
import com.rs.networking.io.buffer.FixedBuffer;
import com.rs.utility.Misc;
import com.rs.utility.game.files.SerializableFilesManager;
import com.rs.utility.game.player.ReturnCode;
import com.rs.utility.networking.AntiFlood;

public final class LoginPacketsDecoder extends Decoder {
	
	public LoginPacketsDecoder(Session session) {
		super(session);
	}
	
	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		int size = stream.readUnsignedShort();
		if (stream.getRemaining() != size) {
			session.getChannel().close();
			return;
		}
		if (packetId == 19) {
			decodeLobbyLogin(stream);
		} else if (packetId == 16) {
			decodeWorldLogin(stream);
		} else {
			if (GameFlags.debugMode) {
				System.out.println("Login PacketId " + packetId);
			}
			session.getChannel().close();
		}
	}
	
	private void decodeLobbyLogin(InputStream buffer) {
		int protocol = buffer.readInt();
		if (protocol != NetworkConstants.PROTOCOL_NUMBER) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		int rsaSize = buffer.readShort(); // RSA block size
		if (rsaSize > buffer.getRemaining()) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.readBytes(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(com.alex.utils.Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS));
		int rsaHeaderKey = rsaBuffer.readUnsignedByte();
		if (rsaHeaderKey != 10) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		int gameType = buffer.readUnsignedByte();
		int language = buffer.readUnsignedByte();
		buffer.skip(24);
		buffer.readString();
		buffer.readInt();
		for (int index = 0; index < 36; index++) {
			int crc = Cache.STORE.getIndexes()[index] == null ? 0 : Cache.STORE.getIndexes()[index].getCRC();
			int receivedCrc = buffer.readInt();
			if (crc != receivedCrc && index < 32) {
				session.getLoginPackets().sendClientPacket(6);
				return;
			}
		}
		
		if (Misc.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= GameConstants.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIp()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		
		Player player;
		
		if (!SerializableFilesManager.containsPlayer(username)) {
			player = new Player(username);
		} else {
			player = SerializableFilesManager.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
		}
		player.init(username, session);
		if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
			session.getLoginPackets().sendClientPacket(ReturnCode.ACCOUNT_DISABLED.getValue());
			return;
		}
		session.getLoginPackets().sendLobbyDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
	}
	
	public void decodeWorldLogin(InputStream buffer) {
		if (CoresManager.shutdownStart != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		int protocol = buffer.readInt();
		if (protocol != NetworkConstants.PROTOCOL_NUMBER) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		boolean reconnecting = buffer.readUnsignedByte() == 1;
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.readBytes(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(com.alex.utils.Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.getLoginPackets().sendClientPacket(10);
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
			session.getLoginPackets().sendClientPacket(22);
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
				session.getLoginPackets().sendClientPacket(6);
				return;
			}
		}
		
		if (Misc.invalidAccountName(username)) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= GameConstants.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIp()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		Player player;
		if (!SerializableFilesManager.containsPlayer(username)) {
			player = new Player(password);
		} else {
			player = SerializableFilesManager.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
		}
		player.setUsername(username);
		session.sync(player);
		if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
			session.getLoginPackets().sendClientPacket(ReturnCode.ACCOUNT_DISABLED.getValue());
			return;
		}
		player.init(username, mode, width, height);
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();
	}
	
}
