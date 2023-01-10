package org.redrune.engine.factory

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DecoderThreadFactory : ThreadFactory {
    /**
     * A [ThreadGroup] to which the created threads will be added.
     */
    private val group: ThreadGroup

    /**
     * An [AtomicInteger] to keep track of the number of threads created by this factory.
     */
    private val threadNumber = AtomicInteger(1)

    /**
     * The prefix to use for the name of the created threads.
     */
    private val namePrefix: String

    /**
     * Initializes the [group] with a [SecurityManager] if it exists, or the current thread's
     * thread group otherwise. It also initializes the [namePrefix] using a static [AtomicInteger]
     * to keep track of the number of instances of this class and an incrementing [threadNumber].
     */
    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "Decoder Pool-" + poolNumber.getAndIncrement() + "-thread-"
    }

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
}