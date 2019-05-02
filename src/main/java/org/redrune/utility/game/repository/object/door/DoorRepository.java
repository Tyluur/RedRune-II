package org.redrune.utility.game.repository.object.door;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.redrune.utility.file.JsonFileManager;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-20
 */
public class DoorRepository {
	
	/**
	 * The doors mapping.
	 */
	private static final Map<Integer, Door> DOORS = new HashMap<>();
	
	/**
	 * The location of the door configuration file
	 */
	private static final String CONFIGURATION_FILE = "./data/repository/object/doors.json";
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main(String[] args) {
		List<Door> doors = getDoorsFromFile();
		if (doors == null) {
			throw new IllegalStateException("Unable to parse doors from file {" + CONFIGURATION_FILE + "}, recheck running directory!");
		}
		saveDoors(doors);
	}
	
	/**
	 * Initializes all of the doors from the file
	 */
	public static void initialize() {
		List<Door> doors = getDoorsFromFile();
		if (doors == null) {
			throw new IllegalStateException("Unable to parse doors from file {" + CONFIGURATION_FILE + "}, recheck running directory!");
		}
		for (Door door : doors) {
			DOORS.put(door.getId(), door);
			Door replaced = new Door(door.getReplaceId());
			replaced.setReplaceId(door.getId());
			DOORS.put(replaced.getId(), replaced);
		}
		System.out.println("Loaded " + DOORS.size() + " doors");
	}
	
	/**
	 * Gets the ids of all the doors that have been registered
	 */
	public static Set<Integer> getDoorIds() {
		return DOORS.keySet();
	}
	
	/**
	 * Finds a door by the id
	 *
	 * @param objectId
	 * 		The id of the door to find
	 */
	public static Door forId(int objectId) {
		return DOORS.get(objectId);
	}
	
	private static void dumpDoorsFromArios() {
		HikariConfig config = new HikariConfig();
		HikariDataSource ds;
		
		{
			config.setJdbcUrl("jdbc:mysql://localhost/arios");
			config.setUsername("debug");
			config.setPassword("debug");
			config.setDriverClassName("com.mysql.cj.jdbc.Driver"); //alternative is Class.forName("com.mysql.cj.jdbc.Driver")
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			ds = new HikariDataSource(config);
		}
		String query = "SELECT * from `door_configs`";
		List<Door> doors = new ArrayList<>();
		try (Connection con = ds.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				int doorId = rs.getInt("id");
				int replaceId = rs.getInt("replaceId");
				int fence = rs.getInt("fence");
				
				Door door = new Door(doorId);
				door.setReplaceId(replaceId);
				doors.add(door);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		saveDoors(doors);
	}
	
	/**
	 * Loads all the data for doors from the file
	 */
	private static List<Door> getDoorsFromFile() {
		File file = new File(CONFIGURATION_FILE);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(CONFIGURATION_FILE);
		return GSON.fromJson(text, new TypeToken<List<Door>>() {
		}.getType());
	}
	
	/**
	 * Saves the list of doors to file
	 *
	 * @param doors
	 * 		The list of doors to save
	 */
	private static void saveDoors(List<Door> doors) {
		JsonFileManager.save(doors, CONFIGURATION_FILE);
	}
	
}
