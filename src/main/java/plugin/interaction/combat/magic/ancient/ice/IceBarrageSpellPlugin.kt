package plugin.interaction.combat.magic.ancient.ice

import game.content.entity.actor.combat.player.style.MagicCombatStyle
import game.content.plugin.combat.spell.type.CombatSpellPlugin
import game.entity.actor.Actor
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import utility.constants.MagicConstants.MagicBook
import java.util.concurrent.TimeUnit

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/28/2017
 */
class IceBarrageSpellPlugin : CombatSpellPlugin {
    override fun spellId(): Int {
        return 23
    }

    override fun delay(player: Player): Int {
        return 4
    }

    override fun animationId(): Int {
        return 1979
    }

    // we don't store a static gfx because it is modifiable
    override fun hitGfx(): Int {
        return -1
    }

    override fun maxHit(player: Player, target: Actor): Int {
        return 300
    }

    override fun cast(source: Player, target: Actor, style: MagicCombatStyle) {
        val freezeDelayed = target.freezeDelayed()
        val frozenTarget = target.isFrozen
        style.sendMultiSpell(source, target, this, {
            if (frozenTarget || freezeDelayed) {
                return@sendMultiSpell
            }
            // only freeze the player if they are unfreezeable when the spell is cast.
            target.freeze(source, TimeUnit.SECONDS.toMillis(20), "You have been frozen!")
        }) {
            val gfx: Int
            val height: Int
            if (target.size >= 2 || freezeDelayed || frozenTarget) {
                gfx = 1677
                height = 100
            } else {
                gfx = 369
                height = 0
            }
            target.setNextGraphics(Graphics(gfx, 0, height))
        }
    }

    override fun exp(): Double {
        return 52.0
    }

    override fun book(): MagicBook {
        return MagicBook.ANCIENTS
    }

    override fun castSoundId(): Int {
        return 171
    }

    override fun impactSoundId(): Int {
        return 168
    }
}