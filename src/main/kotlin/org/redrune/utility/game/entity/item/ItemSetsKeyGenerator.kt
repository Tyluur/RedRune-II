package org.redrune.utility.game.entity.item

import java.util.concurrent.atomic.AtomicInteger

object ItemSetsKeyGenerator {
    private val nextKey = AtomicInteger(500) // After

    // 400
    // keys
    // uses
    // negative
    // keys
    @JvmStatic
    fun generateKey(): Int {
        val key = nextKey.getAndDecrement()
        if (key > 0 && key <= 100) {
            nextKey.set(-1) // starts at negative
        }
        return key
    }
}