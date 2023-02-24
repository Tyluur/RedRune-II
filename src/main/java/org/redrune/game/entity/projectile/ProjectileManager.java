package org.redrune.game.entity.projectile;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;

import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
public class ProjectileManager {

    /**
     * Sends a projectile
     *
     * @param projectile The projectile
     */
    public static void sendProjectile(Projectile projectile) {
        WorldTile sourceLocation = projectile.getSourceTile();
        Region region = RegionManager.getRegion(sourceLocation.getRegionId());
        List<Integer> playersIndexes = region.getPlayerIndexes();
        if (playersIndexes == null) {
            return;
        }
        for (Integer playerIndex : playersIndexes) {
            Player player = World.getPlayers().get(playerIndex);
            if (player == null || !player.hasStarted() || player.isFinished() || !player.withinDistance(sourceLocation)) {
                continue;
            }
            player.getPackets().sendProjectile(projectile);
        }
    }

    /**
     * Creates a new projectile with the speed being calculated, based on the distance from each other [source-target]
     *
     * @param source       The source of the projectile
     * @param target       The target of the projectile
     * @param projectileId The id of the projectile
     * @param startHeight  The start height of the projectile
     * @param endHeight    The end height of the projectile
     * @param delay        The delay on the projectile
     * @param angle        The angle of the projectile
     * @param offset       The distance offset
     */
    public static Projectile createSpeedDefinedProjectile(Actor source, Actor target, int projectileId, int startHeight, int endHeight, int delay, int angle, int offset) {
        return new Projectile(source, target, projectileId, startHeight, endHeight, delay, getSpeedModifier(source, target), angle, offset);
    }

    /**
     * Gets the projectile speed modifier
     *
     * @param source The source
     * @param target The target
     */
    public static int getSpeedModifier(Actor source, Actor target) {
        return 46 + (getLocation(source).getDistance(target.getWorldTile()) * 5);
    }

    /**
     * Gets the source location on construction.
     *
     * @param n The node.
     * @return The centered location.
     */
    public static WorldTile getLocation(Actor n) {
        if (n == null) {
            return null;
        }
        return n.getCenterLocation();
    }

    /**
     * Gets the delay of a ranged/magic hit.
     *
     * @param attacker the attacking mob.
     * @param victim   the victim mob.
     * @param delay    the show delay of the projectile.
     * @param speed    the speed of the projectile (or slowness...the higher the speed the slower the delay).
     * @return the delay of a hit.
     */
    public static double getDelay(Actor attacker, Actor victim, int delay, int speed) {
        /* The distance between the entities. */
        double distance = attacker.getDistance(victim);

        /* The speed at which the projectile is traveling. */
        double projectileSpeed = (delay + speed + distance) * 5;

        /* The delay of the hit. */
        double hitDelay = (projectileSpeed * .02857);

        /* Returns the hit delay. */
        return hitDelay;
    }

    /**
     * Gets the delay before a projectile can arrive at a target. This is used for hit distance calculation, not actual
     * projectile speed calculation.
     *
     * @param source The source
     * @param target The target
     */
    public static int getProjectileDelay(Actor source, Actor target) {
        return 1 + (int) Math.ceil(source.getDistance(target) * 0.3);
    }
}
