package plugin.interaction.combat.magic.modern.teleport

import utility.constants.MagicConstants.MagicBook
import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class ArdougneTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 51
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.WATER_RUNE, 2, MagicConstants.LAW_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(2664, 3305, 0)
    }

    override fun spellId(): Int {
        return 57
    }

    override fun exp(): Double {
        return 61.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}