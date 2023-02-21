package org.redrune.game.content.entity.object;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.player.dialogue.impl.object.ClimbDialogue;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.game.map.Direction;

/**
 * Handles a ladder climbing action.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
public class ClimbActionHandler {

    /**
     * Represents the climb up animation of ladders.
     */
    private static final Animation CLIMB_UP = new Animation(828);

    /**
     * Represents the climb down animation of ladders.
     */
    private static final Animation CLIMB_DOWN = new Animation(827);

    /**
     * Handles the climbing of a ladder.
     *
     * @param player The player.
     * @param object The game object.
     * @param option The option.
     */
    public static void climbLadder(Player player, WorldObject object, String option) {
        WorldObject newLadder = null;
        Animation animation = CLIMB_UP;
        if (option.equalsIgnoreCase("climb-up")) {
            newLadder = getLadder(object, false);
        } else if (option.equalsIgnoreCase("climb-down")) {
            if (object.getDefinitions().getName().equals("Trapdoor")) {
                animation = CLIMB_DOWN;
            }
            newLadder = getLadder(object, true);
        } else if (option.equalsIgnoreCase("climb")) {
            WorldObject upperLadder = getLadder(object, false);
            WorldObject downLadder = getLadder(object, true);
            if (upperLadder == null && downLadder != null) {
                climbLadder(player, object, "climb-down");
                return;
            }
            if (upperLadder != null && downLadder == null) {
                climbLadder(player, object, "climb-up");
                return;
            }
            player.getDialogueManager().startDialogue(ClimbDialogue.class, object);
            return;
        }
        WorldTile destination = newLadder != null ? getDestination(newLadder) : null;
        if (newLadder == null || destination == null) {
            player.getPackets().sendMessage("The ladder doesn't seem to lead anywhere.");
            return;
        }
        if (object.getDefinitions().getName().startsWith("Stair")) {
            animation = null;
        }
        climb(player, animation, destination);
    }

    /**
     * Gets the teleport destination.
     *
     * @param object The object to teleport to.
     * @return The teleport destination.
     */
    public static WorldTile getDestination(WorldObject object) {
        int sizeX = object.getDefinitions().getSizeX();
        int sizeY = object.getDefinitions().getSizeY();
        if (object.getRotation() % 2 != 0) {
            int switcher = sizeX;
            sizeX = sizeY;
            sizeY = switcher;
        }
        Direction dir = Direction.forWalkFlag(object.getDefinitions().getAccessBlockFlag(), object.getRotation());
        if (dir != null) {
            return getDestination(object, sizeX, sizeY, dir, 0);
        }
        switch (object.getRotation()) {
            case 0:
                return getDestination(object, sizeX, sizeY, Direction.SOUTH, 0);
            case 1:
                return getDestination(object, sizeX, sizeY, Direction.EAST, 0);
            case 2:
                return getDestination(object, sizeX, sizeY, Direction.NORTH, 0);
            case 3:
                return getDestination(object, sizeX, sizeY, Direction.WEST, 0);
        }
        return null;
    }

    /**
     * Gets the destination for the given object.
     *
     * @param object The object.
     * @param dir    The preferred direction from the object.
     * @return The teleporting destination.
     */
    private static WorldTile getDestination(WorldObject object, int sizeX, int sizeY, Direction dir, int count) {
        WorldTile loc = object.getWorldTile();
        if (dir.toInteger() % 2 != 0) {
            int x = dir.getStepX();
            if (x > 0) {
                x *= sizeX;
            }
            for (int y = 0; y < sizeY; y++) {
                WorldTile tile = loc.transform(x, y, 0);
                if (RegionManager.getRegion(tile.getRegionId(), true).isTeleportPermitted(tile) && dir.canMove(tile)) {
                    return tile;
                }
            }
        } else {
            int y = dir.getStepY();
            if (y > 0) {
                y *= sizeY;
            }
            for (int x = 0; x < sizeX; x++) {
                WorldTile tile = loc.transform(x, y, 0);
                if (RegionManager.getRegion(tile.getRegionId(), true).isTeleportPermitted(tile) && dir.canMove(tile)) {
                    return tile;
                }
            }
        }
        if (count == 3) {
            return null;
        }
        return getDestination(object, sizeX, sizeY, Direction.get((dir.toInteger() + 1) % 4), count + 1);
    }

    /**
     * Executes the climbing action.
     *
     * @param player      The player.
     * @param animation   The climbing animation.
     * @param destination The destination.
     */
    private static void climb(final Player player, Animation animation, final WorldTile destination, final String... messages) {
        player.getLocks().lock(2);
        player.setNextAnimation(animation);
        SystemManager.SCHEDULER.schedule(new ScheduledTask(2) {
            @Override
            public void run() {
                player.setNextWorldTile(destination);
                for (String message : messages) {
                    player.getPackets().sendMessage(message);
                }
            }
        });
    }

    /**
     * Gets the ladder the object leads to.
     *
     * @param object The ladder object.
     * @param down   If the player is going down a floor.
     * @return The ladder the current ladder object leads to.
     */
    private static WorldObject getLadder(WorldObject object, boolean down) {
        // TODO: fix ladders going to -1 z
        int mod = down ? -1 : 1;
        WorldObject ladder = RegionManager.getStandardObjectWithLoad(object.getWorldTile().transform(0, mod == -1 ? mod * -6400 : 0, mod == -1 ? 0 : mod));
        if (ladder == null || !isLadder(ladder.getDefinitions())) {
            if (ladder != null && ladder.getDefinitions().getName().equals(object.getDefinitions().getName())) {
                ladder = RegionManager.getStandardObjectWithLoad(ladder.getWorldTile().transform(0, 0, mod));
                if (ladder != null) {
                    return ladder;
                }
            }
            ladder = findLadder(object.getWorldTile().transform(0, 0, mod));
            if (ladder == null) {
                ladder = RegionManager.getStandardObjectWithLoad(object.getWorldTile().transform(0, mod * -6400, 0));
                if (ladder == null) {
                    ladder = findLadder(object.getWorldTile().transform(0, mod * -6400, 0));
                }
            }
        }
        return ladder;
    }

    /**
     * Checks if the object is a ladder.
     *
     * @param def The object's definitions.
     * @return {@code True} if so.
     */
    public static boolean isLadder(ObjectDefinitions def) {
        for (String option : def.getOptions()) {
            if (option == null) {
                continue;
            }
            option = option.toLowerCase();
            if (option != null && (option.contains("climb"))) {
                return true;
            }
        }
        return def.getName().equals("Trapdoor");
    }

    /**
     * Finds a ladder (by searching a 10x10 area around the given location).
     *
     * @param l The location.
     * @return The ladder.
     */
    private static WorldObject findLadder(WorldTile l) {
        for (int x = -5; x < 6; x++) {
            for (int y = -5; y < 6; y++) {
                WorldTile transform = l.transform(x, y, 0);
                WorldObject object = RegionManager.getStandardObjectWithLoad(transform);
                if (object != null && isLadder(object.getDefinitions())) {
                    return object;
                }
            }
        }
        return null;
    }
}
