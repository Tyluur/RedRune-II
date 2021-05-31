package org.redrune.engine.factory

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class SlowThreadFactory : ThreadFactory {
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String
    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.MIN_PRIORITY) {
            t.priority = Thread.MIN_PRIORITY
        }
        return t
    }

    companion object {
        private val poolNumber = AtomicInteger(1)
    }

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "Slow Pool-" + poolNumber.getAndIncrement() + "-thread-"
    }
}