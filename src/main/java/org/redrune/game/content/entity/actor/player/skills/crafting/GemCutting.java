package org.redrune.game.content.entity.actor.player.skills.crafting;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.player.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.constants.SkillConstants;

public class GemCutting extends Action {

    private final Gem gem;

    private int quantity;

    public GemCutting(Gem gem, int quantity) {
        this.gem = gem;
        this.quantity = quantity;
    }

    public static void cut(Player player, Gem gem) {
        if (player.getInventory().getItems().getNumberOf(new Item(gem.getUncut(), 1)) <= 1) // contains just
        // 1 lets start
        {
            player.getActionManager().setAction(new GemCutting(gem, 1));
        } else {
            player.getDialogueManager().startDialogue("GemCuttingD", gem);
        }
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            setActionDelay(player, 1);
            player.setNextAnimation(new Animation(gem.getEmote()));
            return true;
        }
        return false;
    }

    public boolean checkAll(Player player) {
        if (player.getSkills().getLevel(SkillConstants.CRAFTING) < gem.getLevelRequired()) {
            player.getDialogueManager().startDialogue("SimpleMessage", "You need a crafting level of " + gem.getLevelRequired() + " to cut that gem.");
            return false;
        }
        if (!player.getInventory().containsOneItem(gem.getUncut())) {
            player.getDialogueManager().startDialogue("SimpleMessage", "You don't have any " + ItemDefinitions.getItemDefinitions(gem.getUncut()).getName().toLowerCase() + " to cut.");
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
        player.getInventory().deleteItem(gem.getUncut(), 1);
        player.getInventory().addItem(gem.getCut(), 1);
        player.getSkills().addXp(SkillConstants.CRAFTING, gem.getExperience());
        player.getPackets().sendMessage("You cut the " + ItemDefinitions.getItemDefinitions(gem.getUncut()).getName().toLowerCase() + ".", true);
        quantity--;
        if (quantity <= 0) {
            return -1;
        }
        player.setNextAnimation(new Animation(gem.getEmote())); // start the
        // emote and add
        // 2 delay
        return 0;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

    /**
     * Enum for gems
     *
     * @author Raghav
     */
    public enum Gem {
        OPAL(1625, 1609, 15.0, 1, 886),
        JADE(1627, 1611, 20, 13, 886),
        RED_TOPAZ(1629, 1613, 25, 16, 887),
        SAPPHIRE(1623, 1607, 50, 20, 888),
        EMERALD(1621, 1605, 67, 27, 889),
        RUBY(1619, 1603, 85, 34, 887),
        DIAMOND(1617, 1601, 107.5, 43, 890),
        DRAGONSTONE(1631, 1615, 137.5, 55, 885),
        ONYX(6571, 6573, 168, 67, 2717);

        private final double experience;

        private final int levelRequired;

        private final int uncut;
        private final int cut;

        private final int emote;

        Gem(int uncut, int cut, double experience, int levelRequired, int emote) {
            this.uncut = uncut;
            this.cut = cut;
            this.experience = experience;
            this.levelRequired = levelRequired;
            this.emote = emote;
        }

        public int getLevelRequired() {
            return levelRequired;
        }

        public double getExperience() {
            return experience;
        }

        public int getUncut() {
            return uncut;
        }

        public int getCut() {
            return cut;
        }

        public int getEmote() {
            return emote;
        }

    }
}
