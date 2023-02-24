package org.redrune.game.global.map.region;

import org.redrune.engine.SystemManager;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.ItemConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.entity.object.ObjectRemoval;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class handles all management operations on regions
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public final class RegionManager {

    private static final Map<Integer, Region> regions = Collections.synchronizedMap(new HashMap<>());

    public static Map<Integer, Region> getRegions() {
        return regions;
    }

    public static void updateActorRegion(Actor actor) {
        if (actor.isFinished()) {
            if (actor instanceof Player) {
                getRegion(actor.getLastRegionId()).removePlayerIndex(actor.getIndex());
            } else {
                getRegion(actor.getLastRegionId()).removeNPCIndex(actor.getIndex());
            }
            return;
        }
        int regionId = actor.getRegionId();
        if (actor.getLastRegionId() != regionId) {
            if (actor instanceof Player) {
                if (actor.getLastRegionId() > 0) {
                    getRegion(actor.getLastRegionId()).removePlayerIndex(actor.getIndex());
                }
                Region region = getRegion(regionId);
                region.addPlayerIndex(actor.getIndex());
                Player player = (Player) actor;
                int musicId = region.getMusicId();
                if (musicId != -1) {
                    player.getMusicManager().checkMusic(musicId);
                }
            } else {
                if (actor.getLastRegionId() > 0) {
                    getRegion(actor.getLastRegionId()).removeNPCIndex(actor.getIndex());
                }
                getRegion(regionId).addNPCIndex(actor.getIndex());
            }
            actor.setLastRegionId(regionId);
            if (actor.isPlayer()) {
                ObjectRemoval.handleRegionChange(actor.toPlayer());
            }
        }
        if (actor instanceof Player) {
            Player player = (Player) actor;
            player.getControllerManager().moved();
            if (player.isRunning() && player.getControllerManager().getController() == null) {
                World.checkControllersAtMove(player);
            }
        }
        actor.checkMultiArea();
    }

    public static Region getRegion(int id) {
        return getRegion(id, false);
    }

    public static Region getRegion(int id, boolean load) {
        Region region = regions.get(id);
        if (region == null) {
            region = new Region(id);
            regions.put(id, region);
        }
        if (load) {
            region.checkLoadMap();
        }
        return region;
    }

    /*
     * checks clip
     */
    public static boolean canMoveNPC(int plane, int x, int y, int size) {
        for (int tileX = x; tileX < x + size; tileX++) {
            for (int tileY = y; tileY < y + size; tileY++) {
                if (getMask(plane, tileX, tileY) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getMask(int plane, int x, int y) {
        WorldTile tile = new WorldTile(x, y, plane);
        int regionId = tile.getRegionId();
        Region region = getRegion(regionId);
        if (region == null) {
            return -1;
        }
        int baseLocalX = x - ((regionId >> 8) * 64);
        int baseLocalY = y - ((regionId & 0xff) * 64);
        return region.getMask(tile.getPlane(), baseLocalX, baseLocalY);
    }

    public static void setMask(int plane, int x, int y, int mask) {
        WorldTile tile = new WorldTile(x, y, plane);
        int regionId = tile.getRegionId();
        Region region = getRegion(regionId);
        if (region == null) {
            return;
        }
        int baseLocalX = x - ((regionId >> 8) * 64);
        int baseLocalY = y - ((regionId & 0xff) * 64);
        region.setMask(tile.getPlane(), baseLocalX, baseLocalY, mask);
    }

    public static int getRotation(int plane, int x, int y) {
        WorldTile tile = new WorldTile(x, y, plane);
        int regionId = tile.getRegionId();
        Region region = getRegion(regionId);
        if (region == null) {
            return 0;
        }
        int baseLocalX = x - ((regionId >> 8) * 64);
        int baseLocalY = y - ((regionId & 0xff) * 64);
        return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
    }

    public static boolean checkProjectileStep(int plane, int x, int y, int dir, int size) {
        int xOffset = Misc.DIRECTION_DELTA_X[dir];
        int yOffset = Misc.DIRECTION_DELTA_Y[dir];
        if (size == 1) {
            int mask = getClipedOnlyMask(plane, x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir]);
            if (xOffset == -1 && yOffset == 0) {
                return (mask & 0x42240000) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (mask & 0x60240000) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (mask & 0x40a40000) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (mask & 0x48240000) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (mask & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (mask & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (mask & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (mask & 0x78240000) == 0 && (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
            }
        } else if (size == 2) {
            if (xOffset == -1 && yOffset == 0) {
                return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
            }
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0 || (getClipedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 0) {
                if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0 || (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == -1) {
                if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0 || (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == 1) {
                if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0 || (getClipedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == -1) {
                if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getClipedOnlyMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == -1) {
                if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == 1) {
                if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getClipedOnlyMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 1) {
                if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int getClipedOnlyMask(int plane, int x, int y) {
        WorldTile tile = new WorldTile(x, y, plane);
        int regionId = tile.getRegionId();
        Region region = getRegion(regionId);
        if (region == null) {
            return -1;
        }
        int baseLocalX = x - ((regionId >> 8) * 64);
        int baseLocalY = y - ((regionId & 0xff) * 64);
        return region.getMaskClipedOnly(tile.getPlane(), baseLocalX, baseLocalY);
    }

    public static boolean checkWalkStep(int plane, int x, int y, int dir, int size) {
        int xOffset = Misc.DIRECTION_DELTA_X[dir];
        int yOffset = Misc.DIRECTION_DELTA_Y[dir];
        int rotation = getRotation(plane, x + xOffset, y + yOffset);
        if (rotation != 0) {
            for (int rotate = 0; rotate < (4 - rotation); rotate++) {
                int fakeChunckX = xOffset;
                int fakeChunckY = yOffset;
                xOffset = fakeChunckY;
                yOffset = -fakeChunckX;
            }
        }

        if (size == 1) {
            int mask = getMask(plane, x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir]);
            if (xOffset == -1 && yOffset == 0) {
                return (mask & 0x42240000) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (mask & 0x60240000) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (mask & 0x40a40000) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (mask & 0x48240000) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (mask & 0x43a40000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (mask & 0x60e40000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (mask & 0x4e240000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (mask & 0x78240000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
            }
        } else if (size == 2) {
            if (xOffset == -1 && yOffset == 0) {
                return (getMask(plane, x - 1, y) & 0x43a40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (getMask(plane, x + 2, y) & 0x60e40000) == 0 && (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (getMask(plane, x, y - 1) & 0x43a40000) == 0 && (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (getMask(plane, x, y + 2) & 0x4e240000) == 0 && (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (getMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getMask(plane, x, y - 1) & 0x63e40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getMask(plane, x + 2, y) & 0x78e40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getMask(plane, x, y + 2) & 0x7e240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
            }
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((getMask(plane, x - 1, y) & 0x43a40000) != 0 || (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 0) {
                if ((getMask(plane, x + size, y) & 0x60e40000) != 0 || (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == -1) {
                if ((getMask(plane, x, y - 1) & 0x43a40000) != 0 || (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == 1) {
                if ((getMask(plane, x, y + size) & 0x4e240000) != 0 || (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == -1) {
                if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == -1) {
                if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == 1) {
                if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 1) {
                if ((getMask(plane, x + size, y + size) & 0x78240000) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void spawnTemporaryObject(final WorldObject object, long time) {
        spawnObject(object);
        SystemManager.SLOW_EXECUTOR.schedule(() -> {
            try {
                if (!isSpawnedObject(object)) {
                    return;
                }
                removeObject(object);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, time, TimeUnit.MILLISECONDS);
    }

    public static void spawnObject(WorldObject object) {
        getRegion(object.getRegionId()).spawnObject(object, object.getPlane(), object.getXInRegion(), object.getYInRegion(), false);
    }

    public static void unclipTile(WorldTile tile) {
        getRegion(tile.getRegionId()).unclip(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
    }

    public static void removeObject(WorldObject object) {
        getRegion(object.getRegionId()).removeObject(object, object.getPlane(), object.getXInRegion(), object.getYInRegion());
    }

    public static boolean isSpawnedObject(WorldObject object) {
        return getRegion(object.getRegionId()).getSpawnedObjects().contains(object);
    }

    public static boolean removeTemporaryObject(final WorldObject object, long time) {
        removeObject(object);
        SystemManager.SLOW_EXECUTOR.schedule(() -> {
            try {
                spawnObject(object);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, time, TimeUnit.MILLISECONDS);
        return true;
    }

    public static void replaceObject(WorldObject original, WorldObject replace) {
        removeObject(original);
        spawnObject(replace);
        replace.setReplaced(original);
    }

    public static void replaceObject(WorldObject original, WorldObject replace, long time) {
        removeObject(original);
        spawnObject(replace);
        replace.setReplaced(original);
        SystemManager.SLOW_EXECUTOR.schedule(() -> {
            try {
                if (replace != null) {
                    removeObject(replace);
                }
                if (original != null) {
                    spawnObject(original);
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, time, TimeUnit.MILLISECONDS);
    }

    public static WorldObject getStandardObject(WorldTile tile) {
        return getRegion(tile.getRegionId()).getStandardObject(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
    }

    public static WorldObject getStandardObjectWithLoad(WorldTile tile) {
        return getRegion(tile.getRegionId(), true).getStandardObject(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
    }

    public static boolean containsObjectWithId(int id, WorldTile tile) {
        return getRegion(tile.getRegionId()).containsObjectWithId(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(), id);
    }

    public static WorldObject getObjectWithType(WorldTile tile, int type) {
        return getRegion(tile.getRegionId()).getObjectWithType(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(), type);
    }

    public static WorldObject getObjectWithId(WorldTile tile, int id) {
        return getRegion(tile.getRegionId()).getObjectWithId(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(), id);
    }

    /*
     * type 0 - gold if not tradeable
     * type 1 - gold if destroyable
     * type 2 - no gold
     */
    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime, int type, final int publicTime) {
        if (type != 2) {
            // TODO convert items dropped to bm value
			/*if ((type == 0 && !ItemConstants.isTradeable(item)) || type == 1 && ItemConstants.isDestroy(item)) {
				int price = item.getDefinitions().getValue();
				if (price <= 0)
					return null;
				item.setId(995);
				item.setAmount(price);
			}*/
        }
        final FloorItem floorItem = new FloorItem(item, tile, owner, owner != null, invisible);
        final Region region = getRegion(tile.getRegionId());
        region.forceGetFloorItems().add(floorItem);
        if (invisible) {
            if (owner != null) {
                owner.getPackets().sendGroundItem(floorItem);
            }
            // becomes visible after x time
            if (hiddenTime != -1) {
                SystemManager.SLOW_EXECUTOR.schedule(() -> {
                    try {
                        turnPublic(floorItem, publicTime);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }, hiddenTime, TimeUnit.SECONDS);
            }
        } else {
            // visible
            int regionId = tile.getRegionId();
            for (Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted() || player.isFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
                    continue;
                }
                player.getPackets().sendGroundItem(floorItem);
            }
            // disapears after this time
            if (publicTime != -1) {
                removeGroundItem(floorItem, publicTime);
            }
        }
        return floorItem;
    }

    public static void turnPublic(FloorItem floorItem, int publicTime) {
        if (!floorItem.isInvisible()) {
            return;
        }
        int regionId = floorItem.getTile().getRegionId();
        final Region region = getRegion(regionId);
        if (!region.forceGetFloorItems().contains(floorItem)) {
            return;
        }
        Player realOwner = floorItem.hasOwner() ? World.getPlayer(floorItem.getOwner().getUsername()) : null;
        if (!ItemConstants.isTradeable(floorItem)) {
            region.forceGetFloorItems().remove(floorItem);
            if (realOwner != null) {
                if (realOwner.getMapRegionsIds().contains(regionId) && realOwner.getPlane() == floorItem.getTile().getPlane()) {
                    realOwner.getPackets().sendRemoveGroundItem(floorItem);
                }
            }
            return;
        }
        floorItem.setInvisible(false);
        for (Player player : World.getPlayers()) {
            if (player == null || player == realOwner || !player.hasStarted() || player.isFinished() || player.getPlane() != floorItem.getTile().getPlane() || !player.getMapRegionsIds().contains(regionId)) {
                continue;
            }
            player.getPackets().sendGroundItem(floorItem);
        }
        // disapears after this time
        if (publicTime != -1) {
            removeGroundItem(floorItem, publicTime);
        }
    }

    public static void addGroundItem(final Item item, final WorldTile tile) {
        // adds item, not invisible, no owner, no time to disapear
        addGroundItem(item, tile, null, false, -1, 2, -1);
    }

    public static void addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime) {
        addGroundItem(item, tile, owner, invisible, hiddenTime, 2, 150);
    }

    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime, int type) {
        return addGroundItem(item, tile, owner, invisible, hiddenTime, type, 150);
    }

    public static void updateGroundItem(Item item, final WorldTile tile, final Player owner) {
        final FloorItem floorItem = getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
        if (floorItem == null) {
            addGroundItem(item, tile, owner, false, 360, true);
            return;
        }
        floorItem.setAmount(floorItem.getAmount() + item.getAmount());
        owner.getPackets().sendRemoveGroundItem(floorItem);
        owner.getPackets().sendGroundItem(floorItem);

    }

    public static void addGroundItem(final Item item, final WorldTile tile, final Player owner/* null for default */, final boolean underGrave, long hiddenTime/* default 3minutes */, boolean invisible) {
        final FloorItem floorItem = new FloorItem(item, tile, owner, owner != null && underGrave, invisible);
        final Region region = getRegion(tile.getRegionId());
        region.forceGetFloorItems().add(floorItem);

        if (invisible && hiddenTime != -1) {
            if (owner != null) {
                owner.getPackets().sendGroundItem(floorItem);
            }
            SystemManager.SLOW_EXECUTOR.schedule(() -> {
                try {
                    if (!region.forceGetFloorItems().contains(floorItem)) {
                        return;
                    }
                    int regionId = tile.getRegionId();
                    if ((owner != null && underGrave) || ItemConstants.isTradeable(floorItem)) {
                        region.forceGetFloorItems().remove(floorItem);
                        if (owner.getMapRegionsIds().contains(regionId) || owner.getPlane() != tile.getPlane()) {
                            owner.getPackets().sendRemoveGroundItem(floorItem);
                        }
                        return;
                    }
                    floorItem.setInvisible(false);
                    for (Player player : World.getPlayers()) {
                        if (player == null || player == owner || !player.hasStarted() || player.isFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
                            continue;
                        }
                        player.getPackets().sendGroundItem(floorItem);
                    }
                    removeGroundItem(floorItem, 180);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }, hiddenTime, TimeUnit.SECONDS);
            return;
        }
        int regionId = tile.getRegionId();
        for (Player player : World.getPlayers()) {
            if (player == null || !player.hasStarted() || player.isFinished() || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)) {
                continue;
            }
            player.getPackets().sendGroundItem(floorItem);
        }
        removeGroundItem(floorItem, 180);
    }

    private static void removeGroundItem(final FloorItem floorItem, long publicTime) {
        SystemManager.SLOW_EXECUTOR.schedule(() -> {
            try {
                int regionId = floorItem.getTile().getRegionId();
                Region region = getRegion(regionId);
                if (!region.forceGetFloorItems().contains(floorItem)) {
                    return;
                }
                region.forceGetFloorItems().remove(floorItem);
                for (Player player : World.getPlayers()) {
                    if (player == null || !player.hasStarted() || player.isFinished() || player.getPlane() != floorItem.getTile().getPlane() || !player.getMapRegionsIds().contains(regionId)) {
                        continue;
                    }
                    player.getPackets().sendRemoveGroundItem(floorItem);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, publicTime, TimeUnit.SECONDS);
    }

    public static boolean removeGroundItem(Player player, FloorItem floorItem) {
        return removeGroundItem(player, floorItem, true);
    }

    public static boolean removeGroundItem(Player player, FloorItem floorItem, boolean add) {
        int regionId = floorItem.getTile().getRegionId();
        Region region = getRegion(regionId);
        if (!region.forceGetFloorItems().contains(floorItem)) {
            return false;
        }
        if (player.getInventory().getFreeSlots() == 0) {
            return false;
        }
        region.forceGetFloorItems().remove(floorItem);
        if (add) {
            player.getInventory().addItem(floorItem.getId(), floorItem.getAmount());
        }
        if (floorItem.isInvisible() || floorItem.isGrave()) {
            player.getPackets().sendRemoveGroundItem(floorItem);
            return true;
        } else {
            for (Player p2 : World.getPlayers()) {
                if (p2 == null || !p2.hasStarted() || p2.isFinished() || p2.getPlane() != floorItem.getTile().getPlane() || !p2.getMapRegionsIds().contains(regionId)) {
                    continue;
                }
                p2.getPackets().sendRemoveGroundItem(floorItem);
            }
            return true;
        }
    }

    public static void sendGraphics(Actor creator, Graphics graphics, WorldTile tile) {
        if (creator == null) {
            for (Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
                    continue;
                }
                player.getPackets().sendGraphics(graphics, tile);
            }
        } else {
            for (int regionId : creator.getMapRegionsIds()) {
                List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
                if (playersIndexes == null) {
                    continue;
                }
                for (Integer playerIndex : playersIndexes) {
                    Player player = World.getPlayers().get(playerIndex);
                    if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(tile)) {
                        continue;
                    }
                    player.getPackets().sendGraphics(graphics, tile);
                }
            }
        }
    }

    public static void sendProjectile(Actor shooter, WorldTile startTile, WorldTile receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
        for (int regionId : shooter.getMapRegionsIds()) {
            List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (Integer playerIndex : playersIndexes) {
                Player player = World.getPlayers().get(playerIndex);
                if (player == null || !player.hasStarted() || player.isFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
                    continue;
                }
                player.getPackets().sendProjectile(null, startTile, receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, 1);
            }
        }
    }

    public static void sendProjectile(Actor shooter, WorldTile receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
        for (int regionId : shooter.getMapRegionsIds()) {
            List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (Integer playerIndex : playersIndexes) {
                Player player = World.getPlayers().get(playerIndex);
                if (player == null || !player.hasStarted() || player.isFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
                    continue;
                }
                player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, shooter.getSize());
            }
        }
    }

    public static void sendProjectile(Actor shooter, Actor receiver, int gfxId, int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
        for (int regionId : shooter.getMapRegionsIds()) {
            List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
            if (playersIndexes == null) {
                continue;
            }
            for (Integer playerIndex : playersIndexes) {
                Player player = World.getPlayers().get(playerIndex);
                if (player == null || !player.hasStarted() || player.isFinished() || (!player.withinDistance(shooter) && !player.withinDistance(receiver))) {
                    continue;
                }
                int size = shooter.getSize();
                player.getPackets().sendProjectile(receiver, new WorldTile(shooter.getCoordFaceX(size), shooter.getCoordFaceY(size), shooter.getPlane()), receiver, gfxId, startHeight, endHeight, speed, delay, curve, startDistanceOffset, size);
            }
        }
    }

    public static void spawnTempGroundObject(final WorldObject object, final int replaceId, long time) {
        spawnObject(object);
        SystemManager.SLOW_EXECUTOR.schedule(() -> {
            try {
                removeObject(object);
                addGroundItem(new Item(replaceId), object, null, false, 180);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, time, TimeUnit.MILLISECONDS);
    }

    public static void sendObjectAnimation(Actor creator, WorldObject object, Animation animation) {
        if (creator == null) {
            for (Player player : World.getPlayers()) {
                if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(object)) {
                    continue;
                }
                player.getPackets().sendObjectAnimation(object, animation);
            }
        } else {
            for (int regionId : creator.getMapRegionsIds()) {
                List<Integer> playersIndexes = getRegion(regionId).getPlayerIndexes();
                if (playersIndexes == null) {
                    continue;
                }
                for (Integer playerIndex : playersIndexes) {
                    Player player = World.getPlayers().get(playerIndex);
                    if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(object)) {
                        continue;
                    }
                    player.getPackets().sendObjectAnimation(object, animation);
                }
            }
        }
    }

}
