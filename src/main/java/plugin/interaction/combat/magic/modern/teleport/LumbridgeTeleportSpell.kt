package plugin.interaction.combat.magic.modern.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class LumbridgeTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 31
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.EARTH_RUNE, 1, MagicConstants.AIR_RUNE, 3, MagicConstants.LAW_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(3222, 3218, 0)
    }

    override fun spellId(): Int {
        return 43
    }

    override fun exp(): Double {
        return 41.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}