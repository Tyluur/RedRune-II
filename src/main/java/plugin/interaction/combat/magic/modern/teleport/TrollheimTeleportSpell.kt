package plugin.interaction.combat.magic.modern.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class TrollheimTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 61
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.FIRE_RUNE, 2, MagicConstants.LAW_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(2888, 3674, 0)
    }

    override fun spellId(): Int {
        return 69
    }

    override fun exp(): Double {
        return 68.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}