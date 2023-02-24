package plugin.rsinterface

import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.ColorConstants
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.key.AttributeKey
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.map.Coordinates
import java.io.Serializable
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/12/2017
 */
class TeleportationInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        if (!player.getTemporaryAttribute("quest_selection_interface", "null")
                .equals("teleportation", ignoreCase = true)
        ) {
            return false
        }
        val teleportSlotId = componentId - 7
        var uncollapsed =
            if (player.getTemporaryAttribute<Any?>("uncollapsed_teleport") != null) player.getTemporaryAttribute<TravelLocations>(
                "uncollapsed_teleport"
            ) else null

        // a player has not selected a place to travel to
        if (uncollapsed == null) {
            if (teleportSlotId >= 0 && teleportSlotId < TravelLocations.values().size) {
                player.putTemporaryAttribute(
                    "uncollapsed_teleport",
                    TravelLocations.values()[teleportSlotId].also { uncollapsed = it })
                uncollapse(player, uncollapsed)
            }
        } else {
            val uncollapsedTeleportsSlot = uncollapsed!!.ordinal
            val uncollapsedTeleportsStart = uncollapsed!!.ordinal + 1
            val uncollapsedTeleportsEnd = uncollapsed!!.ordinal + uncollapsed!!.destinations.size
            if (teleportSlotId < uncollapsedTeleportsStart) {
                if (teleportSlotId == uncollapsedTeleportsSlot) {
                    displaySelectionInterface(player, false)
                    player.attributes.removeAttribute<Any>(AttributeKey.LAST_UNCOLLAPSED_TELEPORT)
                } else {
                    player.putTemporaryAttribute(
                        "uncollapsed_teleport",
                        TravelLocations.values()[teleportSlotId].also { uncollapsed = it })
                    uncollapse(player, uncollapsed)
                }
            } else if (teleportSlotId > uncollapsedTeleportsEnd) {
                val uncollapsedArray = generateUncollapsedArray(
                    uncollapsed!!
                )
                if (teleportSlotId >= uncollapsedArray.size) {
                    return true
                }
                val newDestination = uncollapsedArray[teleportSlotId]
                if (newDestination is TravelLocations) {
                    displaySelectionInterface(player, true)
                    player.putTemporaryAttribute("uncollapsed_teleport", newDestination)
                    uncollapse(player, uncollapsed)
                }
            } else if (teleportSlotId >= uncollapsedTeleportsStart && teleportSlotId <= uncollapsedTeleportsEnd) {
                val destinations: List<Array<Any>> = uncollapsed!!.destinations
                val destinationIndex = teleportSlotId - uncollapsedTeleportsStart
                teleport(player, destinations[destinationIndex][1] as WorldTile, uncollapsed!!, destinationIndex)
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(INTERFACE_ID)
    }

    enum class TravelLocations(
        /**
         * The title of the teleport
         */
        val title: String,
    ) : Coordinates {
        PVP("PvP") {
            override fun populateDestinations() {
                add(
                    "Revenant Cave",
                    Coordinates.REVENANTS_CAVE,
                    "East Dragons",
                    Coordinates.EAST_DRAGONS,
                    "West Dragons",
                    Coordinates.WEST_DRAGONS,
                    "Graveyard",
                    Coordinates.GRAVEYARD,
                    "Obelisk: Lvl 50",
                    Coordinates.LVL_50_OBELISK,
                    "Mage Bank",
                    Coordinates.MAGE_BANK
                )
            }
        },
        MINIGAMES("Minigames") {
            override fun populateDestinations() {
                add(
                    "Duel Arena",
                    Coordinates.DUEL_ARENA,
                    "Pest Control",
                    Coordinates.PEST_CONTROL,
                    "Fight Caves",
                    Coordinates.TZHAAR,
                    "Barrows",
                    Coordinates.BARROW,
                    "Warriors Guild",
                    Coordinates.WARRIORS_GUILD,
                    "Clan Wars",
                    Coordinates.CLAN_WARS,
                    "Dicing",
                    Coordinates.DICING_AREA /*, "Castle Wars", CASTLE_WARS*/
                )
            }
        },
        BOSSES("Bosses") {
            override fun populateDestinations() {
                add(
                    "Nex",
                    Coordinates.NEX_DUNGEON,
                    "Godwars",
                    Coordinates.GODWARS_DUNGEON,
                    "Glacors",
                    Coordinates.GLACOR_DUNGEON,
                    "Kalphite Queen",
                    Coordinates.KALPHITE_QUEEN,
                    "King Black Dragon",
                    Coordinates.KING_BLACK_DRAGON,
                    "Chaos Elemental",
                    Coordinates.CHAOS_ELEMENTAL,
                    "Frost Dragons",
                    Coordinates.FROST_DRAGONS,
                    "Tormented Demons",
                    Coordinates.TORMENTED_DEMONS
                )
                add(
                    "Dagannoth Kings",
                    Coordinates.DAGANNOTH_KINGS,
                    "Corporeal Beast",
                    Coordinates.CORPOREAL_BEAST,
                    "Ice Strykwyrms",
                    Coordinates.STRYKEWYRM_DUNGEON,
                    "Sea Troll Queen",
                    Coordinates.SEA_TROLL_QUEEN,
                    "Bork",
                    Coordinates.BORK
                )
            }

            override fun handlePostTeleportation(player: Player, index: Int) {
                when (index) {
                    0, 1 -> player.controllerManager.startController("GodWars")
                }
            }
        },
        SKILLING("Skilling") {
            override fun populateDestinations() {
                add(
                    "Skill Zone",
                    Coordinates.SKILL_ZONE,
                    "Gnome Agility Course",
                    Coordinates.GNOME_AGILITY,
                    "Barbarian Agility Course",
                    Coordinates.BARBARIAN_AGILITY,
                    "Wilderness Agility Course",
                    Coordinates.WILDERNESS_AGILITY,
                    "The Abyss",
                    Coordinates.ABYSS
                )
                add(
                    "Catherby Farming",
                    WorldTile(2817, 3460, 0),
                    "Essence Mine",
                    Coordinates.ESSENCE_MINE,
                    "Plank Making",
                    Coordinates.LUMBER_YARD_PLANKS,
                    "Living Rock Cavern",
                    Coordinates.LIVING_ROCK_CAVERNS,
                    "Hunter Training",
                    Coordinates.HUNTER_TRAINING
                )
                add("Desert Phoenix Lair", WorldTile(3414, 3157, 0), "Rogues' Den", Coordinates.ROGUES_DEN)
            }
        },
        MONSTERS("Monsters") {
            override fun populateDestinations() {
                add(
                    "Rock Crabs",
                    Coordinates.ROCK_CRABS,
                    "Experiments",
                    Coordinates.EXPERIMENTS,
                    "Ogres",
                    Coordinates.OGRES,
                    "Yaks",
                    Coordinates.YAKS,
                    "Bandits",
                    Coordinates.BANDITS,
                    "Moss Giants",
                    Coordinates.MOSS_GIANTS,
                    "Chaos Druids",
                    Coordinates.DRUIDS,
                    "Tzhaar",
                    Coordinates.TZHAAR,
                    "Dust Devils",
                    Coordinates.DUST_DEVILS
                )
                add(
                    "Ape-Atoll Guards",
                    Coordinates.MONKEY_GUARDS,
                    "Armoured Zombies",
                    Coordinates.ARMOURED_ZOMBIES,
                    "Chaos Tunnels",
                    Coordinates.CHAOS_TUNNELS,
                    "Ice Giants",
                    Coordinates.ICE_GIANTS,
                    "Chickens",
                    Coordinates.CHICKENS,
                    "Monkey Skeletons",
                    Coordinates.APE_ATOLL_DUNGEON
                )
            }
        },
        DUNGEONS("Dungeons") {
            override fun populateDestinations() {
                add(
                    "Slayer Tower",
                    Coordinates.SLAYER_TOWER,
                    "Taverly Dungeon",
                    Coordinates.TAVERLY_DUNGEON,
                    "Fremennik Slayer Dungeon",
                    Coordinates.FREMENNIK_SLAYER_DUNGEON,
                    "Brimhaven Dungeon",
                    Coordinates.BRIMHAVEN_DUNGEON,
                    "Kuradal's Dungeon",
                    Coordinates.KURADAL_SLAYER_DUNGEON,
                    "Asgarnian Dungeon",
                    Coordinates.ASGARNIAN_ICE_DUNGEON,
                    "Ancient Cavern",
                    Coordinates.ANCIENT_CAVERN,
                    "Jadinko Lair",
                    Coordinates.JADINKO_LAIR
                )
            }
        },
        CITIES("Cities") {
            override fun populateDestinations() {
                add(
                    "Varrock",
                    Coordinates.VARROCK,
                    "Falador",
                    Coordinates.FALADOR,
                    "Camelot",
                    Coordinates.CAMELOT,
                    "Draynor",
                    Coordinates.DRAYNOR,
                    "Catherby",
                    Coordinates.CATHERBY,
                    "Al Kharid",
                    Coordinates.AL_KHARID,
                    "Karamja",
                    Coordinates.KARAMJA,
                    "Lumbridge",
                    Coordinates.LUMBRIDGE,
                    "Neitiznot",
                    Coordinates.NEITIZNOT
                )
                add(
                    "Ardougne",
                    Coordinates.ARDOUGNE,
                    "Rellekka",
                    Coordinates.RELLEKKA,
                    "Miscellania",
                    Coordinates.MISCELLANIA,
                    "The Grand Tree",
                    Coordinates.GRAND_TREE,
                    "Yanille",
                    Coordinates.YANILLE,
                    "Watchtower",
                    Coordinates.WATCHTOWER
                )
            }
        };

        companion object {
            init {
                Arrays.stream(values())
                    .forEach { obj: TravelLocations? -> obj?.populateDestinations() }
            }
        }

        /**
         * The method used to populate the destinations
         */
        abstract fun populateDestinations()

        /**
         * The list of destinations that can be travelled to, with the first slot in the Object[] as the name of the
         * destination, and the second slot as the `WorldTile` `Object`
         */
        val destinations: MutableList<Array<Any>> = ArrayList()

        /**
         * Adds destinations to the [.destinations] list
         *
         * @param params
         * The parameters, `String` first then `WorldTile`
         */
        protected fun add(vararg params: Any) {
            for (i in 0 until params.size) {
                val param = params[i]
                if (param is String) {
                    val proceeding = params[i + 1]
                    if (proceeding is WorldTile) {
                        destinations.add(arrayOf(param, proceeding))
                    } else {
                        throw IllegalStateException("Unexpected parameter $proceeding in $this TravelLocation")
                    }
                }
            }
        }

        /**
         * Handles actions after the teleport has been sent
         *
         * @param player
         * The player
         * @param index
         * The index of the teleport
         */
        open fun handlePostTeleportation(player: Player, index: Int) {}
    }

    class TransportationLocation(val destination: WorldTile, val locations: TravelLocations, val optionIndex: Int) :
        Serializable {

        companion object {
            private const val serialVersionUID = 2836199090952003422L
        }
    }

    companion object {
        /**
         * The possible messages the wizard can say
         */
        private val WIZARD_MESSAGES = arrayOf(
            "Amitus! Setitii!",
            "Sparanti Morudo Calmentor!",
            "Daemonicas Abhoris!",
            "Senventior disthine molenko!"
        )

        /**
         * The id of the interface which is scrollable and clickable with 106 options
         */
        private const val INTERFACE_ID = 156

        /**
         * Uncollapses a travel location for a player
         *
         * @param player
         * The player
         * @param travelLocations
         * The `TravelLocations` `Object` to be uncollapsed
         */
        private fun uncollapse(player: Player, travelLocations: TravelLocations?) {
            val interfaceId = 156
            var start = 7
            for (i in start..107) {
                player.packets.sendIComponentText(interfaceId, i, "")
            }
            for (locations in TravelLocations.values()) {
                sendLocationText(player, locations, start)
                start++

                // we're looping on the one we should be uncollapsing
                if (locations == travelLocations) {
                    for (destinations in locations.destinations) {
                        player.packets.sendIComponentText(interfaceId, start, ">>   " + destinations[0])
                        start++
                    }
                }
            }
            player.attributes.putAttribute(AttributeKey.LAST_UNCOLLAPSED_TELEPORT, travelLocations)
        }

        /**
         * Displays the interface to select a teleport
         *
         * @param player
         * The player
         * @param showLastUncollapsed
         * If the last `TravelLocations` `Object` the player viewed should be shown
         */
        @JvmStatic
        fun displaySelectionInterface(player: Player, showLastUncollapsed: Boolean) {
            val interfaceId = 156
            var start = 7
            player.interfaceManager.sendInterface(interfaceId)
            player.packets.sendRunScript(677, 100)
            for (i in start..107) {
                player.packets.sendIComponentText(interfaceId, i, "")
            }
            for (locations in TravelLocations.values()) {
                sendLocationText(player, locations, start)
                start++
            }
            player.packets.sendGlobalString(211, "Select a Destination")
            player.putTemporaryAttribute("quest_selection_interface", "teleportation")
            player.removeTemporaryAttribute<Any>("uncollapsed_teleport")
            if (!showLastUncollapsed) {
                return
            }
            val last = player.attributes.getAttribute<Any?>(AttributeKey.LAST_UNCOLLAPSED_TELEPORT, null)
            if (last != null) {
                val locations = TravelLocations.valueOf(last.toString())
                player.putTemporaryAttribute("uncollapsed_teleport", locations)
                uncollapse(player, locations)
            }
        }

        private fun generateUncollapsedArray(uncollapsed: TravelLocations): Array<Any> {
            val list: MutableList<Any> = ArrayList()
            for (location in TravelLocations.values()) {
                list.add(location)
                if (location == uncollapsed) {
                    list.addAll(ArrayList(uncollapsed.destinations))
                }
            }
            return list.toTypedArray()
        }

        /**
         * Teleports a player to the destination and handles post teleportation
         *
         * @param player
         * The player
         * @param destination
         * The destination
         * @param travelLocations
         * The travelLocations we're on
         * @param optionIndex
         * The option index of the teleport
         */
        private fun teleport(
            player: Player,
            destination: WorldTile,
            travelLocations: TravelLocations,
            optionIndex: Int,
        ) {
            if (Wilderness.isAtWild(destination)) {
                player.dialogueManager.startDialogue(object : Dialogue() {
                    override fun start() {
                        npc(
                            1263,
                            NORMAL,
                            "This destination is in the wilderness.",
                            "Are you sure you wish to travel here?"
                        )
                    }

                    override fun run(interfaceId: Int, option: Int) {
                        when (stage.toInt()) {
                            -1 -> {
                                options(
                                    DEFAULT_OPTION,
                                    "Yes, I want to travel to a wilderness location.",
                                    "No, thanks for the notification!"
                                )
                                stage = 0
                            }

                            0 -> {
                                if (option == FIRST) {
                                    teleportPlayer(
                                        player,
                                        destination,
                                        Runnable { travelLocations.handlePostTeleportation(player, optionIndex) })
                                }
                                end()
                            }
                        }
                    }

                    override fun finish() {}
                })
            } else {
                player.attributes.putAttribute(
                    AttributeKey.LAST_TRANSPORTATION_LOCATION,
                    TransportationLocation(destination, travelLocations, optionIndex)
                )
                teleportPlayer(
                    player,
                    destination,
                    Runnable { travelLocations.handlePostTeleportation(player, optionIndex) })
            }
        }

        /**
         * Sends the location text
         *
         * @param player
         * The player
         * @param locations
         * The `TravelLocations` `Object`
         * @param slot
         * The slot of the text
         */
        private fun sendLocationText(player: Player, locations: TravelLocations, slot: Int) {
            player.packets.sendIComponentText(
                156,
                slot,
                "<u><col=" + ColorConstants.MAROON + ">" + locations.title + "</u>"
            )
        }

        /**
         * Teleports the player to the destination and performs some graphical things to make it look cool
         *
         * @param player
         * The player
         * @param destination
         * The destination
         * @param task
         * The task to be performed once the teleport is done
         */
        @JvmStatic
        fun teleportPlayer(player: Player, destination: WorldTile?, task: Runnable?) {
            player.closeInterfaces()
            val wizard = Misc.findLocalNPC(player, 9434)
            WorldTasksManager.schedule(object : WorldTask() {
                override fun run() {
                    Magic.sendTeleportSpell(
                        player,
                        14293,
                        -1,
                        94,
                        -1,
                        0,
                        0.0,
                        destination,
                        4,
                        false,
                        MagicConstants.MAGIC_TELEPORT
                    )
                    player.setCloseInterfacesEvent(task)
                }
            }, 0)
            if (wizard == null) {
                return
            }
            wizard.resetWalkSteps()
            wizard.nextFaceWorldTile = player
            wizard.setNextFaceActor(player)
            wizard.nextForceTalk =
                ForceTalk(Misc.randomArraySlot(WIZARD_MESSAGES))
        }
    }
}