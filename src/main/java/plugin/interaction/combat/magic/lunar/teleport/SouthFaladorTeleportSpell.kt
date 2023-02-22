package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/28/2017
 */
class SouthFaladorTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 72
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 2, MagicConstants.LAW_RUNE, 1, MagicConstants.AIR_RUNE, 2)
    }

    override fun destination(): WorldTile {
        return WorldTile(3005, 3327, 0)
    }

    override fun spellId(): Int {
        return 67
    }

    override fun exp(): Double {
        return 70.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }
}