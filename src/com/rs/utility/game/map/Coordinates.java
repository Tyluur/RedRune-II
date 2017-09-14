package com.rs.utility.game.map;

import com.rs.game.entity.WorldTile;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Apr 20, 2015
 */
public interface Coordinates {
	
	WorldTile SUMMONING_DUNGEON = new WorldTile(2207, 5347, 0),
	
	/**
	 * Dungeons
	 **/
	
	APE_ATOLL_DUNGEON = new WorldTile(2735, 9130, 0),
	
	TAVERLY_DUNGEON = new WorldTile(2884, 9798, 0),
	
	NEX_DUNGEON = new WorldTile(2903, 5204, 0),
	
	GODWARS_DUNGEON = new WorldTile(2880, 5311, 2),
	
	CHAOS_TUNNELS = new WorldTile(3242, 5469, 0),
	
	FREMENNIK_SLAYER_DUNGEON = new WorldTile(2805, 10002, 0),
	
	POLYPORE_DUNGEON = new WorldTile(4620, 5458, 3),
	
	GLACOR_DUNGEON = new WorldTile(4177, 5729, 0),
	
	/**
	 * Skilling locations
	 */
	
	GNOME_AGILITY = new WorldTile(2479, 3437, 0),
	
	BARBARIAN_AGILITY = new WorldTile(2548, 3569, 0),
	
	WILDERNESS_AGILITY = new WorldTile(2997, 3914, 0),
	
	MISCELLANIA = new WorldTile(2539, 3866, 0),
	
	FISHING_GUILD = new WorldTile(2585, 3422, 0),
	
	FALADOR_MINES = new WorldTile(3058, 9776, 0),
	
	MINING_GUILD = new WorldTile(3046, 9756, 0),
	
	CRAFTING_GUILD = new WorldTile(2935, 3283, 0),
	
	ESSENCE_MINE = new WorldTile(2911, 4832, 0),
	
	ABYSS = new WorldTile(3039, 4834, 0),
	
	LUMBER_YARD_PLANKS = new WorldTile(3302, 3489, 0),
	
	SLAYER_TOWER = new WorldTile(3428, 3538, 0),
	
	LIVING_ROCK_CAVERNS = (new WorldTile(3651, 5122, 0)),
	
	HUNTER_TRAINING = (new WorldTile(2555, 2931, 0)),
	
	ROGUES_DEN = new WorldTile(3050, 4979, 1),
	
	/**
	 * Monster Spawns
	 **/
	
	EXPERIMENTS = new WorldTile(3556, 9946, 0),
	
	YAKS = new WorldTile(2325, 3803, 0),
	
	BANDITS = new WorldTile(3169, 2983, 0),
	
	MOSS_GIANTS = new WorldTile(2675, 9560, 0),
	
	OGRES = new WorldTile(2497, 3087, 0),
	
	DRUIDS = new WorldTile(2906, 3499, 0),
	
	TZHAAR = new WorldTile(2439, 5172, 0),
	
	DUST_DEVILS = new WorldTile(3214, 9354, 0),
	
	CHICKENS = new WorldTile(3238, 3296, 0),
	
	ARMOURED_ZOMBIES = new WorldTile(3083, 9672, 0),
	
	MONKEY_GUARDS = new WorldTile(2797, 2791, 0),
	
	CHAOS_ELEMENTAL = new WorldTile(3277, 3918, 0),
	
	KING_BLACK_DRAGON = new WorldTile(3006, 3849, 0),
	
	AVATAR_OF_DESTRUCTION = new WorldTile(3281, 3876, 0),
	
	QUEEN_BLACK_DRAGON = new WorldTile(1441, 6364, 0),
	
	ROCK_CRABS = new WorldTile(2685, 3717, 0),
	
	TORMENTED_DEMONS = new WorldTile(2571, 5736, 0),
	
	DAGANNOTH_KINGS = new WorldTile(2900, 4449, 0),
	
	KALPHITE_QUEEN = new WorldTile(3226, 3108, 0),
	
	CORPOREAL_BEAST = new WorldTile(2963, 4384, 2),
	
	FROST_DRAGONS = new WorldTile(3135, 3795, 0),
	
