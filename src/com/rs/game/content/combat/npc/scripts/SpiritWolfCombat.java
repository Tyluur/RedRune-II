package com.rs.game.content.combat.npc.scripts;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.combat.npc.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.constants.NPCConstants;

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
				if (!(((NPC) target).getCombatDefinitions().getAttackStyle() == NPCConstants.SPECIAL)) {
					target.setAttackedByDelay(3000);// three seconds
				} else {
					familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare that monster.");
				}
			} else if (target instanceof Player) {
				familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare a player.");
			} else if (target instanceof Familiar) {
				familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot scare other familiars.");
			}
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 40, NPCConstants.MAGE, target)));
		}
		return defs.getAttackDelay();
	}

}
