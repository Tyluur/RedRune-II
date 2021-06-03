package org.redrune.game.content.entity.actor.player.event.item

import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.entity.actor.player.skills.crafting.LeatherCrafting
import org.redrune.game.content.entity.actor.player.skills.herblore.Herblore
import org.redrune.game.content.plugin.PluginRepository.handleItemOnItem
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerInventory
import org.redrune.game.entity.item.ItemOnItemHandler
import org.redrune.game.entity.item.ItemOnItemHandler.ItemOnItem

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class ItemInterfaceInteractionEvent(
    private val interfaceId: Int,
    private val itemUsedId: Int,
    private val fromSlot: Int,
    private val interfaceId2: Int,
    private val itemUsedWithId: Int,
    private val toSlot: Int
) : Event() {
    override fun run(player: Player) {
        if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == PlayerInventory.INVENTORY_INTERFACE) {
            if (player.familiar != null) {
                player.familiar.setSpecial(true)
                if (player.familiar.specialAttack == SpecialAttack.ITEM) {
                    if (player.familiar.hasSpecialOn()) {
                        player.familiar.submitSpecial(toSlot)
                    }
                }
            }
            return
        }
        if (interfaceId == PlayerInventory.INVENTORY_INTERFACE && interfaceId == interfaceId2 && !player.interfaceManager.containsInventoryInter()) {
            if (toSlot >= 28 || fromSlot >= 28) {
                return
            }
            val usedWith = player.inventory.getItem(toSlot)
            val itemUsed = player.inventory.getItem(fromSlot)
            if (itemUsed == null || usedWith == null || itemUsed.id != itemUsedId || usedWith.id != itemUsedWithId) {
                return
            }
            if (!player.controllerManager.canUseItemOnItem(itemUsed, usedWith)) {
                return
            }
            if (handleItemOnItem(player, itemUsed, usedWith)) {
                return
            }
            val herblore = Herblore.isHerbloreSkill(itemUsed, usedWith)
            if (herblore > -1) {
                player.dialogueManager.startDialogue("HerbloreD", herblore, itemUsed, usedWith)
                return
            }
            if (itemUsed.id == LeatherCrafting.NEEDLE.id || usedWith.id == LeatherCrafting.NEEDLE.id) {
                if (LeatherCrafting.handleItemOnItem(player, itemUsed, usedWith)) {
                    return
                }
            }
            val itemOnItem = ItemOnItem.forId(itemUsedId)
            if (itemOnItem != null) {
                if (itemUsedWithId == itemOnItem.item2) {
                    ItemOnItemHandler.handleItemOnItem(player, itemOnItem, usedWith.id, itemUsed.id)
                }
                return
            }
            player.packets.sendMessage("Nothing interesting happens.")
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }
}