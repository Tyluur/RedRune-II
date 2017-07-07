package org.redrune.game.content.activity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

import java.util.BitSet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class Activity implements ActivityFlag {
	
	/**
	 * The player doing the activity
	 */
	@Getter
	@Setter
	protected transient Player player;
	
	/**
	 * The parameters of the activity
	 */
	@Getter
	protected final Object[] parameters;
	
	/**
	 * The flag set
	 */
	protected final BitSet flags = new BitSet();
	
	public Activity(Object... parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Handles the start of the activity
	 */
	public void start() {
	
	}
	
	/**
	 * Handles the change of location
	 */
	public void updateLocation() {
	
	}
	
	/**
	 * Each game tick in which the activity is set for a player, this method is called
	 */
	public void tick() {
	
	}
	
	/**
	 * Handles the end of an activity
	 */
	public void end() {
		player.getManager().getActivities().end();
	}
	
	/**
	 * Checks if this activity handles the interaction with a node
	 *
	 * @param node
	 * 		The node
	 * @param option
	 * 		The option
	 */
	public boolean handleNodeInteraction(Node node, InteractionOption option) {
		if (node.isPlayer() && handlePlayerOption(node.toPlayer(), option)) {
			return true;
		} else if (node.isNPC() && handleNPCOption(node.toNPC(), option)) {
			return true;
		} else if (node.isGameObject() && handleObject(node.toGameObject(), option)) {
			return true;
		} else if (node.isItem() && handleItem(node.toItem(), option)) {
			return true;
		}
		return false;
	}
	
	/**
	 * If the activity handles the player option
	 *
	 * @param target
	 * 		The target
	 * @param option
	 * 		The option
	 */
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		return false;
	}
	
	/**
	 * If the activity handles the npc option
	 *
	 * @param npc
	 * 		The npc
	 * @param option
	 * 		The option
	 */
	protected boolean handleNPCOption(NPC npc, InteractionOption option) {
		return false;
	}
	
	/**
	 * Handles the object interaction
	 *
	 * @param object
	 * 		The object
	 * @param option
	 * 		The option clicked
	 */
	protected boolean handleObject(GameObject object, InteractionOption option) {
		return false;
	}
	
	/**
	 * Handles the item interaction
	 *
	 * @param item
	 * 		The item
	 * @param option
	 * 		The option
	 */
	protected boolean handleItem(Item item, InteractionOption option) {
		return false;
	}
	
	/**
	 * Checks if we are at the activity
	 */
	public boolean isAtActivity() {
		return false;
	}
	
	/**
	 * Sets a flag to true
	 *
	 * @param flag
	 * 		The flag
	 */
	public void flag(byte flag) {
		this.flags.set(flag, true);
	}
	
	/**
	 * Checks if the activity saves on logout.
	 */
	public boolean savesOnLogout() {
		return flags.get(SAVE_ON_LOGOUT);
	}
}