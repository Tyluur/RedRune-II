package com.rs.game.entity.actor.npc;

import com.rs.cache.Cache;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.content.controller.impl.activity.Wilderness;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitLook;
import com.rs.game.entity.actor.npc.combat.NPCCombat;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.mask.Transformation;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.world.World;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;
import com.rs.utility.Misc.Direction;
import com.rs.utility.constants.NPCConstants;
import com.rs.utility.repo.npc.characteristic.NPCCharacteristicRepository;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NPC extends Actor implements Serializable {
	
	public static int NO_WALK = 0x0, NORMAL_WALK = 0x2, WATER_WALK = 0x4, FLY_WALK = 0x8;
	
	private static final long serialVersionUID = -4794678936277614443L;
	
	private int id;
	
	private WorldTile respawnTile;
	
	private int mapAreaNameHash;
	
	private boolean canBeAttackFromOutOfArea;
	
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab
	
	// def, blahblah till 9
	private boolean spawned;
	
	private WorldTile forceWalk;
	
	private long lastAttackedByTarget;
	
	private boolean cantInteract;
	
	private int capDamage;
	
	private int lureDelay;
	
	private boolean cantFollowUnderCombat;
	
	private boolean forceAgressive;
	
	private int forceTargetDistance;
	
	private boolean forceFollowClose;
	
	private boolean forceMultiAttacked;
	
	// name changing masks
	private String name;
	
	private int combatLevel;
	
	@Getter
	@Setter
	private int walkType;
	
	private transient NPCCombat combat;
	
	// npc masks
	private transient Transformation nextTransformation;
	
	private transient boolean changedName;
	
	private transient boolean changedCombatLevel;
	
	public NPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}
	
	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.spawned = spawned;
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(Direction.NORTH.getValue());
		setWalkType(getDefinitions().getWalkMask());
		bonuses = NPCCharacteristicRepository.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}
	
	public void setRandomWalk(boolean forceRandomWalk) {
		setWalkType(forceRandomWalk ? NORMAL_WALK : NO_WALK);
	}
	
	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}
	
	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCharacteristicRepository.getCombatDefinitions(id);
	}
	
	public static void main(String[] args) throws IOException {
		Cache.init();
	}
	
	@Override
	public NPC toNPC() {
		return this;
	}
	
	@Override
	public void finish() {
		if (hasFinished()) {
			return;
		}
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0;
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0;
	}
	
	public boolean isFamiliar() {
		return this instanceof Familiar;
	}
	
	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCCharacteristicRepository.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}
	
	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}
	
	@Override
	public int getSize() {
		return getDefinitions().getSize();
	}
	
	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextTransformation != null || changedCombatLevel || changedName;
	}
	
	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
	}
	
	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}
	
	public void processNPC() {
		if (isDead()) {
			return;
		}
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Misc.currentTimeMillis()) {
							if (((getWalkType() & NORMAL_WALK) != 0) && Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (!withinDistanceFromSpawn()) {
									forceWalkRespawnTile();
									return;
								}
								addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		if (isForceWalking()) {
			if (getFreezeDelay() < Misc.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps()) {
						addWalkSteps(forceWalk.getX(), forceWalk.getY(), getSize(), true);
					}
					if (!hasWalkSteps()) {
						setNextWorldTile(new WorldTile(forceWalk));
						forceWalk = null;
					}
				} else {
					forceWalk = null;
				}
			}
		}
	}
	
	public boolean isForceWalking() {
		return forceWalk != null;
	}
	
	public boolean checkAgressivity() {
		// if(!(Wilderness.isAtWild(this) &&
		// getDefinitions().hasAttackOption())) {
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAggressivenessType() == NPCConstants.PASSIVE) {
				return false;
			}
		}
		// }
		ArrayList<Actor> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Actor target = possibleTarget.get(Misc.getRandom(possibleTarget.size() - 1));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Misc.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}
	
	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}
	
	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}
	
	public ArrayList<Actor> getPossibleTargets() {
		ArrayList<Actor> possibleTarget = new ArrayList<Actor>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null || player.isDead() || player.hasFinished() || !player.isRunning() || player.getAppearance().isHidden() || !Misc.isInRange(getX(), getY(), getSize(), player.getX(), player.getY(), player.getSize(), forceTargetDistance > 0 ? forceTargetDistance : getCombatDefinitions().getAttackStyle() == NPCConstants.SPECIAL ? 64 : 8) || (!forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea()) && (player.getAttackedBy() != this && (player.getAttackedByDelay() > Misc.currentTimeMillis() || player.getFindTargetDelay() > Misc.currentTimeMillis()))) || !clipedProjectile(player, false) || (!forceAgressive && !Wilderness.isAtWild(this) && player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2)) {
						continue;
					}
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}
	
	public void setTarget(Actor actor) {
		if (isForceWalking()) // if force walk not gonna get target
		{
			return;
		}
		combat.setTarget(actor);
		lastAttackedByTarget = Misc.currentTimeMillis();
	}
	
	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}
	
	@Override
	public void sendDeath(Actor source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathAnim()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					reset();
					setLocation(respawnTile);
					finish();
					if (getId() == 1265) {
						setRespawnTask();
					}
					if (!spawned) {
						setRespawnTask();
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage) {
			hit.setDamage(capDamage);
		}
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE && hit.getLook() != HitLook.MAGIC_DAMAGE) {
			return;
		}
		Actor source = hit.getSource();
		if (source == null) {
			return;
		}
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) {
					final NPC target = this;
					if (hit.getDamage() > 0) {
						World.sendProjectile(p2, this, 2263, 11, 11, 20, 5, 0, 0);
					}
					p2.heal(hit.getDamage() / 5);
					p2.getPrayer().drainPrayer(hit.getDamage() / 5);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							setNextGraphics(new Graphics(2264));
							if (hit.getDamage() > 0) {
								World.sendProjectile(target, p2, 2263, 11, 11, 20, 5, 0, 0);
							}
						}
					}, 1);
				}
				if (hit.getDamage() == 0) {
					return;
				}
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
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
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
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
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets().sendGameMessage("Your curse drains Strength from the enemy, boosting your Strength.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
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
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2218, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
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
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2221, 35, 35, 20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
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
					
					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Misc.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets().sendGameMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
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
				}
			}
		}
		
	}
	
	@Override
	public void setAttackedBy(Actor target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget() && !(combat.getTarget() instanceof Familiar)) {
			lastAttackedByTarget = Misc.currentTimeMillis();
		}
	}
	
	@Override
	public String toString() {
		return "NPC{" + "id=" + id + ", tile=" + getWorldTile() + ", name='" + getName() + '\'' + '}';
	}
	
	public String getName() {
		return name != null ? name : getDefinitions().getName();
	}
	
	public void setName(String string) {
		this.name = getDefinitions().getName().equals(string) ? null : string;
		changedName = true;
	}
	
	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 == 0 || definitions.getRespawnDirection() <= 0 || definitions.getRespawnDirection() > 8) {
			return 0;
		}
		return (4 + definitions.getRespawnDirection()) << 11;
	}
	
	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}
	
	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCCharacteristicRepository.getBonuses(id);
	}
	
	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}
	
	public int getId() {
		return id;
	}
	
	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		CoresManager.slowExecutor.schedule(() -> {
			try {
				spawn();
			} catch (Exception | Error e) {
				e.printStackTrace();
			}
		}, getId() == 1265 ? 500 : getCombatDefinitions().getRespawnDelay() * 600, TimeUnit.MILLISECONDS);
	}
	
	public void deserialize() {
		if (combat == null) {
			combat = new NPCCombat(this);
		}
		spawn();
	}
	
	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}
	
	public NPCCombat getCombat() {
		return combat;
	}
	
	public void drop() {
		List<Drop> drops = NPCCharacteristicRepository.getDrops(id);
		if (drops == null) {
			return;
		}
		Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null) {
			return;
		}
		if (killer.slayerTask.getTaskMonstersLeft() > 0) {
			for (String m : killer.slayerTask.getCurrentTask().slayable) {
				if (getDefinitions().getName().equals(m)) {
					killer.slayerTask.onMonsterDeath(killer, this);
					break;
				}
			}
		}
		Drop[] possibleDrops = new Drop[drops.size()];
		int possibleDropsCount = 0;
		for (Drop drop : drops) {
			if (drop.getRate() == 100) {
				sendDrop(killer, drop);
			} else {
				if ((Misc.getRandomDouble(99) + 1) <= drop.getRate() * 1.5) {
					possibleDrops[possibleDropsCount++] = drop;
				}
			}
		}
		if (possibleDropsCount > 0) {
			sendDrop(killer, possibleDrops[Misc.getRandom(possibleDropsCount - 1)]);
		}
	}
	
	public void sendDrop(Player player, Drop drop) {
		int size = getSize();
		World.addGroundItem(new Item(drop.getItemId(), drop.getMinAmount() + Misc.getRandom(drop.getExtraAmount())), new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), player, false, 180, true);
	}
	
	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}
	
	public int[] getBonuses() {
		return bonuses;
	}
	
	public WorldTile getRespawnTile() {
		return respawnTile;
	}
	
	public boolean isUnderCombat() {
		return combat.underCombat();
	}
	
	public boolean canBeAttackedByAutoRelatie() {
		return Misc.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}
	
	public void removeTarget() {
		if (combat.getTarget() == null) {
			return;
		}
		combat.removeTarget();
	}
	
	public boolean hasForceWalk() {
		return forceWalk != null;
	}
	
	public boolean isCantInteract() {
		return cantInteract;
	}
	
	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract) {
			combat.reset();
		}
	}
	
	public int getCapDamage() {
		return capDamage;
	}
	
	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}
	
	public int getLureDelay() {
		return lureDelay;
	}
	
	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}
	
	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}
	
	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}
	
	public Transformation getNextTransformation() {
		return nextTransformation;
	}
	
	public boolean isForceAgressive() {
		return forceAgressive;
	}
	
	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}
	
	public int getForceTargetDistance() {
		return forceTargetDistance;
	}
	
	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}
	
	public boolean isForceFollowClose() {
		return forceFollowClose;
	}
	
	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}
	
	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}
	
	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}
	
	public String getCustomName() {
		return name;
	}
	
	public int getCustomCombatLevel() {
		return combatLevel;
	}
	
	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().getCombatLevel();
	}
	
	public void setCombatLevel(int level) {
		combatLevel = getDefinitions().getCombatLevel() == level ? -1 : level;
		changedCombatLevel = true;
	}
	
	public boolean hasChangedName() {
		return changedName;
	}
	
	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}
	
	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane());
	}
	
	public boolean withinDistanceFromSpawn() {
		return withinDistance(respawnTile, 16);
	}
	
	public boolean withinDistanceFromSpawn(int distance) {
		return withinDistance(this, distance);
	}
}
