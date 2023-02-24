package plugin.interaction.combat.range

import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.engine.cycle.GameCycleWorker.Companion.ticksPassed
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.functions.RandomFunction
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/6/2017
 */
class CrossbowPlugin : RangeWeaponPlugin() {
    override fun getWeaponNames(): Array<String> {
        return arguments("* crossbow")
    }

    override fun fire(source: Player, target: Actor, style: AbstractCombatStyle) {
        val weaponId = source.equipment.weaponId
        val ammoId = source.equipment.ammoId
        val name = ItemDefinitions.getItemDefinitions(weaponId).name.toLowerCase()
        ProjectileManager.sendProjectile(
            ProjectileManager.createSpeedDefinedProjectile(
                source,
                target,
                27,
                38,
                36,
                41,
                5,
                0
            )
        )
        val optional = BoltSpecial.getBoltSpecial(ammoId)
        // found a possible bolt
        if (optional.isPresent) {
            val special = optional.get()
            // the bolt was fired so we don't need to send another hit
            if (special.canFire(source, target)) {
                special.fire(source, target, style, weaponId)
                return
            }
        }
        if (!name.contains("karil's crossbow")) {
            dropAmmo(source, target, 1)
        } else {
            source.equipment.removeAmmo(ammoId, 1)
        }
        style.sendHit(
            source,
            target,
            style.calculator.getMaximumHit(source, 1.0),
            style.getRandomDamage(source, target, 1.0),
            ProjectileManager.getProjectileDelay(source, target)
        )
    }

    private enum class BoltSpecial(
        /**
         * The id of the bolt used for this special
         */
        val boltId: Int,
        /**
         * The id of the graphics
         */
        val graphicsId: Int,
        /**
         * The height of the graphics
         */
        val graphicsHeight: Int = 0,
    ) {
        JADE_BOLT(9237, 755) {
            override fun getDamageModifier(): Double {
                return 1.0
            }

            override fun fire(
                source: Player,
                target: Actor,
                style: AbstractCombatStyle,
                weaponId: Int,
            ): CombatSwingDetail {
                if (target.isNPC) {
                    target.toNPC().combat.target = null
                } else {
                    target.toPlayer().stopAll()
                }
                return super.fire(source, target, style, weaponId)
            }
        },
        RUBY_BOLT(9242, 754) {
            override fun getDamageModifier(): Double {
                return 1.0
            }

            override fun fire(
                source: Player,
                target: Actor,
                style: AbstractCombatStyle,
                weaponId: Int,
            ): CombatSwingDetail {
                target.setNextGraphics(Graphics(graphicsId, graphicsHeight, 0))
                source.applyHit(
                    Hit(
                        target,
                        if (source.hitpoints > 20) (source.hitpoints * 0.10).toInt() else 1,
                        HitSplat.REFLECTED_DAMAGE
                    )
                )
                return style.sendHit(
                    source,
                    target,
                    style.calculator.getMaximumHit(source, 1.0),
                    (target.hitpoints * 0.20).toInt(),
                    ProjectileManager.getProjectileDelay(source, target)
                )
            }
        },
        DIAMOND_BOLT(9243, 758) {
            override fun getDamageModifier(): Double {
                return 1.05
            }
        },
        DRAGON_BOLT(9244, 756) {
            override fun getDamageModifier(): Double {
                return 1.45
            }

            override fun canFire(source: Player, target: Actor?): Boolean {
                return !CombatAlgorithm.hasAntiDragProtection(target) && super.canFire(source, target)
            }
        },
        ONYX_BOLT(9245, 753) {
            override fun getDamageModifier(): Double {
                return 1.25
            }

            override fun canFire(source: Player, target: Actor?): Boolean {
                return source.getTemporaryAttribute("onyx-effect", 0L) <= ticksPassed && super.canFire(source, target)
            }

            override fun fire(
                source: Player,
                target: Actor,
                style: AbstractCombatStyle,
                weaponId: Int,
            ): CombatSwingDetail {
                return super.fire(source, target, style, weaponId).consume { detail: CombatSwingDetail ->
                    source.putTemporaryAttribute("onyx-effect", ticksPassed + 12)
                    source.heal((detail.hit.damage * 0.25).toInt())
                }
            }
        };

        /**
         * The damage modifier of the bolt special
         */
        abstract fun getDamageModifier(): Double

        /**
         * Checks if the bolt special can be fired
         *
         * @param source
         * The source of the special
         */
        open fun canFire(source: Player, target: Actor?): Boolean {
            return RandomFunction.random(13) == 5
        }

        /**
         * When the style is fired
         */
        open fun fire(source: Player, target: Actor, style: AbstractCombatStyle, weaponId: Int): CombatSwingDetail {
            target.setNextGraphics(Graphics(graphicsId, graphicsHeight, 0))
            return style.sendHit(
                source,
                target,
                style.calculator.getMaximumHit(source, 1.0),
                style.getRandomDamage(source, target, 1.0),
                ProjectileManager.getProjectileDelay(source, target)
            )
        }

        companion object {
            /**
             * Gets the bolt special by the id of the bolt we're using
             *
             * @param boltId
             * The id of the bolt
             */
            fun getBoltSpecial(boltId: Int): Optional<BoltSpecial> {
                for (special in values()) {
                    if (special.boltId == boltId) {
                        return Optional.of(special)
                    }
                }
                return Optional.empty()
            }
        }
    }
}