package org.redrune.game.content.entity.actor.player.event.npc

import org.redrune.game.GameFlags
import org.redrune.game.content.entity.actor.player.dialogue.impl.Banker
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager.openCollectionBox
import org.redrune.game.content.entity.actor.player.skills.fishing.Fishing
import org.redrune.game.content.entity.actor.player.skills.fishing.Fishing.FishingSpots
import org.redrune.game.content.entity.actor.player.skills.thieving.PickPocketAction
import org.redrune.game.content.entity.actor.player.skills.thieving.PickPocketableNPC
import org.redrune.game.content.plugin.PluginRepository.handleNPC
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.RouteEvent
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-25
 */
class NPCInteractionEvent(
    /**
     * The npc to interact with
     */
    private val npc: NPC,
    /**
     * The clickOption the player used on the npc
     */
    private val clickOption: ClickOption
) : Event() {
    override fun run(player: Player) {
        player.setNextFaceActor(npc)
        player.setRouteEvent(RouteEvent(npc, {
            npc.resetWalkSteps()
            when (clickOption) {
                ClickOption.FIRST -> handleFirstOption(player)
                ClickOption.SECOND -> handleSecondOption(player)
                ClickOption.THIRD -> handleThirdOption(player)
                ClickOption.FOURTH -> handleFourthOption(player)
            }
        }, true))
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }

    /**
     * Performs the necessary actions when the player arrives at the npc after a first option packet
     *
     * @param player The player
     */
    private fun handleFirstOption(player: Player) {
        val option = npc.definitions.getOption(1)
        val spot = FishingSpots.forId(npc.id or 1 shl 24)
        if (spot != null) {
            player.actionManager.action = Fishing(spot, npc)
            return
        }
        player.interactionManager.startInteraction(npc) // if its a spot, they dont interact with players
        if (!player.controllerManager.canEntityClick(npc, ClickOption.FIRST)) {
            return
        }
        if (handleNPC(player, npc, option)) {
            return
        }
        if (npc.definitions.name.contains("Banker") || npc.definitions.name.contains("banker")) {
            player.dialogueManager.startDialogue(Banker::class.java, npc.id)
        } else {
            player.packets.sendMessage("Nothing interesting happens.")
            if (GameFlags.debugMode) {
                println("No plugin registered for option $option on npc $npc")
            }
        }
    }

    /**
     * Performs the necessary actions when the player arrives at the npc after a second option packet
     *
     * @param player The player
     */
    private fun handleSecondOption(player: Player) {
        val option = npc.definitions.getOption(3)
        if (option == null) {
            System.err.println("Unable to perform event due to undefined option. [npc=$npc, clickOption=$clickOption]")
            player.packets.sendMessage("This has not yet been added, please let somebody know!")
            return
        }
        val spot = FishingSpots.forId(npc.id or (2 shl 24))
        if (spot != null) {
            player.actionManager.action = Fishing(spot, npc)
            return
        }
        player.interactionManager.startInteraction(npc) // if its a spot, they dont interact with players
        val pocket = PickPocketableNPC.get(npc.id)
        if (pocket != null) {
            player.actionManager.action = PickPocketAction(npc, pocket)
            return
        }
        if (npc is Familiar) {
            if (player.familiar !== npc) {
                player.packets.sendMessage("That isn't your familiar.")
                return
            }
            if (npc.getDefinitions().hasOption("store")) {
                player.familiar.store()
            } else if (npc.getDefinitions().hasOption("cure")) {
                if (!player.poisonManager.isPoisoned) {
                    player.packets.sendMessage("Your aren't poisoned or diseased.")
                    return
                } else {
                    player.familiar.drainSpecial(2)
                    player.attributes.addPoisonImmune(120)
                }
            }
            return
        }
        if (!player.controllerManager.canEntityClick(npc, ClickOption.SECOND)) {
            return
        }
        if (handleNPC(player, npc, option)) {
            return
        }
        if (npc.definitions.name.contains("Banker") || npc.definitions.name.contains("banker") || npc.id == 13455) {
            player.bank.openBank()
        } else {
            player.packets.sendMessage("Nothing interesting happens.")
            if (GameFlags.debugMode) {
                println("No plugin registered for option $option on npc $npc")
            }
        }
    }

    /**
     * Performs the necessary actions when the player arrives at the npc after a third option packet
     *
     * @param player The player
     */
    private fun handleThirdOption(player: Player) {
        val option = npc.definitions.getOption(4)
        if (option == null) {
            System.err.println("Unable to perform event due to undefined option. [npc=$npc, clickOption=$clickOption]")
            player.packets.sendMessage("This has not yet been added, please let somebody know!")
            return
        }
        player.interactionManager.startInteraction(npc) // if its a spot, they dont interact with players
        if (!player.controllerManager.canEntityClick(npc, ClickOption.THIRD)) {
        } else if (npc.definitions.name.contains("Banker") || npc.definitions.name.contains("banker")) {
            openCollectionBox(player)
        } else if (handleNPC(player, npc, option)) {
        } else {
            if (GameFlags.debugMode) {
                println("No plugin registered for option [option$option, idx=3] on npc $npc")
            }
            player.packets.sendMessage("Nothing interesting happens.")
        }
    }

    /**
     * Performs the necessary actions when the player arrives at the npc after a fourth option packet
     *
     * @param player The player
     */
    private fun handleFourthOption(player: Player) {
        val option = npc.definitions.getOption(5)
        if (option == null) {
            System.err.println("Unable to perform event due to undefined option. [npc=$npc, clickOption=$clickOption]")
            player.packets.sendMessage("This has not yet been added, please let somebody know!")
            return
        }
        player.interactionManager.startInteraction(npc) // if its a spot, they dont interact with players
        if (!player.controllerManager.canEntityClick(npc, ClickOption.FOURTH)) {
            return
        }
        if (handleNPC(player, npc, option)) {
            return
        }
        player.packets.sendMessage("Nothing interesting happens.")
        if (GameFlags.debugMode) {
            println("No plugin registered for option $option on npc $npc")
        }
    }
}