package org.redrune.game.node.entity.player.link;

import lombok.Setter;
import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;
import org.redrune.network.web.sql.SQLRepository;
import org.redrune.network.web.sql.database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class WebManager {
	
	/**
	 * The player who this manager is for
	 */
	@Setter
	private Player player;
	
	/**
	 * The map of table data
	 */
	private Map<String, Object> tableData = new HashMap<>();
	
	/**
	 * Handles the login aspect of the web management
	 */
	public void handleLogin() {
		if (!GameFlags.webIntegrated) {
			return;
		}
		Optional<DatabaseConnection> optional = Optional.empty();
		try {
			optional = Optional.ofNullable(SQLRepository.getConnectionPool().nextFree());
			if (!optional.isPresent()) {
				return;
			}
			Statement stmt = optional.get().createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM `core_members` WHERE member_id='" + player.getVariables().getRowId() + "'");
			ResultSetMetaData metaData = resultSet.getMetaData();
			int count = metaData.getColumnCount();
			if (resultSet.next()) {
				for (int i = 1; i <= count; i++) {
					String columnName = metaData.getColumnName(i);
					Object data = resultSet.getObject(i);
					storeData(columnName, data);
				}
			}
			stmt.close();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			optional.ifPresent(DatabaseConnection::returnConnection);
		}
		player.getDetails().storeRights(getPlayerRights());
	}
	
	/**
	 * Stores data into our table data map
	 *
	 * @param columnName
	 * 		The name of the column the data came from
	 * @param data
	 * 		The data to store
	 */
	private void storeData(String columnName, Object data) {
		//		System.out.println("columnName = [" + columnName + "], data = [" + data + "], type = [" + (data == null ? "null" : data.getClass().getSimpleName()) + "]");
		tableData.put(columnName, data);
	}
	
	/**
	 * Gets the player's rights
	 */
	public Set<PlayerRight> getPlayerRights() {
		Set<PlayerRight> rights = new LinkedHashSet<>();
		
		final int mainGroupId = getRowData("member_group_id", 0);
		final String mGroupOthers = getRowData("mgroup_others", "");
		
		Optional<PlayerRight> optional = PlayerRight.getRightByGroupId(mainGroupId);
		if (optional.isPresent()) {
			rights.add(optional.get());
		} else {
			System.out.println("Unable to find right by id " + mainGroupId);
		}
		
		if (mGroupOthers != null && mGroupOthers.length() > 0) {
			final String[] split = mGroupOthers.split(",");
			for (String separate : split) {
				try {
					int id = Integer.parseInt(separate);
					optional = PlayerRight.getRightByGroupId(id);
					if (optional.isPresent()) {
						rights.add(optional.get());
					} else {
						System.out.println("Unable to find right by id " + id);
					}
				} catch (Exception e) {
					System.err.println("Unable to parse right: " + separate);
					e.printStackTrace();
				}
			}
		}
		return rights;
	}
	
	/**
	 * Gets the value of a table in our {@link #tableData} mapping. All data is cached on login so operations will not
	 * connect directly to the sql database.
	 *
	 * @param columnName
	 * 		The column to use for data
	 * @param defaultValue
	 * 		The default value if we can't find it
	 */
	@SuppressWarnings("unchecked")
	public <T> T getRowData(String columnName, T defaultValue) {
		return (T) tableData.getOrDefault(columnName, defaultValue);
	}
	
	/**
	 * Checks if the player's email is verified
	 */
	public boolean isEmailVerified() {
		String defaultEmail = player.getDetails().getUsername().toLowerCase() + "@redrune.org";
		String currentEmail = getRowData("email", "");
		return !currentEmail.equals(defaultEmail);
	}
	
	/**
	 * Gets the amount of unread private messages the player has
	 */
	public int getUnreadMessages() {
		return getRowData("msg_count_new", 0);
	}
	
	/**
	 * Updates the player's forum table with data
	 *
	 * @param column
	 * 		The name of the column to update
	 * @param value
	 * 		The value to entry
	 */
	public void updateForumTable(String column, Object value) throws SQLException {
		Optional<DatabaseConnection> optional = Optional.empty();
		try {
			optional = Optional.ofNullable(SQLRepository.getConnectionPool().nextFree());
			if (!optional.isPresent()) {
				return;
			}
			Statement stmt = optional.get().createStatement();
			stmt.executeUpdate("UPDATE `core_members` SET `" + column + "` = '" + value + "' WHERE " + "member_id='" + player.getVariables().getRowId() + "';");
			stmt.close();
			// update the data now, this will make it so we don't need to relog if we update anything
			storeData(column, value);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			optional.ifPresent(DatabaseConnection::returnConnection);
		}
	}
	
}
