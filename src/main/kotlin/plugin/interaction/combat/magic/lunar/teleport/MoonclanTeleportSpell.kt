package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class MoonclanTeleportSpell : TeleportSpellPlugin {
    override fun destination(): WorldTile {
        return WorldTile(2100, 3915, 0)
    }

    override fun levelRequired(): Int {
        return 69
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 1, MagicConstants.EARTH_RUNE, 2)
    }

    override fun spellId(): Int {
        return 43
    }

    override fun exp(): Double {
        return 66.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}