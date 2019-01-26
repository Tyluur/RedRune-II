package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.cache.huffman.Huffman;
import org.redrune.cache.loaders.ItemEquipIds;
import org.redrune.engine.SystemManager;
import org.redrune.engine.boot.BootHandler;
import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.npc.FishingSpotsHandler;
import org.redrune.game.content.combat.npc.CombatScriptsHandler;
import org.redrune.game.content.entity.actor.player.controller.ControllerHandler;
import org.redrune.game.content.cutscene.CutscenesHandler;
import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler;
import org.redrune.game.content.entity.actor.player.market.ShopRepository;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtensionHolder;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.global.map.region.RegionBuilder;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.worldlist.WorldList;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.networking.ServerChannelHandler;
import org.redrune.networking.codec.packet.IncomingPacketRepository;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.OutLogger;
import org.redrune.utility.game.entity.actor.npc.NPCWalkingFlag;
import org.redrune.utility.game.entity.object.ObjectRemoval;
import org.redrune.utility.game.entity.object.ObjectSpawns;
import org.redrune.utility.game.map.MapArchiveKeys;

import java.util.concurrent.TimeUnit;

import static org.redrune.utility.functions.ArgumentParser.parseArgs;

/**
 * This class runs the server
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since January 25th, 2019
 */
public final class Bootstrap {
	
	/**
	 * The main method invoked by the jvm
	 */
	public static void main(String[] args) {
		// making sure the RedRune logger is used before anything is printed
		System.setOut(new OutLogger(System.out));
		// parsing the arguments
		parseArgs(args);
		// startup work
		registerStartupRequirements();
		
		System.out.println("Server successfully bound game server to port: " + NetworkConstants.PORT_ID + " in " + BootHandler.getSTOPWATCH().elapsed(TimeUnit.MILLISECONDS) + " milliseconds [hostMode=" + GameFlags.hostMode + ", debugMode=" + GameFlags.debugMode + "]");
	}
	
	/**
	 * This method uses {@link BootHandler} to prepare all requirements for the game to start efficiently. All tasks
	 * that require each other are are performed in the same parallel instance, other ones can be performed
	 * individually.
	 *
	 * This is a blocking method due to {@link BootHandler#await()}
	 */
	private static void registerStartupRequirements() {
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
	}
	
}