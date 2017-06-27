package org.redrune.utility;

/**
 * The map of all attribute keys. Keys here can be used in either the temporary attribute map or the saved attribute
 * map.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public enum AttributeKey {
	
	
	// MISC ATTRIBUTES
	COST_VALUE,
	
	LAST_HIT_BY_ENTITY,
	
	LAST_TIME_HIT,
	
	LAST_DIALOGUE_MESSAGE,
	
	INTERACTING_PLAYER,
	
	INTERACTING_NPC,
	
	SKILL_MENU,
	
	// UPDATING ATTRIBUTES
	MAP_REGION_CHANGED,
	
	TELEPORT_LOCATION,
	
	PLAYER_TELEPORTED,
	
	// saved vars
	
	FILTERING_PROFANITY,
	
	MOUSE_BUTTONS,
	
	CHAT_EFFECTS,
	
	ACCEPTING_AID,
	
	LAST_LONGIN_STAMP,
	
	// game bar status
	
	FILTER,
	PUBLIC,
	PRIVATE,
	FRIENDS,
	CLAN,
	TRADE,
	ASSIST,;
}
