package org.redrune.utility.game.map

import org.redrune.game.global.WorldTile

/**
 * @author Tyluur<itstyluur@icloud.com>
 * @since Apr 20, 2015
 */
interface Coordinates {

    companion object {
        /**
         * If the tile is inside the skill zone
         *
         * @param tile The tile
         */
        fun insideSkillZone(tile: WorldTile): Boolean {
            return tile.regionId == 9273
        }

        /**
         * If the tile is inside mage bank area
         *
         * @param tile The tile
         */
        fun insideMageBank(tile: WorldTile): Boolean {
            return tile.regionId == 12349 || tile.regionId == 12093 || tile.regionId == 12605
        }

        val SUMMONING_DUNGEON = WorldTile(2207, 5347, 0)

        /**
         * Dungeons
         */
        val APE_ATOLL_DUNGEON = WorldTile(2735, 9130, 0)
        val TAVERLY_DUNGEON = WorldTile(2884, 9798, 0)
        val NEX_DUNGEON = WorldTile(2903, 5204, 0)
        val GODWARS_DUNGEON = WorldTile(2880, 5311, 2)
        val CHAOS_TUNNELS = WorldTile(3242, 5469, 0)
        val FREMENNIK_SLAYER_DUNGEON = WorldTile(2805, 10002, 0)
        val POLYPORE_DUNGEON = WorldTile(4620, 5458, 3)
        val GLACOR_DUNGEON = WorldTile(4177, 5729, 0)

        /**
         * Skilling locations
         */
        val GNOME_AGILITY = WorldTile(2479, 3437, 0)
        val BARBARIAN_AGILITY = WorldTile(2548, 3569, 0)
        val WILDERNESS_AGILITY = WorldTile(2997, 3914, 0)
        val MISCELLANIA = WorldTile(2539, 3866, 0)
        val FISHING_GUILD = WorldTile(2585, 3422, 0)
        val FALADOR_MINES = WorldTile(3058, 9776, 0)
        val MINING_GUILD = WorldTile(3046, 9756, 0)
        val CRAFTING_GUILD = WorldTile(2935, 3283, 0)
        val ESSENCE_MINE = WorldTile(2911, 4832, 0)
        val ABYSS = WorldTile(3039, 4834, 0)
        val LUMBER_YARD_PLANKS = WorldTile(3302, 3489, 0)
        val SLAYER_TOWER = WorldTile(3428, 3538, 0)
        val LIVING_ROCK_CAVERNS = WorldTile(3651, 5122, 0)
        val HUNTER_TRAINING = WorldTile(2555, 2931, 0)
        val ROGUES_DEN = WorldTile(3050, 4979, 1)

        /**
         * Monster Spawns
         */
        val EXPERIMENTS = WorldTile(3556, 9946, 0)
        val YAKS = WorldTile(2325, 3803, 0)
        val BANDITS = WorldTile(3169, 2983, 0)
        val MOSS_GIANTS = WorldTile(2675, 9560, 0)
        val OGRES = WorldTile(2497, 3087, 0)
        val DRUIDS = WorldTile(2906, 3499, 0)
        val TZHAAR = WorldTile(2439, 5172, 0)
        val DUST_DEVILS = WorldTile(3214, 9354, 0)
        val CHICKENS = WorldTile(3238, 3296, 0)
        val ARMOURED_ZOMBIES = WorldTile(3083, 9672, 0)
        val MONKEY_GUARDS = WorldTile(2797, 2791, 0)
        val CHAOS_ELEMENTAL = WorldTile(3277, 3918, 0)
        val KING_BLACK_DRAGON = WorldTile(3006, 3849, 0)
        val AVATAR_OF_DESTRUCTION = WorldTile(3281, 3876, 0)
        val QUEEN_BLACK_DRAGON = WorldTile(1441, 6364, 0)
        val ROCK_CRABS = WorldTile(2685, 3717, 0)
        val TORMENTED_DEMONS = WorldTile(2571, 5736, 0)
        val DAGANNOTH_KINGS = WorldTile(2900, 4449, 0)
        val KALPHITE_QUEEN = WorldTile(3226, 3108, 0)
        val CORPOREAL_BEAST = WorldTile(2963, 4384, 2)
        val FROST_DRAGONS = WorldTile(3135, 3795, 0)
        val ICE_GIANTS = WorldTile(2986, 3896, 0)
        val BRIMHAVEN_DUNGEON = WorldTile(2710, 9471, 0)
        val STRYKEWYRM_DUNGEON = WorldTile(3435, 5646, 0)
        val JADINKO_LAIR = WorldTile(3012, 9275, 0)
        val ANCIENT_CAVERN = WorldTile(1744, 5325, 0)
        val KURADAL_SLAYER_DUNGEON = WorldTile(1661, 5258, 0)
        val ASGARNIAN_ICE_DUNGEON = WorldTile(3009, 9549, 0)
        val SEA_TROLL_QUEEN = WorldTile(2330, 3691, 0)
        val BORK = WorldTile(3100, 5536, 0)

        /**
         * Wilderness spawns
         */
        val EAST_DRAGONS = WorldTile(3348, 3675, 0)
        val WEST_DRAGONS = WorldTile(2975, 3602, 0)
        val MAGE_BANK = WorldTile(2539, 4716, 0)
        val DESERTED_KEEP = WorldTile(3155, 3924, 0)
        val REVENANTS_CAVE = WorldTile(3037, 10171, 0)
        val GRAVEYARD = WorldTile(3223, 3682, 0)
        val LVL_35_OBELISK = WorldTile(3115, 3792, 0)
        val LVL_44_OBELISK = WorldTile(2973, 3864, 0)
        val LVL_50_OBELISK = WorldTile(3306, 3924, 0)

        /**
         * Cities
         */
        val VARROCK = WorldTile(3213, 3425, 0)
        val FALADOR = WorldTile(2965, 3380, 0)
        val CAMELOT = WorldTile(2758, 3478, 0)
        val CATHERBY = WorldTile(2804, 3434, 0)
        val AL_KHARID = WorldTile(3292, 3185, 0)
        val LUMBRIDGE = WorldTile(3220, 3219, 0)
        val NEITIZNOT = WorldTile(2324, 3804, 0)
        val ARDOUGNE = WorldTile(2652, 3284, 0)
        val RELLEKKA = WorldTile(2674, 3684, 0)
        val DRAYNOR = WorldTile(3080, 3250, 0)
        val GRAND_TREE = WorldTile(2465, 3491, 0)
        val YANILLE = WorldTile(2543, 3092, 0)
        val WATCHTOWER = WorldTile(2937, 4713, 0)
        val KARAMJA = WorldTile(2916, 3152, 0)

        /**
         * Miscellaneous spawns
         */
        val PEST_CONTROL = WorldTile(2664, 2656, 0)
        val DUEL_ARENA = WorldTile(3368, 3267, 0)
        val RUNESPAN = WorldTile(3107, 3160, 1)
        val BARROW = WorldTile(3565, 3288, 0)
        val CLAN_WARS = WorldTile(2993, 9679, 0)
        val WARRIORS_GUILD = WorldTile(2878, 3542, 0)
        val PARTY_ROOM = WorldTile(3046, 3375, 0)
        val CASTLE_WARS = WorldTile(2442, 3090, 0)

        //DICING_AREA = new WorldTile(1698, 5600, 0),
        val DICING_AREA = WorldTile(3080, 3512, 0)
        val STAFF_ZONE = WorldTile(2332, 3681, 0)
        val SKILL_ZONE = WorldTile(2337, 3689, 0)
        val WILDERNESS_RESOURCE_CENTER = WorldTile(3037, 3699, 0)
    }
}