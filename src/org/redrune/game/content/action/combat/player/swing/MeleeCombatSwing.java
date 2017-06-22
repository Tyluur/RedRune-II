package org.redrune.game.content.action.combat.player.swing;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.calc.MeleeCombatCalculator;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitAttributes;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class MeleeCombatSwing extends CombatTypeSwing {
	
	public MeleeCombatSwing() {
		super(new MeleeCombatCalculator());
	}
	
	// TODO: healing from guthans
	@Override
	public void run(Player player, Entity target, int id, int combatStyle) {
		String weaponName = id == -1 ? "unarmed" : ItemDefinitionParser.forId(id).getName();
		// calculations are done first
		final double maxHit = calculator.maximumDamageAppendable(player, id, combatStyle);
		final double attackBonus = calculator.totalAggressiveBoost(player, id, combatStyle);
		final double defenceBonus = calculator.totalDefensiveBoost(target.toPlayer(), id, combatStyle);
		
		// animations
		final int animation = StaticCombatFormulae.getWeaponAttackEmote(id, combatStyle);
		final int blockEmote = target.isPlayer() ? StaticCombatFormulae.getDefenceEmote(target.toPlayer()) : -1;
		
		// finalizing item id because of multithread possible problems
		final int itemId = id;
		
		// the hit (randomized)
		int damage = randomizeHit(maxHit, attackBonus, defenceBonus);
		
		// the delay on the hit
		int delay = 2;
		if (id == 10887 || (weaponName.toLowerCase().contains("maul") && !weaponName.startsWith("Granite"))) {
			delay = 3;
		}
		// constructs the hit and sets its delay
		final Hit hit = new Hit(player, damage, HitSplat.MELEE_DAMAGE);
		hit.setMaxHit(maxHit);
		
		// the block emote is done 1 tick after
		SystemManager.getScheduler().schedule(new ScheduledTask(1, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> {
					if (damage > 0) {
						int xpSlot = StaticCombatFormulae.getXpStyle(id, combatStyle);
						// shared mode
						if (xpSlot == -1) {
							double combatXp = damage / 2.5;
							if (target.isPlayer()) {
								player.getSkills().addExperienceNoMultiplier(SkillConstants.ATTACK, combatXp / 3);
								player.getSkills().addExperienceNoMultiplier(SkillConstants.STRENGTH, combatXp / 3);
								player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, combatXp / 3);
							} else {
								player.getSkills().addExperienceWithMultiplier(SkillConstants.ATTACK, combatXp / 3);
								player.getSkills().addExperienceWithMultiplier(SkillConstants.STRENGTH, combatXp / 3);
								player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, combatXp / 3);
							}
						} else {
							if (target.isPlayer()) {
								player.getSkills().addExperienceNoMultiplier((short) xpSlot, damage / 2.5);
								player.getSkills().addExperienceNoMultiplier(SkillConstants.HITPOINTS, damage / 7.5);
							} else {
								player.getSkills().addExperienceWithMultiplier((short) xpSlot, damage / 2.5);
								player.getSkills().addExperienceWithMultiplier(SkillConstants.HITPOINTS, damage / 7.5);
							}
						}
					}
					// block emote
					target.sendAwaitedAnimation(blockEmote);
				};
			}
		});
		SystemManager.getScheduler().schedule(new ScheduledTask(delay, 1, false) {
			@Override
			public Runnable getTask() {
				return () -> {
					// the attribute is put when the hit actually appears
					hit.getAttributes().put(HitAttributes.WEAPON_USED, itemId);
					// and the hit is applied to the target
					target.getHitMap().applyHit(player, hit);
				};
			}
		});
		// the player does the attack animation
		player.sendAnimation(animation);
	}
}
