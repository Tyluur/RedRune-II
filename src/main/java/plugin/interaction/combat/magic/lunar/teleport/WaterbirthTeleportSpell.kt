package plugin.interaction.combat.magic.lunar.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class WaterbirthTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 72
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 1, MagicConstants.WATER_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(2527, 3739, 0)
    }

    override fun spellId(): Int {
        return 47
    }

    override fun exp(): Double {
        return 72.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}