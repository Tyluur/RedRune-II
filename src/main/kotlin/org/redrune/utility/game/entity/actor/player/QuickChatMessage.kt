package org.redrune.utility.game.entity.actor.player

class QuickChatMessage(@JvmField var fileId: Int, data: ByteArray?) :
    PublicChatMessage(if (data == null) null else String(data), 0x8000)