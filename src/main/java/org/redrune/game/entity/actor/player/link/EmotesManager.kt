package org.redrune.game.entity.actor.player.link

import org.redrune.cache.loaders.NPCDefinitions
import org.redrune.engine.tick.task.WorldTask
import org.redrune.engine.tick.task.WorldTasksManager.schedule
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.WorldTile
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.functions.Misc
import java.io.Serializable

class EmotesManager : Serializable {
    private val unlockedEmotes: ArrayList<Int> = ArrayList()

    @Transient
    private var player: Player? = null

    @Transient
    private var nextEmoteEnd: Long = 0
    fun setPlayer(player: Player?) {
        this.player = player
    }

    fun unlockEmote(id: Int) {
        if (unlockedEmotes.contains(id)) {
            return
        }
        if (unlockedEmotes.add(id)) {
            refreshListConfigs()
        }
    }

    fun refreshListConfigs() {
        if (unlockedEmotes.contains(24) && unlockedEmotes.contains(25)) {
            player!!.packets.sendConfig(465, 7) // goblin quest emotes
        }
        var value1 = 0
        if (unlockedEmotes.contains(32)) {
            value1 += 1
        }
        if (unlockedEmotes.contains(30)) {
            value1 += 2
        }
        if (unlockedEmotes.contains(33)) {
            value1 += 4
        }
        if (unlockedEmotes.contains(31)) {
            value1 += 8
        }
        if (value1 > 0) {
            player!!.packets.sendConfig(802, value1) // stronghold of
        }
        // security emotes
        if (unlockedEmotes.contains(36)) {
            player!!.packets.sendConfig(1085, 249852) // hallowen hand emote
        }
        var value2 = 0
        if (unlockedEmotes.contains(29)) {
            value2 += 1
        }
        if (unlockedEmotes.contains(26)) {
            value2 += 2
        }
        if (unlockedEmotes.contains(27)) {
            value2 += 4
        }
        if (unlockedEmotes.contains(28)) {
            value2 += 8
        }
        if (unlockedEmotes.contains(37)) {
            value2 += 16
        }
        if (unlockedEmotes.contains(35)) {
            value2 += 32
        }
        if (unlockedEmotes.contains(34)) {
            value2 += 64
        }
        if (unlockedEmotes.contains(38)) {
            value2 += 128
        }
        if (unlockedEmotes.contains(39)) {
            value2 += 256
        }
        if (unlockedEmotes.contains(40)) {
            value2 += 512
        }
        if (unlockedEmotes.contains(41)) {
            value2 += 1024
        }
        if (unlockedEmotes.contains(42)) {
            value2 += 2048
        }
        if (unlockedEmotes.contains(43)) {
            value2 += 4096
        }
        if (unlockedEmotes.contains(44)) {
            value2 += 8192
        }
        if (unlockedEmotes.contains(46)) {
            value2 += 16384
        }
        if (unlockedEmotes.contains(45)) {
            value2 += 32768
        }
        if (value2 > 0) {
            player!!.varManager.sendVar(313, value2) //
        }
        player!!.varManager.sendVar(313, 1)
        if (unlockedEmotes.contains(47)) {
            player!!.varManager.sendVar(818, 1)
        }
        player!!.varManager.sendVar(465, 7)
        player!!.varManager.sendVar(802, -1)
        player!!.varManager.sendVar(1085, 249852)
        player!!.varManager.sendVar(313, -1)
        player!!.varManager.sendVar(2033, 1043648799)
        player!!.varManager.sendVar(2032, 7341)
        player!!.varManager.sendVar(1921, -893736236)
        player!!.varManager.sendVar(1404, 123728213)
        player!!.varManager.sendVar(2169, -1)
        player!!.varManager.sendVar(2230, -1)
        player!!.varManager.sendVar(1597, -1)
        player!!.varManager.sendVar(1842, -1)
        player!!.varManager.sendVar(2432, -1)
        player!!.varManager.sendVar(1958, 534)
        player!!.varManager.sendVar(2405, -1)
        player!!.varManager.sendVar(2458, -1)
    }

