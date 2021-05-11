package plugin.interaction.combat.magic.ancient.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class KharyrllTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 66
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.BLOOD_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(3492, 3471, 0)
    }

    override fun spellId(): Int {
        return 42
    }

    override fun exp(): Double {
        return 76.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}