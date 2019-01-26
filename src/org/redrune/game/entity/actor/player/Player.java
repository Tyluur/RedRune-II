package org.redrune.game.entity.actor.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.GameFlags;
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.content.cutscene.CutsceneManager;
import org.redrune.game.content.entity.actor.player.action.ActionManager;
import org.redrune.game.content.entity.actor.player.controller.ControllerManager;
import org.redrune.game.content.entity.actor.player.dialogue.DialogueManager;
import org.redrune.game.content.entity.actor.player.event.EventManager;
import org.redrune.game.content.entity.actor.player.skills.slayer.Slayer;
import org.redrune.game.content.entity.actor.player.skills.slayer.Slayer.SlayerMonsters;
import org.redrune.game.content.entity.actor.player.skills.slayer.SlayerTask;
import org.redrune.game.content.entity.item.Pots;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.mask.*;
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
import org.redrune.networking.Session;
import org.redrune.networking.codec.encode.WorldPacketsEncoder;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.file.SerializableFilesManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.actor.player.PublicChatMessage;
import org.redrune.utility.game.entity.actor.player.QuickChatMessage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends Actor {
	
	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;
	
	private static final long serialVersionUID = 2011932556974180375L;
	
	public SlayerTask slayerTask;
	
	private String displayName;
	
	@Getter
	private String lastIP;
	
	@Getter
	@Setter
	private String lastMac;
	
	/**
	 * The set of the rights the player has
	 */
	@Getter
	private Set<PlayerRight> rights;
	
	@Getter
	@Setter
	private String password;
	
	@Getter
	private PlayerAppearance appearance;
	
	@Getter
	private PlayerInventory inventory;
	
	@Getter
	private PlayerEquipment equipment;
	
	@Getter
	private PlayerSkills skills;
	
	@Getter
	private CombatDefinitions combatDefinitions;
	
	@Getter
	private PlayerPrayer prayer;
	
	@Getter
	private PlayerBank bank;
	
	@Getter
	@Setter
	private ControllerManager controllerManager;
	
	@Getter
	private MusicsManager musicsManager;
	
	@Getter
	private EmotesManager emotesManager;
	
	@Getter
	private ContactManager contactManager;
	
	@Getter
	private AuraManager auraManager;
	
	@Getter
	private PlayerSaving saving;
	
	@Getter
	@Setter
	private Familiar familiar;
	
	@Getter
	private byte runEnergy;
	
	@Getter
	@Setter
	private boolean allowChatEffects;
	
	@Getter
	@Setter
	private boolean mouseButtons;
	
	@Getter
	@Setter
	private int privateChatSetup;
	
	private int skullDelay;
	
	@Getter
	private int skullId;
	
	@Getter
	@Setter
	private boolean forceNextMapLoadRefresh;
	
	@Getter
	private long poisonImmune;
	
	@Getter
	private long fireImmune;
	
	@Getter
	private int[] pouches;
	
	@Getter
	@Setter
	private boolean filterGame;
	
	@Getter
	private ChargesManager charges;
	
	@Getter
	@Setter
	private int[] maxedCapeCustomized;
	
	@Getter
	@Setter
	private int[] completionistCapeCustomized;
	
	@Getter
	@Setter
	private int overloadDelay;
	
	@Getter
	@Setter
	private String currentFriendChatOwner;
	
	@Getter
	@Setter
	private int summoningLeftClickOption;
	
	private List<String> ownedObjectsManagerKeys;
	
	@Getter
	@Setter
	private boolean experienceLocked;
	
	@Getter
	@Setter
	private int temporaryMovementType;
	
	@Getter
	private boolean updateMovementType;
	
	@Getter
	@Setter
	private transient PacketSender packetSender;
	
	@Getter
	@Setter
	private transient String username;
	
	@Getter
	@Setter
	private transient Session session;
	
	private transient boolean clientLoadedMapRegion;
	
	@Getter
	@Setter
	private transient int displayMode;
	
	@Getter
	@Setter
	private transient int trapAmount;
	
	@Getter
	@Setter
	private transient int screenHeight;
	
	@Getter
	@Setter
	private transient int screenWidth;
	
	@Getter
	private transient InterfaceManager interfaceManager;
	
	@Getter
	private transient DialogueManager dialogueManager;
	
	@Getter
	private transient HintIconsManager hintIconsManager;
	
	@Getter
	private transient ActionManager actionManager;
	
	@Getter
	private transient EventManager eventManager;
	
	@Getter
	private transient CutsceneManager cutsceneManager;
	
	@Getter
	private transient PriceCheckManager priceCheckManager;
	
	private transient RouteEvent routeEvent;
	
	@Getter
	@Setter
	private transient FriendChatsManager currentFriendChat;
	
	@Getter
	private transient LocalPlayerUpdate localPlayerUpdate;
	
	@Getter
	private transient LocalNPCUpdate localNPCUpdate;
	
	@Getter
	private transient TradeManager tradeManager;
	
	private transient boolean started;
	
	@Getter
	private transient boolean running;
	
	private transient boolean finishing;
	
	@Getter
	@Setter
	private transient long packetsDecoderPing;
	
	@Getter
	private transient boolean resting;
	
	@Getter
	private transient boolean canPvp;
	
	@Getter
	@Setter
	private transient long foodDelay;
	
	@Getter
	@Setter
	private transient long potDelay;
	
	@Getter
	@Setter
	private transient long boneDelay;
	
	@Setter
	private transient Runnable closeInterfacesEvent;
	
	@Getter
	@Setter
	private transient long lastPublicMessage;
	
	@Getter
	@Setter
	private transient long polDelay;
	
	@Getter
	private transient List<Integer> switchItemCache;
	
	@Getter
	@Setter
	private transient boolean equipDisabled;
	
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
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		contactManager = new ContactManager();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		saving = new PlayerSaving();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		pouches = new int[4];
		slayerTask = new SlayerTask();
		rights = new LinkedHashSet<>(Collections.singletonList(GameFlags.debugMode ? PlayerRight.OWNER : PlayerRight.PLAYER));
		SkillCapeCustomizer.resetSkillCapes(this);
		ownedObjectsManagerKeys = new LinkedList<>();
	}
	
	@Override
	public void finish() {
		if (finishing || hasFinished()) {
			return;
		}
		finishing = true;
		long currentTime = Misc.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime || getEmotesManager().getNextEmoteEnd() >= currentTime) {
			SystemManager.SLOW_EXECUTOR.schedule(() -> {
				try {
					packetsDecoderPing = Misc.currentTimeMillis();
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
		resting = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		poisonImmune = 0;
		fireImmune = 0;
		setRunEnergy(100);
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
			if (resting) {
				super.restoreHitPoints();
			}
			refreshHitPoints();
		}
		return update;
	}
	
	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || temporaryMovementType != 0 || updateMovementType;
	}
	
	@Override
	public void resetMasks() {
		super.resetMasks();
		temporaryMovementType = 0;
		updateMovementType = false;
		if (!clientHasLoadedMapRegion()) {
			// load objects and items here
			setClientHasLoadedMapRegion();
			getPacketSender().refreshSpawnedObjects();
			getPacketSender().refreshSpawnedItems();
		}
	}
	
	@Override
	public void processEntity() {
		cutsceneManager.process();
		super.processEntity();
		if (musicsManager.musicEnded()) {
			musicsManager.replayMusic();
		}
		if (hasSkull()) {
			skullDelay--;
			if (!hasSkull()) {
				appearance.generateAppearanceData();
			}
		}
		if (polDelay == 1) {
			getPackets().sendGameMessage("The power of the light fades. Your resistance to melee attacks return to normal.");
		}
		if (overloadDelay > 0) {
			if (overloadDelay == 1 || isDead()) {
				Pots.resetOverLoadEffect(this);
				return;
			} else if ((overloadDelay - 1) % 25 == 0) {
				Pots.applyOverLoadEffect(this);
			}
			overloadDelay--;
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
		if (getLocks().isMovementLocked() || getLocks().isTeleportLocked()) {
			return;
		}
		super.processReceivedHits();
	}
	
	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		if (!started) {
			if (isAtDynamicRegion()) {
				getPackets().sendMapRegion(!started);
				forceNextMapLoadRefresh = true;
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
		forceNextMapLoadRefresh = false;
	}
	
	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}
	
	@Override
	public void sendDeath(final Actor source) {
		if (prayer.hasPrayersOn() && getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(playerIndex);
								if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.withinDistance(this, 1) || !this.getControllerManager().canHit(player)) {
									continue;
								}
								player.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = RegionManager.getRegion(regionId).getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(this, 1) || !npc.getDefinitions().hasAttackOption() || !this.getControllerManager().canHit(npc)) {
									continue;
								}
								npc.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead() && !source.hasFinished() && source.withinDistance(this, 1)) {
						source.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(SkillConstants.PRAYER) * 2.5)), HitSplat.REGULAR_DAMAGE));
					}
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY(), target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY(), target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() - 1, target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() + 1, target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() - 1, target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() + 1, target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() - 1, target.getPlane()));
						RegionManager.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				RegionManager.sendProjectile(this, new WorldTile(getX() + 2, getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
				RegionManager.sendProjectile(this, new WorldTile(getX() + 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				RegionManager.sendProjectile(this, new WorldTile(getX() + 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				RegionManager.sendProjectile(this, new WorldTile(getX() - 2, getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				RegionManager.sendProjectile(this, new WorldTile(getX() - 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				RegionManager.sendProjectile(this, new WorldTile(getX() - 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				RegionManager.sendProjectile(this, new WorldTile(getX(), getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				RegionManager.sendProjectile(this, new WorldTile(getX(), getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));
						
						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = World.getPlayers().get(playerIndex);
										if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.withinDistance(target, 2) || !Player.this.getControllerManager().canHit(player)) {
											continue;
										}
										player.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = RegionManager.getRegion(regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
										if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(target, 2) || !npc.getDefinitions().hasAttackOption() || !Player.this.getControllerManager().canHit(npc)) {
											continue;
										}
										npc.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target && !source.isDead() && !source.hasFinished() && source.withinDistance(target, 2)) {
								source.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(SkillConstants.PRAYER) * 3), HitSplat.REGULAR_DAMAGE));
							}
						}
						
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() + 2, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY(), getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() - 2, getPlane()));
						
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() + 2, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY(), getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() - 2, getPlane()));
						
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() + 2, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() - 2, getPlane()));
						
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() + 1, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() - 1, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() + 1, getPlane()));
						RegionManager.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() - 1, getPlane()));
					}
				});
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
					getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					Player killer = getMostDamageReceivedSourcePlayer();
					// killer.inventory.addItem(24158, 2);
					if (killer != null) {
						killer.removeDamage(thisPlayer);
						//						killer.increaseKillCount(thisPlayer);
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
	public void handleIngoingHit(final Hit hit) {
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
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Misc.getRandom(100) <= 70) {
				hit.setDamage((int) (hit.getDamage() * 0.75));
			}
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (polDelay > Misc.currentTimeMillis()) {
			hit.setDamage((int) (hit.getDamage() * 0.5));
		}
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17)) {
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18)) {
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2229));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19)) {
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitSplat.ABSORB_DAMAGE));
				}
			} else if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitSplat.ABSORB_DAMAGE));
				}
			} else if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitSplat.ABSORB_DAMAGE));
				}
			}
		}
		if (getAttribute("cast_veng", false) && hit.getDamage() >= 4) {
			removeAttribute("cast_veng");
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75), HitSplat.REGULAR_DAMAGE));
		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.prayer.hasPrayersOn()) {
				if (p2.prayer.usingPrayer(0, 24)) { // smite
					int drain = hit.getDamage() / 4;
					if (drain > 0) {
						prayer.drainPrayer(drain);
					}
				} else {
					if (p2.prayer.usingPrayer(1, 18)) {
						sendSoulSplit(hit, p2);
					}
					if (hit.getDamage() == 0) {
						return;
					}
					if (!p2.prayer.isBoostedLeech()) {
						if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 19)) {
								if (Misc.getRandom(4) == 0) {
									p2.prayer.increaseTurmoilBonus(this);
									p2.prayer.setBoostedLeech(true);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 1)) { // sap att
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(0)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
									} else {
										p2.prayer.increaseLeechBonus(0);
										p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2214));
									p2.prayer.setBoostedLeech(true);
									RegionManager.sendProjectile(p2, this, 2215, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2216));
										}
									}, 1);
									return;
								}
							} else {
								if (p2.prayer.usingPrayer(1, 10)) {
									if (Misc.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(3)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										} else {
											p2.prayer.increaseLeechBonus(3);
											p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										RegionManager.sendProjectile(p2, this, 2231, 35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2232));
											}
										}, 1);
										return;
									}
								}
								if (p2.prayer.usingPrayer(1, 14)) {
									if (Misc.getRandom(7) == 0) {
										if (p2.prayer.reachedMax(7)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										} else {
											p2.prayer.increaseLeechBonus(7);
											p2.getPackets().sendGameMessage("Your curse drains Strength from the enemy, boosting your Strength.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										RegionManager.sendProjectile(p2, this, 2248, 35, 35, 20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2250));
											}
										}, 1);
										return;
									}
								}
								
							}
						}
						if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 2)) { // sap range
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(1)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
									} else {
										p2.prayer.increaseLeechBonus(1);
										p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.prayer.setBoostedLeech(true);
									RegionManager.sendProjectile(p2, this, 2218, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2219));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 11)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(4)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.prayer.increaseLeechBonus(4);
										p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									RegionManager.sendProjectile(p2, this, 2236, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2238));
										}
									});
									return;
								}
							}
						}
						if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
							if (p2.prayer.usingPrayer(1, 3)) { // sap mage
								if (Misc.getRandom(4) == 0) {
									if (p2.prayer.reachedMax(2)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
									} else {
										p2.prayer.increaseLeechBonus(2);
										p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.prayer.setBoostedLeech(true);
									RegionManager.sendProjectile(p2, this, 2221, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2222));
										}
									}, 1);
									return;
								}
							} else if (p2.prayer.usingPrayer(1, 12)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.prayer.reachedMax(5)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.prayer.increaseLeechBonus(5);
										p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									RegionManager.sendProjectile(p2, this, 2240, 35, 35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2242));
										}
									}, 1);
									return;
								}
							}
						}
						
						// overall
						
						if (p2.prayer.usingPrayer(1, 13)) { // leech defence
							if (Misc.getRandom(10) == 0) {
								if (p2.prayer.reachedMax(6)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.prayer.increaseLeechBonus(6);
									p2.getPackets().sendGameMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								RegionManager.sendProjectile(p2, this, 2244, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2246));
									}
								}, 1);
								return;
							}
						}
						
						if (p2.prayer.usingPrayer(1, 15)) {
							if (Misc.getRandom(10) == 0) {
								if (getRunEnergy() <= 0) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100 : p2.getRunEnergy() + 10);
									setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy() - 10 : 0);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								RegionManager.sendProjectile(p2, this, 2256, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2258));
									}
								}, 1);
								return;
							}
						}
						
						if (p2.prayer.usingPrayer(1, 16)) {
							if (Misc.getRandom(10) == 0) {
								if (combatDefinitions.getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.combatDefinitions.restoreSpecialAttack();
									combatDefinitions.decreaseSpecialEnergy(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								RegionManager.sendProjectile(p2, this, 2252, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2254));
									}
								}, 1);
								return;
							}
						}
						
						if (p2.prayer.usingPrayer(1, 4)) { // sap spec
							if (Misc.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.prayer.setBoostedLeech(true);
								if (combatDefinitions.getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									combatDefinitions.decreaseSpecialEnergy(10);
								}
								RegionManager.sendProjectile(p2, this, 2224, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2225));
									}
								}, 1);
								return;
							}
						}
					}
				}
			}
		} else {
			NPC n = (NPC) source;
			if (n.getId() == 13448) {
				sendSoulSplit(hit, n);
			}
		}
	}
	
	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			getPacketSender().sendRunButtonConfig();
		}
	}
	
	@Override
	public void checkMultiArea() {
		if (!started) {
			return;
		}
		boolean isAtMultiArea = isForceMultiArea() || World.isMultiArea(this);
		if (isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 1);
		} else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendGlobalConfig(616, 0);
		}
	}
	
	public void sendSoulSplit(final Hit hit, final Actor user) {
		final Player target = this;
		if (hit.getDamage() > 0) {
			RegionManager.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		}
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0) {
					RegionManager.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				}
			}
		}, 1);
	}
	
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}
	
	public boolean hasSkull() {
		return skullDelay > 0;
	}
	
	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}
	
	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}
	
	public void refreshHitPoints() {
		getPackets().sendConfigByFile(7198, getHitpoints());
	}
	
	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}
	
	@Override
	public Player toPlayer() {
		return this;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Player && ((Player) o).getUsername().equals(username);
	}
	
	@Override
	public String toString() {
		return "Player{" + "displayName='" + displayName + '\'' + ", rights=" + rights + '}';
	}
	
	public void setRouteEvent(RouteEvent routeEvent) {
		this.routeEvent = routeEvent;
		// so when a route event is set it auto-processes it
		if (routeEvent != null && routeEvent.processEvent(this)) {
			setRouteEvent(null);
		}
	}
	
	public void init(String string, Session session) {
		username = string;
		this.session = session;
		if (GameFlags.debugMode) {
			giveRight(PlayerRight.OWNER);
		}
		System.out.println("Inited Player: " + string + ", pass: " + password);
	}
	
	/**
	 * Gives the player the right
	 *
	 * @param right
	 * 		The right
	 */
	public void giveRight(PlayerRight right) {
		this.rights.add(right);
	}
	
	public void init(String username, int displayMode, int screenWidth, int screenHeight) {
		// temporary deleted after reset all chars
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		setPacketSender(new PacketSender(this));
		actionManager = new ActionManager(this);
		eventManager = new EventManager(this);
		cutsceneManager = new CutsceneManager(this);
		tradeManager = new TradeManager(this);
		// loads player on saved instances
		appearance.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		controllerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		contactManager.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		setDirection(Misc.getFaceDirection(0, -1));
		switchItemCache = Collections.synchronizedList(new ArrayList<Integer>());
		initEntity();
		packetsDecoderPing = Misc.currentTimeMillis();
		// inited so lets add it
		World.addPlayer(this);
		RegionManager.updateActorRegion(this);
		System.out.println("Player Logged in: " + username);
	}
	
	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearance.generateAppearanceData();
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
	
	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}
	
	public void toggleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update) {
			getPacketSender().sendRunButtonConfig();
		}
	}
	
	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}
	
	public void restoreRunEnergy() {
		if (getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if (resting && runEnergy < 100) {
				runEnergy++;
			}
			getPackets().sendRunEnergy();
		}
	}
	
	public void run() {
		if (SystemManager.shutdownStart != 0) {
			int delayPassed = (int) ((Misc.currentTimeMillis() - SystemManager.shutdownStart) / 1000);
			getPackets().sendSystemUpdate(SystemManager.shutdownDelay - delayPassed);
		}
		if (GameFlags.debugMode) {
			this.rights.add(PlayerRight.OWNER);
		}
		getPackets().sendGameMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
		lastIP = getSession().getIp();
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		getPacketSender().refreshAllowChatEffects();
		getPacketSender().refreshMouseButtons();
		getPacketSender().refreshPrivateChatSetup();
		getPacketSender().sendRunButtonConfig();
		getEmotesManager().refreshListConfigs();
		getPacketSender().sendDefaultPlayersOptions();
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
		musicsManager.init();
		emotesManager.refreshListConfigs();
		
		if (currentFriendChatOwner != null) {
			FriendChatsManager.joinChat(currentFriendChatOwner, this);
			if (currentFriendChat == null) {
				currentFriendChatOwner = null;
			}
		}
		
		// Checks for familiars.
		if (familiar != null) {
			familiar.respawnFamiliar(this);
		}
		
		running = true;
		updateMovementType = true;
		appearance.generateAppearanceData();
		controllerManager.login(); // checks what to do on login after welcome "Log in"
		OwnedObjectManager.linkKeys(this);
	}
	
	public void logout(boolean lobby) {
		if (!running) {
			return;
		}
		long currentTime = Misc.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets().sendGameMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage("You can't log out while perfoming an emote.");
			return;
		}
		getPackets().sendLogout(lobby);
		running = false;
	}
	
	public void forceOffline() {
		realFinish();
		getPackets().sendLogout(false);
	}
	
	public void realFinish() {
		if (hasFinished()) {
			return;
		}
		stopAll();
		cutsceneManager.logout();
		controllerManager.logout(); // checks what to do on before logout for
		// login
		running = false;
		contactManager.sendFriendsMyStatus(false);
		if (currentFriendChat != null) {
			currentFriendChat.leaveChat(this, true);
		}
		if (familiar != null) {
			familiar.dissmissFamiliar(true);
		}
		setFinished(true);
		session.setDecoder(-1);
		SerializableFilesManager.savePlayer(this);
		RegionManager.updateActorRegion(this);
		World.removePlayer(this);
		System.out.println("Finished Player: " + username + ", pass: " + password);
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
	
	public String getDisplayName() {
		return Misc.formatPlayerNameForDisplay(username);
	}
	
	public void setDisplayName(String displayName) {
		if (Misc.formatPlayerNameForDisplay(username).equals(displayName)) {
			this.displayName = null;
		} else {
			this.displayName = displayName;
		}
	}
	
	public boolean hasDisplayName() {
		return displayName != null;
	}
	
	public void drainRunEnergy() {
		setRunEnergy(runEnergy - 1);
	}
	
	public void setResting(boolean resting) {
		this.resting = resting;
		getPacketSender().sendRunButtonConfig();
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
		if (hasSkull()) {
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
	
	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearance.generateAppearanceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
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
						getPackets().sendGameMessage(message);
					}
				}
			}, useDelay - 1);
		}
	}
	
	public void addPotDelay(long time) {
		potDelay = time + Misc.currentTimeMillis();
	}
	
	public void addFoodDelay(long time) {
		foodDelay = time + Misc.currentTimeMillis();
	}
	
	public void addBoneDelay(long time) {
		boneDelay = time + Misc.currentTimeMillis();
	}
	
	public void addPoisonImmune(long time) {
		poisonImmune = time + Misc.currentTimeMillis();
		getPoisonManager().reset();
	}
	
	public void addFireImmune(long time) {
		fireImmune = time + Misc.currentTimeMillis();
	}
	
	public void kickPlayerFromFriendsChannel(String name) {
		if (currentFriendChat == null) {
			return;
		}
		currentFriendChat.kickPlayerFromChat(this, name);
	}
	
	public void sendFriendsChannelMessage(String message) {
		if (currentFriendChat == null) {
			return;
		}
		currentFriendChat.sendMessage(this, message);
	}
	
	public void sendFriendsChannelQuickMessage(QuickChatMessage message) {
		if (currentFriendChat == null) {
			return;
		}
		currentFriendChat.sendQuickMessage(this, message);
	}
	
	public void sendPublicChatMessage(PublicChatMessage message) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playersIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (Integer playerIndex : playersIndexes) {
				Player p = World.getPlayers().get(playerIndex);
				if (p == null || !p.hasStarted() || p.hasFinished() || p.getLocalPlayerUpdate().getLocalPlayers()[getIndex()] == null) {
					continue;
				}
				p.getPackets().sendPublicMessage(this, message);
			}
		}
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public boolean withinDistance(Player tile) {
		if (cutsceneManager.hasCutscene()) {
			return getMapRegionsIds().contains(tile.getRegionId());
		} else {
			if (tile.getPlane() != getPlane()) {
				return false;
			}
			return Math.abs(tile.getX() - getX()) <= 14 && Math.abs(tile.getY() - getY()) <= 14;
		}
	}
	
	public long getTeleBlockDelay() {
		return getAttribute("TeleBlocked", -1L);
	}
	
	public void setTeleBlockDelay(long teleDelay) {
		putAttribute("TeleBlocked", teleDelay + Misc.currentTimeMillis());
	}
	
	public long getPrayerDelay() {
		return getAttribute("PrayerBlocked", 0L);
	}
	
	public void setPrayerDelay(long teleDelay) {
		putAttribute("PrayerBlocked", teleDelay + Misc.currentTimeMillis());
		prayer.closeAllPrayers();
	}
	
	public void teleportPlayer(int x, int y, int z) {
		setNextWorldTile(new WorldTile(x, y, z));
		stopAll();
	}
	
	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
		{
			ownedObjectsManagerKeys = new LinkedList<>();
		}
		return ownedObjectsManagerKeys;
	}
	
	/**
	 * If there are donator rights in the {@link #rights} set
	 */
	public boolean isDonator() {
		return rights.contains(PlayerRight.PREMIUM_DONATOR) || rights.contains(PlayerRight.EXTREME_DONATOR);
	}
	
	/**
	 * If the {@link #rights} set has any of these parameters, this is true
	 *
	 * @param rights
	 * 		The rights
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
	 * @param rights
	 * 		The rights to store
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
	
	public void addPolDelay(long delay) {
		polDelay = delay + Misc.currentTimeMillis();
	}
	
}