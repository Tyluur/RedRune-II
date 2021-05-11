package plugin.interaction.combat.magic.modern.teleport

import game.content.plugin.combat.spell.type.TeleportSpellPlugin
import game.global.WorldTile
import utility.constants.MagicConstants
import utility.constants.MagicConstants.MagicBook

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 7/27/2017
 */
class MobilisingArmiesTeleportSpell : TeleportSpellPlugin {
    override fun levelRequired(): Int {
        return 10
    }

    override fun runesRequired(): IntArray {
        return arguments(MagicConstants.LAW_RUNE, 1, MagicConstants.WATER_RUNE, 1, MagicConstants.AIR_RUNE, 1)
    }

    override fun destination(): WorldTile {
        return WorldTile(2413, 2848, 0)
    }

    override fun spellId(): Int {
        return 37
    }

    override fun exp(): Double {
        return 19.0
    }

    override fun book(): MagicBook {
        return MagicBook.REGULAR
    }
}