package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.HeadIcons.SkullIcon;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The class used to store important player variables.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class PlayerVariables {
	
	public PlayerVariables() {
		this.storedAttributes = new ConcurrentHashMap<>();
		putAttributeIfEmpty(AttributeKey.FILTERING_PROFANITY, false);
		putAttributeIfEmpty(AttributeKey.CHAT_EFFECTS, true);
		putAttributeIfEmpty(AttributeKey.ACCEPTING_AID, false);
		putAttributeIfEmpty(AttributeKey.DUAL_MOUSE_BUTTONS, true);
	}
	
	/**
	 * The map of saved attributes
	 */
	private final ConcurrentHashMap<AttributeKey, Object> storedAttributes;
	
	/**
	 * The health points of the player
	 */
	@Getter
	@Setter
	private int healthPoints = 100;
	
	/**
	 * The player points
	 */
	@Getter
	@Setter
	private int prayerPoints = 10;
	
	/**
	 * The amount of run energy the player has
	 */
	@Getter
	@Setter
	private double runEnergy = 100;
	
	/**
	 * If the run button has been toggled on
	 */
	@Getter
	@Setter
	private boolean runToggled = false;
	
	/**
	 * If the experience is locked
	 */
	@Getter
	@Setter
	private boolean experienceLocked = false;
	
	/**
	 * If we are in an area we can fight in
	 */
	@Getter
	@Setter
	private boolean inFightArea = false;
	
	/**
	 * The skull icon
	 */
	@Getter
	private SkullIcon skullIcon = SkullIcon.NONE;
	
	/**
	 * Gets a stored attribute
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
	 * Sets a player's skull icon
	 *
	 * @param player
	 * 		The player
	 * @param skullIcon
	 * 		The icon
	 */
	public void setSkullIcon(Player player, SkullIcon skullIcon) {
		this.skullIcon = skullIcon;
		if (player != null) {
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}
	
	/**
	 * Reduces the amount of health points we have
	 *
	 * @param amount
	 * 		The amount to reduce by
	 */
	public boolean reduceHealth(int amount) {
		healthPoints = healthPoints - amount;
		if (healthPoints <= 0) {
			healthPoints = 0;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * If the player is accepting aid
	 */
	public Boolean isAcceptingAid() {
		return getAttribute(AttributeKey.ACCEPTING_AID, true);
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
	
	/**
	 * If the player is filtering profanity
	 */
	public Boolean isFilteringProfanity() {
		return getAttribute(AttributeKey.FILTERING_PROFANITY, false);
	}
}