    fun useBookEmote(id: Int) {
        if (player!!.attackedByDelay + 10000 > Misc.currentTimeMillis()) {
            player!!.packets.sendMessage("You can't do this while you're under combat.")
            return
        }
        player!!.stopAll(false)
        if (!unlockedEmotes.contains(id)) {
            if (id == 41) {
                player!!.dialogueManager.startDialogue(
                    "SimpleMessage",
                    "This emote can be acessed by unlocking 70 pieces of music."
                )
            } else {
                unlockEmote(id)
                useBookEmote(id)
                player!!.dialogueManager.startDialogue("SimpleMessage", "You need to unlock this emote by yourself.")
                return
            }
        } else {
            if (Misc.currentTimeMillis() < nextEmoteEnd) {
                player!!.packets.sendMessage("You're already doing an emote!")
                return
            }
            if (id == 2) { // Yes
                player!!.nextAnimation = Animation(855)
            } else if (id == 3) { // No
                player!!.nextAnimation = Animation(856)
            } else if (id == 4) { // Bow
                player!!.nextAnimation = Animation(858)
            } else if (id == 5) { // Angry
                player!!.nextAnimation = Animation(859)
            } else if (id == 6) { // Think
                player!!.nextAnimation = Animation(857)
            } else if (id == 7) { // Wave
                player!!.nextAnimation = Animation(863)
            } else if (id == 8) { // Shrug
                player!!.nextAnimation = Animation(2113)
            } else if (id == 9) { // Cheer
                player!!.nextAnimation = Animation(862)
            } else if (id == 10) { // Beckon
                player!!.nextAnimation = Animation(864)
            } else if (id == 12) { // Laugh
                player!!.nextAnimation = Animation(861)
            } else if (id == 11) { // Jump for Joy
                player!!.nextAnimation = Animation(2109)
            } else if (id == 13) { // Yawn
                player!!.nextAnimation = Animation(2111)
            } else if (id == 14) { // Dance <3
                player!!.nextAnimation = Animation(866)
            } else if (id == 15) { // Jig
                player!!.nextAnimation = Animation(2106)
            } else if (id == 16) { // Twirl
                player!!.nextAnimation = Animation(2107)
            } else if (id == 17) { // Headbang
                player!!.nextAnimation = Animation(2108)
            } else if (id == 18) { // Cry
                player!!.nextAnimation = Animation(860)
            } else if (id == 19) { // Blow Kiss
                player!!.nextAnimation = Animation(1374)
                player!!.setNextGraphics(Graphics(1702))
            } else if (id == 20) { // Panic
                player!!.nextAnimation = Animation(2105)
            } else if (id == 21) { // Raspberry
                player!!.nextAnimation = Animation(2110)
            } else if (id == 22) { // Clap
                player!!.nextAnimation = Animation(865)
            } else if (id == 23) { // Salute
                player!!.nextAnimation = Animation(2112)
            } else if (id == 24) { // Goblin Bow
                player!!.nextAnimation = Animation(0x84F)
            } else if (id == 25) { // Goblin Salute
                player!!.nextAnimation = Animation(0x850)
            } else if (id == 26) { // Glass Box
                player!!.nextAnimation = Animation(1131)
            } else if (id == 27) { // Climb Rope
                player!!.nextAnimation = Animation(1130)
            } else if (id == 28) { // Lean
                player!!.nextAnimation = Animation(1129)
            } else if (id == 29) { // Glass Wall
                player!!.nextAnimation = Animation(1128)
            } else if (id == 30) { // Idea
                player!!.nextAnimation = Animation(4275)
            } else if (id == 31) { // Stomp
                player!!.nextAnimation = Animation(1745)
            } else if (id == 32) { // Flap
                player!!.nextAnimation = Animation(4280)
            } else if (id == 33) { // Slap Head
                player!!.nextAnimation = Animation(4276)
            } else if (id == 34) { // Zombie Walk
                player!!.nextAnimation = Animation(3544)
            } else if (id == 35) { // Zombie Dance
                player!!.nextAnimation = Animation(3543)
            } else if (id == 36) { // Zombie Hand
                player!!.nextAnimation = Animation(7272)
                player!!.setNextGraphics(Graphics(1244))
            } else if (id == 37) { // Scared
                player!!.nextAnimation = Animation(2836)
            } else if (id == 38) { // Bunny Hop
                player!!.nextAnimation = Animation(6111)
            } else if (id == 39) { // Skillcape
                val capeId = player!!.equipment.capeId
                when (capeId) {
                    9747, 9748, 10639 -> {
                        player!!.nextAnimation = Animation(4959)
                        player!!.setNextGraphics(Graphics(823))
                    }
                    9753, 9754, 10641 -> {
                        player!!.nextAnimation = Animation(4961)
                        player!!.setNextGraphics(Graphics(824))
                    }
                    9750, 9751, 10640 -> {
                        player!!.nextAnimation = Animation(4981)
                        player!!.setNextGraphics(Graphics(828))
                    }
                    9768, 9769, 10647 -> {
                        player!!.nextAnimation = Animation(14242)
                        player!!.setNextGraphics(Graphics(2745))
                    }
                    9756, 9757, 10642 -> {
                        player!!.nextAnimation = Animation(4973)
                        player!!.setNextGraphics(Graphics(832))
                    }
                    9762, 9763, 10644 -> {
                        player!!.nextAnimation = Animation(4939)
                        player!!.setNextGraphics(Graphics(813))
                    }
                    9759, 9760, 10643 -> {
                        player!!.nextAnimation = Animation(4979)
                        player!!.setNextGraphics(Graphics(829))
                    }
                    9801, 9802, 10658 -> {
                        player!!.nextAnimation = Animation(4955)
                        player!!.setNextGraphics(Graphics(821))
                    }
                    9807, 9808, 10660 -> {
                        player!!.nextAnimation = Animation(4957)
                        player!!.setNextGraphics(Graphics(822))
                    }
                    9783, 9784, 10652 -> {
                        player!!.nextAnimation = Animation(4937)
                        player!!.setNextGraphics(Graphics(812))
                    }
                    9798, 9799, 10657 -> {
                        player!!.nextAnimation = Animation(4951)
                        player!!.setNextGraphics(Graphics(819))
                    }
                    9804, 9805, 10659 -> {
                        player!!.nextAnimation = Animation(4975)
                        player!!.setNextGraphics(Graphics(831))
                    }
                    9780, 9781, 10651 -> {
                        player!!.nextAnimation = Animation(4949)
                        player!!.setNextGraphics(Graphics(818))
                    }
                    9795, 9796, 10656 -> {
                        player!!.nextAnimation = Animation(4943)
                        player!!.setNextGraphics(Graphics(815))
                    }
                    9792, 9793, 10655 -> {
                        player!!.nextAnimation = Animation(4941)
                        player!!.setNextGraphics(Graphics(814))
                    }
                    9774, 9775, 10649 -> {
                        player!!.nextAnimation = Animation(4969)
                        player!!.setNextGraphics(Graphics(835))
                    }
                    9771, 9772, 10648 -> {
                        player!!.nextAnimation = Animation(4977)
                        player!!.setNextGraphics(Graphics(830))
                    }
                    9777, 9778, 10650 -> {
                        player!!.nextAnimation = Animation(4965)
                        player!!.setNextGraphics(Graphics(826))
                    }
                    9786, 9787, 10653 -> {
                        player!!.nextAnimation = Animation(4967)
                        player!!.setNextGraphics(Graphics(1656))
                    }
                    9810, 9811, 10611 -> {
                        player!!.nextAnimation = Animation(4963)
                        player!!.setNextGraphics(Graphics(825))
                    }
                    9765, 9766, 10645 -> {
                        player!!.nextAnimation = Animation(4947)
                        player!!.setNextGraphics(Graphics(817))
                    }
                    9789, 9790, 10654 -> {
                        player!!.nextAnimation = Animation(4953)
                        player!!.setNextGraphics(Graphics(820))
                    }
                    12169, 12170, 12524 -> {
                        player!!.nextAnimation = Animation(8525)
                        player!!.setNextGraphics(Graphics(1515))
                    }
                    9948, 9949, 10646 -> {
                        player!!.nextAnimation = Animation(5158)
                        player!!.setNextGraphics(Graphics(907))
                    }
                    9813, 10662 -> {
                        player!!.nextAnimation = Animation(4945)
                        player!!.setNextGraphics(Graphics(816))
                    }
                    18508, 18509 -> {
                        val rand = (Math.random() * (2 + 1)).toInt()
                        player!!.nextAnimation = Animation(13190)
                        player!!.setNextGraphics(Graphics(2442))
                        schedule(object : WorldTask() {
                            var step = 0
                            override fun run() {
                                if (step == 1) {
                                    player!!.appearance.transformIntoNPC(if (rand == 0) 11227 else if (rand == 1) 11228 else 11229)
                                    player!!.nextAnimation =
                                        Animation(if (rand > 0) 13192 else if (rand == 1) 13193 else 13194)
                                }
                                if (step == 3) {
                                    player!!.appearance.transformIntoNPC(-1)
                                    stop()
                                }
                                step++
                            }
                        }, 0, 1)
                    }
                    19709, 19710 -> {
                    }
                    20763 -> {
                        if (player!!.controllerManager.controller != null) {
                            player!!.packets.sendMessage("You cannot do this here!")
                            return
                        }
                        player!!.nextAnimation = Animation(352)
                        player!!.setNextGraphics(Graphics(1446))
                    }
                    20765 -> {
                        if (player!!.controllerManager.controller != null) {
                            player!!.packets.sendMessage("You cannot do this here!")
                            return
                        }
                        val random = Misc.getRandom(2)
                        player!!.nextAnimation = Animation(122)
                        player!!.setNextGraphics(Graphics(if (random == 0) 1471 else 1466))
                    }
                    20767 -> {
                        if (player!!.controllerManager.controller != null) {
                            player!!.packets.sendMessage("This emote is currently unavailable.")
                            return
                        }
                        val size = NPCDefinitions.getNPCDefinitions(1224).size
                        var spawnTile: WorldTile? = WorldTile(WorldTile(player!!.x + 1, player!!.y, player!!.plane))
                        if (!RegionManager.canMoveNPC(spawnTile!!.plane, spawnTile.x, spawnTile.y, size)) {
                            spawnTile = null
                            val dirs = Misc.getCoordOffsetsNear(size)
                            var dir = 0
                            while (dir < dirs[0].size) {
                                val tile = WorldTile(
                                    WorldTile(
                                        player!!.x + dirs[0][dir],
                                        player!!.y + dirs[1][dir],
                                        player!!.plane
                                    )
                                )
                                if (RegionManager.canMoveNPC(tile.plane, tile.x, tile.y, size)) {
                                    spawnTile = tile
                                    break
                                }
                                dir++
                            }
                        }
                        if (spawnTile == null) {
                            player!!.packets.sendMessage("Need more space to perform this skillcape emote.")
                            return
                        }
                        nextEmoteEnd = Misc.currentTimeMillis() + 25 * 600
                        val npcTile: WorldTile = spawnTile
                        schedule(object : WorldTask() {
                            private var step = 0
                            private var npc: NPC? = null
                            override fun run() {
                                if (step == 0) {
                                    npc = NPC(1224, npcTile, -1, true)
                                    npc!!.nextAnimation = Animation(1434)
                                    npc!!.setNextGraphics(Graphics(1482))
                                    player!!.nextAnimation = Animation(1179)
                                    npc!!.setNextFaceActor(player)
                                    player!!.setNextFaceActor(npc)
                                } else if (step == 2) {
                                    npc!!.nextAnimation = Animation(1436)
                                    npc!!.setNextGraphics(Graphics(1486))
                                    player!!.nextAnimation = Animation(1180)
                                } else if (step == 3) {
                                    npc!!.setNextGraphics(Graphics(1498))
                                    player!!.nextAnimation = Animation(1181)
                                } else if (step == 4) {
                                    player!!.nextAnimation = Animation(1182)
                                } else if (step == 5) {
                                    npc!!.nextAnimation = Animation(1448)
                                    player!!.nextAnimation = Animation(1250)
                                } else if (step == 6) {
                                    player!!.nextAnimation = Animation(1251)
                                    player!!.setNextGraphics(Graphics(1499))
                                    npc!!.nextAnimation = Animation(1454)
                                    npc!!.setNextGraphics(Graphics(1504))
                                } else if (step == 11) {
                                    player!!.nextAnimation = Animation(1291)
                                    player!!.setNextGraphics(Graphics(1686))
                                    player!!.setNextGraphics(Graphics(1598))
                                    npc!!.nextAnimation = Animation(1440)
                                } else if (step == 16) {
                                    player!!.setNextFaceActor(null)
                                    npc!!.finish()
                                    stop()
                                }
                                step++
                            }
                        }, 0, 1)
                    }
                    20769, 20771 -> {
                        if (!RegionManager.canMoveNPC(player!!.plane, player!!.x, player!!.y, 3)) {
                            player!!.packets.sendMessage("Need more space to perform this skillcape emote.")
                            return
                        } else if (player!!.controllerManager.controller != null) {
                            player!!.packets.sendMessage("Dont annoy other players!")
                            return
                        }
                        nextEmoteEnd = Misc.currentTimeMillis() + 20 * 600
                        schedule(object : WorldTask() {
                            private var step = 0
                            override fun run() {
                                if (step == 0) {
                                    player!!.nextAnimation = Animation(356)
                                    player!!.setNextGraphics(Graphics(307))
                                } else if (step == 2) {
                                    player!!.appearance.transformIntoNPC(if (capeId == 20769) 1830 else 3372)
                                    player!!.nextAnimation = Animation(1174)
                                    player!!.setNextGraphics(Graphics(1443))
                                } else if (step == 4) {
                                    player!!.packets.sendCameraShake(3, 25, 50, 25, 50)
                                } else if (step == 5) {
                                    player!!.packets.sendStopCameraShake()
                                } else if (step == 8) {
                                    player!!.appearance.transformIntoNPC(-1)
                                    player!!.nextAnimation = Animation(1175)
                                    stop()
                                }
                                step++
                            }
                        }, 0, 1)
                    }
                    else -> player!!.packets.sendMessage("You need to be wearing a skillcape in order to perform this emote.")
                }
            } else if (id == 40) { // Snowman Dance
                player!!.nextAnimation = Animation(7531)
            } else if (id == 41) { // Air Guitar
                player!!.nextAnimation = Animation(2414)
                player!!.setNextGraphics(Graphics(1537))
                player!!.packets.sendMusicEffect(302)
            } else if (id == 42) { // Safety First
                player!!.nextAnimation = Animation(8770)
                player!!.setNextGraphics(Graphics(1553))
            } else if (id == 43) { // Explore
                player!!.nextAnimation = Animation(9990)
                player!!.setNextGraphics(Graphics(1734))
            } else if (id == 44) { // Trick
                player!!.nextAnimation = Animation(10530)
                player!!.setNextGraphics(Graphics(1864))
            } else if (id == 45) { // Freeze
                player!!.nextAnimation = Animation(11044)
                player!!.setNextGraphics(Graphics(1973))
            } else if (id == 46) { // Turkey
                schedule(object : WorldTask() {
                    private var step = 0
                    override fun run() {
                        if (step == 0) {
                            player!!.nextAnimation = Animation(10994)
                            player!!.setNextGraphics(Graphics(86))
                        } else if (step == 1) {
                            player!!.nextAnimation = Animation(10996)
                            player!!.appearance.transformIntoNPC(8499)
                        } else if (step == 6) {
                            player!!.nextAnimation = Animation(10995)
                            player!!.setNextGraphics(Graphics(86))
                            player!!.appearance.transformIntoNPC(-1)
                            stop()
                        }
                        step++
                    }
                }, 0, 1)
            } else if (id == 47) { // Around the world in Eggty days.
                player!!.nextAnimation = Animation(11542)
                player!!.setNextGraphics(Graphics(2037))
            } else if (id == 48) { // Dramatic Point
                player!!.nextAnimation = Animation(12658)
                player!!.setNextGraphics(Graphics(780))
            } else if (id == 49) { // Faint
                player!!.nextAnimation = Animation(14165)
            } else if (id == 50) { // Puppet Master
                player!!.nextAnimation = Animation(14869)
                player!!.setNextGraphics(Graphics(2837))
            } else if (id == 51) { // Taskmaster
                player!!.nextAnimation = Animation(15034)
                player!!.setNextGraphics(Graphics(2930))
            } else if (id == 52) { // Seal Of Approval
                schedule(object : WorldTask() {
                    val random = (Math.random() * (2 + 1)).toInt()
                    private var step = 0
                    override fun run() {
                        if (step == 0) {
                            player!!.nextAnimation = Animation(15104)
                            player!!.setNextGraphics(Graphics(1287))
                        } else if (step == 1) {
                            player!!.nextAnimation = Animation(15106)
                            player!!.appearance.transformIntoNPC(if (random == 0) 13255 else if (random == 1) 13256 else 13257)
                        } else if (step == 2) {
                            player!!.nextAnimation = Animation(15108)
                        } else if (step == 3) {
                            player!!.nextAnimation = Animation(15105)
                            player!!.setNextGraphics(Graphics(1287))
                            player!!.appearance.transformIntoNPC(-1)
                            stop()
                        }
                        step++
                    }
                }, 0, 1)
            } else if (id == 53) { // Invoke Spring
                player!!.nextAnimation = Animation(15357)
                player!!.setNextGraphics(Graphics(1391))
            }
            if (id == 39) {
                setNextEmoteEnd()
            }
        }
    }

