package org.redrune.game.entity.actor.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.entity.actor.player.skills.slayer.SlayerTask;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.AttributeKey;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class PlayerAttributes implements Serializable {
	
	private static final long serialVersionUID = 8356652804472015316L;
	
	/**
	 * The map of saved attributes
	 */
	private final ConcurrentHashMap<AttributeKey, Object> savedAttributes = new ConcurrentHashMap<>();
	
	/**
	 * The owner of the current friend chat we're in
	 */
	@Getter
	@Setter
	private String currentFriendChatOwner;
	
	/**
	 * The display name of the player
	 */
	@Getter
	private String displayName;
	
	/**
	 * The last ip address of the player
	 */
	@Getter
	@Setter
	private String lastIP;
	
	/**
	 * The last mac address of the player
	 */
	@Getter
	@Setter
	private String lastMac;
	
	/**
	 * The current slayer task
	 */
	@Getter
	@Setter
	private SlayerTask slayerTask;
	
	/**
	 * The run energy we have left
	 */
	@Getter
	private byte runEnergy;
	
	/**
	 * If the player is filtering profanity
	 */
	@Getter
	@Setter
	private boolean filteringProfanity;
	
	/**
	 * If we should allow chat effects in the game
	 */
	@Getter
	@Setter
	private boolean allowChatEffects;
	
	/**
	 * The amount of mouse buttons we should use, as a flag
	 */
	@Getter
	@Setter
	private boolean mouseButtons;
	
	/**
	 * The private chat setup option
	 */
	@Getter
	@Setter
	private int privateChatSetup;
	
	/**
	 * The time until our skull disappears
	 */
	@Getter
	@Setter
	private int skullDelay;
	
	/**
	 * The id of our skull
	 */
	@Getter
	@Setter
	private int skullId;
	
	/**
	 * If the next map should force load, used for encoding regions
	 */
	@Getter
	@Setter
	private boolean forceNextMapLoadRefresh;
	
	/**
	 * How long the player is immune to poison for
	 */
	@Getter
	@Setter
	private long poisonImmune;
	
	/**
	 * How long the player is immune to fire for
	 */
	@Getter
	@Setter
	private long fireImmune;
	
	/**
	 * The pouch data, used for runecrafting
	 */
	@Getter
	private int[] pouches;
	
	/**
	 * If we are filtering the game
	 */
	@Getter
	@Setter
	private boolean filterGame;
	
	/**
	 * The customization information of the max cape
	 */
	@Getter
	@Setter
	private int[] maxedCapeCustomized;
	
	/**
	 * The customization information of the completionist cape
	 */
	@Getter
	@Setter
	private int[] completionistCapeCustomized;
	
	/**
	 * The delay until we can use an overload potion again
	 */
	@Getter
	@Setter
	private int overloadDelay;
	
	/**
	 * The option we're using for the summoning orb
	 */
	@Getter
	@Setter
	private int summoningLeftClickOption;
	
	/**
	 * The list of owned object manager keys
	 */
	private List<String> ownedObjectsManagerKeys;
	
	/**
	 * If our experience is locked
	 */
	@Getter
	@Setter
	private boolean experienceLocked;
	
	/**
	 * The temporary movement type flag for teleporting or walking types
	 */
	@Getter
	@Setter
	private int temporaryMovementType;
	
	/**
	 * The update movement ftype flag
	 */
	@Getter
	@Setter
	private boolean updateMovementType;
	
	/**
	 * The player whose attributes this is an instance for
	 */
	@Setter
	private transient Player player;
	
	/**
	 * The cache of items to switch
	 */
	@Getter
	@Setter
	private transient List<Integer> switchItemCache;
	
	/**
	 * The maximum amount of traps
	 */
	@Getter
	@Setter
	private transient int trapAmount;
	
	/**
	 * The last time we received a ping
	 */
	@Getter
	@Setter
	private transient long packetsDecoderPing;
	
	/**
	 * If the player is resting
	 */
	@Getter
	private transient boolean resting;
	
	/**
	 * If the player can pvp in their area
	 */
	@Getter
	private transient boolean canPvp;
	
	/**
	 * The delay until the player can eat again
	 */
	@Getter
	@Setter
	private transient long foodDelay;
	
	/**
	 * The delay until the player can pot again
	 */
	@Getter
	@Setter
	private transient long potDelay;
	
	/**
	 * The delay until the player can bury a bone again
	 */
	@Getter
	@Setter
	private transient long boneDelay;
	
	/**
	 * The last time a player sent a public message
	 */
	@Getter
	@Setter
	private transient long lastPublicMessage;
	
	/**
	 * The delay until the power of light is reset
	 */
	@Getter
	@Setter
	private transient long polDelay;
	
	/**
	 * If equipping items is disabled
	 */
	@Getter
	@Setter
	private transient boolean equipDisabled;
	
	/**
	 * If the client has loaded the map region
	 */
	private transient boolean clientLoadedMapRegion;
	
	public PlayerAttributes() {
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		pouches = new int[4];
		slayerTask = new SlayerTask();
		ownedObjectsManagerKeys = new LinkedList<>();
	}
	
	/**
	 * Gets a  stored attribute
	 *
	 * @param key
	 * 		The key of the attribute
	 */
	
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(AttributeKey key) {
		return (K) savedAttributes.get(key);
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
		return (K) savedAttributes.remove(key);
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
		K value = (K) savedAttributes.remove(key);
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
		Object value = savedAttributes.get(key);
		int digit = -1;
		if (value instanceof Number) {
			digit = ((Number) value).intValue();
		}
		digit++;
		savedAttributes.put(key, digit);
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
		if (!savedAttributes.containsKey(key)) {
			savedAttributes.put(key, value);
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
		savedAttributes.put(key, value);
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
		K value = (K) savedAttributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Adds delay for pots
	 *
	 * @param time
	 * 		The delay to add
	 */
	public void addPotDelay(long time) {
		setPotDelay(time + Misc.currentTimeMillis());
	}
	
	/**
	 * Adds delay for food
	 *
	 * @param time
	 * 		The delay to add
	 */
	public void addFoodDelay(long time) {
		setFoodDelay(time + Misc.currentTimeMillis());
	}
	
	/**
	 * Adds delay for bones
	 *
	 * @param time
	 * 		The delay to add
	 */
	public void addBoneDelay(long time) {
		setBoneDelay(time + Misc.currentTimeMillis());
	}
	
	/**
	 * Adds delay for fire immunity
	 *
	 * @param time
	 * 		The delay to add
	 */
	public void addFireImmune(long time) {
		fireImmune = time + Misc.currentTimeMillis();
	}
	
	/**
	 * Adds delay for the power of light
	 *
	 * @param delay
	 * 		The delay
	 */
	public void addPolDelay(long delay) {
		polDelay = delay + Misc.currentTimeMillis();
	}
	
	/**
	 * Adds delay for poison immunity
	 *
	 * @param time
	 * 		The delay to add
	 */
	public void addPoisonImmune(long time) {
		setPoisonImmune(time + Misc.currentTimeMillis());
		player.getPoisonManager().reset();
	}
	
	/**
	 * Gets the owned object manager keys
	 */
	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) {
			ownedObjectsManagerKeys = new LinkedList<>();
		}
		return ownedObjectsManagerKeys;
	}
	
	/**
	 * Sets the resting flag and refreshes the client configurations
	 *
	 * @param resting
	 * 		The resting flag
	 */
	public void setResting(boolean resting) {
		this.resting = resting;
		player.getPackets().sendRunButtonConfig();
	}
	
	/**
	 * Toggles the run flag
	 */
	public void toggleRun(boolean update) {
		player.setRunModeOn(!player.isRunModeOn());
		setUpdateMovementType(true);
		if (update) {
			player.getPackets().sendRunButtonConfig();
		}
	}
	
	/**
	 * Restores the run energy
	 */
	public void restoreRunEnergy() {
		if (player.getNextRunDirection() == -1 && getRunEnergy() < 100) {
			setRunEnergy(getRunEnergy() + 1);
			if (isResting() && getRunEnergy() < 100) {
				setRunEnergy(getRunEnergy() + 1);
			}
			player.getPackets().sendRunEnergy();
		}
	}
	
	/**
	 * Sets the run energy amount
	 */
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		player.getPackets().sendRunEnergy();
	}
	
	/**
	 * Drains the run energy by 1 point
	 */
	public void drainRunEnergy() {
		setRunEnergy(getRunEnergy() - 1);
	}
	
	/**
	 * Sets the wilderness skull
	 */
	public void setWildernessSkull() {
		setSkullDelay(3_000);
		setSkullId(0);
		player.getAppearance().generateAppearanceData();
	}
	
	/**
	 * If the player has a skull
	 */
	public boolean hasSkull() {
		return skullDelay > 0;
	}
	
	/**
	 * If the client has laoded the map region
	 */
	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}
	
	/**
	 * Sets the {@link #clientLoadedMapRegion} flag to true
	 */
	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}
	
	/**
	 * Sets the {@link #clientLoadedMapRegion} flag to false
	 */
	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}
	
	/**
	 * Sets the display name with modifier checks
	 *
	 * @param displayName
	 * 		The display name to set
	 */
	public void setDisplayName(String displayName) {
		if (Misc.formatPlayerNameForDisplay(player.getUsername()).equals(displayName)) {
			setDisplayNameString(null);
		} else {
			setDisplayNameString(displayName);
		}
	}
	
	/**
	 * This method sets the string form of the display name with no modifier checks
	 *
	 * @param displayName
	 * 		The display name
	 */
	public void setDisplayNameString(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * If the player has a display name
	 */
	public boolean hasDisplayName() {
		return displayName != null;
	}
	
	/**
	 * Sets the attack options on or off, as well as a flag for each player's ability to be attacked in the current
	 * zone
	 *
	 * @param canPvp
	 * 		The flag
	 */
	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		player.getAppearance().generateAppearanceData();
		player.getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		player.getPackets().sendPlayerUnderNPCPriority(canPvp);
	}
	
	public long getTeleBlockDelay() {
		return player.getTemporaryAttribute("TeleBlocked", -1L);
	}
	
	public void setTeleBlockDelay(long teleDelay) {
		player.putAttribute("TeleBlocked", teleDelay + Misc.currentTimeMillis());
	}
	
	public long getPrayerDelay() {
		return player.getTemporaryAttribute("PrayerBlocked", 0L);
	}
	
	public void setPrayerDelay(long teleDelay) {
		player.putAttribute("PrayerBlocked", teleDelay + Misc.currentTimeMillis());
		player.getPrayer().closeAllPrayers();
	}
	
}
