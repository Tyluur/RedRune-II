package org.redrune.game.entity.actor.player.data

import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.SkillConstants
import org.redrune.utility.functions.Misc
import java.io.Serializable

class PlayerSkills : Serializable, SkillConstants {
    var level: IntArray
    var xp: DoubleArray
        private set
    private var xpCounter = 0.0
    private val enabledSkillsTargets: BooleanArray
    private val skillsTargetsUsingLevelMode: BooleanArray
    private val skillsTargetsValues: IntArray

    @Transient
    private var player: Player? = null
    fun passLevels(p: Player) {
        level = p.skills.level
        xp = p.skills.xp
    }

    fun restoreSkills() {
        for (skill in level.indices) {
            level[skill] = getLevelForXp(skill)
            refresh(skill)
        }
    }

    fun getLevelForXp(skill: Int): Int {
        val exp = xp[skill]
        var points = 0
        var output = 0
        for (lvl in 1..if (skill == SkillConstants.DUNGEONEERING) 120 else 99) {
            points += Math.floor(lvl.toDouble() + 300.0 * Math.pow(2.0, lvl.toDouble() / 7.0)).toInt()
            output = Math.floor((points / 4).toDouble()).toInt()
            if (output - 1 >= exp) {
                return lvl
            }
        }
        return if (skill == SkillConstants.DUNGEONEERING) 120 else 99
    }

    fun refresh(skill: Int) {
        player!!.packets.sendSkillLevel(skill)
        player!!.appearance.generateAppearanceData()
    }

    fun setPlayer(player: Player?) {
        this.player = player
    }

    fun getXp(skill: Int): Double {
        return xp[skill]
    }

    /**
     * Drains a skill level with a cap on it
     *
     * @param skill
     * The skill id to drain
     * @param drainAmount
     * The amount to drain
     * @param drainCap
     * The amount we are capped by
     */
    fun drainLevel(skill: Int, drainAmount: Double, drainCap: Double) {
        val skillLevel = level[skill].toInt()
        val levelForXp = getLevelForXp(skill)
        val lowestAllowed = levelForXp - Math.round(levelForXp * drainCap).toInt()
        // can no longer drain past this
        if (skillLevel <= lowestAllowed) {
            return
        }
        val drain = Math.round(levelForXp * drainAmount).toInt()
        drainLevel(skill, drain)
    }

    /**
     * Drains a level
     *
     * @param skill
     * The skill
     * @param drain
     * The amount to drain
     */
    fun drainLevel(skill: Int, drain: Int): Int {
        var drainLeft = drain - level[skill]
        if (drainLeft < 0) {
            drainLeft = 0
        }
        level[skill] -= drain
        if (level[skill] < 0) {
            level[skill] = 0
        }
        refresh(skill)
        return drainLeft
    }

    val combatLevelWithSummoning: Int
        get() = combatLevel + summoningCombatLevel
    val combatLevel: Int
        get() {
            val attack = getLevelForXp(0)
            val defence = getLevelForXp(1)
            val strength = getLevelForXp(2)
            val hp = getLevelForXp(3)
            val prayer = getLevelForXp(5)
            val ranged = getLevelForXp(4)
            val magic = getLevelForXp(6)
            var combatLevel = 3
            combatLevel = ((defence + hp + Math.floor((prayer / 2).toDouble())) * 0.25).toInt() + 1
            val melee = (attack + strength) * 0.325
            val ranger = Math.floor(ranged * 1.5) * 0.325
            val mage = Math.floor(magic * 1.5) * 0.325
            if (melee >= ranger && melee >= mage) {
                combatLevel += melee.toInt()
            } else if (ranger >= melee && ranger >= mage) {
                combatLevel += ranger.toInt()
            } else if (mage >= melee && mage >= ranger) {
                combatLevel += mage.toInt()
            }
            return combatLevel
        }
    val summoningCombatLevel: Int
        get() = getLevelForXp(SkillConstants.SUMMONING) / 8

    fun drainSummoning(amt: Int) {
        val level = getLevel(SkillConstants.SUMMONING)
        if (level == 0) {
            return
        }
        set(SkillConstants.SUMMONING, if (amt > level) 0 else level - amt)
    }

    fun getLevel(skill: Int): Int {
        return level[skill].toInt()
    }

    operator fun set(skill: Int, newLevel: Int) {
        level[skill] = newLevel
        refresh(skill)
    }

    fun init() {
        for (skill in level.indices) {
            refresh(skill)
        }
        refreshEnabledSkillsTargets()
        refreshUsingLevelTargets()
        refreshSkillsTargetsValues()
        refreshXpCounter()
    }

    private fun refreshXpCounter() {
        player!!.packets.sendConfig(1801, (xpCounter * 10).toInt())
    }

    fun resetXpCounter() {
        xpCounter = 0.0
        refreshXpCounter()
    }

    fun addXpNoModifier(skill: Int, exp: Double) {
        if (player!!.attributes.isExperienceLocked) {
            return
        }
        trackExperienceChange(skill, exp)
    }

    fun addXp(skill: Int, exp: Double) {
        if (player!!.attributes.isExperienceLocked) {
            return
        }
        trackExperienceChange(skill, exp)
    }

