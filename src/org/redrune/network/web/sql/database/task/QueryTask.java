package org.redrune.network.web.sql.database.task;

import lombok.Getter;
import org.redrune.network.web.sql.database.DatabaseTaskEngine.QueryPriority;

public class QueryTask {
	
	@Getter
	private QueryPriority priority = QueryPriority.NORMAL;
	
	public QueryTask(QueryPriority priority) {
		this.priority = priority;
	}
	
	public void execute() {
	}
	
}