package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class CatherbyTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 87
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 3, MagicConstants.LAW_RUNE, 3, MagicConstants.WATER_RUNE, 10)
    }

    override fun destination(): WorldTile {
        return WorldTile(2804, 3433, 0)
    }

    override fun spellId(): Int {
        return 44
    }

    override fun exp(): Double {
        return 93.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}