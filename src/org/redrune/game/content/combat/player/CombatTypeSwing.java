package org.redrune.game.content.combat.player;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitAttributes;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.RandomFunction;

/**
 * Handles the swing of the combat type.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public abstract class CombatTypeSwing {
	
	/**
	 * Handles the running of the combat type
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target we're attacking
	 * @param id
	 * 		The weapon or spell used
	 * @param combatStyle
	 * 		The combat style used
	 * @param special
	 * 		The special attack
	 */
	public abstract boolean run(Player player, org.redrune.game.node.entity.Entity target, int id, int combatStyle, SpecialAttackEvent special);
	
	/**
	 * Gets the attack bonus
	 *
	 * @param player
	 * 		The player
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 * @param specialAttack
	 * 		If we are using a special attack
	 */
	public abstract double getAttackBonus(Player player, int weaponId, int combatStyle, boolean specialAttack);
	
	/**
	 * Gets the defence bonus
	 *
	 * @param entity
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 */
	public abstract double getDefenceBonus(org.redrune.game.node.entity.Entity entity, int weaponId, int combatStyle);
	
	/**
	 * Gets the max hit bonus
	 *
	 * @param player
	 * 		The entity
	 * @param weaponId
	 * 		The weapon used
	 * @param combatStyle
	 * 		The combat style used
	 * @param multiplier
	 * 		The max hit multiplier
	 */
	public abstract double getMaxHit(Player player, int weaponId, int combatStyle, double multiplier);
	
	/**
	 * The combat calculator used for this swing type
	 */
	protected final CombatTypeCalculator calculator;
	
	public CombatTypeSwing(CombatTypeCalculator calculator) {
		this.calculator = calculator;
	}
	
	/**
	 * Gets the delay of a ranged/magic hit.
	 *
	 * @param attacker
	 * 		the attacking mob.
	 * @param victim
	 * 		the victim mob.
	 * @param delay
	 * 		the show delay of the projectile.
	 * @param speed
	 * 		the speed of the projectile (or slowness...the higher the speed the slower the delay).
	 * @return the delay of a hit.
	 */
	public static double getDelay(Entity attacker, Entity victim, int delay, int speed) {
		/* The distance between the entities. */
		double distance = attacker.getLocation().getDistance(victim.getLocation());
		
		/* The speed at which the projectile is traveling. */
		double projectileSpeed = (delay + speed + distance) * 5;
		
		/* The delay of the hit. */
		double hitDelay = (projectileSpeed * .02857);
		
		/* Returns the hit delay. */
		return hitDelay;
	}
	
	/**
	 * Calculates a random hit
	 *
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	public int randomizeHit(double maxHit, double attackBonus, double defenceBonus) {
		if (!rollHit(attackBonus, defenceBonus)) {
			//System.out.println("rolled a miss [" + maxHit + ", " + attackBonus + ", " + defenceBonus + "]");
			return 0;
		}
		// the random hit
		int random = RandomFunction.random((int) maxHit);
		// the count index used for re-rolls
		int count = 0;
		// we dont want too low too often, so we reroll
		while (random <= (maxHit * 0.25) && count < 3) {
			random = RandomFunction.random((int) maxHit);
			//System.out.println("rerolled a " + random + " and we got " + random + "[#" + count + "]");
			count++;
		}
		if (random == 0) {
			//System.out.println("Rerolled " + count + " times and got a " + random);
		}
		return random;
	}
	
	/**
	 * Calculates the two modifiers and checks if the hit should randomly miss
	 *
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	private boolean rollHit(double attackBonus, double defenceBonus) {
/*		System.out.println("attackBonus = [" + attackBonus + "], defenceBonus = [" + defenceBonus + "]");
		final boolean hasAttack = attackBonus >= 0;
		return hasAttack && (defenceBonus < 0 || RandomFunction.getRandomDouble((attackBonus + defenceBonus)) > defenceBonus);*/
/*

		double mod = 1.33;
		if (victim == null || style == null) {
			return false;
		}
		if (style != null) {
			if (victim instanceof Player && entity instanceof Familiar && ((Player) victim).getPrayer().get(PrayerType.PROTECT_FROM_SUMMONING)) {
				mod = 0;
			}
		}
		double attackBonus = calculateAccuracy(entity) * accuracyMod * mod * getSetMultiplier(entity, Skills.ATTACK);
		double defenceBonus = calculateDefence(victim, entity) * defenceMod * getSetMultiplier(victim, Skills.DEFENCE);
		double chance = 0.0;
		if (attackBonus < defenceBonus) {
			chance = (attackBonus - 1) / (defenceBonus * 2);
		} else {
			chance = 1 - ((defenceBonus + 1) / (attackBonus * 2));
		}
		double ratio = chance * 100;
		double accuracy = Math.floor(ratio);
		double block = Math.floor(101 - ratio);
		double acc = Math.random() * accuracy;
		double def = Math.random() * block;
		return acc > def;*/
		
		double attack = attackBonus * 1.33;
		double defence = defenceBonus * 1.0D;
		
		double chance;
		if (attack < defence) {
			chance = (attack - 1) / (defence * 2);
		} else {
			chance = 1 - ((defence + 1) / (attack * 2));
		}
		double ratio = Math.floor(chance * 100);
		double block = Math.floor(101 - ratio);
		double acc = RandomFunction.getRandomDouble(ratio);
		double def = RandomFunction.getRandomDouble(block);
		int count = 0;
		if (acc < def) {
			do {
				acc = RandomFunction.getRandomDouble(ratio);
				def = RandomFunction.getRandomDouble(block);
//				System.out.println("low random roll {" + acc + ", " + def + "} #" + count);
				count++;
			} while ((acc > def) && count < 10);
		}
//		System.out.println("attackBonus = [" + attackBonus + "], defenceBonus = [" + defenceBonus + "], chance=" + chance + ", ratio=" + ratio + ", block=" + block + ", attack=" + attack + ", defence=" + def + ", acc = { " + acc + "}, def = { " + def + "}");
		return acc >= def;
	}
	
	/**
	 * Applies a hit to the receiver
	 *
	 * @param attacker
	 * 		The attacking player
	 * @param receiver
	 * 		The receiver of the hit
	 * @param hit
	 * 		The hit
	 * @param itemId
	 * 		The item id used
	 * @param combatStyle
	 * 		The combat style
	 * @param delay
	 * 		The delay for the hit
	 */
	public void applyHit(Player attacker, Entity receiver, Hit hit, int itemId, int combatStyle, int delay) {
		appendExperience(attacker, receiver, itemId, combatStyle, hit.getDamage());
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				// the attribute is put when the hit actually appears
				hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
				// and the hit is applied to the receiver
				receiver.getHitMap().applyHit(hit);
			}
		});
	}
	
	/**
	 * Sends the experience task. The block emote is also sent in this block
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param params
	 * 		The parameters
	 */
	public abstract void appendExperience(Player player, org.redrune.game.node.entity.Entity target, Object... params);
	
	/**
	 * Gets the delay before a projectile can arrive at a target.
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public static int getProjectileDelay(Player player, Entity target) {
		return 1 + (int) Math.ceil(player.getLocation().getDistance(target.getLocation()) * 0.3);
	}
	
}
