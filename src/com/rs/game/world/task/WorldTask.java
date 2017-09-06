package com.rs.game.world.task;

import lombok.Getter;
import lombok.Setter;

public abstract class WorldTask implements Runnable {
	
	@Getter
	@Setter
	protected int ticksPassed;
	
	/**
	 * If the task needs to be removed
	 */
	@Getter
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
}
