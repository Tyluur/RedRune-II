package org.redrune;

import org.apache.commons.cli.*;
import org.redrune.cache.Cache;
import org.redrune.cache.huffman.Huffman;
import org.redrune.cache.loaders.ItemEquipIds;
import org.redrune.engine.SystemManager;
import org.redrune.engine.boot.BootHandler;
import org.redrune.game.GameFlags;
import org.redrune.game.content.actor.npc.FishingSpotsHandler;
import org.redrune.game.content.combat.npc.CombatScriptsHandler;
import org.redrune.game.content.controller.ControllerHandler;
import org.redrune.game.content.cutscene.CutscenesHandler;
import org.redrune.game.content.dialogue.DialogueHandler;
import org.redrune.game.content.market.ShopRepository;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtensionHolder;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.global.map.region.RegionBuilder;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.worldlist.WorldList;
import org.redrune.game.plugin.PluginRepository;
import org.redrune.networking.ServerChannelHandler;
import org.redrune.networking.codec.packet.IncomingPacketRepository;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.game.map.MapArchiveKeys;
import org.redrune.utility.game.entity.actor.npc.NPCWalkingFlag;
import org.redrune.utility.game.entity.object.ObjectRemoval;
import org.redrune.utility.game.entity.object.ObjectSpawns;
import org.redrune.utility.functions.OutLogger;

import java.util.concurrent.TimeUnit;

/**
 * This class handles the startup of the game.
 */
public final class Bootstrap {
	
	/**
	 * The main method invoked by the jvm
	 */
	public static void main(String[] args) {
		System.setOut(new OutLogger(System.out));
		parseArgs(args);
		BootHandler.addWork(() -> {
			try {
				System.out.println("Initializing cache");
				Cache.initialize();
				System.out.println("Initializing system");
				SystemManager.initialize();
				ShopRepository.registerAll();
				System.out.println("Initializing item equip ids");
				ItemEquipIds.initialize();
				System.out.println("Initializing huffman algorithm");
				Huffman.initialize();
				System.out.println("Initializing region builidng");
				RegionBuilder.initialize();
				MapArchiveKeys.initialize();
				ObjectSpawns.initialize();
				IncomingPacketRepository.initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, () -> {
			NPCWalkingFlag.registerFlags();
			NPCExtensionHolder.initialize();
		}, () -> {
			WorldList.initialize();
			PluginRepository.registerAll();
			PunishmentRepository.loadAll();
		}, () -> {
			FishingSpotsHandler.initialize();
			CombatScriptsHandler.registerAll();
			DialogueHandler.init();
			ControllerHandler.registerAll();
			CutscenesHandler.init();
			ObjectRemoval.initialize();
			FriendChatsManager.initialize();
		});
		BootHandler.await();
		try {
			ServerChannelHandler.init();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Server successfully binded game server to port: " + NetworkConstants.PORT_ID + " in " + BootHandler.getSTOPWATCH().elapsed(TimeUnit.MILLISECONDS) + " milliseconds [hostMode=" + GameFlags.hostMode + ", debugMode=" + GameFlags.debugMode + "]");
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
		GameFlags.hostMode = Boolean.parseBoolean(cmd.getOptionValue("hostMode"));
		GameFlags.debugMode = Boolean.parseBoolean(cmd.getOptionValue("debugMode"));
	}
	
}