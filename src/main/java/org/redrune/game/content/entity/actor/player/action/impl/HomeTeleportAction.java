package org.redrune.game.content.entity.actor.player.action.impl;

import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.content.entity.actor.player.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.functions.Misc;

public class HomeTeleportAction extends Action {
	
	protected static final int HOME_ANIMATION = 16385;
	
	protected static final int HOME_GRAPHIC = 3017;
	
	protected static final int DONE_ANIMATION = 16386;
	
	private int currentTime;
	
	private WorldTile tile;
	
	@Override
	public boolean start(final Player player) {
		tile = GameConstants.RESPAWN_PLAYER_LOCATION;
		if (!player.getControllerManager().processMagicTeleport(tile)) {
			return false;
		}
		return process(player);
	}
	
	@Override
	public boolean process(Player player) {
		if (player.getAttackedByDelay() + 10000 > Misc.currentTimeMillis()) {
			player.getPackets().sendMessage("You can't home teleport until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}
	
	@Override
	public int processWithDelay(Player player) {
		player.getWalkSteps().clear();
		if (currentTime++ == 0) {
			player.setNextAnimation(new Animation(HOME_ANIMATION));
			player.setNextGraphics(new Graphics(HOME_GRAPHIC));
		} else if (currentTime == 17) {
			WorldTile teleTile = tile;
			// attemps to randomize tile by 4x4 area
			for (int trycount = 0; trycount < 10; trycount++) {
				teleTile = new WorldTile(tile, 2);
				if (RegionManager.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize())) {
					break;
				}
				teleTile = tile;
			}
			player.setNextWorldTile(teleTile);
			player.setNextAnimation(new Animation(HOME_ANIMATION + 1));
			player.setNextGraphics(new Graphics(HOME_GRAPHIC + 1));
			player.getControllerManager().magicTeleported(MagicConstants.MAGIC_TELEPORT);
			if (player.getControllerManager().getController() == null) {
				Magic.teleControllersCheck(player, teleTile);
			}
			// return 0;
		} else if (currentTime == 21) {
			player.setNextAnimation(new Animation(-1));
			player.setNextGraphics(new Graphics(-1));
			return -1;
		}
		return 0;
	}
	
	@Override
	public void stop(Player player) {
	
	}
	
}