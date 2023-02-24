package org.redrune.game.content.entity.actor.player.event.player;

import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.route.RouteFinder;
import org.redrune.game.global.map.route.strategy.FixedTileStrategy;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-06
 */
public class PlayerWalkEvent extends Event {

    /**
     * The destination x to travel to
     */
    private final int destX;

    /**
     * The destination y to travel to
     */
    private final int destY;

    /**
     * If the player should be forced to run
     */
    private final boolean forceRun;

    public PlayerWalkEvent(int destX, int destY, boolean forceRun) {
        this.destX = destX;
        this.destY = destY;
        this.forceRun = forceRun;
    }

    @Override
    public void run(Player player) {
        if (!player.hasStarted() || !player.getAttributes().clientHasLoadedMapRegion() || player.isDead()) {
            return;
        }
        long currentTime = Misc.currentTimeMillis();
        if (player.getLocks().isMovementLocked()) {
            return;
        }
        if (player.getFreezeDelay() >= currentTime) {
            player.getPackets().sendMessage("A magical force prevents you from moving.");
            return;
        }
        player.stopAll();
        // forces the new run flag
        if (forceRun) {
            player.setRunModeOn(true);
        }
        // calculates the amount of steps in the path
        int calculatedSteps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), new FixedTileStrategy(destX, destY), true);
        // the buffer with the x steps
        int[] bufferX = RouteFinder.getLastPathBufferX();
        // the buffer with they steps
        int[] bufferY = RouteFinder.getLastPathBufferY();

        // adds walk steps to the movement queue
        int last = -1;
        for (int i = calculatedSteps - 1; i >= 0; i--) {
            if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
                break;
            }
            last = i;
        }

        // sends destination on the minimap
        if (last != -1) {
            WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
            player.getPackets().sendMinimapFlag(tile.getLocalX(player.getLastLoadedMapRegionTile()), tile.getLocalY(player.getLastLoadedMapRegionTile()));
        } else {
            player.getPackets().sendResetMinimapFlag();
        }
    }

    @Override
    public EventPolicy[] policies() {
        return arguments(EventPolicy.STOP_WALK, EventPolicy.CLOSE_INTERFACE);
    }
}
