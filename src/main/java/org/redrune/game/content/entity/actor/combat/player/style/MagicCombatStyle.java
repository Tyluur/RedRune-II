package org.redrune.game.content.entity.actor.combat.player.style;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.CombatRoll;
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.entity.actor.combat.player.calc.MagicCombatCalculator;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public class MagicCombatStyle extends AbstractCombatStyle {
	
	/**
	 * Constructs a new combat style enumeration instance
	 */
	public MagicCombatStyle() {
		super(new MagicCombatCalculator());
	}
	
	@Override
	public boolean fireSwing(Player source, Actor target) {
		int spellId = source.getCombatDefinitions().getRealSpellId();
		boolean manualCast = !source.getCombatDefinitions().isAutocasting();
		// we're not auto-casting so we should reset the spell
		if (manualCast) {
			source.getCombatDefinitions().resetSpells(false);
			source.getActionManager().forceStop();
		}
		// we don't have the runes anymore
		if (!CombatAlgorithm.checkCombatSpell(source, spellId, -1, true)) {
			if (manualCast) {
				source.getCombatDefinitions().resetSpells(true);
			}
			return false;
		}
		// finds the spell to cast
		Optional<SpellPlugin> spellOptional = PluginRepository.getSpellPlugin(source.getCombatDefinitions().getMagicBook(), spellId);
		// spell hasn't been registered yet
		if (!spellOptional.isPresent()) {
			source.getPackets().sendMessage("Spell #" + spellId + " has not yet been added, please report this on the forums.");
			return false;
		}
		// spell instance
		SpellPlugin spellPlugin = spellOptional.get();
		if (!(spellPlugin instanceof CombatSpellPlugin)) {
			source.getPackets().sendMessage("Spell #" + spellId + " has not yet been added, please report this on the forums.");
			return false;
		}
		CombatSpellPlugin combatSpellPlugin = (CombatSpellPlugin) spellPlugin;
		switch (source.getCombatDefinitions().getMagicBook()) {
			case REGULAR:
			case ANCIENTS:
			case LUNAR:
				switch (spellId) {
					default:
						combatSpellPlugin.cast(source, target, this);
						break;
				}
				break;
		}
		return true;
	}
	
	@Override
	public final void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId) {
		throw new IllegalStateException("Unable to add experience in magic style without spell parameters");
	}
	
	@Override
	public int getRandomDamage(Actor source, Actor target, double multiplier) {
		return 0;
	}
	
	@Override
	public CombatSwingDetail sendHit(Player source, Actor target, int maxHit, int damage, int delay) {
		return null;
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param plugin
	 * 		The magic spell event
	 * @return The damage that landed
	 */
	public CombatSwingDetail sendSpell(Player player, Actor target, CombatSpellPlugin plugin) {
		return sendSpell(player, target, plugin, null, null);
	}
	
	/**
	 * Sends the spell to the target
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param plugin
	 * 		The magic spell event
	 * @param spellCastTask
	 * 		The task that is executed when the spell is cast
	 * @param hitLandTask
	 * 		The task that is executed when the hit lands.
	 * @return The amount of damage that landed
	 */
	public CombatSwingDetail sendSpell(Player player, Actor target, CombatSpellPlugin plugin, Runnable spellCastTask, Runnable hitLandTask) {
		int damage;
		int maxHit = plugin.maxHit(player, target);
		int minimum = plugin.minimumHit(player);
		if (minimum != -1) {
			damage = CombatRoll.randomizeHit(minimum, maxHit, calculator.getAttackBonus(player), calculator.getDefenceBonus(target, 0, 0), false);
		} else {
			damage = CombatRoll.randomizeHit(maxHit, calculator.getAttackBonus(player), calculator.getDefenceBonus(target, 0, 0));
		}
		// the projectile delay speed
		int projectileDelay = ProjectileManager.getProjectileDelay(player, target);
		// the extra delay calculation
		double delayCalc = ProjectileManager.getDelay(player, target, projectileDelay, 0);
		// the final delay
		final int delay = (int) (projectileDelay + delayCalc);
		
		final Hit hit = new Hit(player, damage, HitSplat.MAGIC_DAMAGE).setMaxHit(maxHit);
		
		addExperience(player, target, hit, plugin);
		
		// uses the spells animation
		if (plugin.animationId() != -1) {
			player.setNextAnimation(new Animation(plugin.animationId()));
		}
		
		if (plugin.castSoundId() != -1) {
			playSingleSound(player, plugin.castSoundId());
		}
		
		// sends the task that is executed when the spell is used successfully
		if (damage > 0 && spellCastTask != null) {
			spellCastTask.run();
		}
		
		SystemManager.SCHEDULER.schedule(new ScheduledTask(1, delay) {
			
			@Override
			public void run() {
				// so we don't have to make a new task for blocking.
				if (delay == 2 && getTicksPassed() == 0 || getTicksPassed() == getGoalTicks() - 1) {
					target.setNextAnimationNoPriority(new Animation(CombatAlgorithm.getDefenceEmote(target)));
				} else if (getTicksPassed() == getGoalTicks()) {
					target.applyHit(hit);
					if (damage == 0) {
						target.setNextGraphics(new Graphics(85, 0, 96));
					} else {
						if (plugin.hitGfx() != -1) {
							target.setNextGraphics(new Graphics(plugin.hitGfx(), 0, plugin.gfxHeight()));
						}
						if (plugin.impactSoundId() != -1) {
							playAreaSound(player, plugin.impactSoundId());
						}
						if (damage > 0 && hitLandTask != null) {
							hitLandTask.run();
						}
					}
					stop();
				}
			}
		});
		return new CombatSwingDetail(player, target, hit);
	}
	
	/**
	 * Adds experience for a spell
	 */
	public void addExperience(Player source, Actor target, Hit hit, CombatSpellPlugin combatSpellPlugin) {
		double magicExp = combatSpellPlugin.exp();
		int damage = hit.getDamage();
		double combatXp = magicExp * 1 + (damage / 5);
		if (combatXp <= 0) {
			return;
		}
		if (source.getCombatDefinitions().isDefensiveCasting()) {
			double defenceXp = damage / 3;
			if (defenceXp > 0) {
				combatXp -= defenceXp;
				if (target.isPlayer()) {
					source.getSkills().addXpNoModifier(DEFENCE, defenceXp);
				} else {
					source.getSkills().addXp(DEFENCE, defenceXp);
				}
			}
		}
		if (target.isPlayer()) {
			source.getSkills().addXpNoModifier(MAGIC, combatXp);
		} else {
			source.getSkills().addXp(MAGIC, combatXp);
		}
		double hpExp = damage / 7.5;
		if (hpExp > 0) {
			if (target.isPlayer()) {
				source.getSkills().addXpNoModifier(HITPOINTS, hpExp);
			} else {
				source.getSkills().addXp(HITPOINTS, hpExp);
			}
		}
	}
	
	/**
	 * Sends a multi spell
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param plugin
	 * 		The event
	 * @param spellCastTask
	 * 		The task for when the spell is cast
	 * @param hitLandTask
	 * 		The task for when the hit lands
	 */
	public List<CombatSwingDetail> sendMultiSpell(Player player, Actor target, CombatSpellPlugin plugin, Runnable spellCastTask, Runnable hitLandTask) {
		// the list consisting of all the entities to attacak, and the first index being the
		// entity we cast the spell on
		List<Actor> entityList = CombatAlgorithm.getMultiAttackTargets(player, target);
		// the list of all contexts
		List<CombatSwingDetail> detailList = new ArrayList<>();
		if (entityList.size() == 0) {
			return detailList;
		}
		// if the first hit was a splash we don't want to keep trying
		for (int i = 0; i < entityList.size(); i++) {
			Actor actor = entityList.get(i);
			CombatSwingDetail context = sendSpell(player, actor, plugin, spellCastTask, hitLandTask);
			detailList.add(context);
			if (i == 0 && context.getHit().getDamage() == 0) {
				return detailList;
			}
		}
		return detailList;
	}
}
