package plugin.interaction.combat.magic.ancient.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class LassarTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 72
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.WATER_RUNE, 4)
    }

    override fun destination(): WorldTile {
        return WorldTile(3006, 3471, 0)
    }

    override fun spellId(): Int {
        return 43
    }

    override fun exp(): Double {
        return 82.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}