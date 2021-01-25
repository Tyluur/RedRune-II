package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.cache.huffman.Huffman;
import org.redrune.cache.loaders.ItemEquipIds;
import org.redrune.engine.SystemManager;
import org.redrune.engine.worker.boot.BootHandler;
import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.combat.npc.CombatScriptsHandler;
import org.redrune.game.content.entity.actor.player.cutscene.CutscenesHandler;
import org.redrune.game.content.entity.actor.npc.FishingSpotsHandler;
import org.redrune.game.content.entity.actor.player.controller.ControllerHandler;
import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler;
import org.redrune.game.content.entity.actor.player.market.ShopRepository;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtensionHolder;
import org.redrune.game.entity.actor.player.link.FriendChatsManager;
import org.redrune.game.global.map.region.RegionBuilder;
import org.redrune.game.global.punishment.PunishmentRepository;
import org.redrune.game.global.worldlist.WorldList;
import org.redrune.networking.NetworkBinder;
import org.redrune.networking.packet.incoming.IncomingPacketRepository;
import org.redrune.utility.constants.PacketConstants;
import org.redrune.utility.functions.OutLogger;
import org.redrune.utility.game.repository.npc.NPCWalkingFlag;
import org.redrune.utility.game.entity.actor.player.Censor;
import org.redrune.utility.game.entity.object.ObjectRemoval;
import org.redrune.utility.game.entity.object.ObjectSpawning;
import org.redrune.utility.game.map.MapArchiveKeys;
import org.redrune.utility.game.repository.object.climbable.ClimbableObjectRepository;
import org.redrune.utility.game.repository.object.door.DoorRepository;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.redrune.utility.functions.ArgumentParser.parseArgs;

/**
 * This class runs the server
 *
 * @author Tyluur <itstyluur@icloud.com>
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
		initialize();
	}
	
	/**
	 * This method uses {@link BootHandler} to prepare all requirements for the game to start efficiently. All tasks
	 * that require each other are performed in the same parallel instance, other ones can be performed
	 * individually.
	 *
	 * This is a blocking method due to {@link BootHandler#await()}
	 */
	private static void initialize() {
		try {
			Cache.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BootHandler.addWork(() -> {
			try {
				System.out.println("Initializing system");
				SystemManager.initialize();
				ShopRepository.registerAll();
				System.out.println("Initializing item equip ids");
				ItemEquipIds.initialize();
				System.out.println("Initializing huffman algorithm");
				Huffman.initialize();
				System.out.println("Initializing region building");
				RegionBuilder.initialize();
				MapArchiveKeys.initialize();
				IncomingPacketRepository.initialize();
				PacketConstants.loadPacketSizes();
				Censor.initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, () -> {
			NPCWalkingFlag.registerFlags();
			NPCExtensionHolder.initialize();
			ObjectSpawning.initialize();
		}, () -> {
			WorldList.initialize();
			// door ids loaded before plugins bc of referencing
			DoorRepository.initialize();
			ClimbableObjectRepository.initialize();
			PluginRepository.registerAll();
			PunishmentRepository.loadAll();
		}, () -> {
			FishingSpotsHandler.initialize();
			CombatScriptsHandler.registerAll();
			DialogueHandler.initialize();
			ControllerHandler.registerAll();
			CutscenesHandler.init();
			ObjectRemoval.initialize();
			FriendChatsManager.initialize();
		});
		BootHandler.await();
		try {
			System.out.println("Startup took " + BootHandler.getSTOPWATCH().elapsed(TimeUnit.MILLISECONDS) + " milliseconds [hostMode=" + GameFlags.hostMode + ", debugMode=" + GameFlags.debugMode + "]");
			NetworkBinder.bind();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}