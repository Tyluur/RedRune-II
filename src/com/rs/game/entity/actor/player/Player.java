package com.rs.game.entity.actor.player;

import com.rs.cores.CoresManager;
import com.rs.game.GameConstants;
import com.rs.game.content.SkillCapeCustomizer;
import com.rs.game.content.Trade;
import com.rs.game.content.action.ActionManager;
import com.rs.game.content.action.impl.PlayerCombatAction;
import com.rs.game.content.controler.ControlerManager;
import com.rs.game.content.cutscene.CutsceneManager;
import com.rs.game.content.dialogue.DialogueManager;
import com.rs.game.content.minigame.ClanWars;
import com.rs.game.content.minigame.War;
import com.rs.game.content.node.item.Pots;
import com.rs.game.content.skills.slayer.Slayer;
import com.rs.game.content.skills.slayer.Slayer.SlayerMonsters;
import com.rs.game.content.skills.slayer.SlayerTask;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.data.CombatDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitLook;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.godwars.zaros.Nex;
import com.rs.game.entity.actor.npc.impl.others.Pets;
import com.rs.game.entity.actor.player.data.*;
import com.rs.game.entity.actor.player.link.*;
import com.rs.game.entity.actor.player.render.LocalNPCUpdate;
import com.rs.game.entity.actor.player.render.LocalPlayerUpdate;
import com.rs.game.entity.item.FloorItem;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.networking.Session;
import com.rs.networking.codec.encode.WorldPacketsEncoder;
import com.rs.utility.Misc;
import com.rs.utility.game.files.SerializableFilesManager;
import com.rs.utility.game.player.PublicChatMessage;
import com.rs.utility.game.player.QuickChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends Actor {
	
	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1, RUN_MOVE_TYPE = 2;
	
	private static final long serialVersionUID = 2011932556974180375L;
	
	// honor
	public int killCount, deathCount;
	
	public SlayerTask slayerTask;
	
	public int petId;
	
	// Godwars Killcount
	public int ArmadylKC;
	
	public int BandosKC;
	
	public int SaradominKC;
	
	public int ZamorakKC;
	
	@Getter
	@Setter
	private boolean experienceLocked;
	
	private Trade tradeSession;
	
	private int petFollow = -1;
	
	private int temporaryMovementType;
	
	private boolean updateMovementType;
	
	//loyalty
	private int Loyaltypoints;
	
	private int Loyaltytokens;
	
	//Ticket system
	private int Ticketcount;
	
	// saving stuff
	private String password;
	
	private int rights;
	
	private String displayName;
	
	private String lastIP;
	
	private Appearence appearence;
	
	private Inventory inventory;
	
	private Equipment equipment;
	
	private Skills skills;
	
	private CombatDefinitions combatDefinitions;
	
	private Prayer prayer;
	
	private Bank bank;
	
	private ControlerManager controlerManager;
	
	private MusicsManager musicsManager;
	
	private EmotesManager emotesManager;
	
	private FriendsIgnores friendsIgnores;
	
	private Familiar familiar;
	
	private AuraManager auraManager;
	
	private byte runEnergy;
	
	private boolean allowChatEffects;
	
	private boolean mouseButtons;
	
	private int privateChatSetup;
	
	private int skullDelay;
	
	private int skullId;
	
	private boolean forceNextMapLoadRefresh;
	
	private long poisonImmune;
	
	private long fireImmune;
	
	private int lastVeng;
	
	// interface
	
	private boolean castedVeng;
	
	private int[] pouches;
	
	private long muted;
	
	private long jailed;
	
	private long banned;
	
	private boolean permBanned;
	
	private boolean filterGame;
	
	private ChargesManager charges;
	
	// barrows
	private boolean[] killedBarrowBrothers;
	
	private int hiddenBrother;
	
	private int barrowsKillCount;
	
	private int pestPoints;
	
	// skill capes customizing
	private int[] maxedCapeCustomized;
	
	private int[] completionistCapeCustomized;
	
	private int overloadDelay;
	
	private String currentFriendChatOwner;
	
	private int summoningLeftClickOption;
	
	private List<String> ownedObjectsManagerKeys;
	
	private Pets pet;
	
	private transient boolean finishing;
	
	// transient stuff
	private transient Trade trade;
	
	private transient ClanWars clanWars;
	
	private transient String username;
	
	@Setter
	private transient Session session;
	
	private transient boolean clientLoadedMapRegion;
	
	private transient int displayMode;
	
	private transient int screenWidth;
	
	private transient boolean usingTicket;
	
	private transient int trapAmount;
	
	private transient int screenHeight;
	
	private transient InterfaceManager interfaceManager;
	
	private transient DialogueManager dialogueManager;
	
	private transient HintIconsManager hintIconsManager;
	
	private transient ActionManager actionManager;
	
	private transient CutsceneManager cutsceneManager;
	
	private transient DuelConfigurations duelConfigurations;
	
	private transient PriceCheckManager priceCheckManager;
	
	private transient RouteEvent routeEvent;
	
	public void setRouteEvent(RouteEvent routeEvent) {
		this.routeEvent = routeEvent;
		// so when a route event is set it auto-processes it
		if (routeEvent != null && routeEvent.processEvent(this)) {
			setRouteEvent(null);
		}
	}
	
	private transient FriendChatsManager currentFriendChat;
	
	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	
	private transient LocalNPCUpdate localNPCUpdate;
	
	// player stages
	private transient boolean started;
	
	private transient boolean running;
	
	private transient long packetsDecoderPing;
	
	private transient boolean resting;
	
	private transient boolean canPvp;
	
	private transient long lockDelay; // used for doors and stuff like that
	
	private transient long foodDelay;
	
	private transient long potDelay;
	
	private transient long boneDelay;
	
	private transient Runnable closeInterfacesEvent;
	
	private transient long lastPublicMessage;
	
	private transient long polDelay;
	
	private transient Runnable interfaceListenerEvent;// used for static
	
	private transient List<Integer> switchItemCache;
	
	private transient boolean disableEquip;
	
	public Player(String password) {
		super(GameConstants.START_PLAYER_LOCATION);
		setHitpoints(100);
		this.password = password;
		appearence = new Appearence();
		inventory = new Inventory();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		controlerManager = new ControlerManager();
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		friendsIgnores = new FriendsIgnores();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		pouches = new int[4];
		killedBarrowBrothers = new boolean[6];
		slayerTask = new SlayerTask();
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
		if (getAttackedByDelay() + 10000 > currentTime || getEmotesManager().getNextEmoteEnd() >= currentTime || lockDelay >= currentTime) {
			CoresManager.slowExecutor.schedule(() -> {
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
		lastVeng = 0;
		castedVeng = false;
		setRunEnergy(100);
		appearence.generateAppearenceData();
	}
	
	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 10 + equipment.getEquipmentHpIncrease();
	}
	
	@Override
	public int getSize() {
		return appearence.getSize();
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
			refreshSpawnedObjects();
			refreshSpawnedItems();
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
				appearence.generateAppearenceData();
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
		if (lastVeng > 0) {
			lastVeng--;
			if (lastVeng == 0 && castedVeng) {
				castedVeng = false;
				getPackets().sendGameMessage("Your vengeance has faded.");
			}
		}
		charges.process();
		auraManager.process();
		if (routeEvent != null && routeEvent.processEvent(this)) {
			routeEvent = null;
		}
		actionManager.process();
		prayer.processPrayer();
		controlerManager.process();
	}
	
	@Override
	public void processReceivedHits() {
		if (lockDelay > Misc.currentTimeMillis()) {
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
						List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(playerIndex);
								if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.withinDistance(this, 1) || !target.getControlerManager().canHit(player)) {
									continue;
								}
								player.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(this, 1) || !npc.getDefinitions().hasAttackOption() || !target.getControlerManager().canHit(npc)) {
									continue;
								}
								npc.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead() && !source.hasFinished() && source.withinDistance(this, 1)) {
						source.applyHit(new Hit(target, Misc.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5)), HitLook.REGULAR_DAMAGE));
					}
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				World.sendProjectile(this, new WorldTile(getX() - 2, getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				
				World.sendProjectile(this, new WorldTile(getX(), getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));
						
						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = World.getPlayers().get(playerIndex);
										if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.withinDistance(target, 2) || !target.getControlerManager().canHit(player)) {
											continue;
										}
										player.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(Skills.PRAYER) * 3), HitLook.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
										if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(target, 2) || !npc.getDefinitions().hasAttackOption() || !target.getControlerManager().canHit(npc)) {
											continue;
										}
										npc.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(Skills.PRAYER) * 3), HitLook.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target && !source.isDead() && !source.hasFinished() && source.withinDistance(target, 2)) {
								source.applyHit(new Hit(target, Misc.getRandom(skills.getLevelForXp(Skills.PRAYER) * 3), HitLook.REGULAR_DAMAGE));
							}
						}
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() - 2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() - 2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() - 2, getPlane()));
						
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() - 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() - 1, getPlane()));
					}
				});
			}
		}
		setNextAnimation(new Animation(-1));
		if (!controlerManager.sendDeath()) {
			return;
		}
		addLockDelay(7);
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
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE && hit.getLook() != HitLook.MAGIC_DAMAGE) {
			return;
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
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17)) {
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMagePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18)) {
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getRangePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2229));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19)) {
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = source instanceof Nex ? 0 : (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source.getMeleePrayerMultiplier()));
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage, HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
		if (hit.getDamage() >= 200) {
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage() * combatDefinitions.getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaking(new Hit(source, reducedDamage, HitLook.ABSORB_DAMAGE));
				}
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75), HitLook.REGULAR_DAMAGE));
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
						if (hit.getLook() == HitLook.MELEE_DAMAGE) {
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
									World.sendProjectile(p2, this, 2215, 35, 35, 20, 5, 0, 0);
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
										World.sendProjectile(p2, this, 2231, 35, 35, 20, 5, 0, 0);
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
										World.sendProjectile(p2, this, 2248, 35, 35, 20, 5, 0, 0);
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
						if (hit.getLook() == HitLook.RANGE_DAMAGE) {
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
									World.sendProjectile(p2, this, 2218, 35, 35, 20, 5, 0, 0);
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
									World.sendProjectile(p2, this, 2236, 35, 35, 20, 5, 0, 0);
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
						if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
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
									World.sendProjectile(p2, this, 2221, 35, 35, 20, 5, 0, 0);
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
									World.sendProjectile(p2, this, 2240, 35, 35, 20, 5, 0, 0);
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
								World.sendProjectile(p2, this, 2244, 35, 35, 20, 5, 0, 0);
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
								World.sendProjectile(p2, this, 2256, 35, 35, 20, 5, 0, 0);
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
									combatDefinitions.desecreaseSpecialAttack(10);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2252, 35, 35, 20, 5, 0, 0);
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
									combatDefinitions.desecreaseSpecialAttack(10);
								}
								World.sendProjectile(p2, this, 2224, 35, 35, 20, 5, 0, 0);
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
			sendRunButtonConfig();
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
	
	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : getRun() ? 1 : 0);
	}
	
	public void sendSoulSplit(final Hit hit, final Actor user) {
		final Player target = this;
		if (hit.getDamage() > 0) {
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		}
		user.heal(hit.getDamage() / 5);
		prayer.drainPrayer(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0) {
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				}
			}
		}, 1);
	}
	
	public byte getRunEnergy() {
		return runEnergy;
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
	
	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<WorldObject> spawnedObjects = World.getRegion(regionId).getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects) {
					if (object.getPlane() == getPlane()) {
						getPackets().sendSpawnedObject(object);
					}
				}
			}
			List<WorldObject> removedObjects = World.getRegion(regionId).getRemovedObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects) {
					if (object.getPlane() == getPlane()) {
						getPackets().sendDestroyObject(object);
					}
				}
			}
		}
	}
	
	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && this != item.getOwner() || item.getTile().getPlane() != getPlane()) {
					continue;
				}
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && this != item.getOwner() || item.getTile().getPlane() != getPlane()) {
					continue;
				}
				getPackets().sendGroundItem(item);
			}
		}
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
	
	public void sendMessage(String message) {
		getPackets().sendGameMessage(message);
	}
	
	public void init(String string, Session session) {
		username = string;
		this.session = session;
		System.out.println("Inited Player: " + string + ", pass: " + password);
	}
	
	public void init(String username, int displayMode, int screenWidth, int screenHeight) {
		// temporary deleted after reset all chars
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		slayerTask = new SlayerTask();
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		cutsceneManager = new CutsceneManager(this);
		// loads player on saved instances
		appearence.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		controlerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		setDirection(Misc.getFaceDirection(0, -1));
		switchItemCache = Collections.synchronizedList(new ArrayList<Integer>());
		initEntity();
		packetsDecoderPing = Misc.currentTimeMillis();
		// inited so lets add it
		World.addPlayer(this);
		World.updateEntityRegion(this);
		System.out.println("Player Logged in: " + username);
	}
	
	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateAppearenceData();
	}
	
	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
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
	
	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if (update) {
			sendRunButtonConfig();
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
		if (World.exiting_start != 0) {
			int delayPassed = (int) ((Misc.currentTimeMillis() - World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay - delayPassed);
		}
		getPackets().sendGameMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
		lastIP = getSession().getIp();
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshPrivateChatSetup();
		sendRunButtonConfig();
		getEmotesManager().refreshListConfigs();
		sendDefaultPlayersOptions();
		checkMultiArea();
		inventory.init();
		equipment.init();
		skills.init();
		combatDefinitions.init();
		prayer.init();
		friendsIgnores.init();
		Notes.sendUnlockNotes(this);
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
		
		// Checks for pets.
		if (pet != null) {
			pet.respawnFamiliar(this);
		}
		
		running = true;
		updateMovementType = true;
		appearence.generateAppearenceData();
		controlerManager.login(); // checks what to do on login after welcome "Log in"
		OwnedObjectManager.linkKeys(this);
	}
	
	public void sendDefaultPlayersOptions() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 3, false);
		getPackets().sendPlayerOption("Req Assist", 4, false);
	}
	
	public void logout() {
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
		if (lockDelay >= currentTime) {
			getPackets().sendGameMessage("You can't log out while perfoming an action.");
			return;
		}
		getPackets().sendLogout();
		running = false;
	}
	
	public EmotesManager getEmotesManager() {
		return emotesManager;
	}
	
	public void realFinish() {
		if (hasFinished()) {
			return;
		}
		stopAll();
		cutsceneManager.logout();
		controlerManager.logout(); // checks what to do on before logout for
		// login
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (currentFriendChat != null) {
			currentFriendChat.leaveChat(this, true);
		}
		if (familiar != null) {
			familiar.dissmissFamiliar(true);
		}
		setFinished(true);
		session.setDecoder(-1);
		SerializableFilesManager.savePlayer(this);
		World.updateEntityRegion(this);
		World.removePlayer(this);
		System.out.println("Finished Player: " + username + ", pass: " + password);
	}
	
	public Pets getPet() {
		return pet;
	}
	
	public void setPet(Pets pets) {
		this.pet = pets;
		
	}
	
	public int getPetId() {
		return petId;
	}
	
	public void setPetId(int petId) {
		this.petId = petId;
	}
	
	public int getPetFollow() {
		return petFollow;
	}
	
	public void setPetFollow(int petFollow) {
		this.petFollow = petFollow;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getMessageIcon() {
		return getRights() == 2 || getRights() == 1 ? getRights() : getRights();
	}
	
	public int getRights() {
		return rights;
	}
	
	public void setRights(int rights) {
		this.rights = rights;
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
	
	public Appearence getAppearence() {
		return appearence;
	}
	
	public Equipment getEquipment() {
		return equipment;
	}
	
	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}
	
	public int getDisplayMode() {
		return displayMode;
	}
	
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
	
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}
	
	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}
	
	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}
	
	public Session getSession() {
		return session;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Skills getSkills() {
		return skills;
	}
	
	public void drainRunEnergy() {
		setRunEnergy(runEnergy - 1);
	}
	
	public boolean isResting() {
		return resting;
	}
	
	//	public void increaseKillCount(Player killed) {
	//		killed.deathCount++;
	//		if (killed.getSession().getIP().equals(getSession().getIP()))
	//			return;
	//		killCount++;
	//		getPackets().sendGameMessage(
	//				"<col=ff0000>You have killed " + killed.getDisplayName()
	//				+ ", you have now " + killCount + " kills.");
	//		PkRank.checkRank(this);
	//	}
	
	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}
	
	public ActionManager getActionManager() {
		return actionManager;
	}
	
	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}
	
	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}
	
	public void sendItemsOnDeath(Player killer) {
		if (rights == 2) {
			return;
		}
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
		/*
		 * for (Item item : containedItems) { if (item != null) { for (String
		 * string : Settings.DONATOR_ITEMS) { if
		 * (item.getDefinitions().getName().toLowerCase() .contains(string)) {
		 * containedItems.remove(item); } } } }
		 */
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
				int price = item.getDefinitions().getValue(item.getId());
				if (price >= lastItem.getDefinitions().getValue(item.getId())) {
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
			if (getRights() == 7) {
				return;
			}
			World.addGroundItem(item, getLastWorldTile(), killer, true, 180, true);
		}
	}
	
	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Misc.getRandom(6)) {
			case 0:
				p.setNextWorldTile(new WorldTile(3014, 3195, 0));
				break;
			case 1:
				p.setNextWorldTile(new WorldTile(3015, 3189, 0));
				break;
			case 2:
				p.setNextWorldTile(new WorldTile(3014, 3189, 0));
				break;
			case 3:
				p.setNextWorldTile(new WorldTile(3014, 3192, 0));
				break;
			case 4:
				p.setNextWorldTile(new WorldTile(3018, 3180, 0));
				break;
			case 5:
				p.setNextWorldTile(new WorldTile(3018, 3189, 0));
				break;
			case 6:
				p.setNextWorldTile(new WorldTile(3018, 3189, 0));
				break;
		}
	}
	
	public boolean isCanPvp() {
		return canPvp;
	}
	
	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
	}
	
	public Prayer getPrayer() {
		return prayer;
	}
	
	public long getLockDelay() {
		return lockDelay;
	}
	
	public void setInfiniteStopDelay() {
		lockDelay = Long.MAX_VALUE;
	}
	
	public void resetLockDelay() {
		lockDelay = 0;
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
		stopAll();
		addLockDelay(totalDelay);
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
	
	public void stopAll() {
		stopAll(true);
	}
	
	public void addLockDelay(long delay) {
		lockDelay = Misc.currentTimeMillis() + (delay * 600);
	}
	
	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}
	
	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces) {
		if (getTrade() != null) {
			return;
		}
		routeEvent = null;
		if (stopInterfaces && getTradeSession() == null) {
			closeInterfaces();
		}
		if (stopWalk) {
			resetWalkSteps();
		}
		actionManager.forceStop();
		combatDefinitions.resetSpells(false);
		setNextFaceActor(null);
	}
	
	public Trade getTrade() {
		return trade;
	}
	
	public Trade getTradeSession() {
		return tradeSession;
		
	}
	
	public void closeInterfaces() {
		if (getTrade() != null) {
			return;
		}
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
	
	public void setTradeSession(Trade session2) {
		this.tradeSession = session2;
		
	}
	
	public Bank getBank() {
		return bank;
	}
	
	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}
	
	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}
	
	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}
	
	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}
	
	public void refreshPrivateChatSetup() {
		getPackets().sendConfig(287, privateChatSetup);
	}
	
	public int getPrivateChatSetup() {
		return privateChatSetup;
	}
	
	public void setPrivateChatSetup(int privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
	}
	
	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}
	
	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}
	
	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}
	
	public void addPotDelay(long time) {
		potDelay = time + Misc.currentTimeMillis();
	}
	
	public long getPotDelay() {
		return potDelay;
	}
	
	public void addFoodDelay(long time) {
		foodDelay = time + Misc.currentTimeMillis();
	}
	
	public long getFoodDelay() {
		return foodDelay;
	}
	
	public long getBoneDelay() {
		return boneDelay;
	}
	
	public void addBoneDelay(long time) {
		boneDelay = time + Misc.currentTimeMillis();
	}
	
	public void addPoisonImmune(long time) {
		poisonImmune = time + Misc.currentTimeMillis();
		getPoisonManager().reset();
	}
	
	public long getPoisonImmune() {
		return poisonImmune;
	}
	
	public void addFireImmune(long time) {
		fireImmune = time + Misc.currentTimeMillis();
	}
	
	public long getFireImmune() {
		return fireImmune;
	}
	
	public MusicsManager getMusicsManager() {
		return musicsManager;
	}
	
	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}
	
	public int getLastVeng() {
		return lastVeng;
	}
	
	public void setLastVeng(int lastVeng) {
		this.lastVeng = lastVeng;
	}
	
	public boolean isCastVeng() {
		return castedVeng;
	}
	
	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}
	
	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}
	
	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}
	
	public int getDeathCount() {
		return deathCount;
	}
	
	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}
	
	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}
	
	public void setInterfaceListenerEvent(Runnable listener) {
		this.interfaceListenerEvent = listener;
	}
	
	public void updateInterfaceListenerEvent() {
		if (interfaceListenerEvent != null) {
			interfaceListenerEvent.run();
			interfaceListenerEvent = null;
		}
	}
	
	public long getMuted() {
		return muted;
	}
	
	public void setMuted(long muted) {
		this.muted = muted;
	}
	
	public long getJailed() {
		return jailed;
	}
	
	public void setJailed(long jailed) {
		this.jailed = jailed;
	}
	
	public boolean isPermBanned() {
		return permBanned;
	}
	
	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}
	
	public long getBanned() {
		return banned;
	}
	
	public void setBanned(long banned) {
		this.banned = banned;
	}
	
	public ChargesManager getCharges() {
		return charges;
	}
	
	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}
	
	public boolean[] setKilledBarrowBrothers(boolean[] b) {
		return this.killedBarrowBrothers = b;
	}
	
	public int getHiddenBrother() {
		return hiddenBrother;
	}
	
	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}
	
	public int[] getPouches() {
		return pouches;
	}
	
	public String getLastIP() {
		return lastIP;
	}
	
	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}
	
	public DuelConfigurations getDuelConfigurations() {
		return duelConfigurations;
	}
	
	public DuelConfigurations setDuelConfigurations(DuelConfigurations duelConfigurations) {
		return this.duelConfigurations = duelConfigurations;
	}
	
	public boolean isDueling() {
		return duelConfigurations != null;
	}
	
	public int getPestPoints() {
		return pestPoints;
	}
	
	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}
	
	public boolean isUpdateMovementType() {
		return updateMovementType;
	}
	
	public long getLastPublicMessage() {
		return lastPublicMessage;
	}
	
	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}
	
	public CutsceneManager getCutsceneManager() {
		return cutsceneManager;
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
			List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
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
	
	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}
	
	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}
	
	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}
	
	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}
	
	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
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
	
	public int getSkullId() {
		return skullId;
	}
	
	public boolean isFilterGame() {
		return filterGame;
	}
	
	public void setFilterGame(boolean filterGame) {
		this.filterGame = filterGame;
	}
	
	public int getOverloadDelay() {
		return overloadDelay;
	}
	
	public void setOverloadDelay(int overloadDelay) {
		this.overloadDelay = overloadDelay;
	}
	
	public Trade setTrade(Trade trade) {
		return this.trade = trade;
	}
	
	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null) {
			return 0;
		}
		return teleblock;
	}
	
	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked", teleDelay + Misc.currentTimeMillis());
	}
	
	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null) {
			return 0;
		}
		return teleblock;
	}
	
	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked", teleDelay + Misc.currentTimeMillis());
		prayer.closeAllPrayers();
	}
	
	public Familiar getFamiliar() {
		return familiar;
	}
	
	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	
	public int getLoyaltyPoints() {
		return Loyaltypoints;
	}
	
	public void setLoyaltyPoints(int Loyaltypoints) {
		this.Loyaltypoints = Loyaltypoints;
	}
	
	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}
	
	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}
	
	public ControlerManager getControlerManager() {
		return controlerManager;
	}
	
	public int getTrapAmount() {
		return trapAmount;
	}
	
	public void setTrapAmount(int trapAmount) {
		this.trapAmount = trapAmount;
	}
	
	public long getPolDelay() {
		return polDelay;
	}
	
	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}
	
	public void teleportPlayer(int x, int y, int z) {
		setNextWorldTile(new WorldTile(x, y, z));
		stopAll();
	}
	
	public boolean isUsingTicket() {
		return usingTicket;
	}
	
	public void setUsingTicket(boolean usingTicket) {
		this.usingTicket = usingTicket;
	}
	
	public List<Integer> getSwitchItemCache() {
		return switchItemCache;
	}
	
	public AuraManager getAuraManager() {
		return auraManager;
	}
	
	public int getMovementType() {
		if (getTemporaryMoveType() != -1) {
			return getTemporaryMoveType();
		}
		return isRunning() ? RUN_MOVE_TYPE : WALK_MOVE_TYPE;
	}
	
	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}
	
	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
		{
			ownedObjectsManagerKeys = new LinkedList<>();
		}
		return ownedObjectsManagerKeys;
	}
	
	public ClanWars getClanWars() {
		return clanWars;
	}
	
	public ClanWars setClanWars(ClanWars clanWars) {
		return this.clanWars = clanWars;
	}
	
	public boolean hasInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombatAction.getSpecialAmmount(weaponId);
		if (combatDefinitions.hasRingOfVigour()) {
			specAmt *= 0.9;
		}
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return false;
		}
		switch (weaponId) {
			case 4153:
				if (getTemporaryAttributtes().get("InstantSpecial") == null) {
					getTemporaryAttributtes().put("InstantSpecial", 4153);
				} else {
					getTemporaryAttributtes().remove("InstantSpecial");
				}
				combatDefinitions.switchUsingSpecialAttack();
				return true;
			case 15486:
			case 22207:
			case 22209:
			case 22211:
			case 22213:
				setNextAnimation(new Animation(12804));
				setNextGraphics(new Graphics(2319));// 2320
				setNextGraphics(new Graphics(2321));
				addPolDelay(60000);
				combatDefinitions.desecreaseSpecialAttack(specAmt);
				return true;
			case 1377:
			case 13472:
				setNextAnimation(new Animation(1056));
				setNextGraphics(new Graphics(246));
				setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
				int defence = (int) (skills.getLevel(Skills.DEFENCE) * 0.90D);
				int attack = (int) (skills.getLevel(Skills.ATTACK) * 0.90D);
				int range = (int) (skills.getLevel(Skills.RANGE) * 0.90D);
				int magic = (int) (skills.getLevel(Skills.MAGIC) * 0.90D);
				int strength = (int) (skills.getLevel(Skills.STRENGTH) * 1.2D);
				skills.set(Skills.DEFENCE, defence);
				skills.set(Skills.ATTACK, attack);
				skills.set(Skills.RANGE, range);
				skills.set(Skills.MAGIC, magic);
				skills.set(Skills.STRENGTH, strength);
				combatDefinitions.desecreaseSpecialAttack(specAmt);
				return true;
			case 35:// Excalibur
			case 8280:
			case 14632:
				setNextAnimation(new Animation(1168));
				setNextGraphics(new Graphics(247));
				setNextForceTalk(new ForceTalk("For ZENITH!"));
				final boolean enhanced = weaponId == 14632;
				skills.set(Skills.DEFENCE, enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D) : (skills.getLevel(Skills.DEFENCE) + 8));
				WorldTasksManager.schedule(new WorldTask() {
					int count = 5;
					
					@Override
					public void run() {
						if (isDead() || hasFinished() || getHitpoints() >= getMaxHitpoints()) {
							stop();
							return;
						}
						heal(enhanced ? 80 : 40);
						if (count-- == 0) {
							stop();
							return;
						}
					}
				}, 4, 2);
				combatDefinitions.desecreaseSpecialAttack(specAmt);
				return true;
		}
		return false;
	}
	
	public void addPolDelay(long delay) {
		polDelay = delay + Misc.currentTimeMillis();
	}
	
	public void resetMessageAmount() {
		getTemporaryAttributtes().put("Message", 0);
	}
	
	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}
	
	public boolean isEquipDisabled() {
		return disableEquip;
	}
	
	public War getOwnedWar() {
		return (getCurrentFriendChatOwner() != null && getCurrentFriendChatOwner().equalsIgnoreCase(getUsername()) && getCurrentFriendChat().getWar() != null) ? getCurrentFriendChat().getWar() : null;
	}
	
	public String getCurrentFriendChatOwner() {
		return currentFriendChatOwner;
	}
	
	public String getUsername() {
		return username;
	}
	
	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	public FriendChatsManager getCurrentFriendChat() {
		return currentFriendChat;
	}
	
	public void setCurrentFriendChat(FriendChatsManager currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}
	
	public void setCurrentFriendChatOwner(String currentFriendChatOwner) {
		this.currentFriendChatOwner = currentFriendChatOwner;
	}
	
	public void NonDonatorReset() {
		setHitpoints(getMaxHitpoints());
		prayer.reset();
	}
	
	public void DonatorReset() {
		setHitpoints(getMaxHitpoints());
		prayer.reset();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		setRunEnergy(100);
	}
	
}