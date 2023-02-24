package org.redrune.utility.constants;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public interface EquipmentConstants {

    byte SLOT_HAT = 0;

    byte SLOT_CAPE = 1;

    byte SLOT_AMULET = 2;

    byte SLOT_WEAPON = 3;

    byte SLOT_CHEST = 4;

    byte SLOT_SHIELD = 5;

    byte SLOT_LEGS = 7;

    byte SLOT_HANDS = 9;

    byte SLOT_FEET = 10;

    byte SLOT_RING = 12;

    byte SLOT_ARROWS = 13;

    byte SLOT_AURA = 14;

    int[] DISABLED_SLOTS = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0};

    String[] CAPES = {"cloak", "cape", "ava's", "tokhaar"};

    String[] HATS = {"visor", "ears", "goggles", "bearhead", "tiara", "cowl", "druidic wreath", "halo", "crown", "sallet", "helm", "hood", "coif", "flaming skull", "Coif", "partyhat", "hat", "cap", " bandana", "full helm (t)", "full helm (g)", "full helm (or)", "cav", "boater", "helmet", "afro", "beard", "gnome goggles", "mask", "Helm of neitiznot", "mitre", "nemes", "wig", "headdress", "double eyepatches",};

    String[] BOOTS = {"boots", "Boots", "shoes", "Shoes", "flippers"};

    String[] GLOVES = {"gloves", "gauntlets", "Gloves", "vambraces", "vamb", "bracers", "brace"};

    String[] AMULETS = {"stole", "amulet", "necklace", "Amulet of", "scarf", "Super dominion medallion"};

    String[] SHIELDS = {"tome of frost", "kiteshield", "sq shield", "Toktz-ket", "books", "book", "kiteshield (t)", "kiteshield (g)", "kiteshield(h)", "defender", "shield", "deflector"};

    String[] ARROWS = {"arrow", "arrows", "arrow(p)", "arrow(+)", "arrow(s)", "bolt", "Bolt rack", "Opal bolts", "Dragon bolts", "bolts (e)", "bolts", "Hand cannon shot"};

    String[] RINGS = {"ring"};

    String[] BODY = {"poncho", "apron", "robe top", "armour", "hauberk", "platebody", "chainbody", "breastplate", "blouse", "robetop", "leathertop", "platemail", "top", "brassard", "body", "platebody (t)", "platebody (g)", "body(g)", "body_(g)", "chestplate", "torso", "shirt", "Rock-shell plate", "coat", "jacket"};

    String[] AURAS = {"poison purge", "Salvation", "Corruption", "salvation", "corruption", "runic accuracy", "sharpshooter", "lumberjack", "quarrymaster", "call of the sea", "reverence", "five finger discount", "resourceful", "equilibrium", "inspiration", "vampyrism", "penance", "wisdom", "jack of trades", "gaze"};

    int[] BODY_LIST = {21463, 21549, 544, 6107};

    int[] LEGS_LIST = {542, 6108, 10340, 7398};

    String[] LEGS = {"leggings", "void knight robe", "druidic robe", "cuisse", "pants", "platelegs", "plateskirt", "skirt", "bottoms", "chaps", "platelegs (t)", "platelegs (or)", "platelegs (g)", "bottom", "skirt", "skirt (g)", "skirt (t)", "chaps (g)", "chaps (t)", "tassets", "legs", "trousers", "robe bottom", "shorts", "black navy slacks",};

    String[] WEAPONS = {"bolas", "stick", "blade", "Butterfly net", "scythe", "rapier", "hatchet", "bow", "Hand cannon", "Inferno adze", "Silverlight", "Darklight", "wand", "Statius's warhammer", "anchor", "spear.", "Vesta's longsword.", "scimitar", "longsword", "sword", "longbow", "shortbow", "dagger", "mace", "halberd", "spear", "Abyssal whip", "Abyssal vine whip", "Ornate katana", "axe", "flail", "crossbow", "Torags hammers", "dagger(p)", "dagger (p++)", "dagger(+)", "dagger(s)", "spear(p)", "spear(+)", "spear(s)", "spear(kp)", "maul", "dart", "dart(p)", "javelin", "javelin(p)", "knife", "knife(p)", "Longbow", "Shortbow", "Crossbow", "Toktz-xil", "Shark fists", "Toktz-mej", "Tzhaar-ket", "staff", "Staff", "godsword", "c'bow", "Crystal bow", "Dark bow", "claws", "warhammer", "hammers", "adze", "hand", "Broomstick", "Flowers", "flowers", "trident", "excalibur", "cane", "sled", "Katana", "bag", "tenderiser", "eggsterminator", "Sled", "sceptre", "decimation", "obliteration", "annihilation", "queen maddie's toy"};

    String[] NOT_FULL_BODY = {"zombie shirt"};

    String[] FULL_BODY = {"robe", "breastplate", "blouse", "pernix body", "vesta's chainbody", "armour", "hauberk", "top", "shirt", "platebody", "Ahrims robetop", "Karils leathertop", "brassard", "chestplate", "torso", "Morrigan's", "Zuriel's", "changshan jacket"};

    String[] FULL_HAT = {"helm", "cowl", "sallet", "med helm", "coif", "Dharoks helm", "Initiate helm", "Coif", "Helm of neitiznot"};

    String[] FULL_MASK = {"sallet", "mask", "full helm", "mask", "Veracs helm", "Guthans helm", "Torags helm", "flaming skull", "Karils coif", "full helm (t)", "full helm (g)"};

    static boolean isFullBody(Item item) {
        String itemName = item.getDefinitions().getName();
        if (itemName == null) {
            return false;
        }
        itemName = itemName.toLowerCase();
        for (int i = 0; i < NOT_FULL_BODY.length; i++) {
            if (itemName.contains(NOT_FULL_BODY[i].toLowerCase())) {
                return false;
            }
        }
        for (int i = 0; i < FULL_BODY.length; i++) {
            if (itemName.contains(FULL_BODY[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    static boolean isFullHat(Item item) {
        String itemName = item.getDefinitions().getName();
        if (itemName == null) {
            return false;
        }
        itemName = itemName.toLowerCase();
        for (int i = 0; i < FULL_HAT.length; i++) {
            if (itemName.contains(FULL_HAT[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    static boolean isFullMask(Item item) {
        String itemName = item.getDefinitions().getName();
        if (itemName == null) {
            return false;
        }
        itemName = itemName.toLowerCase();
        for (int i = 0; i < FULL_MASK.length; i++) {
            if (itemName.contains(FULL_MASK[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    static void sendRemove(Player player, int slotId) {
        if (player.getLocks().isEquipmentLocked() || slotId >= 15) {
            return;
        }
        Item item = player.getEquipment().getItem(slotId);
        if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount())) {
            return;
        }
        player.getEquipment().getItems().set(slotId, null);
        player.getEquipment().refresh(slotId);
        player.getAppearance().generateAppearanceData();
        if (Runecrafting.isTiara(item.getId())) {
            player.getPackets().sendConfig(491, 0);
        }
        if (slotId == 3) {
            player.getCombatDefinitions().decreaseSpecialEnergy(0);
        }
    }

    static boolean sendWear(Player player, int slotId, int itemId) {
        if (player.isFinished() || player.isDead()) {
            return false;
        }
        Item item = player.getInventory().getItem(slotId);
        if (item == null || item.getId() != itemId) {
            return false;
        }
        if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearance().isMale()) && item.getDefinitions().getId() != 4084) {
            player.getPackets().sendMessage("You can't wear that.");
            return true;
        }
        int targetSlot = getItemSlot(itemId);
        if (targetSlot == -1) {
            player.getPackets().sendMessage("You can't wear that.");
            return true;
        }
        boolean isTwoHandedWeapon = targetSlot == 3 && isTwoHandedWeapon(item);
        if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
            player.getPackets().sendMessage("Not enough free space in your inventory.");
            return true;
        }
        HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequirements();
        boolean hasRequiriments = true;
        if (requiriments != null) {
            for (int skillId : requiriments.keySet()) {
                if (skillId > 24 || skillId < 0) {
                    continue;
                }
                int level = requiriments.get(skillId);
                if (level < 0 || level > 120) {
                    continue;
                }
                if (player.getSkills().getLevelForXp(skillId) < level) {
                    if (hasRequiriments) {
                        player.getPackets().sendMessage("You are not high enough level to use this item.");
                    }
                    hasRequiriments = false;
                    String name = SkillConstants.SKILL_NAME[skillId].toLowerCase();
                    player.getPackets().sendMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
                }

            }
        }
        if (!hasRequiriments) {
            return true;
        }
        if (!player.getControllerManager().canEquip(targetSlot, itemId)) {
            return false;
        }
        player.getInventory().deleteItem(slotId, item);
        if (targetSlot == 3) {
            if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
                if (!player.getInventory().addItem(player.getEquipment().getItem(5).getId(), player.getEquipment().getItem(5).getAmount())) {
                    player.getInventory().getItems().set(slotId, item);
                    player.getInventory().refresh(slotId);
                    return true;
                }
                player.getEquipment().getItems().set(5, null);
            }
        } else if (targetSlot == 5) {
            if (player.getEquipment().getItem(3) != null && isTwoHandedWeapon(player.getEquipment().getItem(3))) {
                if (!player.getInventory().addItem(player.getEquipment().getItem(3).getId(), player.getEquipment().getItem(3).getAmount())) {
                    player.getInventory().getItems().set(slotId, item);
                    player.getInventory().refresh(slotId);
                    return true;
                }
                player.getEquipment().getItems().set(3, null);
            }

        }
        if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
            if (player.getInventory().getItems().get(slotId) == null) {
                player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
                player.getInventory().refresh(slotId);
            } else {
                player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
            }
            player.getEquipment().getItems().set(targetSlot, null);
        }
        int oldAmt = 0;
        if (player.getEquipment().getItem(targetSlot) != null) {
            oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
        }
        Item item2 = new Item(itemId, oldAmt + item.getAmount());
        player.getEquipment().getItems().set(targetSlot, item2);
        player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
        player.getAppearance().generateAppearanceData();
        player.getPackets().sendSound(2240, 0, 1);
        if (targetSlot == 3) {
            player.getCombatDefinitions().decreaseSpecialEnergy(0);
        }
        player.getCharges().wear(targetSlot);
        return true;
    }

    static int getItemSlot(int itemId) {
        for (int i = 0; i < BODY_LIST.length; i++) {
            if (itemId == BODY_LIST[i]) {
                return 4;
            }
        }
        for (int i = 0; i < LEGS_LIST.length; i++) {
            if (itemId == LEGS_LIST[i]) {
                return 7;
            }
        }
        String item = ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase();
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < CAPES.length; i++) {
            if (item.contains(CAPES[i].toLowerCase())) {
                return 1;
            }
        }
        for (int i = 0; i < BOOTS.length; i++) {
            if (item.contains(BOOTS[i].toLowerCase())) {
                return 10;
            }
        }
        for (int i = 0; i < GLOVES.length; i++) {
            if (item.contains(GLOVES[i].toLowerCase())) {
                return 9;
            }
        }
        for (int i = 0; i < SHIELDS.length; i++) {
            if (item.contains(SHIELDS[i].toLowerCase())) {
                return 5;
            }
        }
        for (int i = 0; i < AMULETS.length; i++) {
            if (item.contains(AMULETS[i].toLowerCase())) {
                return 2;
            }
        }
        for (int i = 0; i < ARROWS.length; i++) {
            if (item.contains(ARROWS[i].toLowerCase())) {
                return 13;
            }
        }
        for (int i = 0; i < RINGS.length; i++) {
            if (item.contains(RINGS[i].toLowerCase())) {
                return 12;
            }
        }
        for (int i = 0; i < WEAPONS.length; i++) {
            if (item.contains(WEAPONS[i].toLowerCase())) {
                return 3;
            }
        }
        if (itemId == 4084) {
            return 3;
        }
        for (int i = 0; i < HATS.length; i++) {
            if (item.contains(HATS[i].toLowerCase())) {
                return 0;
            }
        }
        for (int i = 0; i < BODY.length; i++) {
            if (item.contains(BODY[i].toLowerCase())) {
                return 4;
            }
        }
        for (int i = 0; i < LEGS.length; i++) {
            if (item.contains(LEGS[i].toLowerCase())) {
                return 7;
            }
        }
        for (int i = 0; i < AURAS.length; i++) {
            if (item.contains(AURAS[i].toLowerCase())) {
                return SLOT_AURA;
            }
        }
        return -1;
    }

    static boolean isTwoHandedWeapon(Item item) {
        int itemId = item.getId();
        if (itemId == 4212) {
            return true;
        }
        if (itemId == 4084) {
            return true;
        } else if (itemId == 4214) {
            return true;
        } else if (itemId == 20281) {
            return true;
        }
        String wepEquiped = item.getDefinitions().getName().toLowerCase();
        if (wepEquiped == null) {
            return false;
        } else if (wepEquiped.equals("stone of power")) {
            return true;
        } else if (wepEquiped.equals("dominion sword")) {
            return true;
        } else if (wepEquiped.endsWith("claws")) {
            return true;
        } else if (wepEquiped.endsWith("anchor")) {
            return true;
        } else if (wepEquiped.contains("2h sword")) {
            return true;
        } else if (wepEquiped.contains("katana")) {
            return true;
        } else if (wepEquiped.equals("seercull")) {
            return true;
        } else if (wepEquiped.contains("shortbow")) {
            return true;
        } else if (wepEquiped.contains("longbow")) {
            return true;
        } else if (wepEquiped.contains("shortbow")) {
            return true;
        } else if (wepEquiped.contains("bow full")) {
            return true;
        } else if (wepEquiped.equals("zaryte bow")) {
            return true;
        } else if (wepEquiped.equals("dark bow")) {
            return true;
        } else if (wepEquiped.endsWith("halberd")) {
            return true;
        } else if (wepEquiped.contains("maul")) {
            return true;
        } else if (wepEquiped.equals("karil's crossbow")) {
            return true;
        } else if (wepEquiped.equals("torag's hammers")) {
            return true;
        } else if (wepEquiped.equals("verac's flail")) {
            return true;
        } else if (wepEquiped.contains("greataxe")) {
            return true;
        } else if (wepEquiped.contains("spear")) {
            return true;
        } else if (wepEquiped.equals("tzhaar-ket-om")) {
            return true;
        } else if (wepEquiped.contains("godsword")) {
            return true;
        } else if (wepEquiped.equals("saradomin sword")) {
            return true;
        } else return wepEquiped.equals("hand cannon");
    }
}
