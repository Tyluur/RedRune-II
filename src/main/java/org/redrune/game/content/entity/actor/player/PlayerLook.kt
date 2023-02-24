package org.redrune.game.content.entity.actor.player

import org.redrune.cache.loaders.ClientScriptMap
import org.redrune.game.content.entity.actor.player.dialogue.impl.MakeOverMage
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.player.Player

object PlayerLook {

    @JvmStatic
    fun openMageMakeOver(player: Player) {
        player.interfaceManager.sendInterface(900)
        player.packets.sendIComponentText(900, 33, "CONFIRM (3000 Gold)")
        player.packets.sendConfigByFile(6098, if (player.appearance.isMale) 0 else 1)
        player.packets.sendConfigByFile(6099, player.appearance.skinColor)
        player.temporaryAttributes["MageMakeOverGender"] = player.appearance.isMale
        player.temporaryAttributes["MageMakeOverSkin"] = player.appearance.skinColor
    }

    fun handleMageMakeOverButtons(player: Player, buttonId: Int) {
        if (buttonId == 14 || buttonId == 16 || buttonId == 15 || buttonId == 17) {
            player.temporaryAttributes["MageMakeOverGender"] = buttonId == 14 || buttonId == 16
        } else if (buttonId >= 20 && buttonId <= 31) {
            val skin: Int
            skin = if (buttonId == 31) {
                11
            } else if (buttonId == 30) {
                10
            } else if (buttonId == 20) {
                9
            } else if (buttonId == 21) {
                8
            } else if (buttonId == 22) {
                7
            } else if (buttonId == 29) {
                6
            } else if (buttonId == 28) {
                5
            } else if (buttonId == 27) {
                4
            } else if (buttonId == 26) {
                3
            } else if (buttonId == 25) {
                2
            } else if (buttonId == 24) {
                1
            } else {
                0
            }
            player.temporaryAttributes["MageMakeOverSkin"] = skin
        } else if (buttonId == 33) {
            val male = player.temporaryAttributes.remove("MageMakeOverGender") as Boolean?
            val skin = player.temporaryAttributes.remove("MageMakeOverSkin") as Int?
            player.closeInterfaces()
            if (male == null || skin == null) {
                return
            }
            if (male == player.appearance.isMale && skin == player.appearance.skinColor) {
                player.dialogueManager.startDialogue(MakeOverMage::class.java, 2676, 1)
            } else {
                player.dialogueManager.startDialogue(MakeOverMage::class.java, 2676, 2)
                if (player.appearance.isMale != male) {
                    if (player.equipment.isWearingArmour) {
                        player.dialogueManager.startDialogue(
                            "SimpleMessage",
                            "You cannot have armor on while changing your gender."
                        )
                        return
                    }
                    if (male) {
                        player.appearance.resetAppearence()
                    } else {
                        player.appearance.female()
                    }
                }
                player.appearance.skinColor = skin
                player.appearance.generateAppearanceData()
            }
        }
    }

    @JvmStatic
    fun openHairdresserSalon(player: Player) {
        if (player.equipment.hatId != -1) {
            player.dialogueManager.startDialogue(
                "SimpleNPCMessage",
                598,
                "I'm afraid I can't see your head at the moment.",
                "Please remove your headgear first."
            )
            return
        }
        if (player.equipment.weaponId != -1 || player.equipment.shieldId != -1) {
            player.dialogueManager.startDialogue(
                "SimpleNPCMessage",
                598,
                "I don't feel comfortable cutting hair",
                "when you are wielding something.",
                "Please remove what you are holding first."
            )
            return
        }
        player.nextAnimation = Animation(11623)
        player.interfaceManager.sendInterface(309)
        player.packets.sendUnlockIComponentOptionSlots(
            309,
            10,
            0,
            ClientScriptMap.getMap(if (player.appearance.isMale) 2339 else 2342).size * 2,
            0
        )
        player.packets.sendUnlockIComponentOptionSlots(309, 16, 0, ClientScriptMap.getMap(2345).size * 2, 0)
        player.packets.sendIComponentText(309, 20, "Free!")
        player.putTemporaryAttribute("hairSaloon", true)
        player.setCloseInterfacesEvent(Runnable {
            player.temporaryAttributes.remove("hairSaloon")
            player.dialogueManager.startDialogue(
                "SimpleNPCMessage",
                598,
                "An excellent choice, " + (if (player.appearance.isMale) "sir" else "lady") + "."
            )
            player.nextAnimation = Animation(-1)
            player.appearance.generateAppearanceData()
        })
    }

