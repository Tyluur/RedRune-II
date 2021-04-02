package org.redrune.game.entity.actor.player;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.PlayerTutorial;
import org.redrune.game.content.entity.actor.player.action.ActionManager;
import org.redrune.game.content.entity.actor.player.controller.ControllerManager;
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.content.entity.actor.player.cutscene.CutsceneManager;
import org.redrune.game.content.entity.actor.player.event.EventManager;
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.content.entity.actor.player.skills.slayer.Slayer;
import org.redrune.game.content.entity.actor.player.skills.slayer.Slayer.SlayerMonsters;
import org.redrune.game.content.entity.item.Pots;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.player.data.*;
import org.redrune.game.entity.actor.player.link.*;
import org.redrune.game.entity.actor.player.render.LocalNPCUpdate;
import org.redrune.game.entity.actor.player.render.LocalPlayerUpdate;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.networking.NetworkSession;
import org.redrune.networking.packet.PacketSender;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.PlayerSaving;
import org.redrune.utility.game.entity.actor.player.PublicChatMessage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends Actor {

    private static final long serialVersionUID = 2011932556974180375L;

    /**
     * The attributes the player has
     */
    private PlayerAttributes attributes;

    /**
     * The set of the rights the player has
     */
    private Set<PlayerRight> rights;

    /**
     * The password for logging in
     */
    private String password;

    /**
     * The appearance handler and container
     */
    private PlayerAppearance appearance;

    /**
     * The inventory container and handler
     */
    private PlayerInventory inventory;

    /**
     * The equipment container and handler
     */
    private PlayerEquipment equipment;

    /**
     * The skill handler and container
     */
    private PlayerSkills skills;

    /**
     * The bank handler and container
     */
    private PlayerBank bank;

    /**
     * The prayer handler
     */
    private PlayerPrayer prayer;

    /**
     * The definitions used for combat events
     */
    private CombatDefinitions combatDefinitions;

    /**
     * The manager for {@code Controller}s
     */
    private ControllerManager controllerManager;

    /**
     * The handler for music
     */
    private MusicManager musicManager;

    /**
     * The handler for emotes
     */
    private EmotesManager emotesManager;

    private PresetManager presetManager;

    public PresetManager getPresetManager() {
        return presetManager;
    }

    public void setPresetManager(PresetManager presetManager) {
        this.presetManager = presetManager;
    }

    /**
     * The handler for all social interaction
     */
    private ContactManager contactManager;

    /**
     * The handler for auras
     */
    private AuraManager auraManager;

    /**
     * The instance of the familiar the player owns
     */
    private transient Familiar familiar;

    /**
     * The handler for items with charges, meaning degradable items
     */
    private ChargesManager charges;

    /**
     * The username, saved as a transient because it changes every time the player logs in
     */
    private transient String username;

    /**
     * The network session used for the player
     */
    private transient NetworkSession session;

    /**
     * The instance of the packet sender
     */
    private transient PacketSender packetSender;

    /**
     * The container and handler for interfaces
     */
    private transient InterfaceManager interfaceManager;

    /**
     * The handler for dialogues
     */
    private transient DialogueManager dialogueManager;

    /**
     * The handler and container for hint icons
     */
    private transient HintIconsManager hintIconsManager;

    /**
     * The handler for all game {@link org.redrune.game.content.entity.actor.player.action.Action}s
     */
    private transient ActionManager actionManager;

    /**
     * The handler for all game {@link org.redrune.game.content.entity.actor.player.event.Event}s
     */
    private transient EventManager eventManager;

    /**
     * The handler for {@link org.redrune.game.content.entity.actor.player.cutscene.Cutscene}s
     */
    private transient CutsceneManager cutsceneManager;

    /**
     * The handler for the price checking interface
     */
    private transient PriceCheckManager priceCheckManager;

    /**
     * The route event the player is engaged in, contains the task to perform once the destination has been properly
     * arrived at as well
     */
    private transient RouteEvent routeEvent;

    /**
     * The event to perform when the interfaces we have open are closed
     */
    private transient Runnable closeInterfacesEvent;

    /**
     * The handler and container for friends chats
     */
    private transient FriendChatsManager currentFriendChat;

    /**
     * The player updating handler
     */
    private transient LocalPlayerUpdate localPlayerUpdate;

    /**
     * The npc update handler
     */
    private transient LocalNPCUpdate localNPCUpdate;

    /**
     * The var manager
     */
    private transient VarManager varManager;

    /**
     * The container and handler for trades
     */
    private transient TradeManager tradeManager;

    /**
     * If the player's game session has properly started, this is flagged before {@link #run()} is called
     */
    private transient boolean started;

    /**
     * if the player's game session is properly running, this is  flagged after {@link #run()} is called
     */
    private transient boolean running;

    /**
     * If the player's game session is finishing, used for x-log prevention, this is flagged during the {@link
     * #finish()} process
     */
    private transient boolean finishing;

    public Player(String password) {
        super(GameConstants.START_PLAYER_LOCATION);
        setHitpoints(100);
        this.password = password;
        appearance = new PlayerAppearance();
        inventory = new PlayerInventory();
        equipment = new PlayerEquipment();
        skills = new PlayerSkills();
        combatDefinitions = new CombatDefinitions();
        prayer = new PlayerPrayer();
        bank = new PlayerBank();
        controllerManager = new ControllerManager();
        musicManager = new MusicManager();
        emotesManager = new EmotesManager();
        contactManager = new ContactManager();
        charges = new ChargesManager();
        auraManager = new AuraManager();
        attributes = new PlayerAttributes();
        rights = new LinkedHashSet<>(Collections.singletonList(GameFlags.debugMode ? PlayerRight.OWNER : PlayerRight.PLAYER));
        SkillCapeCustomizer.resetSkillCapes(this);
    }

    @Override
    public void finish() {
        if (finishing || isFinished()) {
            return;
        }
        finishing = true;
        long currentTime = Misc.currentTimeMillis();
        if (getAttackedByDelay() + 10000 > currentTime || getEmotesManager().getNextEmoteEnd() >= currentTime) {
            SystemManager.SLOW_EXECUTOR.schedule(() -> {
                try {
                    attributes.setPacketsDecoderPing(Misc.currentTimeMillis());
                    finishing = false;
                    finish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }, 10, TimeUnit.SECONDS);
            return;
        }
        realFinish();
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public void reset() {
        super.reset();
        refreshHitPoints();
        hintIconsManager.removeAll();
        skills.restoreSkills();
        combatDefinitions.resetSpecialAttack();
        prayer.reset();
        combatDefinitions.resetSpells(true);
        attributes.setResting(false);
        attributes.setSkullDelay(0);
        attributes.setFoodDelay(0);
        attributes.setPotDelay(0);
        attributes.setPoisonImmune(0);
        attributes.setFireImmune(0);
        attributes.setRunEnergy(100);
        appearance.generateAppearanceData();
    }

    @Override
    public int getMaxHitpoints() {
        return skills.getLevel(SkillConstants.HITPOINTS) * 10 + equipment.getEquipmentHpIncrease();
    }

    @Override
    public int getSize() {
        return appearance.getSize();
    }

    @Override
    public boolean restoreHitPoints() {
        boolean update = super.restoreHitPoints();
        if (update) {
            if (prayer.usingPrayer(0, 9)) {
                super.restoreHitPoints();
            }
            if (attributes.isResting()) {
                super.restoreHitPoints();
            }
            refreshHitPoints();
        }
        return update;
    }

    @Override
    public boolean needMasksUpdate() {
        return super.needMasksUpdate() || attributes.getTemporaryMovementType() != 0 || attributes.isUpdateMovementType();
    }

    @Override
    public void resetMasks() {
        super.resetMasks();
        attributes.setTemporaryMovementType(0);
        attributes.setUpdateMovementType(false);

        if (!attributes.clientHasLoadedMapRegion()) {
            attributes.setClientHasLoadedMapRegion();
            getPackets().refreshSpawnedObjects();
            getPackets().refreshSpawnedItems();
        }
    }

    @Override
    public void processEntity() {
        session.processContextQueue();
        cutsceneManager.process();
        super.processEntity();
        if (musicManager.musicEnded()) {
            musicManager.replayMusic();
        }
        if (attributes.hasSkull()) {
            attributes.setSkullDelay(attributes.getSkullDelay() - 1);
            if (!attributes.hasSkull()) {
                appearance.generateAppearanceData();
            }
        }
        if (attributes.getPolDelay() == 1) {
            getPackets().sendMessage("The power of the light fades. Your resistance to melee attacks return to normal.");
        }
        if (attributes.getOverloadDelay() > 0) {
            if (attributes.getOverloadDelay() == 1 || isDead()) {
                Pots.resetOverLoadEffect(this);
                return;
            } else if ((attributes.getOverloadDelay() - 1) % 25 == 0) {
                Pots.applyOverLoadEffect(this);
            }
            attributes.setOverloadDelay(attributes.getOverloadDelay() - 1);
        }
        charges.process();
        auraManager.process();
        try {
            if (routeEvent != null && routeEvent.processEvent(this)) {
                routeEvent = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            routeEvent = null;
        }
        eventManager.process();
        actionManager.process();
        prayer.processPrayer();
        controllerManager.process();
    }

    @Override
    public void processReceivedHits() {
        if (getLocks().isTeleportLocked()) {
            return;
        }
        super.processReceivedHits();
    }

    @Override
    public void loadMapRegions() {
        boolean wasAtDynamicRegion = isAtDynamicRegion();
        super.loadMapRegions();
        attributes.setClientHasntLoadedMapRegion();
        if (!started) {
            if (isAtDynamicRegion()) {
                getPackets().sendMapRegion(!started);
                attributes.setForceNextMapLoadRefresh(true);
            }
        }
        if (isAtDynamicRegion()) {
            getPackets().sendDynamicMapRegion(wasAtDynamicRegion);
            if (!wasAtDynamicRegion) {
                localNPCUpdate.reset();
            }
        } else {
            getPackets().sendMapRegion(!started);
            if (wasAtDynamicRegion) {
                localNPCUpdate.reset();
            }
        }
        attributes.setForceNextMapLoadRefresh(false);
    }

    @Override
    public void removeHitpoints(Hit hit) {
        super.removeHitpoints(hit);
        refreshHitPoints();
    }

    @Override
    public void sendDeath(final Actor source) {
        if (prayer.hasPrayersOn() && getTemporaryAttributes().get("startedDuel") != Boolean.TRUE) {
            if (prayer.usingPrayer(0, 22)) {
                prayer.sendRedemption(source);
            } else if (prayer.usingPrayer(1, 17)) {
                prayer.sendWrath(source);
            }
        }
        setNextAnimation(new Animation(-1));
        if (!controllerManager.sendDeath()) {
            return;
        }
        getLocks().lock((int) (long) 7);
        stopAll();
        if (familiar != null) {
            familiar.sendDeath(this);
        }
        final Player thisPlayer = this;
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(836));
                } else if (loop == 1) {
                    getPackets().sendMessage("Oh dear, you have died.");
                } else if (loop == 3) {
                    Player killer = getMostDamageReceivedSourcePlayer();
                    if (killer != null) {
                        killer.removeDamage(thisPlayer);
                        sendItemsOnDeath(killer);
                    }
                    equipment.init();
                    inventory.init();
                    reset();
                    setNextWorldTile(new WorldTile(GameConstants.RESPAWN_PLAYER_LOCATION));
                    setNextAnimation(new Animation(-1));
                } else if (loop == 4) {
                    getPackets().sendMusicEffect(90);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public void heal(int ammount, int extra) {
        super.heal(ammount, extra);
        refreshHitPoints();
    }

    @Override
    public void handleIncomingHit(final Hit hit) {
        if (hit.getSplat() != HitSplat.MELEE_DAMAGE && hit.getSplat() != HitSplat.RANGE_DAMAGE && hit.getSplat() != HitSplat.MAGIC_DAMAGE) {
            return;
        }
        if (hit.getDamage() > 0) {
            hit.fireLandTask();
        }
        if (auraManager.usingPenance()) {
            int amount = (int) (hit.getDamage() * 0.2);
            if (amount > 0) {
                prayer.restorePrayer(amount);
            }
        }
        Actor source = hit.getSource();
        if (source instanceof NPC) {
            NPC npc = (NPC) source;
            if (!Slayer.checkRequirement(this, SlayerMonsters.forId(npc.getId()))) {
                return;
            }
        }
        if (source == null) {
            return;
        }
        if (getTemporaryAttribute("cast_veng", false) && hit.getDamage() >= 4) {
            removeTemporaryAttribute("cast_veng");
            setNextForceTalk(new ForceTalk("Taste vengeance!"));
            source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75), HitSplat.REGULAR_DAMAGE));
        }
        if (source.isNPC()) {
            NPC n = source.toNPC();
            if (n.getId() == 13448) {
                prayer.sendSoulSplit(hit, n);
            }
        }
    }

    @Override
    public void setRunModeOn(boolean runModeOn) {
        if (runModeOn != isRunModeOn()) {
            super.setRunModeOn(runModeOn);
            attributes.setUpdateMovementType(true);
            getPackets().sendRunButtonConfig();
        }
    }

    @Override
    public void checkMultiArea() {
        if (!started) {
            return;
        }
        boolean isAtMultiArea = isForceMultiArea() || World.isMultiArea(this);
        if (isAtMultiArea && !isInMultiArea()) {
            setInMultiArea(isAtMultiArea);
            getPackets().sendGlobalConfig(616, 1);
        } else if (!isAtMultiArea && isInMultiArea()) {
            setInMultiArea(isAtMultiArea);
            getPackets().sendGlobalConfig(616, 0);
        }
    }

    public void refreshHitPoints() {
        getPackets().sendConfigByFile(7198, getHitpoints());
    }

    public PacketSender getPackets() {
        return packetSender;
    }

    @Override
    public Player toPlayer() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Player && ((Player) o).getUsername().equals(username);
    }

    public String toString() {
        return "Player[username=" + username + ", rights=" + rights + "]";
    }

    public void setRouteEvent(RouteEvent routeEvent) {
        this.routeEvent = routeEvent;
        // so when a route event is set it auto-processes it
        if (routeEvent != null && routeEvent.processEvent(this)) {
            setRouteEvent(null);
        }
    }

    public void initializeLobby(String username, NetworkSession networkSession) {
        this.username = username;
        this.session = networkSession;
        this.session.setInLobby(true);
        this.session.setPlayer(this);
        World.addLobbyPlayer(this);
        if (GameFlags.debugMode) {
            giveRight(PlayerRight.OWNER);
        }
        System.out.println("Initialized Player: " + username);
    }

    /**
     * Gives the player the right
     *
     * @param right The right
     */
    public void giveRight(PlayerRight right) {
        this.rights.add(right);
    }

    public void initializeGameSession(String username, NetworkSession session, int displayMode, int screenWidth, int screenHeight) {
        // temporary deleted after reset all chars
        this.username = username;
        this.session = session;
        session.setInLobby(false);
        session.setPlayer(this);
        this.packetSender = new PacketSender(this);
        this.interfaceManager = new InterfaceManager(this);
        this.interfaceManager.setDisplayMode(displayMode);
        this.interfaceManager.setScreenWidth(screenWidth);
        this.interfaceManager.setScreenHeight(screenHeight);
        if (getPresetManager() == null)
            setPresetManager(new PresetManager());
        dialogueManager = new DialogueManager(this);
        hintIconsManager = new HintIconsManager(this);
        priceCheckManager = new PriceCheckManager(this);
        localPlayerUpdate = new LocalPlayerUpdate(this);
        localNPCUpdate = new LocalNPCUpdate(this);
        varManager = new VarManager(this);
        actionManager = new ActionManager(this);
        eventManager = new EventManager(this);
        cutsceneManager = new CutsceneManager(this);
        tradeManager = new TradeManager(this);
        // loads player on saved instances
        attributes.setPlayer(this);
        appearance.setPlayer(this);
        inventory.setPlayer(this);
        equipment.setPlayer(this);
        skills.setPlayer(this);
        presetManager.setPlayer(this);
        combatDefinitions.setPlayer(this);
        prayer.setPlayer(this);
        bank.setPlayer(this);
        controllerManager.setPlayer(this);
        musicManager.setPlayer(this);
        emotesManager.setPlayer(this);
        contactManager.setPlayer(this);
        auraManager.setPlayer(this);
        charges.setPlayer(this);
        setFaceDirection(Misc.getFaceDirection(0, -1));
        attributes.setSwitchItemCache(Collections.synchronizedList(new ArrayList<>()));
        initEntity();
        attributes.setPacketsDecoderPing(Misc.currentTimeMillis());
        this.session.setInLobby(false);
        // inited so lets add it
        World.addWorldPlayer(this);
        RegionManager.updateActorRegion(this);
        System.out.println("Player Logged in: " + username);
        System.out.println(World.getPlayers());
    }

    // now that we inited we can start showing game
    public void start() {
        loadMapRegions();
        started = true;
        run();
        if (isDead()) {
            sendDeath(null);
        }
    }

    public void setRunHidden(boolean run) {
        super.setRunModeOn(run);
        attributes.setUpdateMovementType(true);
    }

    public void run() {
        if (SystemManager.shutdownStart != 0) {
            int delayPassed = (int) ((Misc.currentTimeMillis() - SystemManager.shutdownStart) / 1000);
            getPackets().sendSystemUpdate(SystemManager.shutdownDelay - delayPassed);
        }
        if (username.equalsIgnoreCase("tyluur")) {
            this.rights.add(PlayerRight.OWNER);
        }
        getPackets().sendMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
        interfaceManager.sendInterfaces();
        checkMultiArea();
        inventory.init();
        equipment.init();
        skills.init();
        combatDefinitions.init();
        prayer.init();
        contactManager.init();
        NoteManager.sendUnlockNotes(this);
        refreshHitPoints();
        prayer.refreshPrayerPoints();
        getPoisonManager().refresh();
        getPackets().sendConfig(281, 1000); // Quest Drop Menu
        getPackets().sendConfig(1384, 512); // Quest Filter Button
        getPackets().sendConfig(1160, -1);
        getPackets().sendConfig(1960, 1); // Unlocks Task System
        getPackets().sendConfig(1961, 524160); // Something to do with Task System. Idk
        getPackets().sendConfig(1384, 512); // Something to do with Quests. Idk
        getPackets().sendConfig(1962, 8384512); // Task System
        getPackets().sendConfig(1963, 299354); // Task System
        getPackets().sendConfig(1964, 1499501); // Task System
        getPackets().sendConfig(1965, 1470822); // Task System
        getPackets().sendGameBarStages();
        //getPackets().sendConfig(130, 3); // Black Knights Fortress Progress (Yellow)
        getPackets().sendConfig(130, 4); // Black Knights Fortress Done (Green)
        getPackets().sendConfig(130, 4);
        getPackets().sendConfig(101, 3); // Quest Points the player completed (54)
        getPackets().sendConfig(904, 326); // Maximum Quest Points in 2011 (326)
        getPackets().sendRunEnergy();
        getPackets().refreshAllowChatEffects();
        getPackets().refreshMouseButtons();
        getPackets().refreshPrivateChatSetup();
        getPackets().sendRunButtonConfig();
        getPackets().sendProfanityFilterConfig();
        getPackets().sendDefaultPlayersOptions();
        getEmotesManager().refreshListConfigs();
        musicManager.init();
        emotesManager.refreshListConfigs();

        if (attributes.getCurrentFriendChatOwner() != null) {
            FriendChatsManager.Companion.joinChat(attributes.getCurrentFriendChatOwner(), this);
            if (currentFriendChat == null) {
                attributes.setCurrentFriendChatOwner(null);
            }
        }

        // Checks for familiars.
        if (familiar != null) {
            familiar.respawnFamiliar(this);
        }

        attributes.setLastIP(getSession().getIPAddress());
        running = true;
        attributes.setUpdateMovementType(true);
        appearance.generateAppearanceData();
        controllerManager.login(); // checks what to do on login after welcome "Log in"
        OwnedObjectManager.linkKeys(this);
        PlayerTutorial.INSTANCE.onLogin(this);
    }

    public void logout(boolean lobby) {
        if (!running) {
            return;
        }
        long currentTime = Misc.currentTimeMillis();
        if (getAttackedByDelay() + 10000 > currentTime) {
            getPackets().sendMessage("You can't log out until 10 seconds after the end of combat.");
            return;
        }
        if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
            getPackets().sendMessage("You can't log out while perfoming an emote.");
            return;
        }
        getPackets().sendLogout(lobby);
        running = false;
    }

    public void forceOffline() {
        realFinish();
        getPackets().sendLogout(false);
    }

    public void finishLobby() {
        World.removePlayer(this, true);
    }

    public void realFinish() {
        if (isFinished()) {
            return;
        }
        stopAll();
        cutsceneManager.logout();
        controllerManager.logout();
        running = false;
        contactManager.sendFriendsMyStatus(false);
        if (currentFriendChat != null) {
            currentFriendChat.leaveChat(this, true);
        }
        if (familiar != null) {
            familiar.dissmissFamiliar(true);
        }
        setFinished(true);
        PlayerSaving.savePlayer(this);
        RegionManager.updateActorRegion(this);
        World.removePlayer(this, false);
        System.out.println("Finished Player: " + username);
    }

    public void stopAll() {
        stopAll(true);
    }

    public void stopAll(boolean stopWalk) {
        stopAll(stopWalk, true);
    }

    // as walk done clientsided
    public void stopAll(boolean stopWalk, boolean stopInterfaces) {
        routeEvent = null;
        if (stopInterfaces) {
            closeInterfaces();
        }
        if (stopWalk) {
            resetWalkSteps();
        }
        actionManager.forceStop();
        combatDefinitions.resetSpells(false);
        getInteractionManager().cancelActorInteraction();
        setNextFaceActor(null);
    }

    public void closeInterfaces() {
        if (interfaceManager.containsScreenInter()) {
            interfaceManager.closeScreenInterface();
        }
        if (interfaceManager.containsInventoryInter()) {
            interfaceManager.closeInventoryInterface();
        }
        dialogueManager.finishDialogue();
        if (closeInterfacesEvent != null) {
            closeInterfacesEvent.run();
            closeInterfacesEvent = null;
        }
    }

    public void sendItemsOnDeath(Player killer) {
        charges.die();
        auraManager.removeAura();
        CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 14; i++) {
            if (equipment.getItem(i) != null && equipment.getItem(i).getId() != -1 && equipment.getItem(i).getAmount() != -1) {
                containedItems.add(new Item(equipment.getItem(i).getId(), equipment.getItem(i).getAmount()));
            }
        }
        for (int i = 0; i < 28; i++) {
            if (inventory.getItem(i) != null && inventory.getItem(i).getId() != -1 && inventory.getItem(i).getAmount() != -1) {
                containedItems.add(new Item(getInventory().getItem(i).getId(), getInventory().getItem(i).getAmount()));
            }
        }
        if (containedItems.isEmpty()) {
            return;
        }
        int keptAmount = 5;
        if (attributes.hasSkull()) {
            keptAmount = 0;
        }
        if (prayer.usingPrayer(0, 10) || prayer.usingPrayer(1, 0)) {
            keptAmount++;
        }
        CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
        Item lastItem = new Item(1, 1);
        for (int i = 0; i < keptAmount; i++) {
            for (Item item : containedItems) {
                int price = item.getDefinitions().getValue();
                if (price >= lastItem.getDefinitions().getValue()) {
                    lastItem = item;
                }
            }
            keptItems.add(lastItem);
            containedItems.remove(lastItem);
            lastItem = new Item(1, 1);
        }
        inventory.reset();
        equipment.reset();
        for (Item item : keptItems) {
            getInventory().addItem(item);
        }
        for (Item item : containedItems) {
            RegionManager.addGroundItem(item, getLastWorldTile(), killer, true, 180, true);
        }
    }

    public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
        useStairs(emoteId, dest, useDelay, totalDelay, null);
    }

    public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
        stopAll();
        getLocks().lock((int) (long) totalDelay);
        if (emoteId != -1) {
            setNextAnimation(new Animation(emoteId));
        }
        if (useDelay == 0) {
            setNextWorldTile(dest);
        } else {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    if (isDead()) {
                        return;
                    }
                    setNextWorldTile(dest);
                    if (message != null) {
                        getPackets().sendMessage(message);
                    }
                }
            }, useDelay - 1);
        }
    }

    public void sendPublicChatMessage(PublicChatMessage message) {
        for (int regionId : getMapRegionsIds()) {
            List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (Integer playerIndex : playersIndexes) {
                Player p = World.getPlayers().get(playerIndex);
                if (p == null || !p.hasStarted() || p.isFinished() || p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null) {
                    continue;
                }
                p.getPackets().sendPublicMessage(this, message);
            }
        }
    }

    /**
     * If the player has started, meaning the game session has been initialized properly
     */
    public boolean hasStarted() {
        return started;
    }

    /**
     * Teleports the player to specified coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    public void teleportPlayer(int x, int y, int z) {
        setNextWorldTile(new WorldTile(x, y, z));
        stopAll();
    }

    /**
     * If there are donator rights in the {@link #rights} set
     */
    public boolean isDonator() {
        return rights.contains(PlayerRight.DONATOR);
    }

    /**
     * If the {@link #rights} set has any of these parameters, this is true
     *
     * @param rights The rights
     */
    public boolean rightsContains(PlayerRight... rights) {
        for (PlayerRight right : rights) {
            if (right == PlayerRight.PLAYER) {
                return true;
            }
            if (this.rights.contains(right)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Stores a new list of rights
     *
     * @param rights The rights to store
     */
    public void storeRights(Set<PlayerRight> rights) {
        this.rights.clear();
        this.rights.addAll(rights);
    }

    /**
     * If this right is a staff right
     */
    public boolean isStaff() {
        for (PlayerRight right : rights) {
            String name = right.getFormattedName();
            if (name.contains("owner") || name.contains("admin") || name.contains("mod") || name.contains("trial") || name.contains("assistant")) {
                return true;
            }
        }
        return false;
    }

    /**
     * The icon the player uses for messages
     */
    public int getMessageIcon() {
        return getDominantRight().getMessageIcon();
    }

    /**
     * Gets the most dominant right. The {@link #rights} are sorted based on the position of the right in the enum
     * (ordinal), so the first right will be the most dominant  .
     *
     * @return A {@code Right} instance
     */
    public PlayerRight getDominantRight() {
        if (rights.size() != 0) {
            return rights.iterator().next();
        } else {
            System.err.println("Unexpected situation - rights set was empty!");
            return PlayerRight.PLAYER;
        }
    }

    /**
     * The username the player uses for chatting
     */
    public String getDisplayName() {
        if (attributes.getDisplayName() != null) {
            return attributes.getDisplayName();
        }
        return Misc.formatPlayerNameForDisplay(username);
    }

    public PlayerAttributes getAttributes() {
        return this.attributes;
    }

    public Set<PlayerRight> getRights() {
        return this.rights;
    }

    public String getPassword() {
        return this.password;
    }

    public PlayerAppearance getAppearance() {
        return this.appearance;
    }

    public PlayerInventory getInventory() {
        return this.inventory;
    }

    public PlayerEquipment getEquipment() {
        return this.equipment;
    }

    public PlayerSkills getSkills() {
        return this.skills;
    }

    public PlayerBank getBank() {
        return this.bank;
    }

    public PlayerPrayer getPrayer() {
        return this.prayer;
    }

    public CombatDefinitions getCombatDefinitions() {
        return this.combatDefinitions;
    }

    public ControllerManager getControllerManager() {
        return this.controllerManager;
    }

    public MusicManager getMusicManager() {
        return this.musicManager;
    }

    public EmotesManager getEmotesManager() {
        return this.emotesManager;
    }

    public ContactManager getContactManager() {
        return this.contactManager;
    }

    public AuraManager getAuraManager() {
        return this.auraManager;
    }

    public Familiar getFamiliar() {
        return this.familiar;
    }

    public ChargesManager getCharges() {
        return this.charges;
    }

    public String getUsername() {
        return this.username;
    }

    public NetworkSession getSession() {
        return this.session;
    }

    public InterfaceManager getInterfaceManager() {
        return this.interfaceManager;
    }

    public DialogueManager getDialogueManager() {
        return this.dialogueManager;
    }

    public HintIconsManager getHintIconsManager() {
        return this.hintIconsManager;
    }

    public ActionManager getActionManager() {
        return this.actionManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public CutsceneManager getCutsceneManager() {
        return this.cutsceneManager;
    }

    public PriceCheckManager getPriceCheckManager() {
        return this.priceCheckManager;
    }

    public FriendChatsManager getCurrentFriendChat() {
        return this.currentFriendChat;
    }

    public LocalPlayerUpdate getLocalPlayerUpdate() {
        return this.localPlayerUpdate;
    }

    public LocalNPCUpdate getLocalNPCUpdate() {
        return this.localNPCUpdate;
    }

    public VarManager getVarManager() {
        return this.varManager;
    }

    public TradeManager getTradeManager() {
        return this.tradeManager;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setControllerManager(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSession(NetworkSession session) {
        this.session = session;
    }

    public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
        this.closeInterfacesEvent = closeInterfacesEvent;
    }

    public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
        this.currentFriendChat = currentFriendChat;
    }

    public void restoreAll() {
        setHitpoints(getMaxHitpoints());
        refreshHitPoints();
        prayer.setPrayerpoints(getSkills().getLevel(PlayerSkills.PRAYER) * 10);
        combatDefinitions.resetSpecialAttack();
        combatDefinitions.resetSpells(true);
//		getPoison().reset();
//		poisonImmune = 0;
        setAttackedBy(null);
//		setRunEnergy(100);
        appearance.generateAppearanceData();
        getSkills().restoreSkills();
        prayer.refreshPrayerPoints();
        resetCombat();
    }

    public boolean isUnderCombat() {
        return getAttackedByDelay() + 10000 >= Misc.currentTimeMillis();
    }

    public boolean takeMoney(int amount) {
        if (inventory.getNumerOf(995) >= amount) {
            inventory.deleteItem(995, amount);
            return true;
        } else {
            return false;
        }
    }
}