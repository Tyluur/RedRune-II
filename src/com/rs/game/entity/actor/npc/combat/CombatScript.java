package com.rs.game.entity.actor.npc.combat;

import com.rs.game.content.action.impl.PlayerCombatAction;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.data.CombatDefinitions;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitLook;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.impl.familiar.Steeltitan;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;
import com.rs.utility.constants.NPCConstants;

public abstract class CombatScript {
	
	/*
	 * Returns ids and names
	 */
	public abstract Object[] getKeys();
	
	/*
	 * Returns Move Delay
	 */
	public abstract int attack(NPC npc, Actor target);
	
	public static void delayHit(NPC npc, int delay, final Actor target, final Hit... hits) {
		
		npc.getCombat().addAttackedByDelay(target);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				for (Hit hit : hits) {
					NPC npc = (NPC) hit.getSource();
					if (npc.isDead() || npc.hasFinished() || target.isDead() || target.hasFinished()) {
						return;
					}
					target.applyHit(hit);
					npc.getCombat().doDefenceEmote(target);
					if (hit.getLook() == HitLook.MAGIC_DAMAGE && hit.getDamage() == 0) {
						target.setNextGraphics(new Graphics(85, 0, 100));
					}
					if (target instanceof Player) {
						Player p2 = (Player) target;
						if (p2.getCombatDefinitions().isAutoRelatie() && !p2.getActionManager().hasSkillWorking() && !p2.hasWalkSteps()) {
							p2.closeInterfaces();
							p2.getActionManager().setAction(new PlayerCombatAction(npc));
						}
					} else {
						NPC n = (NPC) target;
						if (!n.isUnderCombat() || n.canBeAttackedByAutoRelatie()) {
							n.setTarget(npc);
						}
					}
					
				}
			}
			
		}, delay);
	}
	
	public static Hit getRangeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.RANGE_DAMAGE);
	}
	
	public static Hit getMagicHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MAGIC_DAMAGE);
	}
	
	public static Hit getRegularHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.REGULAR_DAMAGE);
	}
	
	public static Hit getMeleeHit(NPC npc, int damage) {
		return new Hit(npc, damage, HitLook.MELEE_DAMAGE);
	}
	
	public static int getRandomMaxHit(NPC npc, int maxHit, int attackStyle, Actor target) {
		int[] bonuses = npc.getBonuses();
		double att = bonuses == null ? 0 : attackStyle == NPCConstants.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK] : attackStyle == NPCConstants.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK] : bonuses[CombatDefinitions.STAB_ATTACK];
		double def;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			def = p2.getSkills().getLevel(PlayerSkills.DEFENCE) + (attackStyle == NPCConstants.RANGE ? 1.25 : attackStyle == NPCConstants.MAGE ? 2.0 : 1.0) * p2.getCombatDefinitions().getBonuses()[attackStyle == NPCConstants.RANGE ? CombatDefinitions.RANGE_DEF : attackStyle == NPCConstants.MAGE ? CombatDefinitions.MAGIC_DEF : CombatDefinitions.STAB_DEF];
			def *= p2.getPrayer().getDefenceMultiplier();
			if (attackStyle == NPCConstants.MELEE) {
				if (p2.getFamiliar() instanceof Steeltitan) {
					def *= 1.15;
				}
			}
		} else {
			NPC n = (NPC) target;
			def = n.getBonuses() == null ? 0 : n.getBonuses()[attackStyle == NPCConstants.RANGE ? CombatDefinitions.RANGE_DEF : attackStyle == NPCConstants.MAGE ? CombatDefinitions.MAGIC_DEF : CombatDefinitions.STAB_DEF];
		}
		double prob = att / def;
		if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
		{
			prob = 0.90;
		} else if (prob < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
		{
			prob = 0.05;
		}
		if (prob < Math.random()) {
			return 0;
		}
		return Misc.getRandom(maxHit);
	}
	
}
