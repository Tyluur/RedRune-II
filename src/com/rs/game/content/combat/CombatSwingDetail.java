package com.rs.game.content.combat;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Hit;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class CombatSwingDetail {
	
	/**
	 * The source of the swing
	 */
	@Getter
	private final Actor source;
	
	/**
	 * The target of the swing
	 */
	@Getter
	private final Actor target;
	
	/**
	 * The hit of the swing
	 */
	@Getter
	private final Hit hit;
	
	public CombatSwingDetail(Actor source, Actor target, Hit hit) {
		this.source = source;
		this.target = target;
		this.hit = hit;
	}
	
	/**
	 * Accepts the swing to the consumer
	 *
	 * @param consumer
	 * 		The consumer
	 */
	public CombatSwingDetail consume(Consumer<CombatSwingDetail> consumer) {
		consumer.accept(this);
		return this;
	}
}
