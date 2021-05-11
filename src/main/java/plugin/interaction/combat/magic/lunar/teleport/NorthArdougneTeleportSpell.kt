package plugin.interaction.combat.magic.lunar.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class NorthArdougneTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 76
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 1, MagicConstants.WATER_RUNE, 5)
    }

    override fun destination(): WorldTile {
        return WorldTile(2614, 3347, 0)
    }

    override fun spellId(): Int {
        return 69
    }

    override fun exp(): Double {
        return 76.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}