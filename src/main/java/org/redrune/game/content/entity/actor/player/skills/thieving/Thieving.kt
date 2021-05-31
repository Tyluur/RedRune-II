package org.redrune.game.content.entity.actor.player.skills.thieving

import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.entity.`object`.WorldObject
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.ForceTalk
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.constants.SkillConstants
import org.redrune.utility.functions.Misc

/**
 * Handles the Thieving Skill
 *
 * @author Dragonkk
 */
object Thieving {
    @JvmStatic
    fun handleStalls(player: Player, `object`: WorldObject) {
        if (player.attackedBy != null && player.attackedByDelay > Misc.currentTimeMillis()) {
            player.packets.sendMessage("You can't do this while you're under combat.")
            return
        }
        for (stall in Stalls.values()) {
            if (stall.objectId == `object`.id) {
                val emptyStall = WorldObject(
                    stall.replaceObject,
                    10,
                    `object`.rotation,
                    `object`.x,
                    `object`.y,
                    `object`.plane
                )
                if (player.skills.getLevel(SkillConstants.THIEVING) < stall.level) {
                    player.packets.sendMessage(
                        "You need a thieving level of " + stall.level + " to steal from this.",
                        true
                    )
                    return
                }
                if (player.inventory.freeSlots <= 0) {
                    player.packets.sendMessage("Not enough space in your inventory.", true)
                    return
                }
                player.nextAnimation = Animation(881)
                player.locks.lock(2.toLong().toInt())
                WorldTasksManager.schedule(object : WorldTask() {
                    var gaveItems = false
                    override fun run() {
                        // prevents multiempty stall spawn if many ppl using
                        // same spot and also checks if stall there still
                        if (!RegionManager.containsObjectWithId(`object`.id, `object`)) {
                            stop()
                            return
                        }
                        if (!gaveItems) {
                            player.inventory.addItem(
                                stall.getItem(Misc.getRandom(stall.item.size - 1)),
                                Misc.getRandom(stall.amount)
                            )
                            player.skills.addXp(SkillConstants.THIEVING, stall.experience.toDouble())
                            gaveItems = true
                            checkGuards(player)
                        } else {
                            RegionManager.spawnTemporaryObject(emptyStall, ((1500 * stall.time).toLong()))
                            stop()
                        }
                    }
                }, 0, 0)
            }
        }
    }

    fun checkGuards(player: Player) {
        var guard: NPC? = null
        var lastDistance = -1
        for (regionId in player.mapRegionsIds) {
            val npcIndexes: List<Int> = RegionManager.getRegion(regionId).npCsIndexes ?: continue
            for (npcIndex in npcIndexes) {
                val npc: NPC = World.getNPCs().get(npcIndex) ?: continue
                if (!isGuard(npc.id) || npc.isUnderCombat || npc.isDead || !npc.withinDistance(
                        player,
                        4
                    ) || !npc.clipedProjectile(player, true)
                ) {
                    continue
                }
                val distance: Int = Misc.getDistance(npc.x, npc.y, player.x, player.y)
                if (lastDistance == -1 || lastDistance > distance) {
                    guard = npc
                    lastDistance = distance
                }
            }
        }
        if (guard != null) {
            guard.nextForceTalk = ForceTalk("Hey, what do you think you are doing!")
            guard.setTarget(player)
        }
    }

    fun isGuard(npcId: Int): Boolean {
        return npcId == 32 || npcId == 21 || npcId == 2256 || npcId == 23
    }

    enum class Stalls(
        val objectId: Int,
        val level: Int,
        val item: IntArray,
        val amount: Int,
        val time: Int,
        val experience: Int,
        val replaceObject: Int
    ) {
        VEGETABAL(4706, 2, intArrayOf(1957, 1965, 1942, 1982, 1550), 1, 2, 10, 34381), CAKE(
            34384,
            5,
            intArrayOf(1891, 1897, 2309),
            1,
            2.5.toInt(),
            16,
            34381
        ),
        CRAFTING(4874, 5, intArrayOf(1755, 1592, 1597), 1, 7, 16, 34381), MONKEY_FOOD(
            4875,
            5,
            intArrayOf(1963),
            1,
            7,
            16,
            34381
        ),
        MONKEY_GENERAL(6573, 5, intArrayOf(1931, 2347, 590), 1, 7, 16, 34381), TEA_STALL(
            6574,
            5,
            intArrayOf(712),
            1,
            7,
            16,
            34381
        ),
        SILK_STALL(34383, 20, intArrayOf(950), 1, 8, 24, 34381), WINE_STALL(
            14011,
            22,
            intArrayOf(1937, 1993, 1987, 1935, 7919),
            1,
            16,
            27,
            2046
        ),
        SEED_STALL(
            7053,
            27,
            intArrayOf(5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5105),
            30,
            11,
            10,
            2047
        ),
        FUR_STALL(34387, 35, intArrayOf(6814, 958), 1, 15, 36, 34381), FISH_STALL(
            4707,
            42,
            intArrayOf(331, 359, 377),
            1,
            16,
            42,
            34381
        ),
        CROSSBOW_STALL(17031, 49, intArrayOf(877, 9420, 9440), 1, 11, 52, 34381), SILVER_STALL(
            34382,
            50,
            intArrayOf(442),
            1,
            30,
            54,
            34381
        ),
        SPICE_STALL(34386, 65, intArrayOf(2007), 1, 80, 81, 34381), MAGIC_STALL(
            4877,
            65,
            intArrayOf(556, 557, 554, 555, 563),
            30,
            80,
            100,
            34381
        ),
        SCIMITAR_STALL(4878, 65, intArrayOf(1323), 1, 80, 100, 34381), GEM_STALL(
            34385,
            75,
            intArrayOf(1623, 1621, 1619, 1617),
            1,
            180,
            16,
            34381
        );

        fun getItem(count: Int): Int {
            return item[count]
        }
    }
}