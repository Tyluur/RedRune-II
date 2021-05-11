package game.content.entity.actor.combat.npc.scripts;

import game.content.entity.actor.combat.npc.CombatScript;
import game.entity.actor.Actor;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.npc.NPC;
import game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import game.entity.actor.npc.impl.familiar.Familiar;
import game.entity.actor.player.Player;
import game.global.map.region.RegionManager;
import utility.constants.BonusConstants;

public class SpiritWolfCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6829, 6828 };
	}

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8293));
			npc.setNextGraphics(new Graphics(1334));
			RegionManager.sendProjectile(npc, target, 1333, 34, 16, 30, 35, 16, 0);
			if (target instanceof NPC) {
				if (target.toNPC().getDefinitions().getSize() >= 3) {
					familiar.getOwner().getPackets().sendMessage("Your familiar cannot scare that monster.");
				} else {
					target.setAttackedByDelay(3000);// three seconds
				}
			} else if (target instanceof Player) {
				familiar.getOwner().getPackets().sendMessage("Your familiar cannot scare a player.");
			} else if (target instanceof Familiar) {
				familiar.getOwner().getPackets().sendMessage("Your familiar cannot scare other familiars.");
			}
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 40, BonusConstants.MAGIC_ATTACK, target)));
		}
		return defs.getAttackDelay();
	}

}
