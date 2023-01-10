package org.redrune.engine.factory

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * A [ThreadFactory] implementation that creates threads with a name prefix and an increasing count.
 *
 * @param name the prefix to use for the name of the created threads
 * @param priority the priority to set for the created threads. The default is [Thread.NORM_PRIORITY]
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@icloud.com>
 */
class JS5ThreadFactory @JvmOverloads constructor(
    private val name: String,
    private val priority: Int = Thread.NORM_PRIORITY
) : ThreadFactory {

    /**
     * An [AtomicInteger] to keep track of the number of threads created by this factory.
     */
    private val threadCount = AtomicInteger()
    override fun newThread(r: Runnable): Thread {
        val thread = Thread(r, StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString())
        thread.priority = priority
        return thread
    }
}