package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;

public class BarricadeCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        // TODO Auto-generated method stub
        return new Object[]{"Barricade"};
    }

    /*
     * empty
     */
    @Override
    public int attack(NPC npc, Actor target) {
        return 0;
    }

}
