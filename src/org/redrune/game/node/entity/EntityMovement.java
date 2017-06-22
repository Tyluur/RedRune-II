package org.redrune.game.node.entity;

import lombok.Getter;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.render.flag.impl.TeleportUpdate;
import org.redrune.game.world.region.RegionManager;
import org.redrune.game.world.route.RouteFinder;
import org.redrune.game.world.route.strategy.EntityStrategy;
import org.redrune.game.world.route.strategy.ObjectStrategy;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.Misc;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class EntityMovement {
	
	/**
	 * The steps to walk to
	 */
	private final ConcurrentLinkedQueue<int[]> walkSteps;
	
	/**
	 * The entity
	 */
	private final Entity entity;
	
	/**
	 * The next walk direction
	 */
	@Getter
	private int nextWalkDirection;
	
	/**
	 * The next run direction
	 */
	@Getter
	private int nextRunDirection;
	
	EntityMovement(Entity entity) {
		this.entity = entity;
		this.walkSteps = new ConcurrentLinkedQueue<>();
	}
	
	@Override
	public String toString() {
		return "[walk=" + nextWalkDirection + ", run=" + nextRunDirection + ", steps=" + walkSteps + "]";
	}
	
	/**
	 * Processes the movement
	 */
	public void processMovement() {
		nextWalkDirection = nextRunDirection = -1;
		if (updateTeleport()) {
			return;
		}
		if (walkSteps.isEmpty()) {
			return;
		}
		nextWalkDirection = getNextWalkStep();
		if (nextWalkDirection != -1) {
			moveLocation(RegionManager.DIRECTION_DELTA_X[nextWalkDirection], RegionManager.DIRECTION_DELTA_Y[nextWalkDirection]);
			entity.putAttribute("direction", Misc.getFaceDirection(RegionManager.DIRECTION_DELTA_X[nextWalkDirection], RegionManager.DIRECTION_DELTA_Y[nextWalkDirection]));
			if (isRunning()) {
				nextRunDirection = getNextWalkStep();
				if (nextRunDirection != -1) {
					moveLocation(RegionManager.DIRECTION_DELTA_X[nextRunDirection], RegionManager.DIRECTION_DELTA_Y[nextRunDirection]);
					entity.putAttribute("direction", Misc.getFaceDirection(RegionManager.DIRECTION_DELTA_X[nextRunDirection], RegionManager.DIRECTION_DELTA_Y[nextRunDirection]));
				}
			}
		}
		RegionManager.updateEntityRegion(entity);
		if (entity.needsMapUpdate()) {
			entity.loadMapRegions();
		}
	}
	
	/**
	 * Checks if the player is teleporting, if so does the teleporting and
	 * returns true.
	 *
	 * @return {@code True} if the player is teleporting, {@code false} if not.
	 */
	private boolean updateTeleport() {
		if (entity.getAttribute(AttributeKey.TELEPORT_LOCATION) != null) {
			resetWalkSteps();
			entity.setLocation(entity.getAttribute(AttributeKey.TELEPORT_LOCATION));
			RegionManager.updateEntityRegion(entity);
			entity.removeAttribute(AttributeKey.TELEPORT_LOCATION);
			entity.getUpdateMasks().register(new TeleportUpdate());
			if (entity.needsMapUpdate()) {
				entity.loadMapRegions();
			}
			entity.putAttribute(AttributeKey.PLAYER_TELEPORTED, true);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the next walk step
	 */
	private int getNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}
	
	/**
	 * Moves the entity to a location
	 *
	 * @param xOffset
	 * 		The x offset
	 * @param yOffset
	 * 		The y offset
	 */
	private void moveLocation(int xOffset, int yOffset) {
		entity.setLocation(entity.getLocation().transform(xOffset, yOffset, 0));
	}
	
	/**
	 * Checks if the entity is running.
	 *
	 * @return {@code True} if a ctrl + click action was performed, <br> the player has the run option enabled or the
	 * NPC is a familiar, <p> {@code false} if not.
	 */
	public boolean isRunning() {
		return entity.isNPC() || (entity.isPlayer() && entity.toPlayer().getVariables().isRunToggled());
	}
	
	/**
	 * Resets the walk steps
	 */
	public void resetWalkSteps() {
		walkSteps.clear();
	}
	
	/**
	 * Checks if a projectile can travel to the tile
	 *
	 * @param tile
	 * 		The tile
	 * @param checkClose
	 * 		If we should check close-by tiles
	 * @param size
	 * 		The size of the projectile
	 */
	public boolean clippedProjectile(Location tile, boolean checkClose, int size) {
		int myX = entity.getLocation().getX();
		int myY = entity.getLocation().getY();
		if (entity.isNPC() && size == 1) {
			NPC n = (NPC) entity;
			Location location = n.getMiddleWorldTile();
			myX = location.getX();
			myY = location.getY();
		}
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
				if (!RegionManager.isTileFree(entity.getLocation().getPlane(), lastTileX, lastTileY, dir, size)) {
					return false;
				}
			} else if (!RegionManager.checkProjectileStep(entity.getLocation().getPlane(), lastTileX, lastTileY, dir, size)) {
				return false;
			}
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY) {
				return true;
			}
		}
	}
	
	/**
	 * Resets the steps and sets the player to run
	 *
	 * @param forceRun
	 * 		If the player should run
	 */
	public void reset(boolean forceRun) {
		if (forceRun && entity.isPlayer()) {
			entity.toPlayer().getVariables().setRunToggled(true);
			entity.toPlayer().sendSettings();
		}
		resetWalkSteps();
	}
	
	/**
	 * Checks if the entity is moving
	 */
	public boolean isMoving() {
		return nextWalkDirection != -1 || nextRunDirection != -1 || hasWalkSteps();
	}
	
	/**
	 * Checks if we have walk steps
	 */
	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
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
			if ((entity.isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
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
	
	private int[] calculatedStep(int myX, int myY, int destX, int destY, int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if ((entity.isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if ((entity.isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true)) {
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
			if ((entity.isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if ((entity.isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true)) {
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
	
	/**
	 * Adds walk steps with -1 max steps and checking tiles.
	 *
	 * @param destX
	 * 		The x
	 * @param destY
	 * 		The y
	 */
	public boolean addWalkSteps(final int destX, final int destY) {
		return addWalkSteps(destX, destY, -1, true);
	}
	
	/**
	 * Adds a walk step
	 *
	 * @param destX
	 * 		The step x
	 * @param destY
	 * 		The step y
	 * @param maxStepsCount
	 * 		The max steps to add
	 * @param check
	 * 		If we should check the tiles for masks
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
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
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}
	
	/**
	 * Gets the last walk tile
	 */
	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0) {
			return new int[] { entity.getLocation().getX(), entity.getLocation().getY() };
		}
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}
	
	/**
	 * Adds a walk step
	 *
	 * @param nextX
	 * 		The step x
	 * @param nextY
	 * 		The step y
	 * @param lastX
	 * 		The last x
	 * @param lastY
	 * 		The last y
	 * @param check
	 * 		If we should check the tiles
	 */
	private boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = Misc.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		if (check && !RegionManager.isTileFree(entity.getLocation().getPlane(), lastX, lastY, dir, entity.getSize())) {
			return false;
		}
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}
	
	/**
	 * Checks if a clipped projectile can be sent to a node
	 *
	 * @param node
	 * 		The node
	 * @param checkClose
	 * 		If we should check close
	 */
	public boolean clippedProjectileToNode(Node node, boolean checkClose) {
		Location tile = node.getLocation();
		if (node.isNPC()) {
			NPC npc = node.toNPC();
			if (entity.isPlayer()) {
				return npc.getMovement().clippedProjectileToNode(entity, checkClose);
			}
			tile = npc.getMiddleWorldTile();
		} else if (node.isPlayer()) {
			return clippedProjectile(node.getLocation(), checkClose, 1) || node.toPlayer().getMovement().clippedProjectile(entity.getLocation(), checkClose, 1);
		}
		return clippedProjectile(tile, checkClose, 1);
	}
	
	public boolean calcFollow(Entity target, boolean inteligent) {
		return calcFollow(target, -1, inteligent);
	}
	
	public boolean calcFollow(Entity target, int maxStepsCount, boolean intelligent) {
		if (intelligent) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getPlane(), entity.getSize(), target.isGameObject() ? new ObjectStrategy(target.toGameObject()) : new EntityStrategy(target), true);
			if (steps == -1) {
				return false;
			}
			if (steps == 0) {
				return true;
			}
			int[] bufferX = RouteFinder.getLastPathBufferX();
			int[] bufferY = RouteFinder.getLastPathBufferY();
			for (int step = steps - 1; step >= 0; step--) {
				if (!addWalkSteps(bufferX[step], bufferY[step], maxStepsCount, true)) {
					break;
				}
			}
			return true;
		}
		return findBasicRoute(entity, target, target.getLocation(), maxStepsCount);
	}
	
	public static boolean findBasicRoute(Entity src, Entity target, Location dest, int maxStepsCount) {
		int[] srcPos = src.getMovement().getLastWalkTile();
		int[] destPos = { dest.getX(), dest.getY() };
		int srcSize = src.getSize();
		//set destSize to 0 to walk under it else follows
		int destSize = target.getSize();
		int[] destScenePos = { destPos[0] + destSize - 1, destPos[1] + destSize - 1 };//Arrays.copyOf(destPos, 2);//destSize == 1 ? Arrays.copyOf(destPos, 2) : new int[] {WorldTile.getCoordFaceX(destPos[0], destSize, destSize, -1), WorldTile.getCoordFaceY(destPos[1], destSize, destSize, -1)};
		while (maxStepsCount-- != 0) {
			int[] srcScenePos = { srcPos[0] + srcSize - 1, srcPos[1] + srcSize - 1 };//srcSize == 1 ? Arrays.copyOf(srcPos, 2) : new int[] { WorldTile.getCoordFaceX(srcPos[0], srcSize, srcSize, -1), WorldTile.getCoordFaceY(srcPos[1], srcSize, srcSize, -1)};
			if (!Misc.isOnRange(srcPos[0], srcPos[1], srcSize, destPos[0], destPos[1], destSize, 0)) {
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] < destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] + 1, srcPos[1] + 1)) && src.getMovement().addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] > destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] - 1, srcPos[1] - 1)) && src.getMovement().addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] > destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] + 1, srcPos[1] - 1)) && src.getMovement().addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] < destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] - 1, srcPos[1] + 1)) && src.getMovement().addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] + 1, srcPos[1])) && src.getMovement().addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0] - 1, srcPos[1])) && src.getMovement().addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				if (srcScenePos[1] < destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0], srcPos[1] + 1)) && src.getMovement().addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[1] > destScenePos[1] && (!(src instanceof NPC) || src.getMovement().canWalkNPC(srcPos[0], srcPos[1] - 1)) && src.getMovement().addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]--;
					continue;
				}
				return false;
			}
			break;
		}
		return true;
	}
	
	public boolean canWalkNPC(int toX, int toY) {
		return canWalkNPC(toX, toY, false);
	}
	
	public boolean canWalkNPC(int toX, int toY, boolean checkUnder) {
		// TODO if (!isAtMultiArea())return true
		int size = entity.getSize();
		for (int regionId : entity.getMapRegionsIds()) {
			CopyOnWriteArraySet<NPC> npcIndexes = RegionManager.getRegion(regionId).getNpcs();
			if (npcIndexes != null) {
				for (NPC target : npcIndexes) {
					if (target == null || target == entity || target.isDead() || !target.isRenderable() || target.getLocation().getPlane() != entity.getLocation().getPlane()) {
						continue;
					}
					int targetSize = target.getSize();
					if (!checkUnder && target.getMovement().getNextWalkDirection() == -1) {
						int previewDir = getPreviewNextWalkStep();
						if (previewDir != -1) {
							Location tile = target.getLocation().transform(RegionManager.DIRECTION_DELTA_X[previewDir], RegionManager.DIRECTION_DELTA_Y[previewDir], 0);
							if (colides(tile.getX(), tile.getY(), targetSize, entity.getLocation().getX(), entity.getLocation().getY(), size)) {
								continue;
							}
							if (colides(tile.getX(), tile.getY(), targetSize, toX, toY, size)) {
								return false;
							}
						}
					}
					if (colides(target.getLocation().getX(), target.getLocation().getY(), targetSize, entity.getLocation().getX(), entity.getLocation().getY(), size)) {
						continue;
					}
					if (colides(target.getLocation().getX(), target.getLocation().getY(), targetSize, toX, toY, size)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private int getPreviewNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}
	
	private static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
		for (int checkX1 = x1; checkX1 < x1 + size1; checkX1++) {
			for (int checkY1 = y1; checkY1 < y1 + size1; checkY1++) {
				for (int checkX2 = x2; checkX2 < x2 + size2; checkX2++) {
					for (int checkY2 = y2; checkY2 < y2 + size2; checkY2++) {
						if (checkX1 == checkX2 && checkY1 == checkY2) {
							return true;
						}
					}
					
				}
			}
		}
		return false;
	}
}
