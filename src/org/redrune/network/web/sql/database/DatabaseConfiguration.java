package org.redrune.network.web.sql.database;

public interface DatabaseConfiguration {

	/**
	 * Create a new database connection
	 * 
	 * @return The new connection
	 */
	public DatabaseConnection newConnection();

}