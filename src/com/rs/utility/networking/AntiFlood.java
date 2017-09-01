package com.rs.utility.networking;

import com.rs.game.GameConstants;

import java.util.ArrayList;

/**
 * Anti Flood
 *
 * @Author Apache Ah64
 */
public class AntiFlood {
	
	private static ArrayList<String> connections = new ArrayList<String>(GameConstants.PLAYERS_LIMIT * 3);
	
	public static boolean contains(String ip) {
		return connections.contains(ip);
	}
	
	public static void add(String ip) {
		// if(!connections.contains(ip))
		connections.add(ip);
	}
	
	public static void remove(String ip) {
		if (connections.contains(ip)) {
			connections.remove(ip);
		}
	}
	
	public static int getSessionsIP(String ip) {
		int amount = 1;
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).equalsIgnoreCase(ip)) {
				amount++;
			}
		}
		return amount;
	}
	
	public static ArrayList<String> getConnections() {
		return connections;
	}
}