package com.rs;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.GameConstants;
import com.rs.game.GameFlags;
import com.rs.game.content.controller.ControllerHandler;
import com.rs.game.content.cutscene.CutscenesHandler;
import com.rs.game.content.dialogue.DialogueHandler;
import com.rs.game.content.node.npc.FishingSpotsHandler;
import com.rs.game.entity.actor.npc.combat.CombatScriptsHandler;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.link.FriendChatsManager;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.world.World;
import com.rs.game.world.region.Region;
import com.rs.game.world.region.RegionBuilder;
import com.rs.game.world.worldlist.WorldList;
import com.rs.networking.NetworkConstants;
import com.rs.networking.ServerChannelHandler;
import com.rs.utility.Misc;
import com.rs.utility.cache.huffman.Huffman;
import com.rs.utility.game.files.SerializableFilesManager;
import com.rs.utility.game.map.MapArchiveKeys;
import com.rs.utility.game.npc.NPCWalkingFlag;
import com.rs.utility.game.object.ObjectRemoval;
import com.rs.utility.game.object.ObjectSpawns;
import com.rs.utility.system.OutLogger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class Launcher {
	
	private Launcher() {
	
	}
	
	public static void main(String[] args) throws Exception {
		long currentTime = Misc.currentTimeMillis();
		GameFlags.hostMode = false;
		GameFlags.debugMode = true;
		System.setOut(new OutLogger(System.out));
		
		System.out.println("Initiating RS2Cache Files");
		Cache.init();
		ItemEquipIds.init();
		System.out.println("Initiating Item Equipment IDs");
		Huffman.init();
		System.out.println("Initiating Huffman");
		MapArchiveKeys.init();
		System.out.println("Initiating Map xTeas");
		ObjectSpawns.init();
		System.out.println("Initiating Object Spawns");
		NPCWalkingFlag.registerFlags();
		System.out.println("Initiating Item Handlers");
		FishingSpotsHandler.init();
		CombatScriptsHandler.init();
		System.out.println("Initiating Dialogue Handlers");
		DialogueHandler.init();
		System.out.println("Initiating Controllers");
		ControllerHandler.registerAll();
		System.out.println("Initiating Cutscenes");
		CutscenesHandler.init();
		ObjectRemoval.initialize();
		System.out.println("Initiating Friend Chats");
		FriendChatsManager.init();
		System.out.println("Initiating Cores");
		CoresManager.init();
		System.out.println("Initiating World");
		World.init();
		System.out.println("Initiating Region Builder");
		RegionBuilder.init();
		System.out.println("Initiating Server Channel Handler");
		WorldList.init();
		PluginRepository.registerAll();
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println("ERROR: COULD NOT LOAD SERVER CHANNEL HANDLER!");
			System.exit(1);
			return;
		}
		System.out.println("Server successfully bootstrapped to port: " + NetworkConstants.PORT_ID + " in " + ((Misc.currentTimeMillis() - currentTime) / 1000) + " seconds.");
		addAccountsSavingTask();
		if (GameFlags.hostMode) {
			addUpdatePlayersOnlineTask();
		}
		addCleanMemoryTask();
		// Donations.init();
	}
	
	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
	
	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					setWebsitePlayersOnline(World.getPlayers().size());
				} catch (Throwable e) {
					// e.printStackTrace();
				}
			}
		}, 2, 2, TimeUnit.MINUTES);
	}
	
	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	
	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			SerializableFilesManager.savePlayer(player);
		}
	}
	
	private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL("http://127.0.0.1/matrix/updateplayeramount.php?players=" + amount + "&auth=JFHDJF3847234");
		url.openStream().close();
	}
	
	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : World.getRegions().values()) {
				region.removeMapFromMemory();
			}
		}
		for (Index index : Cache.STORE.getIndexes()) {
			index.resetCachedFiles();
		}
		CoresManager.fastExecutor.purge();
		System.gc();
	}
	
	public static void shutdown() {
		try {
			closeServices();
		} finally {
			System.exit(0);
		}
	}
	
	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (GameFlags.hostMode) {
			try {
				setWebsitePlayersOnline(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec("java -server -Xms2048m -Xmx20000m -cp bin;/data/libs/netty-3.2.7.Final.jar;/data/libs/FileStore.jar Launcher false false true false");
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}*/
	public static void restart() {
		closeServices();
		System.gc();
		try {
			Runtime.getRuntime().exec("java -server -Xms2048m -Xmx3000m -cp bin;/data/libs/netty-3.2.7.Final.jar;/data/libs/FileStore.jar Launcher true true false");
			//Runtime.getRuntime().exec("java -server -Xmx815m -cp bin;lib/*; com.rs.Launcher true true false");
			System.exit(2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
}
