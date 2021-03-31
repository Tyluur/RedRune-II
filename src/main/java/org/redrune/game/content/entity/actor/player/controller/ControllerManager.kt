package org.redrune.game.content.entity.actor.player.controller

import org.redrune.game.content.entity.item.Foods.Food
import org.redrune.game.content.entity.item.Pots.Pot
import org.redrune.game.entity.Entity
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.game.ClickOption
import java.io.Serializable

class ControllerManager : Serializable {

    var lastController: String? = GameConstants.DEFAULT_CONTROLLER

    var lastControllerArguments: Array<Any>? = null

    @Transient
    private var player: Player? = null

    @Transient
    var controller: Controller? = null
        private set

    @Transient
    private var inited = false
    fun startController(key: String?, vararg parameters: Any) {
        if (controller != null) {
            forceStop()
        }
        controller = ControllerHandler.getController(key)
        if (controller == null) {
            return
        }
        controller!!.setPlayer(player)
        lastControllerArguments = arrayOf(parameters)
        lastController = key
        controller!!.start()
        inited = true
    }

    fun forceStop() {
        if (controller != null) {
            controller!!.forceClose()
            controller = null
        }
        lastControllerArguments = null
        lastController = null
        inited = false
    }

    fun login() {
        if (lastController == null) {
            return
        }
        controller = ControllerHandler.getController(lastController)
        if (controller == null) {
            forceStop()
            return
        }
        controller!!.setPlayer(player)
        if (controller!!.login()) {
            forceStop()
        } else {
            inited = true
        }
    }

    fun logout() {
        if (controller == null) {
            return
        }
        if (controller!!.logout()) {
            forceStop()
        }
    }

    fun canMove(dir: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canMove(dir)
    }

    fun checkWalkStep(lastX: Int, lastY: Int, nextX: Int, nextY: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.checkWalkStep(lastX, lastY, nextX, nextY)
    }

    fun keepCombating(target: Actor?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.keepCombating(target)
    }

    fun canEquip(slotId: Int, itemId: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canEquip(slotId, itemId)
    }

    fun canAddInventoryItem(itemId: Int, amount: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canAddInventoryItem(itemId, amount)
    }

    fun trackXP(skillId: Int, addedXp: Int) {
        if (controller == null || !inited) {
            return
        }
        controller!!.trackXP(skillId, addedXp)
    }

    fun canDeleteInventoryItem(itemId: Int, amount: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canDeleteInventoryItem(itemId, amount)
    }

    fun canUseItemOnItem(itemUsed: Item?, usedWith: Item?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canUseItemOnItem(itemUsed, usedWith)
    }

    fun canAttack(actor: Actor?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canAttack(actor)
    }

    /**
     * Checks if we can click on an entity
     *
     * @param entity
     * The entity
     * @param option
     * The option
     */
    fun canEntityClick(entity: Entity?, option: ClickOption?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canEntityClick(entity, option)
    }

    fun canHit(actor: Actor?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canHit(actor)
    }

    fun moved() {
        if (controller == null || !inited) {
            return
        }
        controller!!.moved()
    }

    fun magicTeleported(type: Int) {
        if (controller == null || !inited) {
            return
        }
        controller!!.magicTeleported(type)
    }

    fun sendInterfaces() {
        if (controller == null || !inited) {
            return
        }
        controller!!.sendInterfaces()
    }

    fun process() {
        if (controller == null || !inited) {
            return
        }
        controller!!.process()
    }

    fun sendDeath(): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.sendDeath()
    }

    fun canEat(food: Food?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canEat(food)
    }

    fun canPot(pot: Pot?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.canPot(pot)
    }

    fun useDialogueScript(key: Any?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.useDialogueScript(key)
    }

    fun processMagicTeleport(toTile: WorldTile?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.processMagicTeleport(toTile)
    }

    fun processItemTeleport(toTile: WorldTile?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.processItemTeleport(toTile)
    }

    fun processObjectTeleport(toTile: WorldTile?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.processObjectTeleport(toTile)
    }

    fun processButtonClick(interfaceId: Int, componentId: Int, slotId: Int, packetId: Int): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.processButtonClick(interfaceId, componentId, slotId, packetId)
    }

    fun processItemOnNPC(npc: NPC?, item: Item?): Boolean {
        return if (controller == null || !inited) {
            true
        } else controller!!.processItemOnNPC(npc, item)
    }

    fun processItemOnPlayer(player: Player?, item: Item?): Boolean {
        return if (controller == null!! || !inited) {
            true
        } else controller!!.processItemOnPlayer(player, item)
    }

    fun removeControllerWithoutCheck() {
        controller = null
        lastControllerArguments = null
        lastController = null
        inited = false
        println("removed: [$lastController]")
    }

    fun handleItemOption1(playerr: Player?, slotId: Int, itemId: Int, item: Item): Boolean {
        if (itemId != item.id) {
            return false
        }
        when (itemId) {
            -1 -> return false
        }
        return true
    }

    fun setPlayer(player: Player?) {
        this.player = player
    }

    companion object {
        private const val serialVersionUID = 2084691334731830796L
    }
}