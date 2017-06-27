package org.redrune.game.node.entity;

import org.redrune.game.node.entity.data.Hit;

/**
 * This class contains methods that are necessary for the abstraction of entities.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public interface EntityDetails {
	
	/**
	 * Gets the hitpoints of the entity at the moment
	 */
	int getHitpoints();
	
	/**
	 * Gets the maximum hitpoints of the entity
	 */
	int getMaxHitpoints();
	
	/**
	 * What is executed on the tick of an entity.
	 */
	void tick();
	
	/**
	 * Receives the hit and makes any modification [soaking] necessary, as well as secondary listeners [prayer
	 * deflections]
	 *
	 * @param hit
	 * 		The hit received
	 */
	void receiveHit(Hit hit);
	
	/**
	 * If we are fighting
	 */
	boolean fighting();
	
}
