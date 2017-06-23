package org.redrune.game.content.action.combat.player.swing;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatRegistry;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.calc.RangeCombatCalculator;
import org.redrune.game.content.action.combat.player.registry.BowFireEvent;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitAttributes;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RangeCombatSwing extends CombatTypeSwing {
	
	public RangeCombatSwing() {
		super(new RangeCombatCalculator());
	}
	
	@Override
	public boolean run(Player player, Entity target, int weaponId, int combatStyle, SpecialAttackEvent special) {
		int response = StaticCombatFormulae.getRangeResponse(player);
		if (response == 3) {
			player.getTransmitter().sendMessage("You don't have any more ammo left to use.");
			return false;
		} else if (response == 1) {
			player.getTransmitter().sendMessage("The ammo you're using is ineffective with your bow.");
			return false;
		}
		// we check the special attacks
		boolean usingSpecial = special != null;
		
		if (usingSpecial) {
			// set the special attack off now...
			player.getCombatDefinitions().setSpecialActivated(false);
			if (player.getCombatDefinitions().getSpecialEnergy() < special.energyRequired()) {
				player.getTransmitter().sendMessage("You don't have enough special attack energy.");
				usingSpecial = false;
			}
		}
		// custom attack send
		if (usingSpecial) {
			special.fire(player, target, this, combatStyle);
			player.getCombatDefinitions().reduceSpecial(special.energyRequired());
		} else {
			Optional<BowFireEvent> optional = CombatRegistry.getBow(weaponId);
			if (!optional.isPresent()) {
				player.getTransmitter().sendMessage("This bow has not yet been configured, please report it on the forums.");
				return false;
			}
			BowFireEvent event = optional.get();
			player.sendAnimation(StaticCombatFormulae.getWeaponAttackEmote(weaponId, player.getCombatDefinitions().getAttackStyle()));
			event.fire(player, target, this, weaponId, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS));
		}
		return true;
	}
	
	@Override
	public double getAttackBonus(Player player, int weaponId, int combatStyle) {
		return calculator.totalAggressiveBoost(player, combatStyle);
	}
	
	@Override
	public double getDefenceBonus(Entity entity, int weaponId, int combatStyle) {
		return calculator.totalDefensiveBoost(entity);
	}
	
	@Override
	public double getMaxHit(Player player, int weaponId, int combatStyle, double accuracyIncrease) {
		return calculator.maximumDamageAppendable(player, weaponId, combatStyle, accuracyIncrease);
	}
	
	@Override
	public void applyHit(Player attacker, Entity receiver, Hit hit, int itemId, int combatStyle, int delay) {
		appendExperience(attacker, receiver, hit.getDamage());
		SystemManager.getScheduler().schedule(new ScheduledTask(delay, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> {
					// the attribute is put when the hit actually appears
					hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
					// and the hit is applied to the receiver
					receiver.getHitMap().applyHit(attacker, hit);
				};
			}
		});
	}
	
	@Override
	public void appendExperience(Player player, Entity target, Object... params) {
		int damage = (int) params[0];
		
		if (damage > 0) {
			double combatXp = damage / 2.5;
			int attackStyle = player.getCombatDefinitions().getAttackStyle();
			if (attackStyle == 2) {
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.RANGE, combatXp / 2);
					player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, combatXp / 2);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.RANGE, combatXp / 2);
					player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, combatXp / 2);
				}
			} else {
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.RANGE, combatXp);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.RANGE, combatXp);
				}
			}
		}
	}
	
}
