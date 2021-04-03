package org.redrune.game.content.entity.actor.player.skills

import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item
import org.redrune.utility.functions.Misc

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
class PresetHandler {

    enum class Preset(val title: String) {

        PURE("Pure") {
            override fun inventory() = hashMapOf(
                1 to Item(6570, 1),
                2 to Item(1725, 1),
                3 to Item(4587, 1),
                4 to Item(544, 1),
                5 to Item(3842, 1),
                7 to Item(542, 1),
                9 to Item(7459, 1),
                10 to Item(3105, 1)
            )

            override fun equipment() = hashMapOf(
                1 to Item(6570, 1),
                2 to Item(1725, 1),
                3 to Item(4587, 1),
                4 to Item(544, 1),
                5 to Item(3842, 1),
                7 to Item(542, 1),
                9 to Item(7459, 1),
                10 to Item(3105, 1)
            )

            override fun skills() = hashMapOf(
                0 to 60,
                1 to 1,
                2 to 99,
                3 to 99,
                4 to 99,
                5 to 1,
                6 to 99
            )

            override fun spellBook() = 1

            override fun prayerBook() = 2

        },


        PURE_RANGE("Pure - Ranged") {
            override fun inventory() = hashMapOf(
                0 to Item(6685, 1),
                1 to Item(6685, 1),
                2 to Item(3024, 1),
                3 to Item(3024, 1),
                4 to Item(2440, 1),
                5 to Item(2436, 1),
                6 to Item(2444, 1),
                7 to Item(15272, 1),
                8 to Item(15272, 1),
                9 to Item(15272, 1),
                10 to Item(15272, 1),
                11 to Item(15272, 1),
                12 to Item(15272, 1),
                13 to Item(15272, 1),
                14 to Item(15272, 1),
                15 to Item(15272, 1),
                16 to Item(15272, 1),
                17 to Item(15272, 1),
                18 to Item(15272, 1),
                19 to Item(15272, 1),
                20 to Item(4153, 1),
                21 to Item(15272, 1),
                22 to Item(15272, 1),
                23 to Item(15272, 1),
                24 to Item(15272, 1),
                25 to Item(15272, 1),
                26 to Item(15272, 1),
                27 to Item(15272, 1)
            )

            override fun equipment() = hashMapOf(
                1 to Item(6570, 1),
                2 to Item(1725, 1),
                3 to Item(861, 1),
                4 to Item(544, 1),
                7 to Item(2497, 1),
                9 to Item(7459, 1),
                10 to Item(3105, 1),
                13 to Item(892, 1000),
            )

            override fun skills() = hashMapOf(
                0 to 60,
                1 to 1,
                2 to 99,
                3 to 99,
                4 to 99,
                6 to 99,
            )

            override fun spellBook() = 2

            override fun prayerBook() = 1


        },

        /* ZERKER("Zerker") {

         },

         ZERKER_BRID("Zerker - Hybrid") {

         },

         MAIN("Main") {

         },


         MAIN_HYBRID("Main - Hybrid") {

         }
 */
        ;

        abstract fun inventory(): HashMap<Int, Item>

        abstract fun equipment(): HashMap<Int, Item>

        abstract fun skills(): HashMap<Int, Int>

        abstract fun spellBook(): Int

        abstract fun prayerBook(): Int
                ;


    }