    fun handleHairdresserSalonButtons(player: Player, buttonId: Int, slotId: Int) {
        if (buttonId == 6) {
            player.temporaryAttributes["hairSaloon"] = true
        } else if (buttonId == 7) {
            player.temporaryAttributes["hairSaloon"] = false
        } else if (buttonId == 18) {
            player.closeInterfaces()
        } else if (buttonId == 10) {
            val hairSalon = player.temporaryAttributes["hairSaloon"] as Boolean?
            if (hairSalon != null && hairSalon) {
                player.appearance.setHairStyle(
                    ClientScriptMap.getMap(if (player.appearance.isMale) 2339 else 2342).getKeyForValue(slotId / 2)
                        .toInt()
                )
            } else if (player.appearance.isMale) {
                player.appearance.setBeardStyle(ClientScriptMap.getMap(703).getIntValue((slotId / 2).toLong()))
            }
        } else if (buttonId == 16) {
            player.appearance.setHairColor(ClientScriptMap.getMap(2345).getIntValue((slotId / 2).toLong()))
        }
    }

    @JvmStatic
    fun openThessaliasMakeOver(player: Player) {
        if (player.equipment.isWearingArmour) {
            player.dialogueManager.startDialogue(
                "SimpleNPCMessage",
                548,
                "You're not able to try on my clothes with all that armour."
            )
            return
        }
        player.nextAnimation = Animation(11623)
        player.interfaceManager.sendInterface(729)
        player.packets.sendIComponentText(729, 21, "Free!")
        player.temporaryAttributes["ThessaliasMakeOver"] = 0
        player.packets.sendUnlockIComponentOptionSlots(729, 12, 0, 100, 0)
        player.packets.sendUnlockIComponentOptionSlots(729, 17, 0, ClientScriptMap.getMap(3282).size * 2, 0)
        player.setCloseInterfacesEvent(Runnable {
            player.dialogueManager.startDialogue("SimpleNPCMessage", 548, "A marvellous choise. You look splendid!")
            player.nextAnimation = Animation(-1)
            player.appearance.appearanceData
            player.temporaryAttributes.remove("ThessaliasMakeOver")
        })
    }

    fun handleThessaliasMakeOverButtons(player: Player, buttonId: Int, slotId: Int) {
        if (buttonId == 6) {
            player.temporaryAttributes["ThessaliasMakeOver"] = 0
        } else if (buttonId == 7) {
            if (ClientScriptMap.getMap(if (player.appearance.isMale) 690 else 1591)
                    .getKeyForValue(player.appearance.topStyle) >= 32
            ) {
                player.temporaryAttributes["ThessaliasMakeOver"] = 1
            } else {
                player.packets.sendMessage("You can't select different arms to go with that top.")
            }
        } else if (buttonId == 8) {
            if (ClientScriptMap.getMap(if (player.appearance.isMale) 690 else 1591)
                    .getKeyForValue(player.appearance.topStyle) >= 32
            ) {
                player.temporaryAttributes["ThessaliasMakeOver"] = 2
            } else {
                player.packets.sendMessage("You can't select different wrists to go with that top.")
            }
        } else if (buttonId == 9) {
            player.temporaryAttributes["ThessaliasMakeOver"] = 3
        } else if (buttonId == 19) { //confirm
            player.closeInterfaces()
        } else if (buttonId == 12) { //set part
            val stage = player.temporaryAttributes["ThessaliasMakeOver"] as Int?
            if (stage == null || stage == 0) {
                player.appearance.topStyle =
                    ClientScriptMap.getMap(if (player.appearance.isMale) 690 else 1591)
                        .getIntValue((slotId / 2).toLong())
                if (!player.appearance.isMale) {
                    player.appearance.setBeardStyle(player.appearance.topStyle)
                }
                player.appearance.setArmsStyle(if (player.appearance.isMale) 26 else 65) //default
                player.appearance.setWristsStyle(if (player.appearance.isMale) 34 else 68) //default
            } else if (stage == 1) //arms
            {
                player.appearance.setArmsStyle(
                    ClientScriptMap.getMap(if (player.appearance.isMale) 711 else 693)
                        .getIntValue((slotId / 2).toLong())
                )
            } else if (stage == 2) //wrists
            {
                player.appearance.setWristsStyle(ClientScriptMap.getMap(751).getIntValue((slotId / 2).toLong()))
            } else {
                player.appearance.setLegsStyle(
                    ClientScriptMap.getMap(if (player.appearance.isMale) 1586 else 1607)
                        .getIntValue((slotId / 2).toLong())
                )
            }
        } else if (buttonId == 17) { //color
            val stage = player.temporaryAttributes["ThessaliasMakeOver"] as Int?
            if (stage == null || stage == 0 || stage == 1) {
                player.appearance.setTopColor(ClientScriptMap.getMap(3282).getIntValue((slotId / 2).toLong()))
            } else if (stage == 3) {
                player.appearance.setLegsColor(ClientScriptMap.getMap(3284).getIntValue((slotId / 2).toLong()))
            }
        }
    }
}