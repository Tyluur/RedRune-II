package org.redrune.game.content.action.combat.player.swing;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatRegistry;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.calc.MagicCombatCalculator;
import org.redrune.game.content.action.combat.player.registry.MagicSpellContext;
import org.redrune.game.content.action.combat.player.registry.MagicSpellEvent;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitAttributes;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class MagicCombatSwing extends CombatTypeSwing {
	
	public MagicCombatSwing() {
		super(new MagicCombatCalculator());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean run(Player player, Entity target, int spellId, int combatStyle, SpecialAttackEvent special) {
		final boolean regularCast = player.getCombatDefinitions().getAutocastId() == -1;
		// we're not auto-casting so we should reset the spell
		if (regularCast) {
			player.getCombatDefinitions().resetSpells(false);
			player.getManager().getActions().stopAction();
		}
		// we don't have the runes anymore
		if (!CombatRegistry.checkCombatSpell(player, spellId, -1, true)) {
			if (!regularCast) {
				player.getCombatDefinitions().resetSpells(true);
			}
			return false;
		}
		// finds the spell to cast
		Optional<MagicSpellEvent<?>> spellOptional = CombatRegistry.getSpell(player.getCombatDefinitions().getSpellbook(), spellId);
		// spell hasn't been registered yet
		if (!spellOptional.isPresent()) {
			player.getTransmitter().sendMessage("This spell has not yet been added! Please suggest it on forums.");
			return false;
		}
		// spell instasnce
		MagicSpellEvent spellEvent = spellOptional.get();
		switch (player.getCombatDefinitions().getSpellbook()) {
			case REGULAR:
			case ANCIENTS:
			case LUNARS:
				switch (spellId) {
					default:
						// all spells usually are cast like this
						// context will change for spells that have different parameters.
						spellEvent.cast(player, new MagicSpellContext(target, this));
						break;
				}
				break;
		}
		return true;
	}
	
	@Override
	public double getAttackBonus(Player player, int weaponId, int combatStyle) {
		return 0;
	}
	
	@Override
	public double getDefenceBonus(Entity entity, int weaponId, int combatStyle) {
		return 0;
	}
	
	@Override
	public double getMaxHit(Player player, int weaponId, int combatStyle, double accuracyIncrease) {
		return 0;
	}
	
	@Override
	public void appendExperience(Player player, Entity target, Object... params) {
		double magicExp = (double) params[0];
		int damage = (int) params[1];
		
		double combatXp = magicExp * 1 + (damage / 5);
		if (combatXp <= 0) {
			return;
		}
		if (player.getCombatDefinitions().isDefensiveCasting()) {
			double defenceXp = damage / 3;
			if (defenceXp > 0) {
				combatXp -= defenceXp;
				if (target.isPlayer()) {
					player.getSkills().addExperienceNoMultiplier(SkillConstants.DEFENCE, defenceXp);
				} else {
					player.getSkills().addExperienceWithMultiplier(SkillConstants.DEFENCE, defenceXp);
				}
			}
		}
		if (target.isPlayer()) {
			player.getSkills().addExperienceNoMultiplier(SkillConstants.MAGIC, combatXp);
		} else {
			player.getSkills().addExperienceWithMultiplier(SkillConstants.MAGIC, combatXp);
		}
		
		double hpExp = damage / 7.5;
		if (hpExp > 0) {
			if (target.isPlayer()) {
				player.getSkills().addExperienceNoMultiplier(SkillConstants.HITPOINTS, hpExp);
			} else {
				player.getSkills().addExperienceWithMultiplier(SkillConstants.HITPOINTS, hpExp);
			}
		}
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param event
	 * 		The magic spell event
	 * @return If the hit landed (damage > 0).
	 */
	public boolean sendSpell(Player player, Entity target, MagicSpellEvent event) {
		return sendSpell(player, target, event, null, null);
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param event
	 * 		The magic spell event
	 * @param sendTask
	 * 		The task that is executed when the spell is used
	 * @param hitLandTask
	 * 		The task that is executed when the hit lands.
	 * @return If the hit landed (damage > 0).
	 */
	public boolean sendSpell(Player player, Entity target, MagicSpellEvent event, Runnable sendTask, Runnable hitLandTask) {
		int maxHit = event.maxHit();
		int damage = randomizeHit(maxHit, calculator.totalAggressiveBoost(player), calculator.totalDefensiveBoost(target));
		appendExperience(player, target, event.exp(), damage);
		
		int delay = getProjectileDelay(player, target);
		
		final Hit hit = new Hit(player, damage, HitSplat.MAGIC_DAMAGE).setMaxHit(maxHit);
		if (sendTask != null) {
			sendTask.run();
		}
		SystemManager.getScheduler().schedule(new ScheduledTask(1, delay, false) {
			
			@Override
			public Runnable getTask() {
				return () -> {
					// so we don't have to make a new task for blocking.
					if (getPulseCount() == getMaxPulses() - 2) {
						target.sendAwaitedAnimation(target.isPlayer() ? StaticCombatFormulae.getDefenceEmote(target.toPlayer()) : -1);
					} else if (getPulseCount() == getMaxPulses() - 1) {
						// the attribute is put when the hit actually appears
						hit.getAttributes().put(HitAttributes.WEAPON_USED, player.getEquipment().getWeaponId());
						// and the hit is applied to the receiver
						target.getHitMap().applyHit(player, hit);
						if (damage == 0) {
							target.sendGraphics(85, 96, 0);
						} else {
							if (event.hitGfx() != -1) {
								target.sendGraphics(event.hitGfx(), event.gfxHeight(), 0);
							}
						}
						if (hitLandTask != null) {
							hitLandTask.run();
						}
						stop();
					}
				};
			}
		});
		return damage > 0;
	}
}
