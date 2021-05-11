package game.content.entity.item;

import engine.SystemManager;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.content.plugin.PluginRepository;
import game.entity.actor.player.Player;
import game.entity.actor.player.data.PlayerEquipment;
import game.entity.item.Item;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.constants.EquipmentConstants;
import utility.game.repository.item.ItemCharacteristicRepository;

import java.util.concurrent.TimeUnit;

public class InventoryOptionsHandler {

    public static void handleItemOption1(Player player, final int slotId, final int itemId, Item item) {
        if (!player.getControllerManager().handleItemOption1(player, slotId, itemId, item)) {
            return;
        }
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(1))) {
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption2(final Player player, final int slotId, final int itemId, Item item) {
        if (item.getDefinitions().isWearItem()) {

            if (player.getAttributes().isEquipDisabled()) {
                return;
            }

            if (player.getAttributes().getSwitchItemCache().isEmpty()) {
                player.getAttributes().getSwitchItemCache().add(new Integer[]{itemId, slotId});
                SystemManager.SLOW_EXECUTOR.schedule(() -> {
                    try {
                        WorldTasksManager.schedule(new WorldTask() {

                            @Override
                            public void run() {
                                player.processSwitches();
                            }
                        }, 0);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }, 300, TimeUnit.MILLISECONDS);

            } else if (!player.getAttributes().getSwitchItemCache().contains(slotId)) {
                player.getAttributes().getSwitchItemCache().add(new Integer[]{itemId, slotId});
            }

            player.stopAll(false);
            PlayerEquipment.equipMultipleSlots(player, new int[]{slotId});
            player.getCombatDefinitions().setUsingSpecialAttack(false);

            return;
        } else if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(2))) {
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption3(Player player, int slotId, int itemId, Item item) {
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(3))) {
            return;
        } else if (EquipmentConstants.getItemSlot(itemId) == EquipmentConstants.SLOT_AURA) {
            player.getAuraManager().sendTimeRemaining(itemId);
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption4(Player player, int slotId, int itemId, Item item) {
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(4))) {
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption5(Player player, int slotId, int itemId, Item item) {
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(5))) {
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption6(Player player, int slotId, int itemId, Item item) {
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(4))) {
            return;
        }
        player.getPackets().sendMessage("Nothing interesting happens.");
    }

    public static void handleItemOption7(Player player, int slotId, int itemId, Item item) {
        if (PluginRepository.handleItem(player, item, slotId, item.getDefinitions().getInventoryOption(7))) {
            return;
        }
        if (item.getDefinitions().isDestroyItem()) {
            player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
            return;
        }
        if (player.getCharges().degradeCompletly(item)) {
            return;
        }
        player.getInventory().deleteItem(slotId, item);
        RegionManager.addGroundItem(item, new WorldTile(player), player, false, 180, true);
        player.getPackets().sendSound(2739, 0, 1);
    }

    public static void handleItemOption8(Player player, int slotId, int itemId, Item item) {
        player.getPackets().sendMessage(ItemCharacteristicRepository.getExamine(item.getId()));
    }

    public static Item contains(int id1, Item item1, Item item2) {
        if (item1.getId() == id1) {
            return item2;
        }
        if (item2.getId() == id1) {
            return item1;
        }
        return null;
    }

    public static boolean contains(int id1, int id2, Item... items) {
        boolean containsId1 = false;
        boolean containsId2 = false;
        for (Item item : items) {
            if (item.getId() == id1) {
                containsId1 = true;
            } else if (item.getId() == id2) {
                containsId2 = true;
            }
        }
        return containsId1 && containsId2;
    }
}