package org.redrune.game.entity.actor.player.data;

import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;


public final class PresetManager implements Serializable {

    private static final long serialVersionUID = -2928476953478619103L;
    private final transient boolean eco = false;//THEESE ARE TEMPORARY
    private final transient boolean halfEco = false;
    private final transient boolean spawn = true;
    private final transient int priceLimit = 100000;
    public HashMap<String, Preset> setups;
    /**
     * Instantiated variables below
     **/
    private transient Player player;

    public PresetManager() {
        setups = new HashMap<String, Preset>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private int getMaxSize() {
        return 28;
    }

    public void reset() {
        setups.clear();
        player.getPackets().sendMessage(
                "All of your sets have been cleared. You now have " + getMaxSize() + " available slots.");
    }

    public void removePreset(String name) {
        if (name == "")
            return;
        name = name.toLowerCase();
        player.getPackets()
                .sendMessage((setups.remove(name) == null ? "No set was found for the query: " + name
                        : "Successfully removed the set: " + name) + ".");
    }

    public void savePreset(String name) {
        final int size = setups.size(), max = getMaxSize();
        if (size >= max) {
            player.getPackets().sendMessage("You were unable to store the set " + name
                    + " as your maximum capacity (" + max + ") has been reached.", true);
            return;
        }
        if (name == "")
            return;
        name = name.toLowerCase();
        final Preset set = setups.get(name);
        if (set != null) {
            player.getPackets().sendMessage("You were unable to store the set " + name + " as it already exists.",
                    true);
            return;
        }
        final Item[] inventory = player.getInventory().getItems().getItemsCopy(),
                equipment = player.getEquipment().getItems().getItemsCopy();
        setups.put(name,
                new Preset(name, inventory, equipment, player.getPrayer().isAncientCurses(),
                        (byte) player.getCombatDefinitions().getSpellBook(), (Arrays.copyOf(player.getSkills().getXp(), 7))));

        player.getPackets().sendMessage("You've successfully stored the set " + name + ".", true);
    }

    public void printPresets() {
        final int size = setups.size();
        player.getPackets().sendMessage("You have used " + size + "/" + getMaxSize() + " available setups.", true);
        if (size > 0) {
            player.getPackets().sendMessage("<col=ff0000>Your available setups are:", true);
            for (final String key : setups.keySet()) {
                player.getPackets().sendMessage(key, true);
            }
        }
    }

    public void loadPreset(String name, Player p2) {
        if (name == "")
            return;
        if (player.getControllerManager().getController() instanceof Wilderness) {
            player.getPackets().sendMessage(
                    "You can't load gear presets in the wilderness.");
            return;
        }
        name = name.toLowerCase();
        final Preset set = (p2 != null ? p2.getPresetManager().setups.get(name) : setups.get(name));
        if (set == null) {
            player.getPackets().sendMessage("You were unable to load the set " + name + " as it does not exist.",
                    true);
            return;
        }

        Item[] inventory = player.getInventory().getItems().getItemsCopy();
        Item[] equipment = player.getEquipment().getItems().getItemsCopy();
        for (Item item : inventory) {
            if (item != null)
                player.getBank().addItem(item, true);
        }
        for (Item item : equipment) {
            if (item != null)
                player.getBank().addItem(item, true);
        }
        player.getInventory().reset();
        player.getInventory().refresh();
        player.getEquipment().reset();
        player.getEquipment().refresh();
        player.getAppearance().generateAppearanceData();
        if (halfEco || spawn) {
            if (set.getLevels() != null) {
                for (int id = 0; id < set.getLevels().length; id++) {
                    player.getSkills().setXp(id, set.getLevels()[id]);
                    player.getSkills().set(id, player.getSkills().getLevelForXp(id));
                }
            }
        }
        player.refreshHitPoints();
        player.getPrayer().reset();
        Item[] data = set.getEquipment();
        if (data != null && data.length > 0) {
            skip:
            for (int i = 0; i < data.length; i++) {
                final Item item = data[i];
                if (item == null)
                    continue;
                final HashMap<Integer, Integer> requirements = item.getDefinitions().getWearingRequirements();
                if (requirements != null) {
                    for (final int skillId : requirements.keySet()) {
                        if (skillId > 24 || skillId < 0)
                            continue;
                        final int level = requirements.get(skillId);
                        if (level < 0 || level > 120)
                            continue;
                        if (player.getSkills().getLevelForXp(skillId) < level) {
                            player.getPackets()
                                    .sendMessage("You were unable to equip your " + item.getName().toLowerCase()
                                            + ", as you don't meet the requirements to wear them.", true);
                            continue skip;
                        }
                    }
                }
                if ((halfEco && set.getEquipment()[i].getDefinitions().getValue() >= priceLimit)
                        || eco
                        || (!item.getDefinitions().isTradeable() && (halfEco || eco))) {
                    if (player.getBank().getItem(set.getEquipment()[i].getId()) != null && player.getBank()
                            .getItem(set.getEquipment()[i].getId()).getAmount() >= item.getAmount()) {
                        int[] slot = player.getBank().getItemSlot(set.getEquipment()[i].getId());
                        player.getBank().removeItem2(slot, item.getAmount(), true, false);
                    } else {
                        player.getPackets().sendMessage(
                                "Couldn't find item " + item.getAmount() + " x " + item.getName() + " in bank.");
                        continue;
                    }
                }
                player.getEquipment().getItems().set(i, new Item(set.getEquipment()[i].getId(), item.getAmount()));
                player.getEquipment().refresh(i);
            }
        }

        data = set.getInventory();
        if (data != null && data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                final Item item = data[i];
                if (item == null)
                    continue;
                if ((halfEco && set.getInventory()[i] != null
                        && set.getInventory()[i].getDefinitions().getValue() >= priceLimit)
                        || eco
                        || (!item.getDefinitions().isTradeable() && (eco || halfEco))) {
                    if (player.getBank().getItem(set.getInventory()[i].getId()) != null && player.getBank()
                            .getItem(set.getInventory()[i].getId()).getAmount() >= item.getAmount()) {
                        int[] slot = player.getBank().getItemSlot(set.getInventory()[i].getId());
                        player.getBank().removeItem2(slot, item.getAmount(), true, false);
                    } else {
                        player.getInventory().addItem(0, 1);
                        player.getPackets().sendMessage(
                                "Couldn't find item " + item.getAmount() + " x " + item.getName() + " in bank.");
                        continue;
                    }
                }
                player.getInventory().addItem(item);
            }
        }

        player.getInventory().deleteItem(0, 28);
        player.getCombatDefinitions().setSpellBook(set.getSpellBook());
        player.getPrayer().setPrayerBook(set.isAncientCurses());
        player.getAppearance().generateAppearanceData();
        player.getPackets().sendMessage("Loaded setup: " + name + ".");

    }

}