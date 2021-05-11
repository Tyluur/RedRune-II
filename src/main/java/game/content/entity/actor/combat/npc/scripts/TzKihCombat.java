package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.npc.impl.familiar.Familiar;
import game.entity.actor.player.Player;
import utility.constants.BonusConstants;

public class TzKihCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "tz-kih" };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (npc instanceof Familiar) {// TODO get anim and gfx
			if (usingSpecial) {
				for (Actor actor : npc.getPossibleTargets(true, true)) {
					damage = getRandomMaxHit(npc, 70, BonusConstants.MAGIC_ATTACK, target);
					Player player = (Player) target;
					if (player.getTemporaryAttributes().get("drainingPrayer") != null) {
						player.getPrayer().drainPrayer(damage);
					} else {
						delayHit(npc, 1, actor, getMagicHit(npc, damage));
					}
				}
			}
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(8257));
		damage = getRandomMaxHit(npc, 50, BonusConstants.MAGIC_ATTACK, target);
		Player player = (Player) target;
		if (player.getTemporaryAttributes().get("drainingPrayer") != null) {
			player.getPrayer().drainPrayer(damage);
		} else {
			delayHit(npc, 1, target, getMagicHit(npc, damage));
		}
		return defs.getAttackDelay();
	}
}
