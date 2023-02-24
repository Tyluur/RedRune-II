package org.redrune.game.content.entity.actor.player.action.impl;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.player.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Gircat <gircat101@gmail.com> Created on Jul 30, 2014 at 2:35:47 AM.
 */
public class WaterFillingAction extends Action {

    private final Fill fill;

    private int quantity;

    public WaterFillingAction(Fill fill, int quantity) {
        this.fill = fill;
        this.quantity = quantity;
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            setActionDelay(player, 1);
            player.setNextAnimation(new Animation(832));
            player.getPackets().sendMessage("You fill the " + ItemDefinitions.getItemDefinitions(fill.full).getName() + ".");
            return true;
        }
        return false;
    }

    public boolean checkAll(Player player) {
        if (!player.getInventory().containsOneItem(fill.empty)) {
            player.getDialogueManager().startDialogue("SimpleMessage", "You don't have any " + ItemDefinitions.getItemDefinitions(fill.empty).getName().toLowerCase() + " to fill.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        player.getInventory().deleteItem(fill.empty, 1);
        player.getInventory().addItem(fill.full, 1);
        quantity--;
        if (quantity <= 0) {
            return -1;
        }
        player.setNextAnimation(new Animation(fill.ordinal() == 5 ? 2272 : 832));
        return fill.ordinal() == 5 ? 3 : 0;
    }

    @Override
    public void stop(final Player player) {
        setActionDelay(player, 3);
    }

    public static boolean isFilling(Player player, int empty, boolean isSpot) {
        for (Fill fill : Fill.values()) {
            if (fill.empty == empty) {
                if (isSpot && fill.ordinal() <= 4) {
                    return false;
                }
                fill(player, fill);
                return true;
            }
        }
        return false;
    }

    private static void fill(Player player, Fill fill) {
        if (player.getInventory().getItems().getNumberOf(new Item(fill.empty, 1)) <= 1) {
            player.getActionManager().setAction(new WaterFillingAction(fill, 1));
        } else {
            player.getDialogueManager().startDialogue("WaterFillingD", fill);
        }
    }

    public enum Fill {
        VIAL(229, 227),
        BOWL(1923, 1921),
        BUCKET(1925, 1929),
        JUG(1935, 1937),
        VASE(3734, 3735),
        PLANT_POT(5350, 5354);

        private final int empty;
        private final int full;

        Fill(int empty, int full) {
            this.empty = empty;
            this.full = full;
        }

        public int getEmpty() {
            return empty;
        }

    }
}
