package org.redrune.game.node.entity;

import lombok.Getter;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The entity's hit map. This holds the last 2 damages done to this player, a
 * record of which players hit what on this entity, ...
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 */
public final class HitMap {
	
	/**
	 * The entity.
	 */
	private transient final Entity entity;
	
	/**
	 * Holds all the hit data.
	 */
	private final Map<Player, Integer> hitRecord;
	
	/**
	 * A list of hits to deal.
	 */
	@Getter
	private final List<Hit> hitList;
	
	/**
	 * The damage constructor.
	 */
	HitMap(Entity entity) {
		this.entity = entity;
		this.hitRecord = new HashMap<>();
		this.hitList = new LinkedList<>();
	}
	
	/**
	 * Applies a hit to the entity
	 *
	 * @param attacker
	 * 		The attacker
	 * @param hit
	 * 		The hit
	 */
	public void applyHit(Entity attacker, Hit hit) {
		entity.receiveHit(attacker, hit);
		submitDamage(attacker, hit.getDamage());
		
		hitList.add(hit);
	}
	
	/**
	 * Submits damage to the hit record.
	 *
	 * @param attacker
	 * 		The attacking entity.
	 * @param damage
	 * 		The amount of damage.
	 */
	private void submitDamage(Entity attacker, int damage) {
		if (attacker == null || !attacker.isPlayer()) {
			return;
		}
		Player dealer = (Player) attacker;
		Integer totalDamage = hitRecord.get(dealer);
		if (totalDamage == null) {
			totalDamage = 0;
		}
		hitRecord.put(dealer, damage + totalDamage);
	}
	
	/**
	 * Gets the entity with the most damage.
	 *
	 * @return The Player with the most damage.
	 */
	public Entity getMostDamageEntity() {
		int currentMaxDamage = 0;
		Entity e = (entity instanceof Player ? (Player) entity : (entity));
		for (Player p : hitRecord.keySet()) {
			boolean bool = World.get().getPlayerByUsername(p.getDetails().getUsername()).isPresent();
			if (bool && hitRecord.get(p) > currentMaxDamage) {
				currentMaxDamage = hitRecord.get(p);
				e = p;
			} else if (!bool) {
				hitRecord.remove(p);
			}
		}
		return e;
	}
	
	/**
	 * Clears the hit record.
	 */
	public void clear() {
		hitRecord.clear();
	}
	
}
