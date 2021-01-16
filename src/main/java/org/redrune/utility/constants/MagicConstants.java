package org.redrune.utility.constants;

import org.redrune.game.global.WorldTile;


import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
public interface MagicConstants {
	
	int AIR_RUNE = 556;
	
	int WATER_RUNE = 555;
	
	int EARTH_RUNE = 557;
	
	int FIRE_RUNE = 554;
	
	int MIND_RUNE = 558;
	
	int BODY_RUNE = 559;
	
	int NATURE_RUNE = 561;
	
	int CHAOS_RUNE = 562;
	
	int DEATH_RUNE = 560;
	
	int BLOOD_RUNE = 565;
	
	int SOUL_RUNE = 566;
	
	int ASTRAL_RUNE = 9075;
	
	int LAW_RUNE = 563;
	
	int STEAM_RUNE = 4694;
	
	int MIST_RUNE = 4695;
	
	int DUST_RUNE = 4696;
	
	int SMOKE_RUNE = 4697;
	
	int MUD_RUNE = 4698;
	
	int LAVA_RUNE = 4699;
	
	int ARMADYL_RUNE = 21773;
	
	int BANANA = 1963;
	
	WorldTile[] TABS = { new WorldTile(3217, 3426, 0), new WorldTile(3222, 3218, 0), new WorldTile(2965, 3379, 0), new WorldTile(2758, 3478, 0), new WorldTile(2660, 3306, 0), new WorldTile(2547, 3113, 2), GameConstants.START_PLAYER_LOCATION };
	
	int MAGIC_TELEPORT = 0;
	
	int ITEM_TELEPORT = 1;
	
	int OBJECT_TELEPORT = 2;
	
	int TELE_MOVE_TYPE = 127;
	
	int WALK_MOVE_TYPE = 1;
	
	int RUN_MOVE_TYPE = 2;
	
	enum MagicBook {
		REGULAR(192),
		ANCIENTS(193),
		LUNAR(430),
		DUNGEONEERING(950);
		
		/**
		 * The id of the interface
		 */
		private final int interfaceId;
		
		MagicBook(int interfaceId) {
			this.interfaceId = interfaceId;
		}
		
		/**
		 * Gets a magic book by its interface id
		 */
		public static Optional<MagicBook> getMagicBook(int interfaceId) {
			return Arrays.stream(values()).filter(book -> book.getInterfaceId() == interfaceId).findFirst();
		}

        public int getInterfaceId() {
            return this.interfaceId;
        }
    }
}
