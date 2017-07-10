package org.redrune.game.node.entity.player.link;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class ActivityManager {
	
	/**
	 * The instance of the player
	 */
	private transient Player player;
	
	/**
	 * The instance of the activity
	 */
	private Activity activity;
	
	/**
	 * Sets and starts an activity
	 *
	 * @param activity
	 * 		The activity
	 */
	public void startActivity(Activity activity) {
		this.activity = activity;
		this.activity.setPlayer(player);
		this.activity.start();
	}
	
	/**
	 * Ticks the activity
	 */
	public void process() {
		getActivity().ifPresent(Activity::tick);
	}
	
	/**
	 * Handles custom node interaction
	 *
	 * @param node
	 * 		The node
	 * @param option
	 * 		The option
	 */
	public boolean handleNodeInteraction(Node node, InteractionOption option) {
		return activity != null && activity.handleNodeInteraction(node, option);
	}
	
	/**
	 * If the activity handles the entity's death
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean handleEntityDeath(Entity entity) {
		return activity != null && activity.handleEntityDeath(entity);
	}
	
	/**
	 * Sets the activity player
	 *
	 * @param player
	 * 		The player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		getActivity().ifPresent(activity -> activity.setPlayer(player));
	}
	
	/**
	 * Gets the activity
	 */
	public Optional<Activity> getActivity() {
		return Optional.ofNullable(activity);
	}
	
	/**
	 * Ends the activity
	 */
	public void end() {
		this.activity = null;
	}
}
