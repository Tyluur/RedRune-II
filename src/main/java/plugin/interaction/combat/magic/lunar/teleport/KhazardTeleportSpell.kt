package plugin.interaction.combat.magic.lunar.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class KhazardTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 78
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 2, MagicConstants.WATER_RUNE, 4)
    }

    override fun destination(): WorldTile {
        return WorldTile(2656, 3157, 0)
    }

    override fun spellId(): Int {
        return 41
    }

    override fun exp(): Double {
        return 80.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}