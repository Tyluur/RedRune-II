package game.entity.actor;

import cache.codec.loaders.AnimationDefinitions;
import cache.codec.loaders.ObjectDefinitions;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.content.entity.actor.combat.function.Magic;
import game.entity.Entity;
import game.entity.actor.link.InteractionManager;
import game.entity.actor.link.PoisonManager;
import game.entity.actor.lock.ActionLocks;
import game.entity.actor.mask.*;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.impl.familiar.Familiar;
import game.entity.actor.player.Player;
import game.entity.object.WorldObject;
import game.global.World;
import game.global.WorldTile;
import game.global.map.region.DynamicRegion;
import game.global.map.region.RegionManager;
import game.global.map.route.RouteFinder;
import game.global.map.route.strategy.ActorStrategy;
import game.global.map.route.strategy.FixedTileStrategy;
import game.global.map.route.strategy.ObjectStrategy;
import utility.constants.GameConstants;
import utility.constants.MagicConstants;
import utility.constants.NetworkConstants;
import utility.constants.SkillConstants;
import utility.functions.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public abstract class Actor extends WorldTile implements Entity {
	
	private static final long serialVersionUID = -3372926325008880753L;
	
	public abstract void finish();
	
	/**
	 * The prayer multiplier for magic combat
	 */
	public abstract double getMagePrayerMultiplier();
	
	/**
	 * The prayer multiplier for ranged combat
	 */
	public abstract double getRangePrayerMultiplier();
	
	/**
	 * The prayer multiplier for melee combat
	 */
	public abstract double getMeleePrayerMultiplier();
	
	/**
	 * The hitpoints of the actor
	 */
	private int hitpoints;
	
	/**
	 * The size of the map of the actor
	 */
	private int mapSize;
	
	/**
	 * The handler and container of poison
	 */
	private final PoisonManager poisonManager;
	
	/**
	 * If run mode is on, this is used for processing movement in a 2-tile-per-step fashion
	 */
	private boolean runModeOn;
	
	/**
	 * The index of the actor
	 */
	private transient int index;
	
	/**
	 * The faceDirection to face
	 */
	private transient int faceDirection;
	
	/**
	 * The id of the last region the actor was in
	 */
	private transient int lastRegionId;
	
	/**
	 * When {@link #loadMapRegions()} is called, this value is updated with a new instance of a world tile
	 */
	private transient WorldTile lastLoadedMapRegionTile;
	
	/**
	 * A list of all the region ids the actor is in
	 */
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds;
	
	/**
	 * The last world tile this actor was at, in regards to walking
	 */
	private transient WorldTile lastWorldTile;
	
	/**
	 * The next world tile this actor will be at, in regards to teleporting
	 */
	private transient WorldTile nextWorldTile;
	
	/**
	 * The next direction in the walk block for the walk step
	 */
	private transient int nextWalkDirection;
	
	/**
	 * The next direction in the walk block for the run step
	 */
	private transient int nextRunDirection;
	
	/**
	 * The next tile we should face
	 */
	private transient WorldTile nextFaceWorldTile;
	
	/**
	 * The boolean used for synchronizing teleportation
	 */
	private transient boolean teleported;
	
	/**
	 * The temporary attributes of this actor
	 */
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes;
	
	/**
	 * The manager for all actor-actor interactions
	 */
	private transient InteractionManager interactionManager;
	
	/**
	 * The action locks.
	 */
	private transient ActionLocks locks;
	
	/**
	 * The steps to walk to, these are called by more than 1 thread so they must be concurrently modifiable
	 */
	private transient ConcurrentLinkedQueue<int[]> walkSteps;
	
	/**
	 * The hits the actor has received, each time a hit is received, the hit is stored here
	 */
	private transient ConcurrentLinkedQueue<Hit> receivedHits;
	
	/**
	 * A collection of actors and the amount of damage they have done
	 */
	private transient ConcurrentHashMap<Actor, Integer> receivedDamage;
	
	/**
	 * If this actor has been finished, in regards to their existence in the game world
	 */
	private transient boolean finished;
	
	/**
	 * The next animation we should perform
	 */
	private transient Animation nextAnimation;
	
	/**
	 * One of the four next graphics to perform
	 */
	private transient Graphics nextGraphics1, nextGraphics2, nextGraphics3, nextGraphics4;
	
	/**
	 * The next hits to show in the player updating sequence, this is used for when there are too many hits on the
	 * player at the current moment and more should appear
	 */
	private transient ArrayList<Hit> nextHits;
	
	/**
	 * The next force movement mask
	 */
	private transient ForceMovement nextForceMovement;
	
	/**
	 * The force talk mask
	 */
	private transient ForceTalk nextForceTalk;
	
	/**
	 * The actor this actor should face next, -2 and -1 mean none
	 */
	private transient int nextFaceEntity;
	
	/**
	 * The actor this actor faced last
	 */
	private transient int lastFaceEntity;
	
	/**
	 * The actor who last attacked us
	 */
	private transient Actor attackedBy;
	
	/**
	 * The last time we were attacked is stored here, this is used to calculate when actions can next be done
	 */
	private transient long attackedByDelay;
	
	/**
	 * If this actor is in a multi area
	 */
	private transient boolean inMultiArea;
	
	/**
	 * If this actor is at a dynamic region
	 */
	private transient boolean atDynamicRegion;
	
	/**
	 * The time the last animation we performed should've ended at
	 */
	private transient long lastAnimationEnd;
	
	/**
	 * If we should force the current area we are at to appear as a multi area for us only
	 */
	private transient boolean forceMultiArea;
	
	/**
	 * How long the actor is frozen for
	 */
	private transient long freezeDelay;
	
	/**
	 * The time that we are not allowed to be frozen again, tihs is a gap between being frozen and the next time we can
	 * be frozen
	 */
	private transient long frozenBlocked;
	
	/**
	 * The delay until the actor [npc only] can find its next target
	 */
	private transient long findTargetDelay;
	
	// creates Entity and saved classes
	public Actor(WorldTile tile) {
		super(tile);
		poisonManager = new PoisonManager();
	}
	
	public boolean isFamiliar() {
		return this instanceof Familiar;
	}
	
	public final void initEntity() {
		mapRegionsIds = new CopyOnWriteArrayList<>();
		walkSteps = new ConcurrentLinkedQueue<>();
		receivedHits = new ConcurrentLinkedQueue<>();
		receivedDamage = new ConcurrentHashMap<>();
		temporaryAttributes = new ConcurrentHashMap<>();
		interactionManager = new InteractionManager(this);
		locks = new ActionLocks();
		nextHits = new ArrayList<>();
		nextWalkDirection = nextRunDirection - 1;
		lastFaceEntity = -1;
		nextFaceEntity = -2;
		poisonManager.setActor(this);
	}
	
	public void reset() {
		setHitpoints(getMaxHitpoints());
		receivedHits.clear();
		resetCombat();
		walkSteps.clear();
		poisonManager.reset();
		resetReceivedDamage();
		temporaryAttributes.clear();
	}
	
	/**
	 * The maximum hitpoints that this actor can have
	 */
	public abstract int getMaxHitpoints();
	
	public void resetCombat() {
		attackedBy = null;
		attackedByDelay = 0;
		freezeDelay = 0;
	}
	
	public void resetReceivedDamage() {
		receivedDamage.clear();
	}
	
	public void move(WorldTile nextWorldTile) {
		this.nextWorldTile = nextWorldTile;
	}
	
	public void removeDamage(Actor actor) {
		receivedDamage.remove(actor);
	}
	
	public Player getMostDamageReceivedSourcePlayer() {
		Player player = null;
		NPC npc = null;
		Familiar familiar = null;
		int damage = -1;
		for (Actor source : receivedDamage.keySet()) {
			if (source instanceof NPC) {
				npc = (NPC) source;
				if (npc.isFamiliar()) {
					familiar = (Familiar) npc;
				}
			}
			if (familiar == null && npc != null) {
				continue;
			}
			
			Integer d = receivedDamage.get(source);
			if (d == null) {
				receivedDamage.remove(source);
				continue;
			}
			if (familiar != null) {
				d += receivedDamage.get(familiar.getOwner()) != null ? receivedDamage.get(familiar.getOwner()) : 0;
				player = familiar.getOwner();
			} else {
				player = (Player) source;
			}
			if (d > damage) {
				damage = d;
			}
			
		}
		return player;
	}
	
	public boolean addWalkSteps(int destX, int destY) {
		return addWalkSteps(destX, destY, -1);
	}
	
	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount) {
		return addWalkSteps(destX, destY, -1, true);
	}
	
	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		do {
			stepCount++;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
				return false;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
		} while (lastTile[0] != destX || lastTile[1] != destY);
		return true;
	}
	
	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0) {
			return new int[] { getX(), getY() };
		}
		int[] step = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}
	
	// return cliped step
	public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		
		if (check) {
			if (!RegionManager.checkWalkStep(getPlane(), lastX, lastY, dir, getSize())) {
				return false;
			}
			if (this instanceof Player) {
				if (!((Player) this).getControllerManager().checkWalkStep(lastX, lastY, nextX, nextY)) {
					return false;
				}
			}
		}
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}
	
	public abstract int getSize();
	
	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(WorldTile tile, boolean checkClose) {
		return clipedProjectile(tile, checkClose, 1); // size 1 thats arrow
		// size, the tile has to
		// be target center
		// coord not base
	}
	
	/*
	 * returns if cliped
	 */
	public boolean clipedProjectile(WorldTile tile, boolean checkClose, int size) {
		int myX = getX();
		int myY = getY();
		int destX = tile.getX();
		int destY = tile.getY();
		int lastTileX = myX;
		int lastTileY = myY;
		while (true) {
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			int dir = Misc.getMoveDirection(myX - lastTileX, myY - lastTileY);
			if (dir == -1) {
				return false;
			}
			if (checkClose) {
				if (!RegionManager.checkWalkStep(getPlane(), lastTileX, lastTileY, dir, size)) {
					return false;
				}
			} else if (!RegionManager.checkProjectileStep(getPlane(), lastTileX, lastTileY, dir, size)) {
				return false;
			}
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY) {
				return true;
			}
		}
	}
	
	/*
	 * return added all steps
	 */
	public boolean checkWalkStepsInteract(int fromX, int fromY, final int destX, final int destY, int maxStepsCount, int size, boolean calculate) {
		int[] lastTile = new int[] { fromX, fromY };
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;
			
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!checkWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				int[] myT = checkcalculatedStep(myRealX, myRealY, destX, destY, lastTile[0], lastTile[1], size);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
				return true;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}
	
	// return cliped step
	public boolean checkWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		
		return !check || RegionManager.checkWalkStep(getPlane(), lastX, lastY, dir, getSize());
	}
	
	public int[] checkcalculatedStep(int myX, int myY, int destX, int destY, int lastX, int lastY, int size) {
		if (myX < destX) {
			myX++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > size || myX - destX < -1 || myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myX++;
			} else if (!(myX - destX > size || myX - destX < -1 || myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > size || myX - destX < -1 || myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!checkWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > size || myX - destX < -1 || myY - destY > size || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY) {
			return null;
		}
		return new int[] { myX, myY };
	}
	
	public boolean addWalkStepsInteract(int destX, int destY, int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size, calculate);
	}
	
	/*
	 * return added all steps
	 */
	public boolean addWalkStepsInteract(final int destX, final int destY, int maxStepsCount, int sizeX, int sizeY, boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;
			
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY, lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)) {
				return true;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}
	
	public int[] calculatedStep(int myX, int myY, int destX, int destY, int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myX++;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY) {
			return null;
		}
		return new int[] { myX, myY };
	}
	
	public boolean restoreHitPoints() {
		int maxHp = getMaxHitpoints();
		if (hitpoints > maxHp) {
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(1, 5) && Misc.getRandom(100) <= 15) {
					return false;
				}
			}
			hitpoints -= 1;
			return true;
		} else if (hitpoints < maxHp) {
			hitpoints += 1;
			if (this instanceof Player) {
				Player player = (Player) this;
				if (player.getPrayer().usingPrayer(0, 9) && hitpoints < maxHp) {
					hitpoints += 1;
				} else if (player.getPrayer().usingPrayer(0, 26) && hitpoints < maxHp) {
					hitpoints += hitpoints + 4 > maxHp ? maxHp - hitpoints : 4;
				}
				
			}
			return true;
		}
		return false;
	}
	
	public boolean needMasksUpdate() {
		return nextFaceEntity != -2 || nextAnimation != null || nextGraphics1 != null || nextGraphics2 != null || nextGraphics3 != null || nextGraphics4 != null || (nextWalkDirection == -1 && nextFaceWorldTile != null) || !nextHits.isEmpty() || nextForceMovement != null || nextForceTalk != null;
	}
	
	public void resetMasks() {
		nextAnimation = null;
		nextGraphics1 = null;
		nextGraphics2 = null;
		nextGraphics3 = null;
		nextGraphics4 = null;
		if (nextWalkDirection == -1) {
			nextFaceWorldTile = null;
		}
		nextForceMovement = null;
		nextForceTalk = null;
		nextFaceEntity = -2;
		nextHits.clear();
	}
	
	public void processEntity() {
		poisonManager.processPoison();
		processMovement();
		processReceivedHits();
	}
	
	public void processMovement() {
		lastWorldTile = new WorldTile(this);
		if (lastFaceEntity >= 0) {
			Actor target = lastFaceEntity >= 32768 ? World.getPlayers().get(lastFaceEntity - 32768) : World.getNPCs().get(lastFaceEntity);
			if (target != null) {
				faceDirection = Misc.getFaceDirection(target.getCoordFaceX(target.getSize()) - getX(), target.getCoordFaceY(target.getSize()) - getY());
			}
		}
		nextWalkDirection = nextRunDirection = -1;
		if (nextWorldTile != null) {
			int lastPlane = getPlane();
			setLocation(nextWorldTile);
			nextWorldTile = null;
			teleported = true;
			if (this instanceof Player) {
				((Player) this).getAttributes().setTemporaryMovementType(MagicConstants.TELE_MOVE_TYPE);
			}
			RegionManager.updateActorRegion(this);
			if (needMapUpdate()) {
				loadMapRegions();
			} else if (this instanceof Player && lastPlane != getPlane()) {
				((Player) this).getAttributes().setClientHasntLoadedMapRegion();
			}
			resetWalkSteps();
			return;
		}
		teleported = false;
		if (walkSteps.isEmpty()) {
			return;
		}
		if (this instanceof Player) {
			if (((Player) this).getEmotesManager().getNextEmoteEnd() >= Misc.currentTimeMillis()) {
				return;
			}
		}
		nextWalkDirection = getNextWalkStep();
		if (nextWalkDirection != -1) {
			if (this instanceof Player) {
				if (!((Player) this).getControllerManager().canMove(nextWalkDirection)) {
					nextWalkDirection = -1;
					resetWalkSteps();
					return;
				}
			}
			moveLocation(Misc.DIRECTION_DELTA_X[nextWalkDirection], Misc.DIRECTION_DELTA_Y[nextWalkDirection], 0);
			if (runModeOn) {
				if (this instanceof Player && ((Player) this).getAttributes().getRunEnergy() <= 0) {
					setRunModeOn(false);
				} else {
					nextRunDirection = getNextWalkStep();
					if (nextRunDirection != -1) {
						if (this instanceof Player) {
							Player player = (Player) this;
							if (!player.getControllerManager().canMove(nextRunDirection)) {
								nextRunDirection = -1;
								resetWalkSteps();
								return;
							}
							player.getAttributes().drainRunEnergy();
						}
						moveLocation(Misc.DIRECTION_DELTA_X[nextRunDirection], Misc.DIRECTION_DELTA_Y[nextRunDirection], 0);
					} else if (this instanceof Player) {
						((Player) this).getAttributes().setTemporaryMovementType(MagicConstants.WALK_MOVE_TYPE);
					}
				}
			}
		}
		RegionManager.updateActorRegion(this);
		if (needMapUpdate()) {
			loadMapRegions();
		}
	}
	
	public void processReceivedHits() {
		// agility locks movement and interactions, so if they're both locked then hits should queue
		if (isPlayer() && toPlayer().getEmotesManager().getNextEmoteEnd() >= Misc.currentTimeMillis() || (locks.isMovementLocked() && locks.isInteractionLocked())) {
			return;
		}
		Hit hit;
		int count = 0;
		while ((hit = receivedHits.poll()) != null && count++ < 10) {
			processHit(hit);
		}
	}
	
	private boolean needMapUpdate() {
		int lastMapRegionX = lastLoadedMapRegionTile.getChunkX();
		int lastMapRegionY = lastLoadedMapRegionTile.getChunkY();
		int regionX = getChunkX();
		int regionY = getChunkY();
		int size = ((NetworkConstants.MAP_SIZES[mapSize] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size || Math.abs(lastMapRegionY - regionY) >= size;
	}
	
	public void loadMapRegions() {
		mapRegionsIds.clear();
		atDynamicRegion = false;
		int regionX = getChunkX();
		int regionY = getChunkY();
		int mapHash = NetworkConstants.MAP_SIZES[mapSize] >> 4;
		for (int xCalc = (regionX - mapHash) / 8; xCalc <= ((regionX + mapHash) / 8); xCalc++) {
			for (int yCalc = (regionY - mapHash) / 8; yCalc <= ((regionY + mapHash) / 8); yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				if (RegionManager.getRegion(regionId, this instanceof Player) instanceof DynamicRegion) {
					atDynamicRegion = true;
				}
				mapRegionsIds.add(yCalc + (xCalc << 8));
			}
		}
		lastLoadedMapRegionTile = new WorldTile(this); // creates a immutable
		// copy of this
	}
	
	public void resetWalkSteps() {
		walkSteps.clear();
	}
	
	private int getNextWalkStep() {
		int[] step = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}
	
	@Override
	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		super.moveLocation(xOffset, yOffset, planeOffset);
		faceDirection = Misc.getFaceDirection(xOffset, yOffset);
	}
	
	private void processHit(Hit hit) {
		if (isDead()) {
			hit.setDamage(0);
		}
		removeHitpoints(hit);
		nextHits.add(hit);
	}
	
	public boolean isDead() {
		return hitpoints == 0;
	}
	
	public void removeHitpoints(Hit hit) {
		if (isDead() || hit.getSplat() == HitSplat.ABSORB_DAMAGE) {
			return;
		}
		if (hit.getSplat() == HitSplat.HEALED_DAMAGE) {
			heal(hit.getDamage());
			return;
		}
		if (hit.getDamage() > hitpoints) {
			hit.setDamage(hitpoints);
		}
		addReceivedDamage(hit.getSource(), hit.getDamage());
		hitpoints -= hit.getDamage();
		if (hitpoints <= 0) {
			sendDeath(hit.getSource());
		} else if (this instanceof Player) {
			Player player = (Player) this;
			if (player.getEquipment().getRingId() == 2550) {
				if (hit.getSource() != null && hit.getSource() != player && hit.getDamage() > 0 && !hit.getSplat().equals(HitSplat.REFLECTED_DAMAGE)) {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							hit.getSource().applyHit(new Hit(player, (int) (hit.getDamage() * 0.1), HitSplat.REFLECTED_DAMAGE));
						}
					});
				}
			}
			if (player.getPrayer().hasPrayersOn()) {
				if ((hitpoints < player.getMaxHitpoints() * 0.1) && player.getPrayer().usingPrayer(0, 23)) {
					setNextGraphics(new Graphics(436));
					hitpoints += player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 2.5;
					player.getSkills().set(SkillConstants.PRAYER, 0);
					player.getPrayer().setPrayerpoints(0);
				} else if (player.getEquipment().getAmuletId() != 11090 && player.getEquipment().getRingId() == 11090 && player.getHitpoints() <= player.getMaxHitpoints() * 0.1) {
					Magic.sendNormalTeleportSpell(player, 1, 0, GameConstants.RESPAWN_TILE);
					player.getEquipment().deleteItem(11090, 1);
					player.getPackets().sendMessage("Your ring of life saves you, but is destroyed in the process.");
				}
			}
			if (player.getEquipment().getAmuletId() == 11090 && player.getHitpoints() <= player.getMaxHitpoints() * 0.2) {// priority
				// over
				// ring
				// of
				// life
				player.heal((int) (player.getMaxHitpoints() * 0.3));
				player.getEquipment().deleteItem(11090, 1);
				player.getPackets().sendMessage("Your pheonix necklace heals you, but is destroyed in the process.");
			}
		}
	}
	
	public void heal(int ammount) {
		heal(ammount, 0);
	}
	
	public void addReceivedDamage(Actor source, int amount) {
		if (source == null) {
			return;
		}
		Integer damage = receivedDamage.get(source);
		damage = damage == null ? amount : damage + amount;
		receivedDamage.put(source, damage);
	}
	
	public abstract void sendDeath(Actor source);
	
	public void applyHit(Hit hit) {
		if (isDead()) {
			return;
		}
		// agility locks movement, but regular teleportation only locks teleporting
		if (getLocks().isTeleportLocked() && !getLocks().isMovementLocked()) {
			return;
		}
		receivedHits.add(hit);
		handleIncomingHit(hit);
	}
	
	public void setNextGraphics(Graphics nextGraphics) {
		if (nextGraphics == null) {
			if (nextGraphics4 != null) {
				nextGraphics4 = null;
			} else if (nextGraphics3 != null) {
				nextGraphics3 = null;
			} else if (nextGraphics2 != null) {
				nextGraphics2 = null;
			} else {
				nextGraphics1 = null;
			}
		} else {
			if (nextGraphics.equals(nextGraphics1) || nextGraphics.equals(nextGraphics2) || nextGraphics.equals(nextGraphics3) || nextGraphics.equals(nextGraphics4)) {
				return;
			}
			if (nextGraphics1 == null) {
				nextGraphics1 = nextGraphics;
			} else if (nextGraphics2 == null) {
				nextGraphics2 = nextGraphics;
			} else if (nextGraphics3 == null) {
				nextGraphics3 = nextGraphics;
			} else {
				nextGraphics4 = nextGraphics;
			}
		}
	}
	
	public void heal(int ammount, int extra) {
		hitpoints = hitpoints + ammount >= getMaxHitpoints() + extra ? getMaxHitpoints() + extra : hitpoints + ammount;
	}
	
	public abstract void handleIncomingHit(Hit hit);
	
	public void setNextAnimationNoPriority(Animation nextAnimation) {
		if (lastAnimationEnd > Misc.currentTimeMillis()) {
			return;
		}
		setNextAnimation(nextAnimation);
	}
	
	public void setNextAnimation(Animation nextAnimation) {
		if (nextAnimation != null && nextAnimation.getIds()[0] >= 0) {
			lastAnimationEnd = Misc.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		}
		this.nextAnimation = nextAnimation;
	}
	
	public boolean hasTeleported() {
		return teleported;
	}
	
	public WorldTile getLastLoadedMapRegionTile() {
		return lastLoadedMapRegionTile;
	}
	
	public WorldTile getNextFaceWorldTile() {
		return nextFaceWorldTile;
	}
	
	public void setNextFaceWorldTile(WorldTile nextFaceWorldTile) {
		if (nextFaceWorldTile.getX() == getX() && nextFaceWorldTile.getY() == getY()) {
			return;
		}
		this.nextFaceWorldTile = nextFaceWorldTile;
		if (nextWorldTile != null) {
			faceDirection = Misc.getFaceDirection(nextFaceWorldTile.getX() - nextWorldTile.getX(), nextFaceWorldTile.getY() - nextWorldTile.getY());
		} else {
			faceDirection = Misc.getFaceDirection(nextFaceWorldTile.getX() - getX(), nextFaceWorldTile.getY() - getY());
		}
	}
	
	public void cancelFaceEntityNoCheck() {
		nextFaceEntity = -2;
		lastFaceEntity = -1;
	}
	
	/**
	 * This method faces the actor as long as the next face entity != -1
	 *
	 * @param actor
	 * 		The actor to face
	 */
	public void setNextFaceActor(Actor actor) {
		if (actor == null) {
			nextFaceEntity = -1;
			lastFaceEntity = -1;
		} else {
			nextFaceEntity = actor.getClientIndex();
			lastFaceEntity = nextFaceEntity;
		}
	}
	
	public int getClientIndex() {
		return index + (this instanceof Player ? 32768 : 0);
	}
	
	public void addFreezeDelay(long time) {
		addFreezeDelay(time, false);
	}
	
	public void addFreezeDelay(long time, boolean entangleMessage) {
		long currentTime = Misc.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			if (this instanceof Player) {
				Player p = (Player) this;
				if (!entangleMessage) {
					p.getPackets().sendMessage("You have been frozen.");
				}
			}
		}
	}

	private transient long teleblockDelay;
	private transient long teleblockImmunity;

	public boolean isTeleblocked() {
		return teleblockDelay >= Misc.currentTimeMillis();
	}

	public boolean hasTeleblockImmunity() {
		return teleblockImmunity >= Misc.currentTimeMillis();
	}

	public void teleblock(Entity teleblocker, long time) {
		teleblockDelay = time + Misc.currentTimeMillis();
		teleblockImmunity = time + TimeUnit.SECONDS.toMillis(15);
		if (this.isPlayer()) {
			Player p2 = (Player) this;
			p2.getPackets().sendMessage("You have been teleblocked.");
		}
	}
	
	public boolean freezeDelayed() {
		return frozenBlocked >= Misc.currentTimeMillis();
	}
	
	public void freeze(Entity freezer, long time, String message) {
		long currentTime = Misc.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			addFrozenBlockedDelay(time + TimeUnit.SECONDS.toMillis(3));
			if (isPlayer()) {
				toPlayer().getPackets().sendMessage(message);
			}
		}
		putTemporaryAttribute("frozen_by", freezer);
	}
	
	private void addFrozenBlockedDelay(long time) {
		frozenBlocked = Misc.currentTimeMillis() + time;
	}
	
	public void addFreezeDelay(long time, boolean entangleMessage, Entity freezer) {
		long currentTime = Misc.currentTimeMillis();
		if (currentTime > freezeDelay) {
			resetWalkSteps();
			freezeDelay = time + currentTime;
			if (isPlayer()) {
				Player p = (Player) this;
				if (!entangleMessage) {
					p.getPackets().sendMessage("You have been frozen.");
				}
			}
		}
		putTemporaryAttribute("frozen_by", freezer);
	}
	
	/**
	 * This method faces the actor's tile
	 *
	 * @param target
	 * 		The target
	 */
	public void faceActor(Actor target) {
		setNextFaceWorldTile(new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()), target.getPlane()));
	}
	
	public void faceObject(WorldObject object) {
		ObjectDefinitions objectDef = object.getDefinitions();
		setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()), object.getCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()), object.getPlane()));
	}
	
	public void setForceMultiArea(boolean forceMultiArea) {
		this.forceMultiArea = forceMultiArea;
		checkMultiArea();
	}
	
	public void checkMultiArea() {
		inMultiArea = forceMultiArea || World.isMultiArea(this);
	}
	
	public WorldTile getLastWorldTile() {
		return lastWorldTile;
	}
	
	public ArrayList<Hit> getNextHits() {
		return nextHits;
	}
	
	public void playSound(int soundId, int type) {
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = RegionManager.getRegion(regionId).getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.isRunning() || player.isFinished() || !withinDistance(player)) {
						continue;
					}
					player.getPackets().sendSound(soundId, 0, type);
				}
			}
		}
	}
	
	public CopyOnWriteArrayList<Integer> getMapRegionsIds() {
		return mapRegionsIds;
	}
	
	public boolean isFrozen() {
		return freezeDelay >= Misc.currentTimeMillis();
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putTemporaryAttribute(Object key, K value) {
		getTemporaryAttributes().put(key, value);
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <K> K getTemporaryAttribute(Object key, K defaultValue) {
		K value = (K) getTemporaryAttributes().get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <K> K getTemporaryAttribute(Object key) {
		return (K) getTemporaryAttributes().get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <K> K removeTemporaryAttribute(Object key) {
		return (K) getTemporaryAttributes().remove(key);
	}
	
	@SuppressWarnings("unchecked")
	public <K> K removeTemporaryAttribute(Object key, K defaultValue) {
		K value = (K) getTemporaryAttributes().remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Checks if the entity is moving
	 */
	public boolean isMoving() {
		return nextWalkDirection != -1 || nextRunDirection != -1 || hasWalkSteps();
	}
	
	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
	}
	
	public boolean calcFollow(WorldTile target, boolean inteligent) {
		return calcFollow(target, -1, true, inteligent);
	}
	
	//used for normal npc follow int maxStepsCount, boolean calculate used to save mem on normal path
	public boolean calcFollow(WorldTile target, int maxStepsCount, boolean calculate, boolean inteligent) {
		if (inteligent) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, getX(), getY(), getPlane(), getSize(), target instanceof WorldObject ? new ObjectStrategy((WorldObject) target) : target instanceof Entity ? new ActorStrategy((Actor) target) : new FixedTileStrategy(target.getX(), target.getY()), true);
			if (steps == -1) {
				return false;
			}
			if (steps == 0) {
				return true;
			}
			int[] bufferX = RouteFinder.getLastPathBufferX();
			int[] bufferY = RouteFinder.getLastPathBufferY();
			for (int step = steps - 1; step >= 0; step--) {
				if (!addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
					break;
				}
			}
			return true;
		}
		return RouteFinder.findBasicRoute(this, target, maxStepsCount, true);
	}
	
	/**
	 * Gets the center location.
	 *
	 * @return The center location.
	 */
	public WorldTile getCenterLocation() {
		int offset = getSize() >> 1;
		return getWorldTile().transform(offset, offset, 0);
	}

	public int getHitpoints() {
		return this.hitpoints;
	}

	public int getMapSize() {
		return this.mapSize;
	}

	public PoisonManager getPoisonManager() {
		return this.poisonManager;
	}

	public boolean isRunModeOn() {
		return this.runModeOn;
	}

	public int getIndex() {
		return this.index;
	}

	public int getFaceDirection() {
		return this.faceDirection;
	}

	public int getLastRegionId() {
		return this.lastRegionId;
	}

	public int getNextWalkDirection() {
		return this.nextWalkDirection;
	}

	public int getNextRunDirection() {
		return this.nextRunDirection;
	}

	public ConcurrentHashMap<Object, Object> getTemporaryAttributes() {
		return this.temporaryAttributes;
	}

	public InteractionManager getInteractionManager() {
		return this.interactionManager;
	}

	public ActionLocks getLocks() {
		return this.locks;
	}

	public ConcurrentLinkedQueue<int[]> getWalkSteps() {
		return this.walkSteps;
	}

	public boolean isFinished() {
		return this.finished;
	}

	public Animation getNextAnimation() {
		return this.nextAnimation;
	}

	public Graphics getNextGraphics1() {
		return this.nextGraphics1;
	}

	public Graphics getNextGraphics2() {
		return this.nextGraphics2;
	}

	public Graphics getNextGraphics3() {
		return this.nextGraphics3;
	}

	public Graphics getNextGraphics4() {
		return this.nextGraphics4;
	}

	public ForceMovement getNextForceMovement() {
		return this.nextForceMovement;
	}

	public ForceTalk getNextForceTalk() {
		return this.nextForceTalk;
	}

	public int getNextFaceEntity() {
		return this.nextFaceEntity;
	}

	public int getLastFaceEntity() {
		return this.lastFaceEntity;
	}

	public Actor getAttackedBy() {
		return this.attackedBy;
	}

	public long getAttackedByDelay() {
		return this.attackedByDelay;
	}

	public boolean isInMultiArea() {
		return this.inMultiArea;
	}

	public boolean isAtDynamicRegion() {
		return this.atDynamicRegion;
	}

	public long getLastAnimationEnd() {
		return this.lastAnimationEnd;
	}

	public boolean isForceMultiArea() {
		return this.forceMultiArea;
	}

	public long getFreezeDelay() {
		return this.freezeDelay;
	}

	public long getFrozenBlocked() {
		return this.frozenBlocked;
	}

	public long getFindTargetDelay() {
		return this.findTargetDelay;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	public void setRunModeOn(boolean runModeOn) {
		this.runModeOn = runModeOn;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setFaceDirection(int faceDirection) {
		this.faceDirection = faceDirection;
	}

	public void setLastRegionId(int lastRegionId) {
		this.lastRegionId = lastRegionId;
	}

	public void setNextWorldTile(WorldTile nextWorldTile) {
		this.nextWorldTile = nextWorldTile;
	}

	public void setNextWalkDirection(int nextWalkDirection) {
		this.nextWalkDirection = nextWalkDirection;
	}

	public void setNextRunDirection(int nextRunDirection) {
		this.nextRunDirection = nextRunDirection;
	}

	public void setInteractionManager(InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setNextForceMovement(ForceMovement nextForceMovement) {
		this.nextForceMovement = nextForceMovement;
	}

	public void setNextForceTalk(ForceTalk nextForceTalk) {
		this.nextForceTalk = nextForceTalk;
	}

	public void setLastFaceEntity(int lastFaceEntity) {
		this.lastFaceEntity = lastFaceEntity;
	}

	public void setAttackedBy(Actor attackedBy) {
		this.attackedBy = attackedBy;
	}

	public void setAttackedByDelay(long attackedByDelay) {
		this.attackedByDelay = attackedByDelay;
	}

	public void setInMultiArea(boolean inMultiArea) {
		this.inMultiArea = inMultiArea;
	}

	public void setAtDynamicRegion(boolean atDynamicRegion) {
		this.atDynamicRegion = atDynamicRegion;
	}

	public void setLastAnimationEnd(long lastAnimationEnd) {
		this.lastAnimationEnd = lastAnimationEnd;
	}

	public void setFreezeDelay(long freezeDelay) {
		this.freezeDelay = freezeDelay;
	}

	public void setFrozenBlocked(long frozenBlocked) {
		this.frozenBlocked = frozenBlocked;
	}

	public void setFindTargetDelay(long findTargetDelay) {
		this.findTargetDelay = findTargetDelay;
	}
}
