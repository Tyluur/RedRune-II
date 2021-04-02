package org.redrune.engine.worker.boot

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.redrune.engine.worker.boot.BootHandler.countDownLatch
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Tyluur<itstyluur></itstyluur>@icloud.com>
 * @since 10/24/2015
 */
class BootWorker internal constructor(number: Int) : Thread() {

    /**
     * The load of work we must complete
     */
    internal val workLoad = CopyOnWriteArrayList<BootTask>()

    /**
     * The number of the book worker
     */
    private val number: Int

    override fun run() {
        for (work in ArrayList(workLoad)) {
            GlobalScope.launch {
                try {
                    //					long start = System.currentTimeMillis();
                    work.task.run()
                    workLoad.remove(work)
                    countDownLatch!!.countDown()
                    //					long delay = System.currentTimeMillis() - start;
                    //					System.out.println("Worker #" + number + ":\t\tFinished job " + work.getTaskNumber() + " in " + delay + " ms\t\t\tqueue=[" + BootHandler.workersLeftDetails() + "]");
                } catch (e: Exception) {
                    e.printStackTrace()
                    System.exit(1)
                }
            }
        }
    }

    /**
     * Adds more work to the [.workLoad]
     *
     * @param work
     * The work to add
     * @param index
     * The index of the work
     */
    fun addToWorkLoad(work: Runnable?, index: Int) {
        workLoad.add(BootTask(work, index))
    }

    /**
     * Gets the size of the workload
     */
    val workLoadSize: Int
        get() = workLoad.size

    init {
        name = "Boot Worker #" + (number + 1)
        this.number = number
    }
}