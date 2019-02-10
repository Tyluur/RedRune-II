package org.redrune.game.content.entity.actor.combat.player.style;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.CombatRoll;
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.entity.actor.combat.player.calc.RangeCombatCalculator;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class RangeCombatStyle extends AbstractCombatStyle {
	
	public RangeCombatStyle() {
		super(new RangeCombatCalculator());
	}
	
	@Override
	public boolean fireSwing(Player source, Actor target) {
		int response = CombatAlgorithm.getRangeResponse(source);
		if (response == 3) {
			source.getPackets().sendGameMessage("You don't have any more ammo left to use.");
			return false;
		} else if (response == 1) {
			source.getPackets().sendGameMessage("The ammo you're using is ineffective with your bow.");
			return false;
		}
		int weaponId = source.getEquipment().getWeaponId();
		if (source.getCombatDefinitions().isUsingSpecialAttack()) {
			Optional<SpecialAttackPlugin> optional = PluginRepository.getSpecialPlugin(weaponId);
			int energy = CombatAlgorithm.getSpecialAmount(weaponId);
			if (energy == 0 || !optional.isPresent()) {
				source.getPackets().sendGameMessage("This weapon has no special attack registered; please report this on forums.");
				return false;
			}
			source.getCombatDefinitions().switchUsingSpecialAttack();
			if (source.getCombatDefinitions().getSpecialAttackPercentage() < energy) {
				source.getPackets().sendGameMessage("You don't have enough power left.");
				return fireSwing(source, target);
			}
			SpecialAttackPlugin plugin = optional.get();
			plugin.fire(source, target, this);
			source.getCombatDefinitions().decreaseSpecialEnergy(energy);
		} else {
			Optional<RangeWeaponPlugin> optional = PluginRepository.getRangeWeapon(weaponId);
			if (!optional.isPresent()) {
				source.getPackets().sendGameMessage("This bow has not yet been configured, please report it on the forums.");
				return false;
			}
			RangeWeaponPlugin event = optional.get();
			source.setNextAnimation(new Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, source.getCombatDefinitions().getAttackStyle())));
			event.fire(source, target, this);
		}
		return true;
	}
	
	@Override
	public void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId) {
		int damage = hit.getDamage();
		double combatXp = damage / 2.5;
		if (combatXp > 0) {
			source.getAuraManager().checkSuccefulHits(hit.getDamage());
			if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
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
	public int getRandomDamage(Player source, Actor target, double multiplier) {
		int weaponId = source.getEquipment().getWeaponId();
		int combatStyle = source.getCombatDefinitions().getAttackStyle();
		return CombatRoll.randomizeHit(calculator.getMaximumHit(source, 1), calculator.getAttackBonus(source), calculator.getDefenceBonus(target, weaponId, combatStyle));
	}
	
	@Override
	public CombatSwingDetail sendHit(Player source, Actor target, int maxHit, int damage, int delay) {
		final Hit hit = new Hit(source, damage, HitSplat.RANGE_DAMAGE).setMaxHit(maxHit);
		addExperience(source, target, hit, source.getCombatDefinitions().getAttackStyle(), source.getEquipment().getWeaponId());
		
		SystemManager.SCHEDULER.schedule(new ScheduledTask(1, delay) {
			@Override
			public void run() {
				if (getTicksPassed() == getGoalTicks() - 1) {
					target.setNextAnimationNoPriority(new Animation(CombatAlgorithm.getDefenceEmote(target)));
				} else if (getTicksPassed() == getGoalTicks()) {
					target.applyHit(hit);
					stop();
				}
			}
		});
		return new CombatSwingDetail(source, target, hit);
	}
}
