package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class OuraniaTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 71
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 1, MagicConstants.ASTRAL_RUNE, 2, MagicConstants.EARTH_RUNE, 6)
    }

    override fun destination(): WorldTile {
        return WorldTile(2469, 3247, 0)
    }

    override fun spellId(): Int {
        return 54
    }

    override fun exp(): Double {
        return 69.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}