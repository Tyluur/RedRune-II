package org.redrune.game.entity.actor.player.data;

import org.redrune.utility.constants.AttributeKey;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class PlayerSaving implements Serializable {
	
	private static final long serialVersionUID = 8356652804472015316L;
	
	/**
	 * The map of saved attributes
	 */
	private final EnumMap<AttributeKey, Object> storedAttributes = new EnumMap<>(AttributeKey.class);
	
	/**
	 * Gets a  stored attribute
	 *
	 * @param key
	 * 		The key of the attribute
	 */
	
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(AttributeKey key) {
		return (K) storedAttributes.get(key);
	}
	
	/**
	 * Removes the value for the attribute
	 *
	 * @param key
	 * 		The key
	 * @return The value that was removed
	 */
	@SuppressWarnings("unchecked")
	public <K> K removeAttribute(AttributeKey key) {
		return (K) storedAttributes.remove(key);
	}
	
	/**
	 * Removes an attribute and returns the default value parameter if the key wasnt in the map.
	 *
	 * @param key
	 * 		The key
	 * @param defaultValue
	 * 		The value to return
	 * @return The attribute value, or the default value if nothing existed
	 */
	@SuppressWarnings("unchecked")
	public <K> K removeAttribute(AttributeKey key, K defaultValue) {
		K value = (K) storedAttributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Increments an attribute's value by 1 if they're a number
	 *
	 * @param key
	 * 		The key
	 * @return The new value
	 */
	public int incrementIntegerAttribute(AttributeKey key) {
		Object value = storedAttributes.get(key);
		int digit = -1;
		if (value instanceof Number) {
			digit = ((Number) value).intValue();
		}
		digit++;
		storedAttributes.put(key, digit);
		return digit;
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putAttributeIfEmpty(AttributeKey key, K value) {
		if (!storedAttributes.containsKey(key)) {
			storedAttributes.put(key, value);
		}
		return value;
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putAttribute(AttributeKey key, K value) {
		storedAttributes.put(key, value);
		return value;
	}
	
	/**
	 * Gets an attribute and returns the default value if it doesn't exist
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param defaultValue
	 * 		The value to return if the key doesnt exist in the map
	 */
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(AttributeKey key, K defaultValue) {
		K value = (K) storedAttributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