	ICE_GIANTS = new WorldTile(2986, 3896, 0),
	
	BRIMHAVEN_DUNGEON = new WorldTile(2710, 9471, 0),
	
	STRYKEWYRM_DUNGEON = new WorldTile(3435, 5646, 0),
	
	JADINKO_LAIR = new WorldTile(3012, 9275, 0),
	
	ANCIENT_CAVERN = new WorldTile(1744, 5325, 0),
	
	KURADAL_SLAYER_DUNGEON = new WorldTile(1661, 5258, 0),
	
	ASGARNIAN_ICE_DUNGEON = new WorldTile(3009, 9549, 0),
	
	SEA_TROLL_QUEEN = new WorldTile(2330, 3691, 0),
	
	BORK = new WorldTile(3100, 5536, 0),
	
	/**
	 * Wilderness spawns
	 */
	
	EAST_DRAGONS = new WorldTile(3348, 3675, 0),
	
	WEST_DRAGONS = new WorldTile(2975, 3602, 0),
	
	MAGE_BANK = new WorldTile(2539, 4716, 0),
	
	DESERTED_KEEP = new WorldTile(3155, 3924, 0),
	
	REVENANTS_CAVE = new WorldTile(3037, 10171, 0),
	
	GRAVEYARD = new WorldTile(3223, 3682, 0),
	
	LVL_35_OBELISK = new WorldTile(3115, 3792, 0),
	
	LVL_44_OBELISK = new WorldTile(2973, 3864, 0),
	
	LVL_50_OBELISK = new WorldTile(3306, 3924, 0),
	
	/**
	 * Cities
	 */
	
	VARROCK = new WorldTile(3213, 3425, 0),
	
	FALADOR = new WorldTile(2965, 3380, 0),
	
	CAMELOT = new WorldTile(2758, 3478, 0),
	
	CATHERBY = new WorldTile(2804, 3434, 0),
	
	AL_KHARID = new WorldTile(3292, 3185, 0),
	
	LUMBRIDGE = new WorldTile(3220, 3219, 0),
	
	NEITIZNOT = new WorldTile(2324, 3804, 0),
	
	ARDOUGNE = new WorldTile(2652, 3284, 0),
	
	RELLEKKA = new WorldTile(2674, 3684, 0),
	
	DRAYNOR = new WorldTile(3080, 3250, 0),
	
	GRAND_TREE = new WorldTile(2465, 3491, 0),
	
	YANILLE = new WorldTile(2543, 3092, 0),
	
	WATCHTOWER = new WorldTile(2937, 4713, 0),
	
	KARAMJA = new WorldTile(2916, 3152, 0),
	
	/**
	 * Miscellaneous spawns
	 */
	
	PEST_CONTROL = new WorldTile(2664, 2656, 0),
	
	DUEL_ARENA = new WorldTile(3368, 3267, 0),
	
	RUNESPAN = new WorldTile(3107, 3160, 1),
	
	BARROW = new WorldTile(3565, 3288, 0),
	
	CLAN_WARS = new WorldTile(2993, 9679, 0),
	
	WARRIORS_GUILD = new WorldTile(2878, 3542, 0),
	
	PARTY_ROOM = new WorldTile(3046, 3375, 0),
	
	CASTLE_WARS = new WorldTile(2442, 3090, 0),
	
	//DICING_AREA = new WorldTile(1698, 5600, 0),
	DICING_AREA = new WorldTile(3080, 3512, 0),
	
	STAFF_ZONE = new WorldTile(2332, 3681, 0),
	
	SKILL_ZONE = new WorldTile(2337, 3689, 0),
	
	WILDERNESS_RESOURCE_CENTER = new WorldTile(3037, 3699, 0);
	
	/**
	 * If the tile is inside the skill zone
	 *
	 * @param tile
	 * 		The tile
	 */
	static boolean insideSkillZone(WorldTile tile) {
		return tile.getRegionId() == 9273;
	}
	
	/**
	 * If the tile is inside mage bank area
	 *
	 * @param tile
	 * 		The tile
	 */
	static boolean insideMageBank(WorldTile tile) {
		return tile.getRegionId() == 12349 || tile.getRegionId() == 12093 || tile.getRegionId() == 12605;
	}
}
