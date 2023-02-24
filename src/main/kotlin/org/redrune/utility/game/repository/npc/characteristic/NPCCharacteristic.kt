package org.redrune.utility.game.repository.npc.characteristic

import org.redrune.game.entity.actor.npc.Drop
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class NPCCharacteristic {

    /**
     * The map of combat definitions
     */
    private val combatDefinitions: MutableMap<Int, NPCCombatDefinitions> = HashMap()

    /**
     * The map of combat bonuses
     */
    private val combatBonuses: MutableMap<Int, IntArray> = HashMap()

    /**
     * The map of drops
     */
    private val drops: MutableMap<Int, List<Drop>> = HashMap()

    /**
     * The map of examines
     */
    private val examines: MutableMap<Int, String> = HashMap()

    /**
     * Adds the examine of an npc
     *
     * @param npcId   The id of the npc
     * @param examine The examine text
     */
    fun addExamine(npcId: Int, examine: String) {
        examines[npcId] = examine
    }

    /**
     * Gets an examine
     */
    fun getExamine(npcId: Int): String? {
        return examines[npcId]
    }

    /**
     * Adds a list of drops
     *
     * @param npcId The id of the npc
     * @param drops The list of drops
     */
    fun addDrops(npcId: Int, drops: List<Drop>) {
        this.drops[npcId] = drops
    }

    /**
     * Gets the drops of an npc by its id
     */
    fun getDrops(npcId: Int): List<Drop> {
        return drops[npcId]!!
    }

    /**
     * Adds bonuses to the characteristics
     *
     * @param npcId   The id of the npc
     * @param bonuses The bonuses
     */
    fun addBonuses(npcId: Int, bonuses: IntArray) {
        combatBonuses[npcId] = bonuses
    }

    /**
     * Gets the bonuses of an npc by its id
     */
    fun getBonuses(npcId: Int): IntArray? {
        return combatBonuses[npcId]
    }

    /**
     * Adds combat definitions to the characteristics
     *
     * @param npcId             The id of the npc
     * @param combatDefinitions The combat definitions
     */
    fun addCombatDefinitions(npcId: Int, combatDefinitions: NPCCombatDefinitions) {
        this.combatDefinitions[npcId] = combatDefinitions
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    fun getCombatDefinitions(npcId: Int): NPCCombatDefinitions? {
        return combatDefinitions[npcId]
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    fun getCombatDefinitionsNonNull(npcId: Int): NPCCombatDefinitions {
        var combatDefinitions = combatDefinitions[npcId]
        if (combatDefinitions == null) {
            combatDefinitions = NPCCombatDefinitions()
        }
        return combatDefinitions
    }

    override fun toString(): String {
        return "NPCCharacteristic{definitions=" + combatDefinitions.size + ", combatBonuses=" + combatBonuses.size + ", drops=" + drops.size + ", examines=" + examines.size + "}"
    }
}