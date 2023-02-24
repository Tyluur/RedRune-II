package org.redrune.utility.game.entity.actor.player

import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.Censor.getFilteredMessage

open class ChatMessage(message: String?) {

    @JvmField
    var message: String? = null
    private var filteredMessage: String? = null

    init {
        if (this !is QuickChatMessage) {
            filteredMessage = getFilteredMessage(message!!)
            this.message = Misc.fixChatMessage(message)
        } else {
            this.message = message
        }
    }

    fun getMessage(filtered: Boolean): String? {
        if (this is QuickChatMessage) {
            return message
        }
        return if (filtered) filteredMessage else message
    }
}