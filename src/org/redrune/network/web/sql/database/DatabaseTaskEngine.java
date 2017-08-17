package org.redrune.network.web.sql.database;

import org.redrune.network.web.sql.database.task.QueryTask;
import org.redrune.network.web.sql.database.task.TaskComparator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class DatabaseTaskEngine {
	
	private boolean running = false;
	
	private BlockingQueue<QueryTask> tasks = new PriorityBlockingQueue<>(10, new TaskComparator());
	
	private ExecutorService importantService = Executors.newSingleThreadExecutor();
	
	private ExecutorService normalService = Executors.newFixedThreadPool(2);
	
	public void run() {
		while (running) {
			try {
				final QueryTask task = tasks.take();
				switch (task.getPriority()) {
					case NORMAL:
						normalService.execute(task::execute);
					case IMPORTANT:
						importantService.execute(task::execute);
						break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public enum QueryPriority {
		NORMAL,
		IMPORTANT
	}
}