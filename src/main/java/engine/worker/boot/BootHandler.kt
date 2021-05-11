package engine.worker.boot

import com.google.common.base.Stopwatch
import engine.SystemManager
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 10/24/2015
 */
object BootHandler {
    /**
     * The amount of threads we can construct
     */
    private val THREAD_SIZE = SystemManager.PROCESSOR_COUNT

    /**
     * The list of work we must complete
     */
    private val WORK_TO_COMPLETE: MutableList<Runnable> = ArrayList()

    /**
     * The list of boot workers
     */
    private val BOOT_WORKERS = CopyOnWriteArrayList<BootWorker>()
    /**
     * Gets the [.countDownLatch]
     */
    /**
     * The amount of work that must be complete
     */
    @JvmStatic
    var latch: CountDownLatch? = null
        private set

    /**
     * The instance of the stopwatch
     */
    val stopwatch = Stopwatch.createUnstarted()

    /**
     * Adds all of the runnables to the [.WORK_TO_COMPLETE] list
     *
     * @param work
     * The work we must complete later
     */
    fun addWork(vararg work: Runnable) {
        stopwatch.start()
        Collections.addAll(WORK_TO_COMPLETE, *work)
        prepareAll()
        executeWorkers()
    }

    /**
     * Prepares all essentials for work to be done. We first construct the [.countDownLatch], then create `BootWorker`s into the [.BOOT_WORKERS] list, then the [.prepareBootWorkers] method is ran
     */
    private fun prepareAll() {
        latch = CountDownLatch(WORK_TO_COMPLETE.size)
        for (i in 0 until THREAD_SIZE) {
            BOOT_WORKERS.add(BootWorker(i))
        }
        prepareBootWorkers()
    }

    /**
     * Prepares the workers by populating them with workload from the [.WORK_TO_COMPLETE]
     */
    private fun prepareBootWorkers() {
        var index = 0
        val iterator = WORK_TO_COMPLETE.iterator()
        while (iterator.hasNext()) {
            bestWorker!!.addToWorkLoad(iterator.next(), index)
            iterator.remove()
            index++
        }
    }

    /**
     * Executes the workers
     */
    private fun executeWorkers() {
        BOOT_WORKERS.forEach(Consumer { command: BootWorker? -> SystemManager.SLOW_EXECUTOR.execute(command) })
    }

    /**
     * Getting the best worker to use for the upcoming workload. This is dependent on the amount of work the worker
     * currently has to do
     */
    private val bestWorker: BootWorker?
        get() {
            var leastWorkDone = -1
            var bestWorker: BootWorker? = null
            for (worker in BOOT_WORKERS) {
                if (worker.workLoadSize < leastWorkDone || leastWorkDone == -1) {
                    leastWorkDone = worker.workLoadSize
                    bestWorker = worker
                }
            }
            return bestWorker
        }

    /**
     * Awaits the completion of the countdown
     */
    fun await() {
        try {
            latch!!.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        stopwatch.stop()
    }

    /**
     * Performs the finishing operations on the threads after we have completed
     */
    fun finish() {
        BOOT_WORKERS.forEach(Consumer { obj: BootWorker -> obj.interrupt() })
        BOOT_WORKERS.clear()
    }

    /**
     * Gets the worker numbers left in a list
     */
    private fun workerNumbersLeft(): List<Int> {
        val result: MutableList<Int> = ArrayList()
        BOOT_WORKERS.forEach(Consumer { worker: BootWorker ->
            worker.workLoad.forEach(
                Consumer { load: BootTask -> result.add(load.taskNumber) })
        })
        return result
    }

    /**
     * The details of the workers left
     */
    fun workersLeftDetails(): String {
        val details = StringBuilder()
        val numbersLeft = workerNumbersLeft()
        for (i in numbersLeft.indices) {
            details.append(numbersLeft[i]).append(if (i == numbersLeft.size - 1) "" else ", ")
        }
        return details.toString()
    }
}