package org.redrune.utility.rs.constant;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.rs2.node.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public interface EquipConstants {
	
	/**
	 * The names of items that are fully body items
	 */
	String[] FULL_BODY = { "Investigator's coat", "armour", "hauberk", "top", "shirt", "platebody", "Ahrims robetop", "Karils leathertop", "brassard", "Robe top", "robetop", "platebody (t)", "platebody (g)", "chestplate", "torso", "Morrigan's", "leather body", "robe top", "Pernix body", "Torva platebody" };
	
	/**
	 * The names of items that are full hat items
	 */
	String[] FULL_HAT = { "sallet", "med helm", "coif", "Dharok's helm", "hood", "Initiate helm", "Coif", "Helm of neitiznot" };
	
	/**
	 * The names of items that are full mask items
	 */
	String[] FULL_MASK = { "Christmas ghost hood", "Dragon full helm (or)", "sallet", "full helm", "mask", "Veracs helm", "Guthans helm", "Torags helm", "Karils coif", "full helm (t)", "full helm (g)", "mask" };
	
	/**
	 * The slot in the equipment container
	 */
	byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13, SLOT_AURA = 14;
	
	String[] LEGS = { "leggings", "void knight robe", "druidic robe", "cuisse", "pants", "platelegs", "plateskirt", "skirt", "bottoms", "chaps", "platelegs (t)", "platelegs (g)", "bottom", "skirt", "skirt (g)", "skirt (t)", "chaps (g)", "chaps (t)", "tassets", "legs", "trousers", "robe bottom" };
	
	String[] WEAPONS = { "bolas", "blade", "Butterfly net", "scythe", "rapier", "hatchet", "bow", "Hand cannon", "Inferno adze", "Silverlight", "Darklight", "wand", "Statius's warhammer", "anchor", "spear.", "Vesta's longsword.", "scimitar", "longsword", "sword", "longbow", "shortbow", "dagger", "mace", "halberd", "spear", "Abyssal whip", "Abyssal vine whip", "Ornate katana", "axe", "flail", "crossbow", "Torags hammers", "dagger(p)", "dagger (p++)", "dagger(+)", "dagger(s)", "spear(p)", "spear(+)", "spear(s)", "spear(kp)", "maul", "dart", "dart(p)", "javelin", "javelin(p)", "knife", "knife(p)", "Longbow", "Shortbow", "Crossbow", "Toktz-xil", "Toktz-mej", "Tzhaar-ket", "staff", "Staff", "godsword", "c'bow", "Crystal bow", "Dark bow", "claws", "warhammer", "hammers", "adze", "hand", "Broomstick", "Flowers", "flowers", "trident", "excalibur" };
	
	String[] CAPES = { "cloak", "cape", "ava's", "TokHaar" };
	
	String[] HATS = { "visor", "ears", "goggles", "bearhead", "tiara", "cowl", "druidic wreath", "halo", "Royal", "crown", "sallet", "helm", "hood", "coif", "Coif", "partyhat", "hat", "cap", " bandana", "full helm (t)", "full helm (g)", "cav", "boater", "helmet", "afro", "beard", "gnome goggles", "mask", "Helm of neitiznot", "mitre" };
	
	String[] BOOTS = { "boots", "Boots", "shoes", "Shoes", "flippers" };
	
	String[] GLOVES = { "gloves", "gauntlets", "Gloves", "vambraces", "vamb", "bracers", "brace" };
	
	String[] AMULETS = { "stole", "amulet", "necklace", "Amulet of", "scarf", "Super dominion medallion" };
	
	String[] SHIELDS = { "tome of frost", "kiteshield", "sq shield", "Toktz-ket", "books", "book", "kiteshield (t)", "kiteshield (g)", "kiteshield(h)", "defender", "shield", "deflector" };
	
	String[] ARROWS = { "arrow", "arrows", "arrow(p)", "arrow(+)", "arrow(s)", "bolt", "Bolt rack", "Opal bolts", "Dragon bolts", "bolts (e)", "bolts", "Hand cannon shot" };
	
	String[] RINGS = { "ring" };
	
	String[] BODY = { "poncho", "apron", "robe top", "armour", "hauberk", "platebody", "chainbody", "robetop", "leathertop", "platemail", "top", "brassard", "body", "platebody (t)", "platebody (g)", "body(g)", "body_(g)", "chestplate", "torso", "shirt", "Rock-shell plate" };
	
	String[] AURAS = { "poison purge", "runic accuracy", "sharpshooter", "lumberjack", "quarrymaster", "call of the sea", "reverence", "five finger discount", "resourceful", "equilibrium", "inspiration", "vampyrism", "penance", "wisdom", "jack of trades" };
	
	int[] BODY_LIST = { 21463, 21549, 544, 6107 };
	
	int[] LEGS_LIST = { 542, 6108, 10340, 7398 };
	
	/**
	 * Checks if an item's definitions will result in being full body.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullBody(ItemDefinition def) {
		String weapon = def.getName();
		for (String name : FULL_BODY) {
			if (weapon.contains(name)) {
				return true;
			}
		}
		return def.getId() == 6107 || def.getId() == 13624 || def.getId() == 13887;
	}
	
	/**
	 * Checks if an item's definitions will result in being full hat.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullHat(ItemDefinition def) {
		String weapon = def.getName();
		for (String name : FULL_HAT) {
			if (weapon.endsWith(name)) {
				return true;
			}
		}
		return def.getId() == 14824;
	}
	
	/**
	 * Checks if an item's definitions will result in being full mask.
	 *
	 * @param def
	 * 		The definitions
	 */
	static boolean isFullMask(ItemDefinition def) {
		String weapon = def.getName();
		for (String name : FULL_MASK) {
			if (weapon.endsWith(name)) {
				return true;
			}
		}
		return false;
	}
	
	static int getItemSlot(int itemId) {
		for (int bodyId : BODY_LIST) {
			if (itemId == bodyId) {
				return 4;
			}
		}
		for (int legId : LEGS_LIST) {
			if (itemId == legId) {
				return 7;
			}
		}
		ItemDefinition defs = ItemDefinitionParser.forId(itemId);
		if (defs == null) {
			return -1;
		}
		String item = defs.getName().toLowerCase();
		for (String cape : CAPES) {
			if (item.contains(cape.toLowerCase())) {
				return 1;
			}
		}
		for (String boot : BOOTS) {
			if (item.contains(boot.toLowerCase())) {
				return 10;
			}
		}
		for (String glove : GLOVES) {
			if (item.contains(glove.toLowerCase())) {
				return 9;
			}
		}
		for (String shield : SHIELDS) {
			if (item.contains(shield.toLowerCase())) {
				return 5;
			}
		}
		for (String amulet : AMULETS) {
			if (item.contains(amulet.toLowerCase())) {
				return 2;
			}
		}
		for (String arrow : ARROWS) {
			if (item.contains(arrow.toLowerCase())) {
				return 13;
			}
		}
		for (String ring : RINGS) {
			if (item.contains(ring.toLowerCase())) {
				return 12;
			}
		}
		for (String weapon : WEAPONS) {
			if (item.contains(weapon.toLowerCase())) {
				return 3;
			}
		}
		for (String hat : HATS) {
			if (item.contains(hat.toLowerCase())) {
				return 0;
			}
		}
		for (String body : BODY) {
			if (item.contains(body.toLowerCase())) {
				return 4;
			}
		}
		for (String leg : LEGS) {
			if (item.contains(leg.toLowerCase())) {
				return 7;
			}
		}
		for (String aura : AURAS) {
			if (item.contains(aura.toLowerCase())) {
				return SLOT_AURA;
			}
		}
		return -1;
	}
	
	static boolean isTwoHanded(Item item) {
		return false;
	}
}
