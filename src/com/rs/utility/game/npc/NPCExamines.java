package com.rs.utility.game.npc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

/**
 * This class gets all of the examine messages for npcs.
 * 
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2012-11-03
 */
public class NPCExamines {

	/** The map that is populated with the NPC examines */
	private static final Map<Integer, String> EXAMINES = new HashMap<Integer, String>();

	/**
	 * The location of the unpacked examines.
	 */
	private static final String UNPACKED = "data/repository/npc/unpackedExamines.txt";

	/**
	 * The location of the packed examines.
	 */
	private static final String PACKED = "data/repository/npc/packedExamines.e";

	/**
	 * Loads the examines from the appropriate location.
	 * 
	 * @throws IOException
	 */
	public static void loadExamines() throws IOException {
		if (new File(PACKED).exists())
			loadPacked();
		else
			loadUnpacked();
	}

	/**
	 * Shows the examine message for the parameter npcId.
	 * 
	 * @param npcId
	 *            The npc to find the examine of.
	 * @return The examine message.
	 */
	public static String getExamine(int npcId) {
		String message = "";
		if (EXAMINES.get(npcId) != null) {
			message = EXAMINES.get(npcId);
			formatMessage(message);
		} 
		else {
			message = "Examining npc.";
		}
		return message;
	}

	/**
	 * Formats the message given
	 * 
	 * @param message
	 */
	private static void formatMessage(String message) {
		message = message.replace("<i>", "");
		message = message.replace("</i>", "");
		message = message.replace("<b>", "");
	}

	/**
	 * Loads all of the examines from the {@link #PACKED} file location.
	 */
	private static void loadPacked() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining())
				EXAMINES.put(buffer.getShort() & 0xffff, readString(buffer));
			channel.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all of the examines from the {@link #UNPACKED} file location.
	 * 
	 * @throws IOException
	 */
	private static void loadUnpacked() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(UNPACKED));
		DataOutputStream out = new DataOutputStream(
				new FileOutputStream(PACKED));
		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			if (line.startsWith("//"))
				continue;
			line = line.replace("﻿", "");
			String[] split = line.split(" - ");
			int npcId = Integer.parseInt(split[0]);
			if (split[1].length() > 255)
				continue;
			out.writeShort(npcId);
			writeString(out, split[1]);
			EXAMINES.put(npcId, split[1]);
		}
		in.close();
		out.flush();
		out.close();
	}

	public static String readString(ByteBuffer buffer) {
		int count = buffer.get() & 0xff;
		byte[] bytes = new byte[count];
		buffer.get(bytes, 0, count);
		return new String(bytes);
	}

	public static void writeString(DataOutputStream out, String string)
			throws IOException {
		byte[] bytes = string.getBytes();
		out.writeByte(bytes.length);
		out.write(bytes);
	}

}