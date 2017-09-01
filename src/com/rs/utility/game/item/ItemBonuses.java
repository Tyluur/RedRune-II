package com.rs.utility.game.item;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class ItemBonuses {
	
	private final static String PACKED_PATH = "data/repository/item/bonuses.ib";
	
	private static HashMap<Integer, int[]> itemBonuses;
	
	private ItemBonuses() {
	
	}
	
	public static void init() {
		if (new File(PACKED_PATH).exists()) {
			loadItemBonuses();
		} else {
			throw new RuntimeException("Missing item bonuses.");
		}
	}
	
	private static void loadItemBonuses() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			itemBonuses = new HashMap<>(buffer.remaining() / 38);
			while (buffer.hasRemaining()) {
				int itemId = buffer.getShort() & 0xffff;
				int[] bonuses = new int[18];
				for (int index = 0; index < bonuses.length; index++) {
					bonuses[index] = buffer.getShort();
				}
				itemBonuses.put(itemId, bonuses);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	public static int[] getItemBonuses(int itemId) {
		return itemBonuses.get(itemId);
	}
	
}
