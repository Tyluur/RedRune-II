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
import com.rs.game.content.controler.ControlerHandler;
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
import com.rs.utility.game.item.ItemBonuses;
import com.rs.utility.game.item.ItemExamines;
import com.rs.utility.game.map.MapArchiveKeys;
import com.rs.utility.game.map.MapAreas;
import com.rs.utility.game.npc.*;
import com.rs.utility.game.object.ObjectSpawns;
import com.rs.utility.game.player.PkRank;
import com.rs.utility.game.player.ShopsHandler;
import com.rs.utility.system.OutLogger;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

public final class Launcher {
	
	public static void main(String[] args) throws Exception {
		long currentTime = Misc.currentTimeMillis();
		GameFlags.hostMode = false;
		GameFlags.debugMode = true;
		System.setOut(new OutLogger(System.out));
		
		Date date = new Date(currentTime);
		String displayDate = date.getMonth() + "/" + date.getDate() + "/" + String.valueOf(date.getYear());
		System.out.println("[" + displayDate + " - Launcher] - Initiating RS2Cache Files");
		Cache.init();
		ItemEquipIds.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Item Equipment IDs");
		Huffman.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Huffman");
		MapArchiveKeys.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Map xTeas");
		MapAreas.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Map areas");
		ObjectSpawns.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Object Spawns");
		NPCSpawns.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating NPC Spawns");
		NPCCombatDefinitionsL.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating NPC Combat Definitions");
		NPCBonuses.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating NPC Bonuses");
		NPCDrops.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating NPC Drops");
		ItemExamines.init();
		ItemBonuses.init();
		NPCExamines.loadExamines();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Item Handlers");
		ShopsHandler.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating ShopsHandler");
		FishingSpotsHandler.init();
		CombatScriptsHandler.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Dialogue Handlers");
		DialogueHandler.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Controllers");
		ControlerHandler.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Cutscenes");
		CutscenesHandler.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Friend Chats");
		FriendChatsManager.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Cores");
		CoresManager.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating World");
		World.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Region Builder");
		RegionBuilder.init();
		System.out.println("[" + displayDate + " - Launcher] - Initiating Server Channel Handler");
		WorldList.init();
		PluginRepository.registerAll();
		try {
			ServerChannelHandler.init();
			NPCSpawning.spawnNPCS();
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println("[" + displayDate + " - Launcher] - ERROR: COULD NOT LOAD SERVER CHANNEL HANDLER!");
			System.exit(1);
			return;
		}
		System.out.println("[" + displayDate + " - Launcher] - Server successfully bootstrapped to port: " + NetworkConstants.PORT_ID + " in " + ((Misc.currentTimeMillis() - currentTime) / 1000) + " seconds.");
		addAccountsSavingTask();
		if (GameFlags.hostMode) {
			addUpdatePlayersOnlineTask();
		}
		addCleanMemoryTask();
		// Donations.init();
	}
	
	private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL("http://127.0.0.1/matrix/updateplayeramount.php?players=" + amount + "&auth=JFHDJF3847234");
		url.openStream().close();
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
	
	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			SerializableFilesManager.savePlayer(player);
		}
		PkRank.save();
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
	
	private Launcher() {
	
	}
	
}
