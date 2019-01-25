package org.redrune.game.global.worldlist;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public class WorldEntry {
	
	/**
	 * The activity of the world entry
	 */
	@Getter
	private final String activity;
	
	/**
	 * The ip of the world entry
	 */
	@Getter
	private final String ip;
	
	/**
	 * The id of the country that the world is in
	 */
	@Getter
	private final int countryId;
	
	/**
	 * The flags for the world country, multiple ones sent to build different types of worlds [members/high risk]
	 */
	@Getter
	private final int flag;
	
	/**
	 * The name of the country
	 */
	@Getter
	private final String countryName;
	
	/**
	 * If the country is members only
	 */
	@Getter
	private final boolean members;
	
	WorldEntry(String activity, String ip, int countryId, int flag, String countryName, boolean members) {
		this.activity = activity;
		this.ip = ip;
		this.countryId = countryId;
		this.countryName = countryName;
		this.members = members;
		this.flag = flag;
	}
	
}