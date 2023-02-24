package org.redrune.utility.game.repository.npc.characteristic;

import org.redrune.game.entity.actor.npc.Drop;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class NPCCharacteristic {

    /**
     * The map of combat definitions
     */
    private final Map<Integer, NPCCombatDefinitions> combatDefinitions = new HashMap<>();

    /**
     * The map of combat bonuses
     */
    private final Map<Integer, int[]> combatBonuses = new HashMap<>();

    /**
     * The map of drops
     */
    private final Map<Integer, List<Drop>> drops = new HashMap<>();

    /**
     * The map of examines
     */
    private final Map<Integer, String> examines = new HashMap<>();

    /**
     * Adds the examine of an npc
     *
     * @param npcId   The id of the npc
     * @param examine The examine text
     */
    public void addExamine(int npcId, String examine) {
        examines.put(npcId, examine);
    }

    /**
     * Gets an examine
     */
    public String getExamine(int npcId) {
        return examines.get(npcId);
    }

    /**
     * Adds a list of drops
     *
     * @param npcId The id of the npc
     * @param drops The list of drops
     */
    public void addDrops(int npcId, List<Drop> drops) {
        this.drops.put(npcId, drops);
    }

    /**
     * Gets the drops of an npc by its id
     */
    public List<Drop> getDrops(int npcId) {
        return drops.get(npcId);
    }

    /**
     * Adds bonuses to the characteristics
     *
     * @param npcId   The id of the npc
     * @param bonuses The bonuses
     */
    public void addBonuses(int npcId, int[] bonuses) {
        this.combatBonuses.put(npcId, bonuses);
    }

    /**
     * Gets the bonuses of an npc by its id
     */
    public int[] getBonuses(int npcId) {
        return combatBonuses.get(npcId);
    }

    /**
     * Adds combat definitions to the characteristics
     *
     * @param npcId             The id of the npc
     * @param combatDefinitions The combat definitions
     */
    public void addCombatDefinitions(int npcId, NPCCombatDefinitions combatDefinitions) {
        this.combatDefinitions.put(npcId, combatDefinitions);
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    public NPCCombatDefinitions getCombatDefinitions(int npcId) {
        return combatDefinitions.get(npcId);
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    public NPCCombatDefinitions getCombatDefinitionsNonNull(int npcId) {
        NPCCombatDefinitions combatDefinitions = this.combatDefinitions.get(npcId);
        if (combatDefinitions == null) {
            combatDefinitions = new NPCCombatDefinitions();
        }
        return combatDefinitions;
    }

    @Override
    public String toString() {
        return "NPCCharacteristic{definitions=" + combatDefinitions.size() + ", combatBonuses=" + combatBonuses.size() + ", drops=" + drops.size() + ", examines=" + examines.size() + "}";
    }
}
