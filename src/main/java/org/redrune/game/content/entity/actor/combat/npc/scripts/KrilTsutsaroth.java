package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class KrilTsutsaroth extends CombatScript {
	
	@Override
	public Object[] getKeys() {
		return new Object[] { 6203 };
	}
	
	@Override
	public int attack(NPC npc, Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Misc.getRandom(4) == 0) {
			switch (Misc.getRandom(8)) {
				case 0:
					npc.setNextForceTalk(new ForceTalk("Attack them, you dogs!"));
					break;
				case 1:
					npc.setNextForceTalk(new ForceTalk("Forward!"));
					break;
				case 2:
					npc.setNextForceTalk(new ForceTalk("Death to Saradomin's dogs!"));
					break;
				case 3:
					npc.setNextForceTalk(new ForceTalk("Kill them, you cowards!"));
					break;
				case 4:
					npc.setNextForceTalk(new ForceTalk("The Dark One will have their souls!"));
					npc.playSound(3229, 2);
					break;
				case 5:
					npc.setNextForceTalk(new ForceTalk("Zamorak curse them!"));
					break;
				case 6:
					npc.setNextForceTalk(new ForceTalk("Rend them limb from limb!"));
					break;
				case 7:
					npc.setNextForceTalk(new ForceTalk("No retreat!"));
					break;
				case 8:
					npc.setNextForceTalk(new ForceTalk("Flay them all!"));
					break;
			}
		}
		int attackStyle = Misc.getRandom(2);
		switch (attackStyle) {
			case 0:// magic flame attack
				npc.setNextAnimation(new Animation(14962));
				npc.setNextGraphics(new Graphics(1210));
				for (Actor t : npc.getPossibleTargets()) {
					delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 300, NPCConstants.MAGE, t)));
					RegionManager.sendProjectile(npc, t, 1211, 41, 16, 41, 35, 16, 0);
					if (Misc.getRandom(4) == 0) {
						t.getPoisonManager().makePoisoned(168);
					}
				}
				break;
			case 1:// main attack
			case 2:// melee attack
				int damage = 463;// normal
				if (Misc.getRandom(3) == 0 && target instanceof Player && (((Player) target).getPrayer().usingPrayer(0, 19) || ((Player) target).getPrayer().usingPrayer(1, 9))) {
					Player player = (Player) target;
					damage = 497;
					npc.setNextForceTalk(new ForceTalk("YARRRRRRR!"));
					player.getPrayer().drainPrayer((Math.round(damage / 20)));
					player.getAttributes().setPrayerDelay(Misc.getRandom(5) + 5);
					player.getPackets().sendMessage("K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
				}
				npc.setNextAnimation(new Animation(damage <= 463 ? 14963 : 14968));
				delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, damage, NPCConstants.MELEE, target)));
				break;
		}
		return defs.getAttackDelay();
	}
}
