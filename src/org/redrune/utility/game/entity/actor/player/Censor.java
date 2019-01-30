package org.redrune.utility.game.entity.actor.player;

import org.redrune.utility.functions.Misc;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

public class Censor {
	
	/**
	 * The base location for all censor related data
	 */
	private static final String BASE_LOCATION = "./data/repository/chat/";
	
	/**
	 * The location for packed data
	 */
	private final static String PACKED_PATH = BASE_LOCATION + "packedCensoredWords.e";
	
	/**
	 * The location for unpacked data
	 */
	private final static String UNPACKED_PATH = BASE_LOCATION + "unpackedCensoredWords.txt";
	
	/**
	 * The list of censored words
	 */
	private final static List<String> CENSORED_WORDS = new ArrayList<>();
	
	public static void initialize() {
		if (new File(PACKED_PATH).exists()) {
			loadPackedCensoredWords();
		} else {
			loadUnpackedCensoredWords();
		}
		System.out.println("Loaded " + CENSORED_WORDS.size() + " censored words");
	}
	
	private static void loadPackedCensoredWords() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				CENSORED_WORDS.add(readString(buffer));
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void loadUnpackedCensoredWords() {
		System.out.println("Packing censored words...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("//") || line.startsWith("*")) {
					continue;
				}
				writeString(out, line);
				CENSORED_WORDS.add(line);
			}
			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String readString(ByteBuffer buffer) {
		int count = buffer.get() & 0xff;
		byte[] bytes = new byte[count];
		buffer.get(bytes, 0, count);
		return new String(bytes);
	}
	
	public static void writeString(DataOutputStream out, String string) throws IOException {
		byte[] bytes = string.getBytes();
		out.writeByte(bytes.length);
		out.write(bytes);
	}
	
	public static String getFilteredMessage(String message) {
		message = message.toLowerCase();
		for (String word : CENSORED_WORDS) {
			if (message.contains(word)) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < word.length(); i++) {
					sb.append("*");
				}
				message = message.replace(word, sb.toString());
			}
		}
		return Misc.fixChatMessage(message);
	}
	
}