package org.redrune.utility.game.repository.npc.characteristic

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-20
 */
class AriosNPCCharacteristics(
    private val id: Int,
    private val examine: String,
    private val name: String,
    @JvmField val lifePoints: Int,
    @JvmField val attackLevel: Int,
    @JvmField val strengthLevel: Int,
    @JvmField val defenceLevel: Int,
    @JvmField val rangeLevel: Int,
    @JvmField val magicLevel: Int,
    @JvmField val bonuses: IntArray,
    private val respawnDelay: Int,
    private val attackSpeed: Int,
    private val meleeAnimation: Int,
    private val defenceAnimation: Int,
    private val deathAnimation: Int,
    private val spawnAnimation: Int,
    private val magicAnimation: Int,
    private val rangeAnimation: Int,
    @JvmField val attackGfx: Int,
    private val attackProjectile: Int,
    private val endGfx: Int,
    private val combatStyle: Int,
    private val aggressive: Int,
)