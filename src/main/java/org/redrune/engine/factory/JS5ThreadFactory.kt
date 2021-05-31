package org.redrune.engine.factory

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@icloud.com>
 */
class JS5ThreadFactory @JvmOverloads constructor(
    private val name: String,
    private val priority: Int = Thread.NORM_PRIORITY
) : ThreadFactory {
    private val threadCount = AtomicInteger()
    override fun newThread(r: Runnable): Thread {
        val thread = Thread(r, StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString())
        thread.priority = priority
        return thread
    }
}