package org.redrune.engine.factory

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DecoderThreadFactory : ThreadFactory {
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String
    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.MAX_PRIORITY - 1) {
            t.priority = Thread.MAX_PRIORITY - 1
        }
        return t
    }

    companion object {
        private val poolNumber = AtomicInteger(1)
    }

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "Decoder Pool-" + poolNumber.getAndIncrement() + "-thread-"
    }
}