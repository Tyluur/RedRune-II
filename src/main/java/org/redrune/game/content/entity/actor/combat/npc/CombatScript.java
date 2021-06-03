package org.redrune.game.content.entity.actor.combat.npc;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.combat.player.CombatStyle;
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Steeltitan;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;

public abstract class CombatScript {

    public static void delayHit(NPC npc, int delay, final Actor target, final Hit... hits) {
        npc.getCombat().addAttackedByDelay(target);
        npc.getCombat().doDefenceEmote(target);
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                for (Hit hit : hits) {
                    NPC npc = (NPC) hit.getSource();
                    if (npc.isDead() || npc.isFinished() || target.isDead() || target.isFinished()) {
                        return;
                    }
                    target.applyHit(hit);
                    if (hit.getSplat() == HitSplat.MAGIC_DAMAGE && hit.getDamage() == 0) {
                        target.setNextGraphics(new Graphics(85, 0, 100));
                    }
                    if (target instanceof Player) {
                        Player p2 = (Player) target;
                        if (p2.getCombatDefinitions().isAutoRetaliate() && !p2.getActionManager().hasSkillWorking() && !p2.hasWalkSteps()) {
                            WorldTasksManager.schedule(new WorldTask() {

                                @Override
                                public void run() {
                                    if (p2.getCombatDefinitions().isAutoRetaliate() && !p2.getActionManager().hasSkillWorking() && !p2.hasWalkSteps()) {
                                        p2.closeInterfaces();
                                        p2.getActionManager().setAction(new PlayerCombatAction(npc));
                                    }
                                    stop();
                                }
                            }, 0);
                        }
                    } else {
                        NPC n = (NPC) target;
                        if (!n.isUnderCombat() || n.canBeAttackedByAutoRetaliate()) {
                            n.setTarget(npc);
                        }
                    }
                }
            }

        }, delay);
    }

    public static Hit getRangeHit(NPC npc, int damage) {
        return new Hit(npc, damage, HitSplat.RANGE_DAMAGE);
    }

    public static Hit getMagicHit(NPC npc, int damage) {
        return new Hit(npc, damage, HitSplat.MAGIC_DAMAGE);
    }

    public static Hit getRegularHit(NPC npc, int damage) {
        return new Hit(npc, damage, HitSplat.REGULAR_DAMAGE);
    }

    public static Hit getMeleeHit(NPC npc, int damage) {
        return new Hit(npc, damage, HitSplat.MELEE_DAMAGE);
    }

    public static int getRandomMaxHit(NPC npc, int maxHit, int attackStyle, Actor target) {
        switch (attackStyle) {
            case BonusConstants.SLASH_ATTACK:
            case BonusConstants.STAB_ATTACK:
            case BonusConstants.CRUSH_ATTACK:
                return CombatStyle.MELEE.getStyle().getRandomDamage(npc, target, 1.0);
            case BonusConstants.RANGE_ATTACK:
                break;
            case BonusConstants.MAGIC_ATTACK:
                break;
        }
        int[] bonuses = npc.getBonuses();
        double att = bonuses == null ? 0 : attackStyle == BonusConstants.RANGE_ATTACK ? bonuses[CombatDefinitions.RANGE_ATTACK] : attackStyle == BonusConstants.MAGIC_ATTACK ? bonuses[CombatDefinitions.MAGIC_ATTACK] : bonuses[CombatDefinitions.STAB_ATTACK];
        double def;
        if (target instanceof Player) {
            Player p2 = (Player) target;
            def = p2.getSkills().getLevel(SkillConstants.DEFENCE) + (attackStyle == BonusConstants.RANGE_ATTACK ? 1.25 : attackStyle == BonusConstants.MAGIC_ATTACK ? 2.0 : 1.0) * p2.getCombatDefinitions().getBonuses()[attackStyle == BonusConstants.RANGE_ATTACK ? CombatDefinitions.RANGE_DEF : attackStyle == BonusConstants.MAGIC_ATTACK ? CombatDefinitions.MAGIC_DEF : CombatDefinitions.STAB_DEF];
            def *= p2.getPrayer().getDefenceMultiplier();
            if (attackStyle == BonusConstants.SLASH_ATTACK) {
                if (p2.getFamiliar() instanceof Steeltitan) {
                    def *= 1.15;
                }
            }
        } else {
            NPC n = (NPC) target;
            def = n.getBonuses() == null ? 0 : n.getBonuses()[attackStyle == BonusConstants.RANGE_ATTACK ? CombatDefinitions.RANGE_DEF : attackStyle == BonusConstants.MAGIC_ATTACK ? CombatDefinitions.MAGIC_DEF : CombatDefinitions.STAB_DEF];
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

    /*
     * Returns ids and names
     */
    public abstract Object[] getKeys();

    /*
     * Returns Move Delay
     */
    public abstract int attack(NPC npc, Actor target);

}