    companion object {

        /**
         * The indented values
         */
        private const val INDENT = "          "

        fun sendLoginConfiguration(player: Player) {
            val interfaceId = 34
            val packets = player.packets
            packets.sendIComponentSettings(34, 9, 0, 30, 2621470)
            for (i in 10..15) {
                packets.sendHideIComponent(34, i, true)
            }
            packets.sendHideIComponent(interfaceId, 3, false);
            packets.sendIComponentModel(interfaceId, 3, 835)
//            packets.sendHideIComponent(interfaceId, 1, true);
            packets.sendHideIComponent(interfaceId, 8, true);

            refresh(player)
        }

        fun refresh(player: Player) {
            val presets = arrayListOf<Preset>()

            presets.addAll(Preset.values())

            val interfaceId = 34

            for (i in 0..29) {
                player.packets.sendGlobalString(149 + i, if (i < presets.size) presets[i].title else "")
            }

            var colour = 0

            for (i in presets.indices) {
                val preset = presets[i]

                colour += colourize(Misc.random(1, 3), i)

                player.packets.sendConfig(1440, colour)
            }

            player.packets.sendHideIComponent(interfaceId, 3, true)
            player.packets.sendConfig(1439, -1)
            player.packets.sendIComponentText(
                interfaceId,
                2,
                "Presets"
            )

        }

        /**
         * Unlocks the note interface
         *
         * @param player
         * The player to unlock the note interface for.
         */
        fun unlock(player: Player) {
            val encoder = player.packets
            encoder.sendIComponentSettings(34, 9, 0, 30, 2621470)
            encoder.sendHideIComponent(34, 3, false)
            encoder.sendHideIComponent(34, 44, false)
            for (i in 10..15) {
                encoder.sendHideIComponent(34, i, true)
            }
            player.packets.sendConfig(1439, -1)
            for (i in 1430..1449) {
                player.packets.sendConfig(i, i)
            }
            refresh(player)
        }

        private fun refresh(player: Player, journalText: List<String>) {
            for (i in 0..29) {
                player.packets.sendGlobalString(149 + i, if (journalText.size <= i) "" else journalText[i])
            }
            player.packets.sendConfig(1440, getPrimaryColour(journalText))
            player.packets.sendConfig(1441, getSecondaryColour(journalText))
        }

        fun displayJournalInformation(player: Player) {
            val journalText = ArrayList<String>()
            journalText.add("[COLOUR=0]My Presets")

            // player.getPresetManager().getPresets().keySet()
            //    .forEach { key -> journalText.add("[PRESETFLAG]+" + INDENT + key) }

            journalText.add("[COLOUR=1]+" + INDENT + "Click to add")
            journalText.add("[COLOUR=0]Default Presets")

            //DefaultPresetsLoader.getDefaultPresets()
            //    .forEach { preset -> journalText.add("[PRESETFLAG]+ " + INDENT + preset.getName()) }


            player.packets.sendIComponentSettings(34, 9, 0, 30, 2621470)
            player.packets.sendHideIComponent(34, 3, false)
            player.packets.sendHideIComponent(34, 8, true)
            player.packets.sendHideIComponent(34, 44, false)
            player.packets.sendConfig(1437, 1) // unlocks add notes
            player.packets.sendConfig(1439, -1)
            refresh(player, journalText)

            player.packets.sendConfig(1439, journalText.size)

            for (i in 0..29) {
                player.packets.sendGlobalString(
                    149 + i,
                    filterTags(if (i >= journalText.size) "" else journalText[i])
                )
            }
            player.packets.sendConfig(
                1440,
                getPrimaryColour(journalText)
            )
            player.packets.sendConfig(
                1441,
                getSecondaryColour(journalText)
            )
        }

        private fun filterTags(text: String): String {
            val indexOfClose = text.indexOf("]")
            return text.substring(indexOfClose + 1, text.length)
        }

        fun getPrimaryColour(journalText: List<String>): Int {
            var color = 0
            for (i in 0..15) {
                if (journalText.size > i) {
                    color += colourize(
                        getColour(
                            journalText[i]
                        ), i
                    )
                }
            }
            return color
        }

        fun getSecondaryColour(journalText: List<String>): Int {
            var color = 0
            for (i in 0..14) {
                if (journalText.size > i + 16) {
                    color += colourize(
                        getColour(
                            journalText[i + 16]
                        ), i
                    )
                }
            }
            return color
        }

        /**
         * Colourizes text in the note tab
         *
         * @param colour
         * The colour id for the text
         * @param noteId
         * The note id to colour
         */
        private fun colourize(colour: Int, noteId: Int): Int {
            return (Math.pow(4.0, noteId.toDouble()) * colour).toInt()
        }

        private fun getColour(text: String): Int {
            if (text.contains("[COLOUR=")) {
                val openIndex = text.indexOf("[")
                val closeIndex = text.indexOf("]")
                val substring = text.substring(openIndex, closeIndex)
                return substring.substring(text.indexOf("=") + 1, closeIndex).toInt()
            } else if (text.contains("[PRESETFLAG]")) {
                return 1
            }
            return 1
        }

    }

}