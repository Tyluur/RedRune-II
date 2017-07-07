package org.redrune.game.content.activity.impl;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.content.activity.ActivityFlag;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class WildernessActivity extends Activity {
	
	/**
	 * If we're showing the skull on the wilderness level component
	 */
	private boolean showingSkull;
	
	public WildernessActivity(Object... parameters) {
		super(parameters);
		flag(ActivityFlag.SAVE_ON_LOGOUT);
	}
	
	@Override
	public void start() {
		checkLocations();
	}
	
	@Override
	public void updateLocation() {
		checkLocations();
	}
	
	/**
	 * Checks what to do based on our location
	 */
	private void checkLocations() {
		boolean isAtWild = isAtWild(player.getLocation());
		boolean isAtWildSafe = isAtWildSafe(player.getLocation());
		
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setInFightArea(true);
			player.getManager().getInterfaces().sendPrimaryOverlay(381);
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			player.getManager().getInterfaces().closePrimaryOverlay();
			player.setInFightArea(false);
			showingSkull = false;
		} else if (!isAtWildSafe && !isAtWild) {
			player.setInFightArea(false);
		} else if (isAtWild) {
			//			updateWildLevel();
		}
	}
	
	@Override
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		if (option == InteractionOption.ATTACK_OPTION) {
			if (player.getVariables().isInFightArea() && !target.getVariables().isInFightArea()) {
				player.getTransmitter().sendMessage("That player is not in the wilderness.", false);
				return false;
			}
			return wildernessLevelsVerified(target);
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the target's wilderness level is capable of fighting us
	 *
	 * @param target
	 * 		The target
	 */
	private boolean wildernessLevelsVerified(Player target) {
		if (!(Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= getWildLevel(player.getLocation()) && Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= getWildLevel(target.getLocation()))) {
			player.getTransmitter().sendMessage("You must travel deeper into the wilderness to attack that player.");
			return false;
		}
		return true;
	}
	
	/**
	 * If we are in the wilderness
	 *
	 * @param tile
	 * 		The tile to check for
	 */
	public static boolean isAtWild(Location tile) {
		return isAtWildSafe(tile) || getWildLevel(tile) > 0;
	}
	
	/**
	 * If the tile is at the safe area of the wilderness
	 */
	public static boolean isAtWildSafe(Location tile) {
		return (tile.getX() >= 2940 && tile.getX() <= 3395 && tile.getY() <= 3524 && tile.getY() >= 3523);
	}
	
	/**
	 * Gets the wilderness level at a lcoation
	 *
	 * @param tile
	 * 		The location
	 */
	public static int getWildLevel(Location tile) {
		int x = tile.getX(), y = tile.getY();
		int level = 0;
		if (y >= 10302 && y <= 10357) {
			level = (byte) ((y - 9912) / 8 + 1);
		}
		if (x > 2935 && x < 3400 && y > 3524 && y < 4000) {
			level = (byte) ((Math.ceil((y) - 3520D) / 8D) + 1);
		}
		if (y > 10050 && y < 10179 && x > 3008 && x < 3144) {
			level = (byte) ((Math.ceil((y) - 10048D) / 8D) + 17);
		}
		return level;
	}
}
