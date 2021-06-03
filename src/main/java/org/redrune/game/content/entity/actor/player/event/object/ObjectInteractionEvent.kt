package org.redrune.game.content.entity.actor.player.event.`object`

import org.redrune.game.content.entity.`object`.ObjectHandler
import org.redrune.game.content.entity.actor.player.event.Event
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.game.ClickOption

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
class ObjectInteractionEvent(`object`: WorldObject, option: ClickOption) : Event() {
    /**
     * The object to interact with
     */
    private val `object`: WorldObject = `object`

    /**
     * The option that was clicked on the object
     */
    private val option: ClickOption = option

    override fun run(player: Player) {
        when (option) {
            ClickOption.FIRST -> ObjectHandler.handleOption1(player, `object`)
            ClickOption.SECOND -> ObjectHandler.handleOption2(player, `object`)
            ClickOption.THIRD -> ObjectHandler.handleOption3(player, `object`)
        }
    }

    override fun policies(): Array<EventPolicy> {
        return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK)
    }

}