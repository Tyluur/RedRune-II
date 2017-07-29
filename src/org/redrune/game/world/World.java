package org.redrune.game.world;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.boot.BootHandler;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.impl.EnergyRestorationTask;
import org.redrune.core.task.impl.HitpointsRestorationTask;
import org.redrune.core.task.impl.SkillRestorationTask;
import org.redrune.game.GameConstants;
import org.redrune.game.GameFlags;
import org.redrune.game.content.activity.impl.WildernessActivity;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.market.shop.ShopRepository;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.EntityList;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.extension.RockCrabNPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.RegionBuilder;
import org.redrune.game.world.region.RegionDeletion;
import org.redrune.network.master.client.MasterCommunication;
import org.redrune.network.world.WorldNetwork;
import org.redrune.network.world.packet.incoming.IncomingPacketRepository;
import org.redrune.network.world.packet.incoming.impl.WalkPacketDecoder;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.utility.backend.SequentialService;
import org.redrune.utility.backend.UnexpectedArgsException;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.repository.npc.combat.NPCCombatSwingRepository;
import org.redrune.utility.repository.npc.spawn.NPCSpawn;
import org.redrune.utility.repository.object.ObjectSpawnRepository;
import org.redrune.utility.rs.constant.Directions.Direction;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Contains all the collections and data to handle a world.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class World implements SequentialService {
	
	/**
	 * The world singleton
	 */
	private static World singleton = null;
	
	/**
	 * The id of the world
	 */
	private final byte id;
	
	/**
	 * The list of all players in the world
	 */
	@Getter
	private final EntityList<Player> players = new EntityList<>(GameConstants.PLAYERS_LIMIT, true);
	
	/**
	 * The {@code EntityList} of all npcs that exist.
	 */
	@Getter
	private final EntityList<NPC> npcs = new EntityList<>(GameConstants.NPCS_LIMIT, false);
	
	/**
	 * The instance of the packet repository
	 */
	@Getter
	private final IncomingPacketRepository packetRepository = new IncomingPacketRepository(WalkPacketDecoder.class.getPackage().getName());
	
	/**
	 * The instance of the stopwatch
	 */
	@Getter
	private final Stopwatch stopwatch = Stopwatch.createUnstarted();
	
	/**
	 * If the world is alive
	 */
	@Getter
	@Setter
	private boolean isAlive;
	
	/**
	 * Constructs a new world object
	 */
	public World(String[] args) {
		try {
			SystemManager.setDefaults(args);
		} catch (UnexpectedArgsException e) {
			UnexpectedArgsException.push();
			System.exit(1);
		}
		this.id = GameFlags.worldId;
		packetRepository.storeAll();
		setAlive(true);
	}
	
	@Override
	public void start() {
		// startup necessities
		stopwatch.start();
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
			DialogueRepository.loadSubscriptions();
			NPCCombatSwingRepository.loadAll();
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
		System.out.println("Started world " + id + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
		// finalization
		SystemManager.start();
		// master server can now listen
		MasterCommunication.start();
		// start the world tasks now that everything has loaded
		generateWorldTasks();
		try {
			// this waits for the session to close, so anything after this method will not execute until shutdown
			WorldNetwork.bind();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Generates tasks that operate for the world
	 */
	private void generateWorldTasks() {
		SystemManager.getScheduler().schedule(new EnergyRestorationTask());
		SystemManager.getScheduler().schedule(new SkillRestorationTask());
		SystemManager.getScheduler().schedule(new HitpointsRestorationTask());
	}
	
	/**
	 * Creates a new world
	 *
	 * @param args
	 * 		The arguments of the world
	 */
	public static World create(String[] args) {
		synchronized (World.class) {
			return singleton = new World(args);
		}
	}
	
	/**
	 * Gets the singleton instance
	 *
	 * @return A {@code World} {@code Object}
	 */
	public static World get() {
		return singleton;
	}
	
	/**
	 * Adds an npc to the world, in the form of a {@link NPCSpawn} {@code Object}
	 *
	 * @param spawn
	 * 		The {@code NPCSpawn} object
	 */
	public World addSpawn(NPCSpawn spawn) {
		addNPC(spawn.getNpcId(), spawn.getTile(), spawn.getDirection());
		return this;
	}
	
	/**
	 * Adds an npc to the world
	 *
	 * @param id
	 * 		The id of the npc
	 * @param location
	 * 		The location of the npc
	 * @return The npc that was constructed
	 */
	public NPC addNPC(int id, Location location, Direction direction) {
		final NPC npc;
		if (id == 1266 || id == 1268 || id == 2453 || id == 2886) {
			npc = new RockCrabNPC(id, location, direction);
		} else {
			npc = new NPC(id, location, direction);
		}
		npc.register();
		npcs.add(npc);
		return npc;
	}
	
	/**
	 * Handles the removal of a player
	 *
	 * @param player
	 * 		The player to remove
	 */
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	/**
	 * Finds a player by their username
	 *
	 * @param username
	 * 		The username of the player
	 */
	public Optional<Player> getPlayerByUsername(String username) {
		return players.stream().filter(player -> player.getDetails().getUsername().equalsIgnoreCase(username)).findAny();
	}
	
	/**
	 * Checks if the tile is a pvp area
	 *
	 * @param location
	 * 		The tile
	 */
	public boolean isPvpArea(Location location) {
		// TODO: pvp area
		if (id == 2) {
			return WildernessActivity.isAtWild(location);
		} else {
			return WildernessActivity.isAtWild(location);
		}
	}
}
