package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/28/2017
 */
class IcePlateauTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 89
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 3, MagicConstants.LAW_RUNE, 3, MagicConstants.WATER_RUNE, 8)
    }

    override fun destination(): WorldTile {
        return WorldTile(2972, 3873, 0)
    }

    override fun spellId(): Int {
        return 51
    }

    override fun exp(): Double {
        return 96.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}