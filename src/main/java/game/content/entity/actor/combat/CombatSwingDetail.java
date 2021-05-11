package game.content.entity.actor.combat;

import game.entity.actor.Actor;
import game.entity.actor.mask.Hit;

import java.util.function.Consumer;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
public class CombatSwingDetail {
	
	/**
	 * The source of the swing
	 */
	private final Actor source;
	
	/**
	 * The target of the swing
	 */
	private final Actor target;
	
	/**
	 * The hit of the swing
	 */
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

    public Actor getSource() {
        return this.source;
    }

    public Actor getTarget() {
        return this.target;
    }

    public Hit getHit() {
        return this.hit;
    }
}