    fun setNextEmoteEnd() {
        nextEmoteEnd = player!!.lastAnimationEnd - 600
    }

    fun getNextEmoteEnd(): Long {
        return nextEmoteEnd
    }

    fun setNextEmoteEnd(delay: Long) {
        nextEmoteEnd = Misc.currentTimeMillis() + delay
    }

    fun unlockEmotesBook() {
        player!!.packets.sendUnlockIComponentOptionSlots(590, 8, 0, 103, 0, 1)
    }

    companion object {
        private const val serialVersionUID = 8489480378717534336L
        fun getId(slotId: Int, packetId: Int): Int {
            return if (slotId >= 50 && slotId <= 63) {
                slotId
            } else when (slotId) {
                0 -> 2
                1 -> 3
                2 -> if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
                    4
                } else {
                    -1 // TODO new bow emote
                }
                3 -> 5
                4 -> 6
                5 -> 7
                6 -> 8
                7 -> 9
                8 -> 10
                9 -> 12
                10 -> 11
                11 -> 13
                12 -> 14
                13 -> 15
                14 -> 16
                15 -> 17
                16 -> 18
                17 -> 19
                18 -> 20
                19 -> 21
                20 -> 22
                21 -> 23
                22 -> 24
                23 -> 25
                24 -> 26
                25 -> 27
                26 -> 28
                27 -> 29
                28 -> 30
                29 -> 31
                30 -> 32
                31 -> 33
                32 -> 34
                33 -> 35
                34 -> 36
                35 -> 37
                36 -> 38
                37 -> 39
                38 -> 40
                39 -> 41
                40 -> 42
                41 -> 43
                42 -> 44
                43 -> 45
                44 -> 46
                45 -> 47
                46 -> 48
                47 -> 49
                48 -> 50
                49 -> 51
                50 -> 52
                else -> -1
            }
        }
    }

    init {
        for (emoteId in 2..23) {
            unlockedEmotes.add(emoteId)
        }
        unlockedEmotes.add(39) // skillcape
    }
}