package com.rs.utility.game.object;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.World;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public final class ObjectSpawns {
	
	private ObjectSpawns() {
	}
	
	public static final void init() {
		if (!new File("data/repository/map/packedSpawns").exists()) {
			packObjectSpawns();
		}
	}
	
	private static final void packObjectSpawns() {
		System.out.println("Packing object spawns...");
		if (!new File("data/repository/map/packedSpawns").mkdir()) {
			throw new RuntimeException("Couldn't create packedSpawns directory.");
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/repository/map/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("//")) {
					continue;
				}
				String[] splitedLine = line.split(" - ");
				if (splitedLine.length != 2) {
					throw new RuntimeException("Invalid Object Spawn line: " + line);
				}
				String[] splitedLine2 = splitedLine[0].split(" ");
				String[] splitedLine3 = splitedLine[1].split(" ");
				if (splitedLine2.length != 3 || splitedLine3.length != 4) {
					throw new RuntimeException("Invalid Object Spawn line: " + line);
				}
				int objectId = Integer.parseInt(splitedLine2[0]);
				int type = Integer.parseInt(splitedLine2[1]);
				int rotation = Integer.parseInt(splitedLine2[2]);
				
				WorldTile tile = new WorldTile(Integer.parseInt(splitedLine3[0]), Integer.parseInt(splitedLine3[1]), Integer.parseInt(splitedLine3[2]));
				addObjectSpawn(objectId, type, rotation, tile.getRegionId(), tile, Boolean.parseBoolean(splitedLine3[3]));
			}
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static final void addObjectSpawn(int objectId, int type, int rotation, int regionId, WorldTile tile, boolean cliped) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream("data/repository/map/packedSpawns/" + regionId + ".os", true));
			out.writeShort(objectId);
			out.writeByte(type);
			out.writeByte(rotation);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(cliped);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void loadObjectSpawns(int regionId) {
		File file = new File("data/repository/map/packedSpawns/" + regionId + ".os");
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
				World.spawnObject(new WorldObject(objectId, type, rotation, x, y, plane), cliped);
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
