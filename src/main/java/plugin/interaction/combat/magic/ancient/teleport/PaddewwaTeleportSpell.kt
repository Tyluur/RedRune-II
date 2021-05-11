package plugin.interaction.combat.magic.ancient.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class PaddewwaTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 54
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.FIRE_RUNE, 1, MagicConstants.AIR_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(3099, 9882, 0)
    }

    override fun spellId(): Int {
        return 40
    }

    override fun exp(): Double {
        return 64.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}