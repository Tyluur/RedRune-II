package org.redrune.game.content.entity.actor.combat.npc.scripts;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.npc.CombatScript;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;

public class LeatherDragonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Green dragon", "Blue dragon", "Red dragon", "Black dragon", 742, 14548};
    }

    @Override
    public int attack(final NPC npc, final Actor target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int distanceX = target.getX() - npc.getX();
        int distanceY = target.getY() - npc.getY();
        int size = npc.getSize();
        if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
            return 0;
        }
        if (Misc.getRandom(3) != 0) {
            npc.setNextAnimation(new Animation(defs.getAttackAnim()));
            delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(), BonusConstants.SLASH_ATTACK, target)));
        } else {
            int damage = Misc.getRandom(650);
            npc.setNextAnimation(new Animation(12259));
            npc.setNextGraphics(new Graphics(1, 0, 100));
            final Player player = target instanceof Player ? (Player) target : null;
            if (player != null) {
                if (CombatAlgorithm.hasAntiDragProtection(target) || (player != null && (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
                    damage = 0;
                    player.getPackets().sendMessage("Your " + (CombatAlgorithm.hasAntiDragProtection(target) ? "shield" : "prayer") + " absorb's most of the dragon's breath!", true);
                }
                if (player.getAttributes().getFireImmune() > Misc.currentTimeMillis()) {
                    if (damage != 0) {
                        damage = Misc.getRandom(50);
                    }
                } else if (damage == 0) {
                    damage = Misc.getRandom(50);
                } else {
                    player.getPackets().sendMessage("You are hit by the dragon's fiery breath!", true);
                }
                delayHit(npc, 1, target, getRegularHit(npc, damage));
            }
        }
        return defs.getAttackDelay();
    }
}
