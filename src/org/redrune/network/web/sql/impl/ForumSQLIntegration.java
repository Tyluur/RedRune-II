package org.redrune.network.web.sql.impl;

import org.redrune.network.web.sql.SQLRepository;
import org.redrune.network.web.sql.database.DatabaseConnection;
import org.redrune.utility.backend.BCryptService;
import org.redrune.utility.tool.Misc;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class ForumSQLIntegration {
	
	/**
	 * Validates the users credentials and returns the user's member group id upon successful validation.
	 *
	 * @param username
	 * 		The username
	 * @param password
	 * 		The password
	 * @return <ul><li>-3 in the case of a database connection error.</li><li>-2 if the set did not match</li><li>-1 if
	 * the user did not exist.</li><li>Otherwise, any number greater than 0 is a valid member id.</li></ul>
	 */
	public static int validateCredentials(String username, String password) {
		Optional<DatabaseConnection> optional = Optional.empty();
		try {
			optional = Optional.ofNullable(SQLRepository.getConnectionPool().nextFree());
			if (!optional.isPresent()) {
				return -3;
			}
			Statement stmt = optional.get().createStatement();
			if (stmt == null) {
				return -3;
			}
			int responseCode;
			ResultSet rs = stmt.executeQuery("SELECT * FROM `core_members` WHERE " + "name='" + Misc.formatPlayerNameForDisplay(username) + "' LIMIT 1");
			if (rs.next()) {
				String salt = rs.getString("members_pass_salt");
				String encryptedHash = encryptPassword(password, salt);
				String storedHash = rs.getString("members_pass_hash");
				if (storedHash.equals(encryptedHash)) {
					responseCode = rs.getInt("member_id");
				} else {
					responseCode = -2;
				}
			} else {
				responseCode = -1;
			}
			stmt.close();
			return responseCode;
		} catch (Throwable t) {
			t.printStackTrace();
			return -3;
		} finally {
			optional.ifPresent(DatabaseConnection::returnConnection);
		}
	}
	
	/**
	 * Encrypts the password to IPB4 format
	 *
	 * @param password
	 * 		The password
	 * @param salt
	 * 		The salt
	 */
	private static String encryptPassword(String password, String salt) {
		return BCryptService.hashpw(password, "$2a$13$" + salt);
	}
}
