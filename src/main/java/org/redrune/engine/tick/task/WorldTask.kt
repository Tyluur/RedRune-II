package org.redrune.engine.tick.task;

public abstract class WorldTask implements Runnable {
	
	protected int ticksPassed;
	
	/**
	 * If the task needs to be removed
	 */
    boolean needRemove;
	
	@Override
	public String toString() {
		return "WorldTask[ticksPassed=" + ticksPassed + ", needRemove=" + needRemove + "]";
	}
	
	/**
	 * Stops the task
	 */
	public void stop() {
		needRemove = true;
	}

    public int getTicksPassed() {
        return this.ticksPassed;
    }

    public boolean isNeedRemove() {
        return this.needRemove;
    }

    public void setTicksPassed(int ticksPassed) {
        this.ticksPassed = ticksPassed;
    }
}
