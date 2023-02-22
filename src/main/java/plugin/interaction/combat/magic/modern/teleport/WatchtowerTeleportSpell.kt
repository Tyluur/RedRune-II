package plugin.interaction.combat.magic.modern.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
class WatchtowerTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 58
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.EARTH_RUNE, 2, MagicConstants.LAW_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(2547, 3113, 2)
    }

    override fun spellId(): Int {
        return 62
    }

    override fun exp(): Double {
        return 68.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}