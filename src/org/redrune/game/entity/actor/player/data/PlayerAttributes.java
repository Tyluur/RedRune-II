package org.redrune.game.entity.actor.player.data;

import org.redrune.utility.constants.AttributeKey;

import java.util.EnumMap;

/**
 * All attributes that the player uses are stored here, for example points in minigames
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-24
 */
public final class PlayerAttributes {
	
	/**
	 * The map containing all of the player's attributes
	 */
	private final EnumMap<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);
	
}
