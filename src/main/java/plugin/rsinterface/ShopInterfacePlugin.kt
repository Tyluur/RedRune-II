package plugin.rsinterface

import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.content.entity.actor.player.design.PlayerDesign
import org.redrune.game.content.entity.actor.player.PlayerLook
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.InputEvent.InputEventType
import org.redrune.engine.SystemManager
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.EquipmentConstants
import org.redrune.game.entity.actor.player.data.PlayerEquipment
import plugin.rsinterface.EquipmentBonusesInterfacePlugin
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing.ForgingInterface
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer
import org.redrune.game.content.entity.actor.combat.function.Magic
import org.redrune.game.content.entity.actor.player.dialogue.impl.Transportation
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.utility.functions.Misc
import org.redrune.game.content.entity.actor.player.action.impl.PlayerRestAction
import plugin.rsinterface.GenieSkillSelectionInterfacePlugin
import org.redrune.game.content.entity.actor.player.event.item.ItemInteractionEvent
import org.redrune.game.content.entity.item.InventoryOptionsHandler
import org.redrune.game.content.entity.actor.player.skills.crafting.JewelrySmithing
import org.redrune.game.content.entity.actor.player.market.Shop
import org.redrune.game.content.entity.actor.player.dialogue.impl.SkillsDialogue
import org.redrune.game.content.entity.actor.player.dialogue.impl.LevelUp
import plugin.rsinterface.TeleportationInterfacePlugin.TravelLocations
import plugin.rsinterface.TeleportationInterfacePlugin
import org.redrune.utility.game.map.Coordinates
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue
import org.redrune.utility.constants.ChatAnimations
import plugin.rsinterface.TeleportationInterfacePlugin.TransportationLocation
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.engine.tick.task.WorldTask
import org.redrune.utility.constants.MagicConstants
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
class ShopInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int
    ): Boolean {
        val shop = player.getTemporaryAttribute<Shop>("open_shop") ?: return true
        if (interfaceId == Shop.INTERFACE_ID) {
            if (componentId == 25) {
                when (packetId) {
                    PacketConstants.ACTION_BUTTON1_PACKET -> shop.value(player, slotId, false)
                    PacketConstants.ACTION_BUTTON2_PACKET -> shop.buy(player, slotId, 1)
                    PacketConstants.ACTION_BUTTON3_PACKET -> shop.buy(player, slotId, 5)
                    PacketConstants.ACTION_BUTTON4_PACKET -> shop.buy(player, slotId, 10)
                    PacketConstants.ACTION_BUTTON5_PACKET -> shop.buy(player, slotId, 50)
                    PacketConstants.ACTION_BUTTON9_PACKET -> shop.buy(player, slotId, 500)
                    PacketConstants.ACTION_BUTTON8_PACKET -> {
                        val item = shop.getItem(slotId / 6)
                        player.packets.sendMessage(ItemCharacteristicRepository.getExamine(item))
                    }
                }
            }
        } else if (interfaceId == Shop.INVENTORY_INTERFACE_ID) {
            if (componentId == 0) {
                when (packetId) {
                    PacketConstants.ACTION_BUTTON1_PACKET -> shop.value(player, slotId, true)
                    PacketConstants.ACTION_BUTTON2_PACKET -> shop.sell(player, slotId, 1)
                    PacketConstants.ACTION_BUTTON3_PACKET -> shop.sell(player, slotId, 5)
                    PacketConstants.ACTION_BUTTON4_PACKET -> shop.sell(player, slotId, 10)
                    PacketConstants.ACTION_BUTTON5_PACKET -> shop.sell(player, slotId, 50)
                    PacketConstants.ACTION_BUTTON9_PACKET -> player.inventory.sendExamine(slotId)
                }
            }
        }
        return true
    }

    override fun register() {
        registerInterfacePlugin(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID)
    }
}