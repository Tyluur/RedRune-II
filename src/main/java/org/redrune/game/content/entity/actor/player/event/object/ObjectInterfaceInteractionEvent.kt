package org.redrune.game.content.entity.actor.player.event.`object`

import org.redrune.cache.loaders.ObjectDefinitions
import org.redrune.game.content.entity.`object`.ObjectHandler
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.player.action.impl.WaterFillingAction
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.entity.actor.player.skills.cooking.Cooking
import org.redrune.game.content.entity.actor.player.skills.crafting.JewelrySmithing
import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing
import org.redrune.game.content.plugin.PluginRepository.handleItemOnObject
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerInventory
import org.redrune.game.entity.actor.player.data.RouteEvent
import org.redrune.game.entity.item.Item
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.SkillConstants
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class ObjectInterfaceInteractionEvent(
    `object`: WorldObject,
    y: Int,
    x: Int,
    itemSlot: Int,
    interfaceId: Int,
    itemId: Int,
    item: Item
) : Event() {
    private val `object`: WorldObject
    private val y: Int
    private val x: Int
    private val itemSlot: Int
    private val interfaceId: Int
    private val itemId: Int
    private val item: Item
    override fun run(player: Player) {
        val objectDef: ObjectDefinitions = `object`.definitions
        player.setRouteEvent(RouteEvent(`object`, Runnable label@{
            player.nextFaceWorldTile = WorldTile(
                `object`.getCoordFaceX(objectDef.sizeX, objectDef.sizeY, `object`.rotation),
                `object`.getCoordFaceY(objectDef.sizeX, objectDef.sizeY, `object`.rotation),
                `object`.plane
            )
            if (interfaceId == PlayerInventory.INVENTORY_INTERFACE) {
                if (handleItemOnObject(player, item, `object`)) {
                    return@label
                } else if (`object`.definitions.name == "Anvil") {
                    player.temporaryAttributes["itemUsed"] = itemId
                    val bar: Smithing.ForgingBar = Smithing.ForgingBar.forId(itemId)
                    if (bar != null) {
                        Smithing.ForgingInterface.sendSmithingInterface(player)
                    }
                } else if (itemId == 1438 && `object`.id == 2452) {
                    Runecrafting.enterAirAltar(player)
                } else if (itemId == 1440 && `object`.id == 2455) {
                    Runecrafting.enterEarthAltar(player)
                } else if (itemId == 1442 && `object`.id == 2456) {
                    Runecrafting.enterFireAltar(player)
                } else if (itemId == 1444 && `object`.id == 2454) {
                    Runecrafting.enterWaterAltar(player)
                } else if (itemId == 1446 && `object`.id == 2457) {
                    Runecrafting.enterBodyAltar(player)
                } else if (itemId == 1448 && `object`.id == 2453) {
                    Runecrafting.enterMindAltar(player)
                } else if (`object`.definitions.name == "Furnace") {
                    if (item.id == 2357) {
                        JewelrySmithing.openInterface(player)
                    }
                } else if (itemId == 229 || itemId == 1923 || itemId == 1925 || itemId == 1935 || itemId == 3734 || itemId == 5350 && `object`.definitions
                        .name == "Fountain" || `object`.definitions
                        .name == "Well" || `object`.definitions.name == "Sink"
                ) {
                    if (WaterFillingAction.isFilling(player, itemId, false)) {
                        return@label
                    }
                } else if (itemId == 536 && `object`.definitions.name == "Altar") { //Dragon Bones
                    player.packets.sendMessage("You pray to the gods and they accept your offering.")
                    player.inventory.deleteItem(Item(536, 1))
                    player.skills.addXp(SkillConstants.PRAYER, 650.0)
                    player.packets.sendSound(2738, 0, 1)
                    player.nextAnimation = Animation(896)
                    player.setNextGraphics(Graphics(624))
                    player.inventory.refresh()
                } else if (itemId == 18830 && `object`.definitions.name == "Altar") { //Frost Dragon bones
                    player.packets.sendMessage("You pray to the gods and they accept your offering.")
                    player.inventory.deleteItem(Item(18830, 1))
                    player.skills.addXp(SkillConstants.PRAYER, 1127.0)
                    player.packets.sendSound(2738, 0, 1)
                    player.nextAnimation = Animation(896)
                    player.setNextGraphics(Graphics(624))
                    player.inventory.refresh()
                } else if (itemId == 526 && `object`.definitions.name == "Altar") { //Bones
                    player.packets.sendMessage("You pray to the gods and they accept your offering.")
                    player.inventory.deleteItem(Item(526, 1))
                    player.skills.addXp(SkillConstants.PRAYER, 186.0)
                    player.packets.sendSound(2738, 0, 1)
                    player.nextAnimation = Animation(896)
                    player.setNextGraphics(Graphics(624))
                    player.inventory.refresh()
                } else if (itemId == 532 && `object`.definitions.name == "Altar") { //Big bones
                    player.packets.sendMessage("You pray to the gods and they accept your offering.")
                    player.inventory.deleteItem(Item(532, 1))
                    player.skills.addXp(SkillConstants.PRAYER, 249.0)
                    player.packets.sendSound(2738, 0, 1)
                    player.nextAnimation = Animation(896)
                    player.setNextGraphics(Graphics(624))
                    player.inventory.refresh()
                } else if (`object`.id == 733 || `object`.id == 64729) {
                    player.nextAnimation = Animation(CombatAlgorithm.getWeaponAttackEmote(-1, 0))
                    ObjectHandler.slashWeb(player, `object`)
                } else if (objectDef.name.lowercase(Locale.getDefault()).contains("range") || objectDef.name
                        .lowercase(Locale.getDefault())
                        .contains("stove") || `object`.id == 2732
                ) {
                    val cook = Cooking.isCookingSkill(item)
                    player.dialogueManager.startDialogue("CookingD", cook, `object`)
                } else {
                    player.packets.sendMessage("Nothing interesting happens.")
                }
            }
        }))
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE)
    }

    init {
        this.`object` = `object`
        this.y = y
        this.x = x
        this.itemSlot = itemSlot
        this.interfaceId = interfaceId
        this.itemId = itemId
        this.item = item
    }
}