package plugin.interaction.combat.magic.ancient.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class CarrallangarTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 84
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 2, MagicConstants.SOUL_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(3217, 3677, 0)
    }

    override fun spellId(): Int {
        return 45
    }

    override fun exp(): Double {
        return 94.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}