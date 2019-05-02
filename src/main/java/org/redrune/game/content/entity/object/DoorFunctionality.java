package org.redrune.game.content.entity.object;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.game.map.Direction;
import org.redrune.utility.game.map.Point;
import org.redrune.utility.game.repository.object.door.Door;
import org.redrune.utility.game.repository.object.door.DoorRepository;

/**
 * Handles door actions.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-20
 */
public class DoorFunctionality {
	
	/**
	 * Handles a door action.
	 *
	 * @param player
	 * 		The player.
	 * @param object
	 * 		The object.
	 */
	public static void handleDoor(Player player, WorldObject object) {
		final WorldObject second = (object.getId() == 1530 || object.getId() == 1531) ? null : getSecondDoor(object);
		WorldObject o;
		if ((o = object.getReplaced()) != null) {
			RegionManager.replaceObject(object, o);
			if (second != null && (o = (second).getReplaced()) != null) {
				RegionManager.replaceObject(second, o);
				return;
			}
			return;
		}
		if (object.getDefinitions().containsOption("close")) {
			Door d = DoorRepository.forId(object.getId());
			if (d == null) {
				player.getPackets().sendMessage("The door appears to be stuck.");
				return;
			}
			if (second != null) {
				player.getPackets().sendMessage("The doors appear to be stuck.");
				return;
			}
			int firstDir = (object.getRotation() + 3) % 4;
			Point p = getCloseRotation(object);
			WorldTile firstLoc = object.getWorldTile().transform(p.getX(), p.getY(), 0);
			WorldObject replaced = new WorldObject(d.getReplaceId(), object.getType(), firstDir, firstLoc);
			RegionManager.replaceObject(object, replaced);
			return;
		}
		Door d = DoorRepository.forId(object.getId());
		if (d == null) {
			player.getPackets().sendMessage("The doors appear to be stuck.");
			return;
		}
		if (d.isAutoWalk()) {
			//			TODO: handleAutowalkDoor(player, object);
			player.getPackets().sendMessage("The doors appear to be stuck.");
			return;
		}
		boolean isFence = isFence(object);
		if (second != null) {
			Door s = DoorRepository.forId(second.getId());
			open(object, second, d.getReplaceId(), s == null ? second.getId() : s.getReplaceId(), 500, isFence);
			return;
		}
		open(object, null, d.getReplaceId(), -1, 500, isFence);
	}
	
	/**
	 * Handles the opening and walking through a door.
	 *
	 * @param player
	 * 		The entity walking through the door.
	 * @param object
	 * 		The door object.
	 */
	public static boolean handleAutowalkDoor(final Player player, final WorldObject object, final WorldTile endLocation) {
		if (object.getTemporaryAttribute("in_use", false)) {
			return false;
		}
		final WorldObject second = (object.getId() == 3) ? null : getSecondDoor(object);
		player.getLocks().lock(4);
		object.putTemporaryAttribute("in_use", true);
		if (second != null) {
			second.putTemporaryAttribute("in_use", true);
		}
		SystemManager.SCHEDULER.schedule(new ScheduledTask(1, 3) {
			boolean opened = false;
			
			@Override
			public void run() {
				if (!opened) {
					open(object, second, object.getId(), second == null ? -1 : second.getId(), 2, false);
					player.resetWalkSteps();
					player.addWalkStepsInteract(endLocation.getX(), endLocation.getY(), 1, 1, false);
					opened = true;
				} else {
					if (getTicksPassed() >= 3) {
						object.removeTemporaryAttribute("in_use");
						if (second != null) {
							second.removeTemporaryAttribute("in_use");
						}
						stop();
					}
				}
			}
		});
		return true;
	}
	
	/**
	 * Gets the door next to this door.
	 *
	 * @param object
	 * 		The door.
	 * @return The second door, if any, {@code null} if no second door existed.
	 */
	private static WorldObject getSecondDoor(WorldObject object) {
		WorldTile location = object.getWorldTile();
		WorldObject o;
		int[][] checks = { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, };
		for (int[] check : checks) {
			WorldTile transform = location.transform(check[0], check[1], check[2]);
			o = RegionManager.getObjectWithType(transform, object.getType());
			if (o == null) {
				continue;
			}
			ObjectDefinitions definitions = o.getDefinitions();
			if (definitions == null) {
				continue;
			}
			if (definitions.getName().equalsIgnoreCase(object.getDefinitions().getName())) {
				return o;
			}
		}
		return null;
	}
	
	/**
	 * Gets the closing rotation point.
	 *
	 * @param object
	 * 		The object.
	 * @return The point.
	 */
	private static Point getCloseRotation(WorldObject object) {
		switch (object.getRotation()) {
			case 0:
				return new Point(0, 1);
			case 1:
				return new Point(1, 0);
			case 2:
				return new Point(0, -1);
			case 3:
				return new Point(-1, 0);
		}
		return new Point(0, 0);
	}
	
