package plugin.interaction.combat.magic.ancient.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class SenntistenTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 60
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.SOUL_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(3360, 3387, 0)
    }

    override fun spellId(): Int {
        return 41
    }

    override fun exp(): Double {
        return 70.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}