package plugin.interaction.combat.magic.lunar.teleport

import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin
import org.redrune.game.global.WorldTile
import org.redrune.utility.constants.MagicConstants
import org.redrune.utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/28/2017
 */
class FishingGuildTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 85
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.ASTRAL_RUNE, 3, MagicConstants.LAW_RUNE, 3, MagicConstants.WATER_RUNE, 8)
    }

    override fun destination(): WorldTile {
        return WorldTile(2614, 3386, 0)
    }

    override fun spellId(): Int {
        return 40
    }

    override fun exp(): Double {
        return 90.0
    }

    override fun book(): MagicBook {
        return MagicBook.LUNAR
    }

    override fun randomize(): Boolean {
        return false
    }
}