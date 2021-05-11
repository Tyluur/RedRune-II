package game.entity.actor.player.data;

import game.content.entity.actor.player.market.exchange.ExchangeOffer;
import game.content.entity.actor.player.skills.slayer.SlayerTask;
import game.entity.actor.player.Player;
import utility.constants.key.AttributeKey;
import utility.functions.Misc;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@icloud.com>
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
    private String currentFriendChatOwner = "Tyluur";

    /**
     * The display name of the player
     */
    private String displayName;

    /**
     * The last ip address of the player
     */
    private String lastIP;

    /**
     * The last mac address of the player
     */
    private String lastMac;

    /**
     * The current slayer task
     */
    private SlayerTask slayerTask;

    /**
     * The run energy we have left
     */
    private byte runEnergy;

    /**
     * If the player is filtering profanity
     */
    private boolean filteringProfanity;

    /**
     * If we should allow chat effects in the game
     */
    private boolean allowChatEffects;

    /**
     * The amount of mouse buttons we should use, as a flag
     */
    private boolean mouseButtons;

    /**
     * The private chat setup option
     */
    private int privateChatSetup;

    /**
     * The time until our skull disappears
     */
    private int skullDelay;

    /**
     * The id of our skull
     */
    private int skullId;

    /**
     * If the next map should force load, used for encoding regions
     */
    private boolean forceNextMapLoadRefresh;

    /**
     * How long the player is immune to poison for
     */
    private long poisonImmune;

    /**
     * How long the player is immune to fire for
     */
    private long fireImmune;

    /**
     * The pouch data, used for runecrafting
     */
    private final int[] pouches;

    /**
     * If we are filtering the game
     */
    private boolean filterGame;

    /**
     * The customization information of the max cape
     */
    private int[] maxedCapeCustomized;

    /**
     * The customization information of the completionist cape
     */
    private int[] completionistCapeCustomized;

    /**
     * The delay until we can use an overload potion again
     */
    private int overloadDelay;

    /**
     * The option we're using for the summoning orb
     */
    private int summoningLeftClickOption;

    /**
     * The list of owned object manager keys
     */
    private List<String> ownedObjectsManagerKeys;

    /**
     * If our experience is locked
     */
    private boolean experienceLocked;

    /**
     * The temporary movement type flag for teleporting or walking types
     */
    private int temporaryMovementType;

    /**
     * The update movement ftype flag
     */
    private boolean updateMovementType;

    /**
     * The amount of earning potential we have
     */
    private double earningPotential = 25;

    /**
     * If the player has received a tutorial
     */
    private boolean receivedTutorial = false;

    /**
     * The exchange offers
     */
    private final ExchangeOffer[] offers = new ExchangeOffer[6];

    /**
     * The player whose attributes this is an instance for
     */
    private transient Player player;

    /**
     * The cache of items to switch
     */
    private transient List<Integer[]> switchItemCache;

    /**
     * The maximum amount of traps
     */
    private transient int trapAmount;

    /**
     * The last time we received a ping
     */
    private transient long packetsDecoderPing;

    /**
     * If the player is resting
     */
    private transient boolean resting;

    /**
     * If the player can pvp in their area
     */
    private transient boolean canPvp;

    /**
     * The delay until the player can eat again
     */
    private transient long foodDelay;

    /**
     * The delay until the player can pot again
     */
    private transient long potDelay;

    /**
     * The delay until the player can bury a bone again
     */
    private transient long boneDelay;

    /**
     * The last time a player sent a public message
     */
    private transient long lastPublicMessage;

    /**
     * The delay until the power of light is reset
     */
    private transient long polDelay;

    /**
     * If equipping items is disabled
     */
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
     * @param key The key of the attribute
     */

    @SuppressWarnings("unchecked")
    public <K> K getAttribute(AttributeKey key) {
        return (K) savedAttributes.get(key);
    }

    /**
     * Removes the value for the attribute
     *
     * @param key The key
     * @return The value that was removed
     */
    @SuppressWarnings("unchecked")
    public <K> K removeAttribute(AttributeKey key) {
        return (K) savedAttributes.remove(key);
    }

    /**
     * Removes an attribute and returns the default value parameter if the key wasnt in the map.
     *
     * @param key          The key
     * @param defaultValue The value to return
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
     * @param key The key
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
     * @param key   The key
     * @param value The value
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
     * @param key   The key
     * @param value The value
     */
    public <K> K putAttribute(AttributeKey key, K value) {
        savedAttributes.put(key, value);
        return value;
    }

    /**
     * Gets an attribute and returns the default value if it doesn't exist
     *
     * @param key          The key of the attribute
     * @param defaultValue The value to return if the key doesnt exist in the map
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
     * @param time The delay to add
     */
    public void addPotDelay(long time) {
        setPotDelay(time + Misc.currentTimeMillis());
    }

    /**
     * Adds delay for food
     *
     * @param time The delay to add
     */
    public void addFoodDelay(long time) {
        setFoodDelay(time + Misc.currentTimeMillis());
    }

    /**
     * Adds delay for bones
     *
     * @param time The delay to add
     */
    public void addBoneDelay(long time) {
        setBoneDelay(time + Misc.currentTimeMillis());
    }

    /**
     * Adds delay for fire immunity
     *
     * @param time The delay to add
     */
    public void addFireImmune(long time) {
        fireImmune = time + Misc.currentTimeMillis();
    }

    /**
     * Adds delay for the power of light
     *
     * @param delay The delay
     */
    public void addPolDelay(long delay) {
        polDelay = delay + Misc.currentTimeMillis();
    }

    /**
     * Adds delay for poison immunity
     *
     * @param time The delay to add
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
     * @param resting The resting flag
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
     * @param displayName The display name to set
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
     * @param displayName The display name
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
     * @param canPvp The flag
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
        player.putTemporaryAttribute("TeleBlocked", teleDelay + Misc.currentTimeMillis());
    }

    public long getPrayerDelay() {
        return player.getTemporaryAttribute("PrayerBlocked", 0L);
    }

    public void setPrayerDelay(long teleDelay) {
        player.putTemporaryAttribute("PrayerBlocked", teleDelay + Misc.currentTimeMillis());
        player.getPrayer().closeAllPrayers();
    }

    public String getCurrentFriendChatOwner() {
        return this.currentFriendChatOwner;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getLastIP() {
        return this.lastIP;
    }

    public String getLastMac() {
        return this.lastMac;
    }

    public SlayerTask getSlayerTask() {
        return this.slayerTask;
    }

    public byte getRunEnergy() {
        return this.runEnergy;
    }

    public boolean isFilteringProfanity() {
        return this.filteringProfanity;
    }

    public boolean isAllowChatEffects() {
        return this.allowChatEffects;
    }

    public boolean isMouseButtons() {
        return this.mouseButtons;
    }

    public int getPrivateChatSetup() {
        return this.privateChatSetup;
    }

    public int getSkullDelay() {
        return this.skullDelay;
    }

    public int getSkullId() {
        return this.skullId;
    }

    public boolean isForceNextMapLoadRefresh() {
        return this.forceNextMapLoadRefresh;
    }

    public long getPoisonImmune() {
        return this.poisonImmune;
    }

    public long getFireImmune() {
        return this.fireImmune;
    }

    public int[] getPouches() {
        return this.pouches;
    }

    public boolean isFilterGame() {
        return this.filterGame;
    }

    public int[] getMaxedCapeCustomized() {
        return this.maxedCapeCustomized;
    }

    public int[] getCompletionistCapeCustomized() {
        return this.completionistCapeCustomized;
    }

    public int getOverloadDelay() {
        return this.overloadDelay;
    }

    public int getSummoningLeftClickOption() {
        return this.summoningLeftClickOption;
    }

    public boolean isExperienceLocked() {
        return this.experienceLocked;
    }

    public int getTemporaryMovementType() {
        return this.temporaryMovementType;
    }

    public boolean isUpdateMovementType() {
        return this.updateMovementType;
    }

    public List<Integer[]> getSwitchItemCache() {
        return this.switchItemCache;
    }

    public int getTrapAmount() {
        return this.trapAmount;
    }

    public long getPacketsDecoderPing() {
        return this.packetsDecoderPing;
    }

    public boolean isResting() {
        return this.resting;
    }

    public boolean isCanPvp() {
        return this.canPvp;
    }

    public long getFoodDelay() {
        return this.foodDelay;
    }

    public long getPotDelay() {
        return this.potDelay;
    }

    public long getBoneDelay() {
        return this.boneDelay;
    }

    public long getLastPublicMessage() {
        return this.lastPublicMessage;
    }

    public long getPolDelay() {
        return this.polDelay;
    }

    public boolean isEquipDisabled() {
        return this.equipDisabled;
    }

    public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
        this.currentFriendChatOwner = currentFriendChatOwner;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public void setLastMac(String lastMac) {
        this.lastMac = lastMac;
    }

    public void setSlayerTask(SlayerTask slayerTask) {
        this.slayerTask = slayerTask;
    }

    public void setFilteringProfanity(boolean filteringProfanity) {
        this.filteringProfanity = filteringProfanity;
    }

    public void setAllowChatEffects(boolean allowChatEffects) {
        this.allowChatEffects = allowChatEffects;
    }

    public void setMouseButtons(boolean mouseButtons) {
        this.mouseButtons = mouseButtons;
    }

    public void setPrivateChatSetup(int privateChatSetup) {
        this.privateChatSetup = privateChatSetup;
    }

    public void setSkullDelay(int skullDelay) {
        this.skullDelay = skullDelay;
    }

    public void setSkullId(int skullId) {
        this.skullId = skullId;
    }

    public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
        this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
    }

    public void setPoisonImmune(long poisonImmune) {
        this.poisonImmune = poisonImmune;
    }

    public void setFireImmune(long fireImmune) {
        this.fireImmune = fireImmune;
    }

    public void setFilterGame(boolean filterGame) {
        this.filterGame = filterGame;
    }

    public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
        this.maxedCapeCustomized = maxedCapeCustomized;
    }

    public void setCompletionistCapeCustomized(int[] completionistCapeCustomized) {
        this.completionistCapeCustomized = completionistCapeCustomized;
    }

    public void setOverloadDelay(int overloadDelay) {
        this.overloadDelay = overloadDelay;
    }

    public void setSummoningLeftClickOption(int summoningLeftClickOption) {
        this.summoningLeftClickOption = summoningLeftClickOption;
    }

    public void setExperienceLocked(boolean experienceLocked) {
        this.experienceLocked = experienceLocked;
    }

    public void setTemporaryMovementType(int temporaryMovementType) {
        this.temporaryMovementType = temporaryMovementType;
    }

    public void setUpdateMovementType(boolean updateMovementType) {
        this.updateMovementType = updateMovementType;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSwitchItemCache(List<Integer[]> switchItemCache) {
        this.switchItemCache = switchItemCache;
    }

    public void setTrapAmount(int trapAmount) {
        this.trapAmount = trapAmount;
    }

    public void setPacketsDecoderPing(long packetsDecoderPing) {
        this.packetsDecoderPing = packetsDecoderPing;
    }

    public void setFoodDelay(long foodDelay) {
        this.foodDelay = foodDelay;
    }

    public void setPotDelay(long potDelay) {
        this.potDelay = potDelay;
    }

    public void setBoneDelay(long boneDelay) {
        this.boneDelay = boneDelay;
    }

    public void setLastPublicMessage(long lastPublicMessage) {
        this.lastPublicMessage = lastPublicMessage;
    }

    public void setPolDelay(long polDelay) {
        this.polDelay = polDelay;
    }

    public void setEquipDisabled(boolean equipDisabled) {
        this.equipDisabled = equipDisabled;
    }

    public double getEarningPotential() {
        return earningPotential;
    }

    public void setEarningPotential(double earningPotential) {
        this.earningPotential = earningPotential;
    }

    /**
     * Decreases the amount of earning potential
     *
     * @param amount The amount to decrease by
     */
    public void decreaseEarningPotential(double amount) {
        increaseEarningPotential(-amount);
    }

    /**
     * Increases the amount of earning potential
     *
     * @param amount The amount to increase by
     */
    public void increaseEarningPotential(double amount) {
        earningPotential += amount;
        if (earningPotential > 100) {
            earningPotential = 100;
        } else if (earningPotential < 0) {
            earningPotential = 0;
        }
    }

    /**
     * Gets the formatted amount of earning potential
     */
    public String getFormattedEarningPotential() {
        String colour;
        if (earningPotential < 25) {
            colour = "990000";
        } else if (earningPotential >= 25 && earningPotential < 50) {
            colour = "FF6633";
        } else if (earningPotential >= 50 && earningPotential < 75) {
            colour = "FFCC33";
        } else {
            colour = "33FF33";
        }
        return "EP: <col=" + colour + ">" + (int) earningPotential + "%</col>";
    }

    public boolean isReceivedTutorial() {
        return receivedTutorial;
    }

    public void setReceivedTutorial(boolean receivedTutorial) {
        this.receivedTutorial = receivedTutorial;
    }

    public ExchangeOffer[] getOffers() {
        return offers;
    }
}
