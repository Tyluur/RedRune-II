package com.rs;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemEquipIds;
import com.rs.cores.CoresManager;
import com.rs.game.content.controller.ControllerHandler;
import com.rs.game.content.cutscene.CutscenesHandler;
import com.rs.game.content.dialogue.DialogueHandler;
import com.rs.game.content.market.ShopRepository;
import com.rs.game.content.node.npc.FishingSpotsHandler;
import com.rs.game.content.combat.npc.CombatScriptsHandler;
import com.rs.game.entity.actor.player.link.FriendChatsManager;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.world.region.RegionBuilder;
import com.rs.game.world.worldlist.WorldList;
import com.rs.networking.NetworkConstants;
import com.rs.networking.ServerChannelHandler;
import com.rs.utility.Misc;
import com.rs.utility.cache.huffman.Huffman;
import com.rs.utility.game.map.MapArchiveKeys;
import com.rs.utility.game.npc.NPCWalkingFlag;
import com.rs.utility.game.object.ObjectRemoval;
import com.rs.utility.game.object.ObjectSpawns;
import com.rs.utility.system.OutLogger;
import org.apache.commons.cli.*;

import static com.rs.game.GameFlags.debugMode;
import static com.rs.game.GameFlags.hostMode;

/**
 * This class handles the startup of the game.
 */
public final class Launcher {
	
	/**
	 * The main method invoked by the jvm
	 */
	public static void main(String[] args) throws Exception {
		System.setOut(new OutLogger(System.out));
		parseArgs(args);
		long currentTime = Misc.currentTimeMillis();
		
		System.out.println("Initiating RS2Cache Files");
		Cache.init();
		ShopRepository.registerAll();
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
		CombatScriptsHandler.registerAll();
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
		System.out.println("Initiating Region Builder");
		RegionBuilder.init();
		System.out.println("Initiating Server Channel Handler");
		WorldList.registerAll();
		PluginRepository.registerAll();
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		System.out.println("Server successfully bootstrapped to port: " + NetworkConstants.PORT_ID + " in " + ((Misc.currentTimeMillis() - currentTime) / 1000) + " seconds.");
	}
	
	/**
	 * Parses the arguments from the jvm
	 */
	private static void parseArgs(String[] args) {
		Options options = new Options();
		Option hostOption = new Option("hostMode", "input", true, "game host mode");
		hostOption.setRequired(true);
		Option debugOption = new Option("debugMode", "input", true, "debug server mode");
		options.addOption(hostOption);
		options.addOption(debugOption);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
			return;
		}
		
		hostMode = Boolean.parseBoolean(cmd.getOptionValue("hostMode"));
		debugMode = Boolean.parseBoolean(cmd.getOptionValue("debugMode"));
		System.out.println("hostMode=" + hostMode + ", debugMode=" + debugMode);
	}
	
}