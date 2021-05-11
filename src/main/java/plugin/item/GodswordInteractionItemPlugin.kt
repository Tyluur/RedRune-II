package plugin.item

import cache.codec.loaders.ItemDefinitions
import engine.tick.task.WorldTask
import engine.tick.task.WorldTasksManager
import game.content.entity.actor.player.dialogue.impl.SimpleMessage
import game.content.entity.actor.player.skills.smithing.Smithing
import game.content.plugin.type.ItemOnItemPlugin
import game.content.plugin.type.ItemOnObjectPlugin
import game.content.plugin.type.ItemPlugin
import game.entity.`object`.WorldObject
import game.entity.actor.mask.Animation
import game.entity.actor.player.Player
import game.entity.item.Item
import utility.constants.SkillConstants
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class GodswordInteractionItemPlugin : ItemPlugin, ItemOnItemPlugin, ItemOnObjectPlugin {
    override fun handleItemOnItem(player: Player, used: Item, with: Item): Boolean {
        val usedName = used.name.toLowerCase()
        val hilt = if (usedName.contains("hilt")) used else with
        val swordId = getSwordByHilt(hilt.id)
        if (swordId == -1) {
            return false
        }
        player.inventory.deleteItem(used)
        player.inventory.deleteItem(with)
        player.inventory.addItem(swordId, 1)
        val name = ItemDefinitions.getItemDefinitions(swordId).name
        player.dialogueManager.startDialogue(
            SimpleMessage::class.java,
            "You attach the hilt to the blade and make a" + (if (name.toLowerCase()
                    .startsWith("a")
            ) "n" else "") + " " + name + "."
        )
        return true
    }

    private fun getSwordByHilt(hiltId: Int): Int {
        when (hiltId) {
            11702 -> return 11694
            11704 -> return 11696
            11706 -> return 11698
            11708 -> return 11700
        }
        return -1
    }

    override fun handle(player: Player, item: Item, `object`: WorldObject): Boolean {
        if (!player.inventory.containsItems(GodswordComponents.SHARDS.itemIds, intArrayOf(1, 1, 1))) {
            player.dialogueManager.startDialogue(
                SimpleMessage::class.java,
                "You don't have all the godsword shards necessary to build a blade."
            )
            return true
        }
        if (!player.inventory.containsItem(Smithing.HAMMER, 1)) {
            player.dialogueManager.startDialogue("SimpleMessage", "You need a hammer in order to work with shards.")
            return true
        }
        if (player.skills.getLevel(SkillConstants.SMITHING) < 80) {
            player.dialogueManager.startDialogue(
                "SimpleMessage",
                "You need a Smithing level of 80 to forge godsword shards."
            )
            return true
        }
        player.locks.lock()
        player.nextAnimation = ANVIL_ANIMATION
        player.skills.addXp(SkillConstants.SMITHING, 200.0)
        player.dialogueManager.startDialogue(
            SimpleMessage::class.java,
            "You set to work, trying to fix the ancient sword."
        )
        WorldTasksManager.schedule(object : WorldTask() {
            override fun run() {
                player.inventory.deleteItem(GodswordComponents.Constants.GODSWORD_SHARD_1, 1)
                player.inventory.deleteItem(GodswordComponents.Constants.GODSWORD_SHARD_2, 1)
                player.inventory.deleteItem(GodswordComponents.Constants.GODSWORD_SHARD_3, 1)
                player.inventory.addItem(11690, 1)
                player.dialogueManager.startDialogue(
                    SimpleMessage::class.java,
                    "Even as an experienced smith it is not an easy task, but eventually",
                    "it is done."
                )
                player.locks.unlock()
            }
        }, 3)
        return true
    }

    override fun register() {
        Arrays.stream(GodswordComponents.GODSWORDS.itemIds).forEach { sword: Int -> registerItem(sword, "Dismantle") }
        Arrays.stream(GodswordComponents.HILTS.itemIds)
            .forEach { hilt: Int -> registerItemOnItemIds(hilt, GODSWORD_BLADE) }
        Arrays.stream(GodswordComponents.SHARDS.itemIds).forEach { shard: Int ->
            registerItemOnObjectPlugins(shard, 2782)
            registerItemOnObjectPlugins(shard, 2783)
        }
    }

    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        dismantleGS(player, item, slotId)
        return true
    }

    internal enum class GodswordComponents(val itemIds: IntArray) {
        GODSWORDS(intArrayOf(11694, 11696, 11698, 11700)), HILTS(intArrayOf(11702, 11704, 11706, 11708)), SHARDS(
            intArrayOf(
                Constants.GODSWORD_SHARD_1, Constants.GODSWORD_SHARD_2, Constants.GODSWORD_SHARD_3
            )
        );

        internal object Constants {
            const val GODSWORD_SHARD_1 = 11710
            const val GODSWORD_SHARD_2 = 11712
            const val GODSWORD_SHARD_3 = 11714
        }
    }

    companion object {
        /**
         * The item id of a godsword blade
         */
        private const val GODSWORD_BLADE = 11690

        /**
         * The animation to perform when starting on the anvil
         */
        private val ANVIL_ANIMATION = Animation(898)
        fun dismantleGS(player: Player, item: Item, slot: Int) {
            val gs = (item.id - 11694) / 2
            if (!player.inventory.hasFreeSlots()) {
                player.packets.sendMessage("Not enough space in your inventory.")
                return
            }
            item.id = 11690
            player.inventory.addItem(11702 + gs * 2, 1)
            player.inventory.refresh(slot)
            player.packets.sendMessage("You dismantle the godsword")
        }
    }
}