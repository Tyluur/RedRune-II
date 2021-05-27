package org.redrune.game.content.entity.actor.player.wilderness;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;
import java.util.List;

public class WildernessObelisk {

    /**
     * The left-most tiles for the obelisks
     */
    public static final WorldTile[] OBELISK_CENTER_TILES = {new WorldTile(2978, 3864, 0), new WorldTile(3033, 3730, 0), new WorldTile(3104, 3792, 0), new WorldTile(3154, 3618, 0), new WorldTile(3217, 3654, 0), new WorldTile(3305, 3914, 0)};

    /**
     * The active obelisks
     */
    private static final boolean[] ACTIVE_OBELISK = new boolean[6];

    /**
     * Activates the obelisk by a player
     *
     * @param id     The id of the obelisk
     * @param player The player
     */
    public static void activateObelisk(int id, final Player player) {
        final int index = id - 14826;
        final WorldTile center = OBELISK_CENTER_TILES[index];
        if (ACTIVE_OBELISK[index]) {
            player.getPackets().sendMessage("The obelisk is already active.");
            return;
        }
        ACTIVE_OBELISK[index] = true;
        WorldObject object = RegionManager.getObjectWithId(center, id);
        if (object == null) {
            return;
        }
        List<WorldObject> affected = new ArrayList<>();
        int[][] transformations = {{4, 0, 0}, {0, 4, 0}, {4, 4, 0}};
        for (int[] transformation : transformations) {
            WorldObject transformed = RegionManager.getObjectWithId(center.transform(transformation[0], transformation[1], transformation[2]), object.getId());
            affected.add(transformed);
        }
        affected.add(object);
        affected.forEach(obelisk -> RegionManager.spawnObject(new WorldObject(14825, 10, 0, obelisk.getWorldTile())));

        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                for (int x = 1; x < 4; x++) {
                    for (int y = 1; y < 4; y++) {
                        RegionManager.sendGraphics(player, new Graphics(661), center.transform(x, y, 0));
                    }
                }
                teleportPlayers();
                ACTIVE_OBELISK[index] = false;
                affected.forEach(RegionManager::spawnObject);
            }

            /**
             * Teleports the players close to the obelisk to the new position
             */
            private void teleportPlayers() {
                Region region = RegionManager.getRegion(center.getRegionId());
                List<Integer> playerIndexes = region.getPlayerIndexes();
                WorldTile newCenter = OBELISK_CENTER_TILES[Misc.random(OBELISK_CENTER_TILES.length)];
                if (playerIndexes != null) {
                    for (Integer i : playerIndexes) {
                        Player p = World.getPlayers().get(i);
                        if (p == null || (p.getX() < center.getX() + 1 || p.getX() > center.getX() + 3 || p.getY() < center.getY() + 1 || p.getY() > center.getY() + 3)) {
                            continue;
                        }
                        int offsetX = p.getX() - center.getX();
                        int offsetY = p.getY() - center.getY();
                        Magic.sendTeleportSpell(p, 8939, 8941, 1690, -1, 0, 0, new WorldTile(newCenter.getX() + offsetX, newCenter.getY() + offsetY, 0), 3, false, MagicConstants.OBJECT_TELEPORT);
                        p.getPackets().sendMessage("Ancient magic teleports you to a place within the wilderness!", true);
                    }
                }
            }

        }, 8);

    }
}

