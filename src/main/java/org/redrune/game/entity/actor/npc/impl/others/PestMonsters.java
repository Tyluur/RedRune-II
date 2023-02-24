package org.redrune.game.entity.actor.npc.impl.others;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.global.WorldTile;

@SuppressWarnings("serial")
public class PestMonsters extends NPC {

    public PestMonsters(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public void sendDeath(Actor source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        final NPC npc = (NPC) source;
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        // deathEffects(npc);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    if (!(npc.getId() == 6142) || (npc.getId() == 6144) || (npc.getId() == 6145) || (npc.getId() == 6143)) // Portals
                    {
                        setNextAnimation(new Animation(defs.getDeathAnim()));
                    }
                } else if (loop >= defs.getDeathDelay()) {
                    drop();
                    reset();
                    finish();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }
    /*
     * /** Death effects of NPCs
     *
     * @param n The npc TODO other monsters
     */
    /*
     * private void deathEffects(NPC n) { if (n.getIds() == 6142) { for (Player
     * players : PestControl.playersInGame) {
     * players.getPackets().sendIComponentText(408, 13, "DEAD");
     * players.getPackets
     * ().sendGameMessage("The west portal has been destroyed."); }
     * PestControl.setPortals(0, true); } if (n.getIds() == 6144) { for (Player
     * players : PestControl.playersInGame) {
     * players.getPackets().sendIComponentText(408, 15, "DEAD");
     * players.getPackets
     * ().sendGameMessage("The south-east portal has been destroyed."); }
     * PestControl.setPortals(1, true); } if (n.getIds() == 6145) { for (Player
     * players : PestControl.playersInGame) {
     * players.getPackets().sendIComponentText(408, 16, "DEAD");
     * players.getPackets
     * ().sendGameMessage("The south-west portal has been destroyed."); }
     * PestControl.setPortals(2, true); } if (n.getIds() == 6143) { for (Player
     * players : PestControl.playersInGame) {
     * players.getPackets().sendIComponentText(408, 14, "DEAD");
     * players.getPackets
     * ().sendGameMessage("The east portal has been destroyed."); }
     * PestControl.setPortals(3, true); } }
     */

}
