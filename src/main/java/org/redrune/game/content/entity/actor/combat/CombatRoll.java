package org.redrune.game.content.entity.actor.combat;

import org.redrune.utility.functions.RandomFunction;

/**
 * This class handles the roll of combat hits (finding out the damage to apply, and whether or not the swing should be a
 * miss)
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public final class CombatRoll {
	
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
	public static int randomizeHit(double maxHit, double attackBonus, double defenceBonus) {
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
	 * Calculates a random hit
	 *
	 * @param minimumHit
	 * 		The minimum damage
	 * @param maxHit
	 * 		The max hit
	 * @param attackBonus
	 * 		The attack bonus
	 * @param defenceBonus
	 * 		The defence bonus
	 */
	public static int randomizeHit(double minimumHit, double maxHit, double attackBonus, double defenceBonus, boolean roll) {
		if (roll && !rollHit(attackBonus, defenceBonus)) {
			//System.out.println("rolled a miss [" + maxHit + ", " + attackBonus + ", " + defenceBonus + "]");
			return 0;
		}
		// the random hit
		int random = (int) RandomFunction.random(minimumHit, maxHit);
		// the count index used for re-rolls
		int count = 0;
		// we dont want too low too often, so we reroll
		while (random <= (maxHit * 0.25) && count < 3) {
			random = (int) RandomFunction.random(minimumHit, maxHit);
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
	private static boolean rollHit(double attackBonus, double defenceBonus) {
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
}
