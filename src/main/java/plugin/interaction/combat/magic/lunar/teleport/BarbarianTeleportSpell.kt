package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class BarbarianTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 75
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 2, MagicConstants.FIRE_RUNE, 3)
    }

    override fun destination(): WorldTile {
        return WorldTile(2544, 3572, 0)
    }

    override fun spellId(): Int {
        return 22
    }

    override fun exp(): Double {
        return 77.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}