package org.redrune.game.content.entity.actor.player.event

import org.redrune.game.content.entity.actor.player.event.Event.EventState
import org.redrune.game.entity.actor.player.Player

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-01-25
 */
class EventManager(
    /**
     * The player whose class this is an instance for
     */
    private val player: Player
) {
    /**
     * The queue of events the player is awaiting to perform
     */
    private var event: Event? = null

    /**
     * Processes the event
     */
    fun process() {
        try {
            if (event == null) {
                return
            }
            when (event?.state) {
                EventState.CREATED -> {
                    event!!.firePolicy(player)
                    event!!.run(player)
                    event!!.setState(EventState.STARTED)
                }
                EventState.STARTED -> event!!.setState(EventState.FINISHED)
                EventState.FINISHED -> event = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            event = null
        }
    }

    /**
     * Starts an event
     *
     * @param event
     * The event to start
     */
    fun start(event: Event?) {
        this.event = event
        fire()
    }

    /**
     * This method fires an event for the player
     */
    private fun fire() {
        if (event == null) {
            return
        }
        if (event!!.getState() != EventState.CREATED) {
            return
        }
        event!!.firePolicy(player)
        event!!.run(player)
        event!!.setState(EventState.STARTED)
    }
}