package game.content.entity.actor.player.skills

import com.fasterxml.jackson.core.type.TypeReference
import com.github.michaelbull.logging.InlineLogger
import game.entity.actor.player.Player
import game.entity.actor.player.data.Preset
import utility.functions.Misc
import utility.game.entity.actor.player.JacksonFactory
import java.nio.file.Paths
import kotlin.math.pow


/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
object PresetHandler {

    val presets = mutableListOf<Preset>()

    private val path = Paths.get("./data/repository/item/presets.yml")

    fun loadPresets() {
        val presets = JacksonFactory.mapper.readValue(path.toFile(), object : TypeReference<List<Preset?>?>() {})

        for (preset in presets!!) {
            preset?.let { this.presets.add(it) }
        }

        logger.info { "Loaded ${presets.size} presets successfully" }
    }

    fun dumpPreset(preset: Preset) {
        presets.add(preset)
        JacksonFactory.saveObject(presets, path.toString())
    }

    private val logger = InlineLogger()


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
        packets.sendHideIComponent(interfaceId, 3, false)
        packets.sendIComponentModel(interfaceId, 3, 835)
//            packets.sendHideIComponent(interfaceId, 1, true);
        packets.sendHideIComponent(interfaceId, 8, true)

        refresh(player)
    }

    fun refresh(player: Player) {
        val presets = arrayListOf<Preset>()

        val interfaceId = 34

        for (i in 0..29) {
            player.packets.sendGlobalString(149 + i, if (i < presets.size) presets[i].name else "")
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
        return (4.0.pow(noteId.toDouble()) * colour).toInt()
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