package engine.tick.task

abstract class WorldTask : Runnable {

    @JvmField
    var ticksPassed = 0

    /**
     * If the task needs to be removed
     */
    var isNeedRemove = false

    override fun toString(): String {
        return "WorldTask[ticksPassed=" + ticksPassed + ", needRemove=" + isNeedRemove + "]"
    }

    /**
     * Stops the task
     */
    fun stop() {
        isNeedRemove = true
    }
}