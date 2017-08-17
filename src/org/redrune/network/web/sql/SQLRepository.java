package org.redrune.network.web.sql;

import lombok.Getter;
import org.redrune.core.system.SystemManager;
import org.redrune.game.GameConstants;
import org.redrune.network.web.sql.database.ConnectionPool;
import org.redrune.network.web.sql.database.DatabaseConnection;
import org.redrune.network.web.sql.database.ThreadedSQL;
import org.redrune.network.web.sql.database.mysql.MySQLDatabaseConfiguration;
import org.redrune.utility.backend.configuration.ConfigurationNode;
import org.redrune.utility.backend.configuration.ConfigurationParser;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class SQLRepository {
	
	/**
	 * The MySQL Connection pool
	 */
	@Getter
	private static ConnectionPool<? extends DatabaseConnection> connectionPool;
	
	/**
	 * Loads server configuration.
	 */
	public static void storeConfiguration() {
		try (FileInputStream fis = new FileInputStream(GameConstants.SQL_CONFIGURATION_FILE)) {
			ConfigurationParser parser = new ConfigurationParser(fis);
			ConfigurationNode mainNode = parser.parse();
			if (!mainNode.has("database")) {
				System.out.println("Unable to identify database key");
				return;
			}
			ConfigurationNode databaseNode = mainNode.nodeFor("database");
			MySQLDatabaseConfiguration config = new MySQLDatabaseConfiguration();
			config.setHost(databaseNode.getString("host"));
			config.setPort(databaseNode.getInteger("port"));
			config.setDatabase(databaseNode.getString("database"));
			config.setUsername(databaseNode.getString("username"));
			config.setPassword(databaseNode.getString("password"));
			connectionPool = new ThreadedSQL(config, SystemManager.PROCESSOR_COUNT).getConnectionPool();
			System.out.println("Stored sql database configuration from " + GameConstants.SQL_CONFIGURATION_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
