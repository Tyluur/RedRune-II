package plugin.interaction.combat.magic.ancient.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class DareeyakTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 78
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.FIRE_RUNE, 3, MagicConstants.AIR_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(2990, 3696, 0)
    }

    override fun spellId(): Int {
        return 44
    }

    override fun exp(): Double {
        return 88.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}