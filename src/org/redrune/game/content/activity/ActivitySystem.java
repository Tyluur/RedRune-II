package org.redrune.game.content.activity;

import org.redrune.game.content.activity.impl.WildernessActivity;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class ActivitySystem {
	
	/**
	 * Fires the area activity
	 *
	 * @param player
	 * 		The player
	 */
	public static void fireAreaActivity(Player player) {
		// we have an activity so we dont force another to start
		if (player.getManager().getActivities().getActivity().isPresent()) {
			return;
		}
		// first check is the wilderness activity
		if (WildernessActivity.isAtWild(player.getLocation())) {
			startActivity(player, new WildernessActivity());
		}
	}
	
	/**
	 * Starts an activity and ends the previous one
	 *
	 * @param player
	 * 		The player
	 * @param activity
	 * 		The activity
	 */
	public static void startActivity(Player player, Activity activity) {
		player.getManager().getActivities().getActivity().ifPresent(Activity::end);
		player.getManager().getActivities().startActivity(activity);
	}
	
	/**
	 * Fires a location update to the player's activity
	 *
	 * @param player
	 * 		The player
	 */
	public static void fireLocationUpdate(Player player) {
		fireAreaActivity(player);
		player.getManager().getActivities().getActivity().ifPresent(Activity::updateLocation);
	}
	
}