	/**
	 * Opens the doors.
	 *
	 * @param object
	 * 		The door object.
	 * @param second
	 * 		The second door object.
	 * @param replaceId
	 * 		The replace id.
	 * @param secondReplaceId
	 * 		The second replace id.
	 * @param restoreTicks
	 * 		The amount of ticks before the door(s) should be closed again.
	 */
	private static void open(WorldObject object, WorldObject second, int replaceId, int secondReplaceId, int restoreTicks, boolean fence) {
		int mod = object.getType() == 9 ? -1 : 1;
		int firstDir = (object.getRotation() + ((mod + 4) % 4)) % 4;
		Point p = getRotationPoint(object.getRotation());
		WorldTile firstLoc = object.getWorldTile().transform(p.getX() * mod, p.getY() * mod, 0);
		if (second == null) {
			if (replaceId == 4577) {
				replaceId = 4578;
				firstDir = 3;
				firstLoc = firstLoc.transform(0, 1, 0);
			}
			WorldObject replaced = new WorldObject(replaceId, object.getType(), firstDir, firstLoc);
			RegionManager.replaceObject(object, replaced, restoreTicks * 600);
			return;
		}
		if (fence) {
			openFence(object, second, replaceId, secondReplaceId, restoreTicks);
			return;
		}
		Direction offset = Direction.getDirection(second.getWorldTile().getX() - object.getWorldTile().getX(), second.getWorldTile().getY() - object.getWorldTile().getY());
		int secondDir = (second.getRotation() + mod) % 4;
		if (firstDir == 1 && offset == Direction.NORTH) {
			firstDir = 3;
		} else if (firstDir == 2 && offset == Direction.EAST) {
			firstDir = 0;
		} else if (firstDir == 3 && offset == Direction.SOUTH) {
			firstDir = 1;
		} else if (firstDir == 0 && offset == Direction.WEST) {
			firstDir = 2;
		}
		if (firstDir == secondDir) {
			secondDir = (secondDir + 2) % 4;
		}
		WorldTile secondLoc = second.getWorldTile().transform(p.getX(), p.getY(), 0);
		RegionManager.replaceObject(object, object.transform(replaceId, firstDir, firstLoc), restoreTicks * 600);
		RegionManager.replaceObject(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks * 600);
	}
	
	/**
	 * Gets the rotation point for the object.
	 *
	 * @return The rotation point.
	 */
	public static Point getRotationPoint(int rotation) {
		switch (rotation) {
			case 0:
				return new Point(-1, 0);
			case 1:
				return new Point(0, 1);
			case 2:
				return new Point(1, 0);
			case 3:
				return new Point(0, -1);
		}
		return null;
	}
	
	/**
	 * Handles the opening of a fence.
	 *
	 * @param object
	 * 		The fence object.
	 * @param second
	 * 		The second fence object.
	 * @param replaceId
	 * 		The replace id.
	 * @param secondReplaceId
	 * 		The second replace id.
	 * @param restoreTicks
	 * 		The amount of ticks before the door(s) should be closed again.
	 */
	private static void openFence(WorldObject object, WorldObject second, int replaceId, int secondReplaceId, int restoreTicks) {
		Direction offset = Direction.getDirection(second.getWorldTile().getX() - object.getWorldTile().getX(), second.getWorldTile().getY() - object.getWorldTile().getY());
		int firstDir = (object.getRotation() + 3) % 4;
		Point p = getRotationPoint(object.getRotation());
		WorldTile firstLoc = null;
		int secondDir = (second.getRotation() + 3) % 4;
		if (offset == Direction.WEST || offset == Direction.SOUTH) {
			firstLoc = second.getWorldTile().transform(p.getX(), p.getY(), 0);
			int s = replaceId;
			replaceId = secondReplaceId;
			secondReplaceId = s;
		} else {
			firstLoc = object.getWorldTile().transform(p.getX(), p.getY(), 0);
		}
		if (object.getRotation() == 3 || object.getRotation() == 2) {
			firstDir = (firstDir + 2) % 4;
			secondDir = (secondDir + 2) % 4;
		}
		WorldTile secondLoc = firstLoc.transform(p.getX(), p.getY(), 0);
		RegionManager.replaceObject(object, object.transform(replaceId, firstDir, firstLoc), restoreTicks * 600);
		RegionManager.replaceObject(second, second.transform(secondReplaceId, secondDir, secondLoc), restoreTicks * 600);
	}
	
	/**
	 * Method wrapper for handling the auto walk door.
	 *
	 * @param player
	 * 		the player
	 * @param object
	 * 		the object.
	 */
	public static boolean handleAutowalkDoor(Player player, WorldObject object) {
		return handleAutowalkDoor(player, object, getEndLocation(player, object));
	}
	
	/**
	 * Gets the end location to walk to.
	 *
	 * @param player
	 * 		the player.
	 * @param object
	 * 		the object.
	 * @return the end location.
	 */
	private static WorldTile getEndLocation(Player player, WorldObject object) {
		WorldTile l = object.getWorldTile();
		WorldTile playerTile = player.getWorldTile();
		switch (object.getId()) {
			case 2514: // ranging guild door
				if (playerTile.getX() <= l.getX()) {
					return new WorldTile(2659, 3438, 0);
				} else {
					return new WorldTile(2657, 3439, 0);
				}
		}
		switch (object.getRotation()) {
			case 0:
				if (playerTile.getX() >= l.getX()) {
					l = l.transform(-1, 0, 0);
				}
				break;
			case 1:
				if (playerTile.getY() <= l.getY()) {
					l = l.transform(0, 1, 0);
				}
				break;
			case 2:
				if (playerTile.getX() <= l.getX()) {
					l = l.transform(1, 0, 0);
				}
				break;
			case 3:
				if (playerTile.getY() >= l.getY()) {
					l = l.transform(0, -1, 0);
				}
				break;
		}
		return l;
	}
	
	/**
	 * Checking if a door is a fence or not
	 *
	 * @param object
	 * 		The door
	 */
	private static boolean isFence(WorldObject object) {
		String name = object.getDefinitions().getName().toLowerCase();
		return name.contains("gate") || name.contains("fence");
	}
}
