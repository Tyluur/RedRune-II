package plugin.rsinterface

import org.redrune.game.GameFlags
import org.redrune.game.content.entity.actor.player.controller.impl.activity.pvp.PvPWorld
import org.redrune.game.content.entity.actor.player.dialogue.impl.LevelUp
import org.redrune.game.content.entity.actor.player.dialogue.impl.SimpleNPCMessage
import org.redrune.game.content.plugin.type.InterfacePlugin
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.constants.SkillConstants.*
import org.redrune.utility.game.InputEvent

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
class SkillInterfacePlugin : InterfacePlugin {
    override fun handle(
        player: Player,
        interfaceId: Int,
        componentId: Int,
        itemId: Int,
        slotId: Int,
        packetId: Int,
    ): Boolean {
        when (packetId) {
            PacketConstants.ACTION_BUTTON1_PACKET -> if (interfaceId == 320) {
                player.stopAll()
                var lvlupSkill = -1
                var skillMenu = -1
                when (componentId) {
                    200 -> {
                        skillMenu = 1
                        if (player.temporaryAttributes.remove("leveledUp[0]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 1)
                        } else {
                            lvlupSkill = 0
                            player.packets.sendConfig(1230, 10)
                        }
                    }

                    11 -> {
                        skillMenu = 2
                        if (player.temporaryAttributes.remove("leveledUp[2]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 2)
                        } else {
                            lvlupSkill = 2
                            player.packets.sendConfig(1230, 20)
                        }
                    }

                    28 -> {
                        skillMenu = 5
                        if (player.temporaryAttributes.remove("leveledUp[1]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 5)
                        } else {
                            lvlupSkill = 1
                            player.packets.sendConfig(1230, 40)
                        }
                    }

                    52 -> {
                        skillMenu = 3
                        if (player.temporaryAttributes.remove("leveledUp[4]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 3)
                        } else {
                            lvlupSkill = 4
                            player.packets.sendConfig(1230, 30)
                        }
                    }

                    76 -> if (player.temporaryAttributes.remove("leveledUp[5]") !== java.lang.Boolean.TRUE) {
                        skillMenu = 7
                        player.packets.sendConfig(965, 7)
                    } else {
                        lvlupSkill = 5
                        player.packets.sendConfig(1230, 60)
                    }

                    93 -> if (player.temporaryAttributes.remove("leveledUp[6]") !== java.lang.Boolean.TRUE) {
                        skillMenu = 4
                        player.packets.sendConfig(965, 4)
                    } else {
                        lvlupSkill = 6
                        player.packets.sendConfig(1230, 33)
                    }

                    110 -> if (player.temporaryAttributes.remove("leveledUp[20]") !== java.lang.Boolean.TRUE) {
                        skillMenu = 12
                        player.packets.sendConfig(965, 12)
                    } else {
                        lvlupSkill = 20
                        player.packets.sendConfig(1230, 100)
                    }

                    134 -> {
                        skillMenu = 22
                        if (player.temporaryAttributes.remove("leveledUp[21]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 22)
                        } else {
                            lvlupSkill = 21
                            player.packets.sendConfig(1230, 698)
                        }
                    }

                    193 -> {
                        skillMenu = 6
                        if (player.temporaryAttributes.remove("leveledUp[3]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 6)
                        } else {
                            lvlupSkill = 3
                            player.packets.sendConfig(1230, 50)
                        }
                    }

                    19 -> {
                        skillMenu = 8
                        if (player.temporaryAttributes.remove("leveledUp[16]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 8)
                        } else {
                            lvlupSkill = 16
                            player.packets.sendConfig(1230, 65)
                        }
                    }

                    36 -> {
                        skillMenu = 9
                        if (player.temporaryAttributes.remove("leveledUp[15]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 9)
                        } else {
                            lvlupSkill = 15
                            player.packets.sendConfig(1230, 75)
                        }
                    }

                    60 -> {
                        skillMenu = 10
                        if (player.temporaryAttributes.remove("leveledUp[17]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 10)
                        } else {
                            lvlupSkill = 17
                            player.packets.sendConfig(1230, 80)
                        }
                    }

                    84 -> {
                        skillMenu = 11
                        if (player.temporaryAttributes.remove("leveledUp[12]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 11)
                        } else {
                            lvlupSkill = 12
                            player.packets.sendConfig(1230, 90)
                        }
                    }

                    101 -> {
                        skillMenu = 19
                        if (player.temporaryAttributes.remove("leveledUp[9]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 19)
                        } else {
                            lvlupSkill = 9
                            player.packets.sendConfig(1230, 665)
                        }
                    }

                    118 -> {
                        skillMenu = 20
                        if (player.temporaryAttributes.remove("leveledUp[18]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 20)
                        } else {
                            lvlupSkill = 18
                            player.packets.sendConfig(1230, 673)
                        }
                    }

                    142 -> {
                        skillMenu = 23
                        if (player.temporaryAttributes.remove("leveledUp[22]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 23)
                        } else {
                            lvlupSkill = 22
                            player.packets.sendConfig(1230, 689)
                        }
                    }

                    186 -> {
                        skillMenu = 13
                        if (player.temporaryAttributes.remove("leveledUp[14]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 13)
                        } else {
                            lvlupSkill = 14
                            player.packets.sendConfig(1230, 110)
                        }
                    }

                    179 -> {
                        skillMenu = 14
                        if (player.temporaryAttributes.remove("leveledUp[13]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 14)
                        } else {
                            lvlupSkill = 13
                            player.packets.sendConfig(1230, 115)
                        }
                    }

                    44 -> {
                        skillMenu = 15
                        if (player.temporaryAttributes.remove("leveledUp[10]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 15)
                        } else {
                            lvlupSkill = 10
                            player.packets.sendConfig(1230, 120)
                        }
                    }

                    68 -> {
                        skillMenu = 16
                        if (player.temporaryAttributes.remove("leveledUp[7]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 16)
                        } else {
                            lvlupSkill = 7
                            player.packets.sendConfig(1230, 641)
                        }
                    }

                    172 -> {
                        skillMenu = 17
                        if (player.temporaryAttributes.remove("leveledUp[11]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 17)
                        } else {
                            lvlupSkill = 11
                            player.packets.sendConfig(1230, 649)
                        }
                    }

                    165 -> {
                        skillMenu = 18
                        if (player.temporaryAttributes.remove("leveledUp[8]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 18)
                        } else {
                            lvlupSkill = 8
                            player.packets.sendConfig(1230, 660)
                        }
                    }

                    126 -> {
                        skillMenu = 21
                        if (player.temporaryAttributes.remove("leveledUp[19]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 21)
                        } else {
                            lvlupSkill = 19
                            player.packets.sendConfig(1230, 681)
                        }
                    }

                    150 -> {
                        skillMenu = 24
                        if (player.temporaryAttributes.remove("leveledUp[23]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 24)
                        } else {
                            lvlupSkill = 23
                            player.packets.sendConfig(1230, 705)
                        }
                    }

                    158 -> {
                        skillMenu = 25
                        if (player.temporaryAttributes.remove("leveledUp[24]") !== java.lang.Boolean.TRUE) {
                            player.packets.sendConfig(965, 25)
                        } else {
                            lvlupSkill = 24
                            player.packets.sendConfig(1230, 705)
                        }
                    }
                }
                val skillId = getSkillId(componentId)
                if (PvPWorld.inBankSafe(player) && isSettableSkill(componentId) && GameFlags.pvpWorld && skillId != -1) {
                    val name = SKILL_NAME[skillId]

                    if (player.isUnderCombat || player.equipment.isWearingArmour || player.isDead) {
                        player.dialogueManager.startDialogue(
                            SimpleNPCMessage::class.java, 945,
                            "Please take off any armour before changing your stats."
                        )
                        return true
                    }

                    when (skillId) {
                        ATTACK, STRENGTH, DEFENCE, HITPOINTS, MAGIC, RANGE -> {
                            player.packets.requestClientInput(object :
                                InputEvent("Enter a level:", InputEventType.INTEGER) {
                                override fun handleInput() {
                                    val level: Int = getInput()

                                    if (level <= 0 || level > 99) {
                                        player.dialogueManager.startDialogue(
                                            SimpleNPCMessage::class.java, 945,
                                            "Nice try, noob.",
                                        )
                                        return
                                    }

                                    if (skillId == HITPOINTS && level < 10) {
                                        player.dialogueManager.startDialogue(
                                            SimpleNPCMessage::class.java, 945,
                                            "Nice try, noob.",
                                        )
                                        return
                                    }


                                    player.skills[skillId] = level
                                    player.skills.setXp(skillId, getXPForLevel(level).toDouble())
                                    player.setNextGraphics(Graphics(1320))
                                    player.appearance.generateAppearanceData()

                                    player.dialogueManager.startDialogue(
                                        SimpleNPCMessage::class.java, 945,
                                        "Your $name level has just been set to $level!",
                                        "Enjoy!"
                                    )
                                }
                            })
                        }
                    }
                    return true
                }
                player.interfaceManager.sendInterface(if (lvlupSkill != -1) 741 else 499)
                if (lvlupSkill != -1) {
                    LevelUp.switchFlash(player, lvlupSkill, false)
                }
                if (skillMenu != -1) {
                    player.temporaryAttributes["skillMenu"] = skillMenu
                }
            } else if (interfaceId == 499) {
                var skillMenu = -1
                if (player.temporaryAttributes["skillMenu"] != null) {
                    skillMenu = (player.temporaryAttributes["skillMenu"] as Int?)!!
                }
                when (componentId) {
                    10 -> player.packets.sendConfig(965, skillMenu)
                    11 -> player.packets.sendConfig(965, 1024 + skillMenu)
                    12 -> player.packets.sendConfig(965, 2048 + skillMenu)
                    13 -> player.packets.sendConfig(965, 3072 + skillMenu)
                    14 -> player.packets.sendConfig(965, 4096 + skillMenu)
                    15 -> player.packets.sendConfig(965, 5120 + skillMenu)
                    16 -> player.packets.sendConfig(965, 6144 + skillMenu)
                    17 -> player.packets.sendConfig(965, 7168 + skillMenu)
                    18 -> player.packets.sendConfig(965, 8192 + skillMenu)
                    19 -> player.packets.sendConfig(965, 9216 + skillMenu)
                    20 -> player.packets.sendConfig(965, 10240 + skillMenu)
                    21 -> player.packets.sendConfig(965, 11264 + skillMenu)
                    22 -> player.packets.sendConfig(965, 12288 + skillMenu)
                    23 -> player.packets.sendConfig(965, 13312 + skillMenu)
                    29 -> player.stopAll()
                }
            }

            PacketConstants.ACTION_BUTTON2_PACKET, PacketConstants.ACTION_BUTTON3_PACKET -> if (interfaceId == 320) {
                // set xp target
                val skillId = player.skills.getTargetIdByComponentId(componentId)
                val usingLevel = packetId == PacketConstants.ACTION_BUTTON2_PACKET
                player.packets.requestClientInput(object : InputEvent(
                    "Please enter target " + (if (usingLevel) "level" else "xp") + " you want to set: ",
                    InputEventType.INTEGER
                ) {
                    override fun handleInput() {
                        if (!usingLevel) {
                            var xpTarget = getInput<Int>()
                            if (xpTarget < player.skills.getXp(player.skills.getSkillIdByTargetId(skillId)) || player.skills.getXp(
                                    player.skills.getSkillIdByTargetId(skillId)
                                ) >= 200000000
                            ) {
                                return
                            }
                            if (xpTarget > 200000000) {
                                xpTarget = 200000000
                            }
                            player.skills.setSkillTarget(false, skillId, xpTarget)
                        } else {
                            var levelTarget = getInput<Int>()
                            val curLevel = player.skills.getLevel(player.skills.getSkillIdByTargetId(skillId))
                            if (curLevel >= (if (skillId == 24) 120 else 99)) {
                                return
                            }
                            if (levelTarget > (if (skillId == 24) 120 else 99)) {
                                levelTarget = if (skillId == 24) 120 else 99
                            }
                            if (levelTarget < player.skills.getLevel(player.skills.getSkillIdByTargetId(skillId))) {
                                return
                            }
                            player.skills.setSkillTarget(true, skillId, levelTarget)
                        }
                    }
                })
            }

            PacketConstants.ACTION_BUTTON4_PACKET -> {
                val skillId = player.skills.getTargetIdByComponentId(componentId)
                player.skills.setSkillTargetEnabled(skillId, false)
                player.skills.setSkillTargetValue(skillId, 0)
                player.skills.setSkillTargetUsingLevelMode(skillId, false)
            }
        }
        return true
    }

    private fun getSkillId(componentId: Int): Int {
        return when (componentId) {
            200 -> ATTACK
            11 -> STRENGTH
            28 -> DEFENCE
            52 -> RANGE
            93 -> MAGIC
            193 -> HITPOINTS
            else -> -1
        }
    }

    override fun register() {
        registerInterfacePlugin(320, 499)
    }

    fun isSettableSkill(componentId: Int): Boolean {
        return when (componentId) {
            200, 11, 28, 52, 93, 193 -> {
                true
            }

            else -> {
                false
            }
        }
    }
}