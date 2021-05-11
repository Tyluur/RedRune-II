package plugin.interaction.combat.magic.lunar.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class TrollheimTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 92
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 3, MagicConstants.LAW_RUNE, 3, MagicConstants.WATER_RUNE, 10)
    }

    override fun destination(): WorldTile {
        return WorldTile(2807, 3678, 0)
    }

    override fun spellId(): Int {
        return 75
    }

    override fun exp(): Double {
        return 101.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}