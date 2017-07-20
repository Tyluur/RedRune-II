package org.redrune.game;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.boot.BootHandler;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.market.shop.ShopRepository;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.world.region.RegionBuilder;
import org.redrune.game.world.region.RegionDeletion;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.world.packet.incoming.IncomingPacketRepository;
import org.redrune.network.world.WorldNetwork;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.utility.backend.SequentialService;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.repository.object.ObjectSpawnRepository;
import org.redrune.utility.tool.Misc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/18/2017
 */
public final class GameServer implements SequentialService {
	
	/**
	 * The logger instance
	 */
	private static final Logger LOGGER = Misc.constructLogger(GameServer.class);
	
	/**
	 * The instance of the stopwatch
	 */
	@Getter
	private final Stopwatch stopwatch = Stopwatch.createUnstarted();
	
	/**
	 * The arguments of the server in the jvm
	 */
	private final String[] args;
	
	public GameServer(String[] args) {
		this.args = args;
	}
	
	@Override
	public void start() {
		if (args.length == 0) {
			System.err.println("Unexpected end of JVM arguments!");
			System.err.println("args[0]=[true/false] - debug mode");
			System.err.println("args[1]=[byte] - worldId");
			System.exit(1);
			return;
		}
		// startup necessities
		stopwatch.start();
		// set the debug flags
		SystemManager.setDefaults(args);
	}
	
	@Override
	public void execute() {
		BootHandler.addWork(() -> {
			Cache.init();
			BodyDataParser.loadAll();
			RegionBuilder.init();
			CombatRegistry.registerAll();
			ItemDefinitionParser.loadEquipmentConfiguration();
			ShopRepository.load();
		}, () -> {
			ItemRepository.initialize(false);
			MapKeyRepository.readAll();
		}, () -> {
			RegionDeletion.prepare();
			ObjectSpawnRepository.get().loadAll();
		}, () -> {
			IncomingPacketRepository.storeAll();
			DialogueRepository.loadSubscriptions();
		}, () -> {
			EventRepository.registerEvents(false);
		}, () -> {
			ModuleRepository.registerAllModules(false);
			CommandRepository.populate(false);
		});
		BootHandler.await();
	}
	
	@Override
	public void end() {
		// finalization
		SystemManager.start();
		// master server can now listen
		MasterCommunication.start();
		try {
			WorldNetwork.bind();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		LOGGER.info("Successfully started " + GameConstants.SERVER_NAME + " #" + NetworkConstants.REVISION + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
	}
}
