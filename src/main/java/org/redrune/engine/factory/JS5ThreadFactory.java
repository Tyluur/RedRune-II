package org.redrune.engine.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@icloud.com>
 */
public class JS5ThreadFactory implements ThreadFactory {
	
	private final String name;
	
	private final int priority;
	
	private final AtomicInteger threadCount = new AtomicInteger();
	
	public JS5ThreadFactory(String name) {
		this(name, Thread.NORM_PRIORITY);
	}
	
	public JS5ThreadFactory(String name, int priority) {
		this.name = name;
		this.priority = priority;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, new StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString());
		thread.setPriority(priority);
		return thread;
	}
	
}