    private fun trackExperienceChange(skill: Int, exp: Double) {
        player!!.controllerManager.trackXP(skill, exp.toInt())
        val oldLevel = getLevelForXp(skill)
        xp[skill] += exp
        xpCounter += exp
        refreshXpCounter()
        if (xp[skill] > SkillConstants.MAXIMUM_EXP) {
            xp[skill] = SkillConstants.MAXIMUM_EXP
        }
        val newLevel = getLevelForXp(skill)
        val levelDiff = newLevel - oldLevel
        if (newLevel > oldLevel) {
            level[skill] += levelDiff
            player!!.dialogueManager.startDialogue("LevelUp", skill)
            if (skill == SkillConstants.HITPOINTS) {
                player!!.heal(levelDiff * 10)
            }
            if (skill == SkillConstants.PRAYER) {
                player!!.prayer.restorePrayer(levelDiff * 10)
            }
            if (skill == SkillConstants.SUMMONING || skill <= SkillConstants.MAGIC) {
                player!!.appearance.generateAppearanceData()
            }
        }
        refresh(skill)
    }

    val isMaxed: Boolean
        get() {
            var maxlevels = 0
            for (ji in level.indices) {
                if (getLevel(ji) != 99) {
                    continue
                }
                maxlevels++
            }
            return maxlevels >= 23
        }

    fun addSkillXpRefresh(skill: Int, xp: Double) {
        this.xp[skill] += xp
        level[skill] = getLevelForXp(skill)
    }

    fun resetSkillNoRefresh(skill: Int) {
        xp[skill] = 0.0
        level[skill] = 1
    }

    fun NumberToSkill(number: Int): Boolean {
        var found = 0
        for (i in 0..23) {
            if (getLevel(i) >= 99) {
                found++
            }
        }
        return found >= number
    }

    fun setXp(skill: Int, exp: Double) {
        xp[skill] = exp
        refresh(skill)
    }

    fun getTargetIdByComponentId(componentId: Int): Int {
        return when (componentId) {
            200 -> 0
            11 -> 1
            52 -> 2
            93 -> 3
            28 -> 4
            193 -> 5
            76 -> 6
            19 -> 7
            36 -> 8
            60 -> 9
            84 -> 10
            110 -> 11
            186 -> 12
            179 -> 13
            44 -> 14
            68 -> 15
            172 -> 16
            165 -> 17
            101 -> 18
            118 -> 19
            126 -> 20
            134 -> 21
            142 -> 22
            150 -> 23
            158 -> 24
            else -> -1
        }
    }

    fun getSkillIdByTargetId(targetId: Int): Int {
        return when (targetId) {
            0 -> SkillConstants.ATTACK
            1 -> SkillConstants.STRENGTH
            2 -> SkillConstants.RANGE
            3 -> SkillConstants.MAGIC
            4 -> SkillConstants.DEFENCE
            5 -> SkillConstants.HITPOINTS
            6 -> SkillConstants.PRAYER
            7 -> SkillConstants.AGILITY
            8 -> SkillConstants.HERBLORE
            9 -> SkillConstants.THIEVING
            10 -> SkillConstants.CRAFTING
            11 -> SkillConstants.RUNECRAFTING
            12 -> SkillConstants.MINING
            13 -> SkillConstants.SMITHING
            14 -> SkillConstants.FISHING
            15 -> SkillConstants.COOKING
            16 -> SkillConstants.FIREMAKING
            17 -> SkillConstants.WOODCUTTING
            18 -> SkillConstants.FLETCHING
            19 -> SkillConstants.SLAYER
            20 -> SkillConstants.FARMING
            21 -> SkillConstants.CONSTRUCTION
            22 -> SkillConstants.HUNTER
            23 -> SkillConstants.SUMMONING
            24 -> SkillConstants.DUNGEONEERING
            else -> -1
        }
    }

    fun refreshEnabledSkillsTargets() {
        val value = Misc.get32BitValue(enabledSkillsTargets, true)
        player!!.packets.sendConfig(1966, value)
    }

    fun refreshUsingLevelTargets() {
        val value = Misc.get32BitValue(skillsTargetsUsingLevelMode, true)
        player!!.packets.sendConfig(1968, value)
    }

    fun refreshSkillsTargetsValues() {
        for (i in 0..24) {
            player!!.packets.sendConfig(1969 + i, skillsTargetsValues[i])
        }
    }

    fun setSkillTargetEnabled(id: Int, enabled: Boolean) {
        enabledSkillsTargets[id] = enabled
        refreshEnabledSkillsTargets()
    }

    fun setSkillTargetUsingLevelMode(id: Int, using: Boolean) {
        skillsTargetsUsingLevelMode[id] = using
        refreshUsingLevelTargets()
    }

    fun setSkillTargetValue(skillId: Int, value: Int) {
        skillsTargetsValues[skillId] = value
        refreshSkillsTargetsValues()
    }

    fun setSkillTarget(usingLevel: Boolean, skillId: Int, target: Int) {
        setSkillTargetEnabled(skillId, true)
        setSkillTargetUsingLevelMode(skillId, usingLevel)
        setSkillTargetValue(skillId, target)
    }

    fun refreshAllSkills() {
        for (skill in level.indices) {
            refresh(skill)
        }
    }

    override fun toString(): String {
        var contents = ""
        for ((index, skill) in this.level.withIndex()) {
            contents += "$index to $skill\n"
        }
        return contents
    }

    companion object {
        private const val serialVersionUID = -7086829989489745985L
    }

    init {
        level = IntArray(25)
        xp = DoubleArray(25)
        for (i in level.indices) {
            level[i] = 1
            xp[i] = 0.0
        }
        level[3] = 10
        xp[3] = 1184.0
        level[SkillConstants.HERBLORE] = 3
        xp[SkillConstants.HERBLORE] = 250.0
        enabledSkillsTargets = BooleanArray(25)
        skillsTargetsUsingLevelMode = BooleanArray(25)
        skillsTargetsValues = IntArray(25)
    }
}