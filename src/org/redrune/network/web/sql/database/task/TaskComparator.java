package org.redrune.network.web.sql.database.task;

import org.redrune.network.web.sql.database.DatabaseTaskEngine.QueryPriority;

import java.util.Comparator;

public class TaskComparator implements Comparator<QueryTask> {
	
	@Override
	public int compare(QueryTask o1, QueryTask o2) {
		return o1.getPriority() == QueryPriority.IMPORTANT && o2.getPriority() != QueryPriority.IMPORTANT ? 1 : 0;
	}
}