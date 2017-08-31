package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;

public class BarricadeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		// TODO Auto-generated method stub
		return new Object[] { "Barricade" };
	}

	/*
	 * empty
	 */
	@Override
	public int attack(NPC npc, Actor target) {
		return 0;
	}

}
