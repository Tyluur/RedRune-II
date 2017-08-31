package com.rs.game.entity;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.object.WorldObject;

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
	 * If this entity is a player
	 */
	default boolean isPlayer() {
		return toPlayer() != null;
	}
	
	/**
	 * If this entity is an item
	 */
	default boolean isItem() {
		return toItem() != null;
	}
	
	/**
	 * If this entity is an object
	 */
	default boolean isObject() {
		return toObject() != null;
	}
	
	/**
	 * Casts this entity to the npc delegate, if possible. Only done in the appropriate class.
	 */
	default NPC toNPC() {
		return null;
	}
	
	/**
	 * Casts this entity to the player delegate, if possible. Only done in the appropriate class.
	 */
	default Player toPlayer() {
		return null;
	}
	
	/**
	 * Casts this entity to the item delegate, if possible. Only done in the appropriate class.
	 */
	default Item toItem() {
		return null;
	}
	
	/**
	 * Casts this entity to the object delegate, if possible. Only done in the appropriate class.
	 */
	default WorldObject toObject() {
		return null;
	}
	
}
