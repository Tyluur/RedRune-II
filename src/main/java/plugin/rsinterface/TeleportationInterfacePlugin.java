package plugin.rsinterface;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.ColorConstants;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.constants.key.AttributeKey;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.map.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class TeleportationInterfacePlugin implements InterfacePlugin {
	
	/**
	 * The possible messages the wizard can say
	 */
	private static final String[] WIZARD_MESSAGES = new String[] { "Amitus! Setitii!", "Sparanti Morudo Calmentor!", "Daemonicas Abhoris!", "Senventior disthine molenko!" };
	
	/**
	 * The id of the interface which is scrollable and clickable with 106 options
	 */
	private static final int INTERFACE_ID = 156;
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (!player.getTemporaryAttribute("quest_selection_interface", "null").equalsIgnoreCase("teleportation")) {
			return false;
		}
		
		int teleportSlotId = (componentId - 7);
		TravelLocations uncollapsed = player.getTemporaryAttribute("uncollapsed_teleport") != null ? player.getTemporaryAttribute("uncollapsed_teleport") : null;
		
		// a player has not selected a place to travel to
		if (uncollapsed == null) {
			if (teleportSlotId >= 0 && teleportSlotId < TravelLocations.values().length) {
				player.putTemporaryAttribute("uncollapsed_teleport", uncollapsed = TravelLocations.values()[teleportSlotId]);
				uncollapse(player, uncollapsed);
			}
		} else {
			int uncollapsedTeleportsSlot = uncollapsed.ordinal();
			int uncollapsedTeleportsStart = (uncollapsed.ordinal()) + 1;
			int uncollapsedTeleportsEnd = (uncollapsed.ordinal()) + (uncollapsed.destinations.size());
			
			if (teleportSlotId < uncollapsedTeleportsStart) {
				if (teleportSlotId == uncollapsedTeleportsSlot) {
					displaySelectionInterface(player, false);
					player.getAttributes().removeAttribute(AttributeKey.LAST_UNCOLLAPSED_TELEPORT);
				} else {
					player.putTemporaryAttribute("uncollapsed_teleport", uncollapsed = TravelLocations.values()[teleportSlotId]);
					uncollapse(player, uncollapsed);
				}
			} else if (teleportSlotId > uncollapsedTeleportsEnd) {
				Object[] uncollapsedArray = generateUncollapsedArray(uncollapsed);
				if (teleportSlotId >= uncollapsedArray.length) {
					return true;
				}
				Object newDestination = uncollapsedArray[teleportSlotId];
				if (newDestination instanceof TravelLocations) {
					displaySelectionInterface(player, true);
					player.putTemporaryAttribute("uncollapsed_teleport", uncollapsed = (TravelLocations) newDestination);
					uncollapse(player, uncollapsed);
				}
			} else if (teleportSlotId >= uncollapsedTeleportsStart && teleportSlotId <= uncollapsedTeleportsEnd) {
				List<Object[]> destinations = uncollapsed.destinations;
				int destinationIndex = (teleportSlotId) - uncollapsedTeleportsStart;
				teleport(player, (WorldTile) destinations.get(destinationIndex)[1], uncollapsed, destinationIndex);
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(INTERFACE_ID);
	}
	
	/**
	 * Uncollapses a travel location for a player
	 *
	 * @param player
	 * 		The player
	 * @param travelLocations
	 * 		The {@code TravelLocations} {@code Object} to be uncollapsed
	 */
	private static void uncollapse(Player player, TravelLocations travelLocations) {
		int interfaceId = 156;
		int start = 7;
		
		for (int i = start; i < 108; i++) {
			player.getPackets().sendIComponentText(interfaceId, i, "");
		}
		for (TravelLocations locations : TravelLocations.values()) {
			sendLocationText(player, locations, start);
			start++;
			
			// we're looping on the one we should be uncollapsing
			if (locations.equals(travelLocations)) {
				for (Object[] destinations : locations.destinations) {
					player.getPackets().sendIComponentText(interfaceId, start, ">>   " + destinations[0]);
					start++;
				}
			}
		}
		player.getAttributes().putAttribute(AttributeKey.LAST_UNCOLLAPSED_TELEPORT, travelLocations);
	}
	
	/**
	 * Displays the interface to select a teleport
	 *
	 * @param player
	 * 		The player
	 * @param showLastUncollapsed
	 * 		If the last {@code TravelLocations} {@code Object} the player viewed should be shown
	 */
	public static void displaySelectionInterface(Player player, boolean showLastUncollapsed) {
		int interfaceId = 156;
		int start = 7;
		
		player.getInterfaceManager().sendInterface(interfaceId);
		player.getPackets().sendRunScript(677, 100);
		for (int i = start; i < 108; i++) {
			player.getPackets().sendIComponentText(interfaceId, i, "");
		}
		for (TravelLocations locations : TravelLocations.values()) {
			sendLocationText(player, locations, start);
			start++;
		}
		player.getPackets().sendGlobalString(211, "Select a Destination");
		player.putTemporaryAttribute("quest_selection_interface", "teleportation");
		player.removeTemporaryAttribute("uncollapsed_teleport");
		
		if (!showLastUncollapsed) {
			return;
		}
		Object last = player.getAttributes().getAttribute(AttributeKey.LAST_UNCOLLAPSED_TELEPORT, null);
		if (last != null) {
			TravelLocations locations = TravelLocations.valueOf(last.toString());
			player.putTemporaryAttribute("uncollapsed_teleport", locations);
			uncollapse(player, locations);
		}
	}
	
	private static Object[] generateUncollapsedArray(TravelLocations uncollapsed) {
		List<Object> list = new ArrayList<>();
		
		for (TravelLocations location : TravelLocations.values()) {
			list.add(location);
			if (location.equals(uncollapsed)) {
				list.addAll(new ArrayList<>(uncollapsed.destinations));
			}
		}
		return list.toArray(new Object[list.size()]);
	}
	
	/**
	 * Teleports a player to the destination and handles post teleportation
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 * @param travelLocations
	 * 		The travelLocations we're on
	 * @param optionIndex
	 * 		The option index of the teleport
	 */
	private static void teleport(Player player, WorldTile destination, TravelLocations travelLocations, int optionIndex) {
		if (Wilderness.isAtWild(destination)) {
			player.getDialogueManager().startDialogue(new Dialogue() {
				
				@Override
				public void start() {
					npc(1263, NORMAL, "This destination is in the wilderness.", "Are you sure you wish to travel here?");
				}
				
				@Override
				public void run(int interfaceId, int option) {
					switch (stage) {
						case -1:
							options(DEFAULT_OPTION, "Yes, I want to travel to a wilderness location.", "No, thanks for the notification!");
							stage = 0;
							break;
						case 0:
							if (option == FIRST) {
								teleportPlayer(player, destination, () -> travelLocations.handlePostTeleportation(player, optionIndex));
							}
							end();
							break;
					}
				}
				
				@Override
				public void finish() {
				}
				
			});
		} else {
			player.getAttributes().putAttribute(AttributeKey.LAST_TRANSPORTATION_LOCATION, new TransportationLocation(destination, travelLocations, optionIndex));
			teleportPlayer(player, destination, () -> travelLocations.handlePostTeleportation(player, optionIndex));
		}
	}
	
	/**
	 * Sends the location text
	 *
	 * @param player
	 * 		The player
	 * @param locations
	 * 		The {@code TravelLocations} {@code Object}
	 * @param slot
	 * 		The slot of the text
	 */
	private static void sendLocationText(Player player, TravelLocations locations, int slot) {
		player.getPackets().sendIComponentText(156, slot, "<u><col=" + ColorConstants.MAROON + ">" + locations.getTitle() + "</u>");
	}
	
	/**
	 * Teleports the player to the destination and performs some graphical things to make it look cool
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 * @param task
	 * 		The task to be performed once the teleport is done
	 */
	public static void teleportPlayer(Player player, WorldTile destination, Runnable task) {
		player.closeInterfaces();
		NPC wizard = Misc.findLocalNPC(player, 1263);
		
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				Magic.sendTeleportSpell(player, 14293, -1, 94, -1, 0, 0, destination, 6, false, MagicConstants.MAGIC_TELEPORT);
				player.setCloseInterfacesEvent(task);
			}
		}, 1);
		
		if (wizard == null) {
			return;
		}
		
		wizard.resetWalkSteps();
		wizard.setNextFaceWorldTile(player);
		wizard.setNextForceTalk(new ForceTalk(Misc.randomArraySlot(WIZARD_MESSAGES)));
	}
	
	public enum TravelLocations implements Coordinates {
		
		PVP("PvP") {
			@Override
			public void populateDestinations() {
				add("Revenant Cave", REVENANTS_CAVE, "East Dragons", EAST_DRAGONS, "West Dragons", WEST_DRAGONS, "Graveyard", GRAVEYARD, "Obelisk: Lvl 50", LVL_50_OBELISK, "Mage Bank", MAGE_BANK);
			}
		},
		
		MINIGAMES("Minigames") {
			@Override
			public void populateDestinations() {
				add("Duel Arena", DUEL_ARENA, "Pest Control", PEST_CONTROL, "Fight Caves", TZHAAR, "Barrows", BARROW, "Warriors Guild", WARRIORS_GUILD, "Clan Wars", CLAN_WARS, "Dicing", DICING_AREA/*, "Castle Wars", CASTLE_WARS*/);
			}
		},
		
		BOSSES("Bosses") {
			@Override
			public void populateDestinations() {
				add("Nex", NEX_DUNGEON, "Godwars", GODWARS_DUNGEON, "Glacors", GLACOR_DUNGEON, "Kalphite Queen", KALPHITE_QUEEN, "King Black Dragon", KING_BLACK_DRAGON, "Chaos Elemental", CHAOS_ELEMENTAL, "Frost Dragons", FROST_DRAGONS, "Tormented Demons", TORMENTED_DEMONS);
				add("Dagannoth Kings", DAGANNOTH_KINGS, "Corporeal Beast", CORPOREAL_BEAST, "Ice Strykwyrms", STRYKEWYRM_DUNGEON, "Sea Troll Queen", SEA_TROLL_QUEEN, "Bork", BORK);
			}
			
			@Override
			public void handlePostTeleportation(Player player, int index) {
				switch (index) {
					case 0:
					case 1:
						player.getControllerManager().startController("GodWars");
						break;
				}
			}
		},
		
		SKILLING("Skilling") {
			@Override
			public void populateDestinations() {
				add("Skill Zone", SKILL_ZONE, "Gnome Agility Course", GNOME_AGILITY, "Barbarian Agility Course", BARBARIAN_AGILITY, "Wilderness Agility Course", WILDERNESS_AGILITY, "The Abyss", ABYSS);
				add("Catherby Farming", new WorldTile(2817, 3460, 0), "Essence Mine", ESSENCE_MINE, "Plank Making", LUMBER_YARD_PLANKS, "Living Rock Cavern", LIVING_ROCK_CAVERNS, "Hunter Training", HUNTER_TRAINING);
				add("Desert Phoenix Lair", new WorldTile(3414, 3157, 0), "Rogues' Den", ROGUES_DEN);
			}
		},
		
		MONSTERS("Monsters") {
			@Override
			public void populateDestinations() {
				add("Rock Crabs", ROCK_CRABS, "Experiments", EXPERIMENTS, "Ogres", OGRES, "Yaks", YAKS, "Bandits", BANDITS, "Moss Giants", MOSS_GIANTS, "Chaos Druids", DRUIDS, "Tzhaar", TZHAAR, "Dust Devils", DUST_DEVILS);
				add("Ape-Atoll Guards", MONKEY_GUARDS, "Armoured Zombies", ARMOURED_ZOMBIES, "Chaos Tunnels", CHAOS_TUNNELS, "Ice Giants", ICE_GIANTS, "Chickens", CHICKENS, "Monkey Skeletons", APE_ATOLL_DUNGEON);
			}
		},
		
		DUNGEONS("Dungeons") {
			@Override
			public void populateDestinations() {
				add("Slayer Tower", SLAYER_TOWER, "Taverly Dungeon", TAVERLY_DUNGEON, "Fremennik Slayer Dungeon", FREMENNIK_SLAYER_DUNGEON, "Brimhaven Dungeon", BRIMHAVEN_DUNGEON, "Kuradal's Dungeon", KURADAL_SLAYER_DUNGEON, "Asgarnian Dungeon", ASGARNIAN_ICE_DUNGEON, "Ancient Cavern", ANCIENT_CAVERN, "Jadinko Lair", JADINKO_LAIR);
			}
		},
		
		CITIES("Cities") {
			@Override
			public void populateDestinations() {
				add("Varrock", VARROCK, "Falador", FALADOR, "Camelot", CAMELOT, "Draynor", DRAYNOR, "Catherby", CATHERBY, "Al Kharid", AL_KHARID, "Karamja", KARAMJA, "Lumbridge", LUMBRIDGE, "Neitiznot", NEITIZNOT);
				add("Ardougne", ARDOUGNE, "Rellekka", RELLEKKA, "Miscellania", MISCELLANIA, "The Grand Tree", GRAND_TREE, "Yanille", YANILLE, "Watchtower", WATCHTOWER);
			}
		};
		
		static {
			Arrays.stream(values()).forEach(TravelLocations::populateDestinations);
		}
		
		/**
		 * The method used to populate the destinations
		 */
		public abstract void populateDestinations();
		
		/**
		 * The list of destinations that can be travelled to, with the first slot in the Object[] as the name of the
		 * destination, and the second slot as the {@code WorldTile} {@code Object}
		 */
		private final List<Object[]> destinations = new ArrayList<>();
		
		/**
		 * The title of the teleport
		 */

		private final String title;
		
		TravelLocations(String title) {
			this.title = title;
		}
		
		/**
		 * Adds destinations to the {@link #destinations} list
		 *
		 * @param params
		 * 		The parameters, {@code String} first then {@code WorldTile}
		 */
		protected void add(Object... params) {
			for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				if (param instanceof String) {
					String name = (String) param;
					Object proceeding = params[i + 1];
					if (proceeding instanceof WorldTile) {
						destinations.add(new Object[] { name, proceeding });
					} else {
						throw new IllegalStateException("Unexpected parameter " + proceeding + " in " + this + " TravelLocation");
					}
				}
			}
		}
		
		/**
		 * Handles actions after the teleport has been sent
		 *
		 * @param player
		 * 		The player
		 * @param index
		 * 		The index of the teleport
		 */
		public void handlePostTeleportation(Player player, int index) {
		
		}

		public String getTitle() {
			return title;
		}
	}
	
	public static final class TransportationLocation implements Serializable {
		
		private static final long serialVersionUID = 2836199090952003422L;
		

		private final WorldTile destination;
		

		private final TravelLocations locations;
		

		private final int optionIndex;
		
		public TransportationLocation(WorldTile destination, TravelLocations locations, int optionIndex) {
			this.destination = destination;
			this.locations = locations;
			this.optionIndex = optionIndex;
		}

		public WorldTile getDestination() {
			return destination;
		}

		public TravelLocations getLocations() {
			return locations;
		}

		public int getOptionIndex() {
			return optionIndex;
		}
	}
	
}
