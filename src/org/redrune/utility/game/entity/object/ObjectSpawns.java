package org.redrune.utility.game.entity.object;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public final class ObjectSpawns {
	
	private static final String PACKED_DIRECTORY = "data/repository/map/packedSpawns/";
	
	private static final String UNPACKED_LOCATION = "data/repository/map/unpackedSpawnsList.txt";
	
	private ObjectSpawns() {
	}
	
	public static void initialize() {
		if (!new File(PACKED_DIRECTORY).exists()) {
			packObjectSpawns();
		}
	}
	
	private static void packObjectSpawns() {
		long start = System.currentTimeMillis();
		System.out.println("Packing object spawns...");
		if (!new File(PACKED_DIRECTORY).mkdir()) {
			throw new RuntimeException("Couldn't create packedSpawns directory.");
		}
		for (String line : Misc.getFileText(UNPACKED_LOCATION)) {
			if (line.startsWith("//")) {
				continue;
			}
			line = line.replaceAll("//", "");
			String[] splitedLine = line.split(" ~ ");
			if (splitedLine.length != 2) {
				throw new RuntimeException("Invalid Object Spawn line: " + line);
			}
			String[] objectInformation = splitedLine[0].split(" ");
			String[] spawnInformation = splitedLine[1].split(" ");
			int objectId = Integer.parseInt(objectInformation[0]);
			int type = Integer.parseInt(objectInformation[1]);
			int rotation = Integer.parseInt(objectInformation[2]);
			
			WorldTile tile = new WorldTile(Integer.parseInt(spawnInformation[0]), Integer.parseInt(spawnInformation[1]), Integer.parseInt(spawnInformation[2]));
			addObjectSpawn(objectId, type, rotation, tile.getRegionId(), tile, Boolean.parseBoolean(spawnInformation[3]));
		}
		System.out.println("Packed in " + (System.currentTimeMillis() - start) + " ms.");
	}
	
	private static void addObjectSpawn(int objectId, int type, int rotation, int regionId, WorldTile tile, boolean cliped) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_DIRECTORY + regionId + ".os", true));
			out.writeShort(objectId);
			out.writeByte(type);
			out.writeByte(rotation);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(cliped);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void loadObjectSpawns(int regionId) {
		File file = new File(PACKED_DIRECTORY + regionId + ".os");
		if (!file.exists()) {
			return;
		}
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int objectId = buffer.getShort() & 0xffff;
				int type = buffer.get() & 0xff;
				int rotation = buffer.get() & 0xff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean cliped = buffer.get() == 1;
				RegionManager.spawnObject(new WorldObject(objectId, type, rotation, x, y, plane));
			}
			channel.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void dumpObjectSpawn(int objectId, int type, int rotation, WorldTile tile) throws IOException {
		File file = new File(UNPACKED_LOCATION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		// 47120 10 0 - 2591 3919 0 true
		writer.write(objectId + " " + type + " " + rotation + " ~ " + tile.getX() + " " + tile.getY() + " " + tile.getPlane() + " true // " + ObjectDefinitions.getObjectDefinitions(objectId).getName());
		writer.newLine();
		writer.flush();
		writer.close();
		if (!Misc.deleteDirectory(new File(PACKED_DIRECTORY))) {
			System.out.println("Could not delete directory...");
		}
	}
	
}
