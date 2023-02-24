package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;

public class GeyserTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7340, 7339};
    }

    @Override
    public int attack(NPC npc, Actor target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int distanceX = target.getX() - npc.getX();
        int distanceY = target.getY() - npc.getY();
        boolean distant = false;
        int size = npc.getSize();
        Familiar familiar = (Familiar) npc;
        boolean usingSpecial = familiar.hasSpecialOn();
        int damage = 0;
        if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
            distant = true;
        }
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(7883));
            npc.setNextGraphics(new Graphics(1373));
            if (distant) {// range hit
                if (Misc.getRandom(2) == 0) {
                    delayHit(npc, 1, target, getRangeHit(npc, getRandomMaxHit(npc, 300, BonusConstants.RANGE_ATTACK, target)));
                } else {
                    delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 300, BonusConstants.MAGIC_ATTACK, target)));
                }
            } else {// melee hit
                delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 300, BonusConstants.SLASH_ATTACK, target)));
            }
            RegionManager.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
        } else {
            if (distant) {// range
                damage = getRandomMaxHit(npc, 244, BonusConstants.RANGE_ATTACK, target);
                npc.setNextAnimation(new Animation(7883));
                npc.setNextGraphics(new Graphics(1375));
                RegionManager.sendProjectile(npc, target, 1374, 34, 16, 30, 35, 16, 0);
                delayHit(npc, 2, target, getRangeHit(npc, damage));
            } else {// melee
                damage = getRandomMaxHit(npc, 244, BonusConstants.SLASH_ATTACK, target);
                npc.setNextAnimation(new Animation(7879));
                delayHit(npc, 1, target, getMeleeHit(npc, damage));
            }
        }
        return defs.getAttackDelay();
    }
}
