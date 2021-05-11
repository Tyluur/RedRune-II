package plugin.interaction.combat.magic.ancient.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class HomeTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 0
    }

    override fun runesRequired(): IntArray {
        return IntArray(0)
    }

    override fun destination(): WorldTile {
        return GameConstants.START_TILE
    }

    override fun spellId(): Int {
        return 48
    }

    override fun exp(): Double {
        return 0.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }
}