package org.redrune.game.content.entity.actor.combat.npc;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.map.MapArchiveKeys;

public final class NPCCombat {

    private final NPC npc;

    private int combatDelay;

    private Actor target;

    public NPCCombat(NPC npc) {
        this.npc = npc;
    }

    /*
     * returns if under combat
     */
    public boolean process() {
        if (combatDelay > 0) {
            combatDelay--;
        }
        if (target != null) {
            if (!checkAll()) {
                removeTarget();
                return false;
            }
            if (combatDelay <= 0) {
                combatDelay = combatAttack();
            }
            return true;
        }
        return false;
    }

    public boolean checkAll() {
        Actor target = this.target; // prevents multithread issues
        if (target == null)
            return false;
        if (npc.isDead() || npc.isFinished() || (npc.isForceWalking() && npc.isCantFollowUnderCombat()) || target.isDead() || target.isFinished()
                || npc.getPlane() != target.getPlane())
            return false;
        if (npc instanceof Familiar && target instanceof NPC && ((NPC) target).isCantInteract())
            return false;
        int distanceX = npc.getX() - npc.getRespawnTile().getX();
        int distanceY = npc.getY() - npc.getRespawnTile().getY();
        int size = npc.getSize();
        int maxDistance;
        int agroRatio = npc.getForceTargetDistance() > 0 ? npc.getForceTargetDistance() : 8;
        if (npc.hasForceWalk() && npc.getAttackedByDelay() > Misc.currentTimeMillis())
            npc.setForceWalk(null);
        if (npc.hasForceWalk() && npc.getAttackedByDelay() < Misc.currentTimeMillis())
            return false;
        if (!npc.isCantFollowUnderCombat()) {
            maxDistance = agroRatio > 8 ? agroRatio : 8; // before 32, but its too much
            if (!(npc instanceof Familiar)) {
                if (npc.getMapAreaNameHash() != -1) {
                    // if out his area
                    if (!MapArchiveKeys.isAtArea(npc.getMapAreaNameHash(), npc) || (!npc.canBeAttackFromOutOfArea()
                            && !MapArchiveKeys.isAtArea(npc.getMapAreaNameHash(), target))) {
                        combatDelay = 1;
                        npc.forceWalkRespawnTile();
                        return true;
                    }
                } else if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
                        || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
                    // if more than 32 distance from respawn place
                    npc.forceWalkRespawnTile();
                    combatDelay = 1;
                    return true;
                }
            }
            maxDistance = agroRatio > 16 ? agroRatio : 16;
            distanceX = target.getX() - npc.getX();
            distanceY = target.getY() - npc.getY();
            if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
                    || distanceY < -1 - maxDistance) {
                return false; // if target distance higher 16
            }
        } else {
            distanceX = target.getX() - npc.getX();
            distanceY = target.getY() - npc.getY();
        }
        // checks for no multi area :)
        if (npc instanceof Familiar) {
            Familiar familiar = (Familiar) npc;
            if (!familiar.canAttack(target)) {
                return false;
            }
        } else {
            if (!npc.isForceMultiAttacked()) {
                if (!target.isInMultiArea() || !npc.isInMultiArea()) {
                    if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
                        return false;
                    }
                    if (target.getAttackedBy() != npc && target.getAttackedByDelay() > Misc.currentTimeMillis()) {
                        return false;
                    }
                }
            }
        }
        if (!npc.isCantFollowUnderCombat()) {
            // if is under
            int targetSize = target.getSize();
            /*
             * if (distanceX < size && distanceX > -targetSize && distanceY < size &&
             * distanceY > -targetSize && !target.hasWalkSteps()) {
             */
            if (Misc.colides(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize())
                    && !target.hasWalkSteps()) {
                if (npc.getFreezeDelay() >= Misc.currentTimeMillis()) {
                    combatDelay = 1;
                    return true;
                }
                npc.resetWalkSteps();
                if (!npc.addWalkStepsInteract(target.getX() + target.getSize(), npc.getY(), 1, npc.getSize(), true)) {
                    npc.resetWalkSteps();
                    if (!npc.addWalkStepsInteract(target.getX() - size, npc.getY(), 1, npc.getSize(), true)) {
                        npc.resetWalkSteps();
                        if (!npc.addWalkStepsInteract(npc.getX(), target.getY() + target.getSize(), 1, npc.getSize(), true)) {
                            npc.resetWalkSteps();
                            if (!npc.addWalkStepsInteract(npc.getX(), target.getY() - size, 1, npc.getSize(), true)) {
                                return false;
                            }
                        }
                    }
                }
                combatDelay = 1;
                return true;
            } else if (npc.isMelee() && targetSize == 1
                    && Math.abs(npc.getX() - target.getX()) == 1 && Math.abs(npc.getY() - target.getY()) == 1
                    && !target.hasWalkSteps() && npc.getSize() == 1) {
                if (!npc.addWalkSteps(target.getX(), npc.getY(), 1))
                    npc.addWalkSteps(npc.getX(), target.getY(), 1);
                return true;
            }
            int attackStyle = npc.getCombatDefinitions().getAttackStyle();
            maxDistance = npc.isForceFollowClose() ? 0 : (attackStyle == BonusConstants.STAB_ATTACK || attackStyle == BonusConstants.SLASH_ATTACK || attackStyle == BonusConstants.CRUSH_ATTACK) ? 0 : 7;
            // is far from target, moves to it till can attack
            if ((!npc.clipedProjectile(target, maxDistance == 0)) || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
                npc.resetWalkSteps();
                npc.addWalkStepsInteract(target.getX(), target.getY(), 2, size, true);
                return true;
            } else {
                if (npc.getAttackedByDelay() < Misc.currentTimeMillis() || npc.getAttackedByDelay() == 0)// set flinch if haven't attacked in a while
                    flinch();
                if (npc.getFlinchDelay() > Misc.currentTimeMillis())// dont attack if flinch active
                    combatDelay = 1;
            }
        }
        return true;

    }

    public void removeTarget() {
        this.target = null;
        npc.setNextFaceActor(null);
    }

    /*
     * return combatDelay
     */
    private int combatAttack() {
        Actor target = this.target; // prevents multithread issues
        if (target == null) {
            return 0;
        }
        NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = defs.getAttackStyle();
        int maxDistance = attackStyle == BonusConstants.STAB_ATTACK || attackStyle == BonusConstants.SLASH_ATTACK || attackStyle == BonusConstants.CRUSH_ATTACK ? 0 : 7;
        if (!npc.clipedProjectile(target, maxDistance == 0)) {
            return 0;
        }
        int distanceX = target.getX() - npc.getX();
        int distanceY = target.getY() - npc.getY();
        int size = npc.getSize();
        /*
         * if(npc.hasWalkSteps()) maxDistance += 1;
         */
        if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
            return 0;
        }
        return CombatScriptsHandler.fireCombatScript(npc, target);
    }

    public void addAttackedByDelay(Actor target) { // prevents multithread
        // issues
        target.setAttackedBy(npc);
        target.setAttackedByDelay(Misc.currentTimeMillis() + npc.getCombatDefinitions().getAttackDelay() * 600L + 600); // 8seconds
    }

    public void doDefenceEmote(Actor target) {
        target.setNextAnimationNoPriority(new Animation(CombatAlgorithm.getDefenceEmote(target)));
    }

    public Actor getTarget() {
        return target;
    }

    public void setTarget(Actor target) {
        this.target = target;
        npc.setNextFaceActor(target);
        if (!checkAll()) {
            removeTarget();
        }
    }

    public void flinch() {
        if (npc.getFlinchDelay() > Misc.currentTimeMillis())
            return;
        int attackSpeed = npc.getCombatDefinitions().getAttackDelay() * 600;
        npc.setFlinchDelay((attackSpeed / 2) - 1000);
        npc.setAttackedByDelay((attackSpeed / 2) + 4800);
    }

    public void addCombatDelay(int delay) {
        combatDelay += delay;
    }

    public void setCombatDelay(int delay) {
        combatDelay = delay;
    }

    public boolean underCombat() {
        return target != null;
    }

    public void reset() {
        combatDelay = 0;
        target = null;
    }

}
