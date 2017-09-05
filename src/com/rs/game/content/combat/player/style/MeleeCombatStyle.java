package com.rs.game.content.combat.player.style;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.content.combat.CombatRoll;
import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.content.combat.player.calc.MeleeCombatCalculator;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.data.CombatDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitSplat;
import com.rs.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public class MeleeCombatStyle extends AbstractCombatStyle {
	
	public MeleeCombatStyle() {
		super(new MeleeCombatCalculator());
	}
	
	@Override
	public void fireSwing(Player source, Actor target) {
		int weaponId = source.getEquipment().getWeaponId();
		int combatStyle = source.getCombatDefinitions().getAttackStyle();
		
		String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName();
		
		// the damage
		int damage = getRandomDamage(source, target);
		
		// the delay on the hit
		final int hitDelay = weaponId == 10887 || (weaponName.toLowerCase().contains("maul") && !weaponName.startsWith("Granite")) ? 2 : 1;
		
		// animations
		final int animation = CombatAlgorithm.getWeaponAttackEmote(weaponId, combatStyle);
		
		// sends the hit to the target
		sendHit(source, target, calculator.getMaximumHit(source), damage);
		
		// the player does the attack animation
		source.setNextAnimation(new Animation(animation));
	}
	
	@Override
	public void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId) {
		int damage = hit.getDamage();
		double combatXp = damage / 2.5;
		if (combatXp > 0) {
			source.getAuraManager().checkSuccefulHits(hit.getDamage());
			if (hit.getLook() == HitSplat.RANGE_DAMAGE) {
				if (attackStyle == 2) {
					if (target.isPlayer()) {
						source.getSkills().addXpNoModifier(RANGE, combatXp / 2);
						source.getSkills().addXpNoModifier(DEFENCE, combatXp / 2);
					} else {
						source.getSkills().addXp(RANGE, combatXp / 2);
						source.getSkills().addXp(DEFENCE, combatXp / 2);
					}
				} else {
					if (target.isPlayer()) {
						source.getSkills().addXpNoModifier(RANGE, combatXp);
					} else {
						source.getSkills().addXp(RANGE, combatXp);
					}
				}
			} else {
				int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
				if (xpStyle != CombatDefinitions.SHARED) {
					if (target.isPlayer()) {
						source.getSkills().addXpNoModifier(xpStyle, combatXp);
					} else {
						source.getSkills().addXp(xpStyle, combatXp);
					}
				} else {
					if (target.isPlayer()) {
						source.getSkills().addXpNoModifier(ATTACK, combatXp / 3);
						source.getSkills().addXpNoModifier(STRENGTH, combatXp / 3);
						source.getSkills().addXpNoModifier(DEFENCE, combatXp / 3);
					} else {
						source.getSkills().addXp(ATTACK, combatXp / 3);
						source.getSkills().addXp(STRENGTH, combatXp / 3);
						source.getSkills().addXp(DEFENCE, combatXp / 3);
					}
				}
			}
			double hpXp = damage / 7.5;
			if (hpXp > 0) {
				if (target.isPlayer()) {
					source.getSkills().addXpNoModifier(HITPOINTS, hpXp);
				} else {
					source.getSkills().addXp(HITPOINTS, hpXp);
				}
			}
		}
	}
	
	@Override
	public int getRandomDamage(Player source, Actor target) {
		int weaponId = source.getEquipment().getWeaponId();
		int combatStyle = source.getCombatDefinitions().getAttackStyle();
		return CombatRoll.randomizeHit(calculator.getMaximumHit(source), calculator.getAttackBonus(source), calculator.getDefenceBonus(target, weaponId, combatStyle));
	}
	
	@Override
	public void sendHit(Actor source, Actor target, int maxHit, int damage) {
		System.out.println("max=" + maxHit);
		target.applyHit(new Hit(source, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit));
	}
}
