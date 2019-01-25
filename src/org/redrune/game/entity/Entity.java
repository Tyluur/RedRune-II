package org.redrune.game.entity;

import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public interface Entity {
	
	/**
	 * If this entity is an npc
	 */
	default boolean isNPC() {
		return toNPC() != null;
	}
	
	/**
	 * Casts this entity to the npc delegate, if possible. Only done in the appropriate class.
	 */
	default NPC toNPC() {
		return null;
	}
	
	/**
	 * If this entity is a player
	 */
	default boolean isPlayer() {
		return toPlayer() != null;
	}
	
	/**
	 * Casts this entity to the player delegate, if possible. Only done in the appropriate class.
	 */
	default Player toPlayer() {
		return null;
	}
	
	/**
	 * If this entity is an item
	 */
	default boolean isItem() {
		return toItem() != null;
	}
	
	/**
	 * Casts this entity to the item delegate, if possible. Only done in the appropriate class.
	 */
	default Item toItem() {
		return null;
	}
	
	/**
	 * If this entity is an object
	 */
	default boolean isObject() {
		return toObject() != null;
	}
	
	/**
	 * Casts this entity to the object delegate, if possible. Only done in the appropriate class.
	 */
	default WorldObject toObject() {
		return null;
	}
	
}
