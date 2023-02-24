package org.redrune.game.content.entity.actor.combat.npc;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.BonusConstants;

public class DefaultCombatScript extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Default"};
    }

    @Override
    public int attack(NPC npc, Actor target) {
        NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = defs.getAttackStyle();
        switch (attackStyle) {
            case BonusConstants.STAB_ATTACK:
            case BonusConstants.SLASH_ATTACK:
            case BonusConstants.CRUSH_ATTACK:
                delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target)));
                break;
            case BonusConstants.RANGE_ATTACK:
            case BonusConstants.MAGIC_ATTACK:
                int damage = getRandomMaxHit(npc, defs.getMaxHit(), attackStyle, target);
                delayHit(npc, 2, target, attackStyle == BonusConstants.RANGE_ATTACK ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
                if (defs.getAttackProjectile() != -1) {
                    RegionManager.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
                }
                break;
        }
        if (defs.getAttackGfx() != -1) {
            npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
        }
        npc.setNextAnimation(new Animation(defs.getAttackAnim()));
        return defs.getAttackDelay();
    }
}
