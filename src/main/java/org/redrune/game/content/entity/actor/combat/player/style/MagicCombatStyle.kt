package org.redrune.game.content.entity.actor.combat.player.style

import org.redrune.engine.SystemManager
import org.redrune.engine.tick.schedule.ScheduledTask
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm
import org.redrune.game.content.entity.actor.combat.CombatRoll
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle
import org.redrune.game.content.entity.actor.combat.player.calc.MagicCombatCalculator
import org.redrune.game.content.plugin.PluginRepository.getSpellPlugin
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.Actor
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.mask.Hit
import org.redrune.game.entity.actor.mask.HitSplat
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.projectile.ProjectileManager
import org.redrune.utility.constants.BonusConstants
import org.redrune.utility.constants.MagicConstants.MagicBook
import org.redrune.utility.constants.SkillConstants.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
class MagicCombatStyle
/**
 * Constructs a new combat style enumeration instance
 */
    : AbstractCombatStyle(MagicCombatCalculator()) {
    override fun fireSwing(source: Player, target: Actor): Boolean {
        val spellId = source.combatDefinitions.realSpellId
        val manualCast = !source.combatDefinitions.isAutocasting
        // we're not auto-casting so we should reset the spell
        if (manualCast) {
            source.combatDefinitions.resetSpells(false)
            source.actionManager.forceStop()
        }
        // we don't have the runes anymore
        if (!CombatAlgorithm.checkCombatSpell(source, spellId, -1, true)) {
            if (manualCast) {
                source.combatDefinitions.resetSpells(true)
            }
            return false
        }
        // finds the spell to cast
        val spellOptional = getSpellPlugin(source.combatDefinitions.magicBook, spellId)
        // spell hasn't been registered yet
        if (!spellOptional.isPresent) {
            source.packets.sendMessage("Spell #$spellId has not yet been added, please report this on the forums.")
            return false
        }
        // spell instance
        val spellPlugin = spellOptional.get()
        if (spellPlugin !is CombatSpellPlugin) {
            source.packets.sendMessage("Spell #$spellId has not yet been added, please report this on the forums.")
            return false
        }
        when (source.combatDefinitions.magicBook) {
            MagicBook.REGULAR, MagicBook.ANCIENTS, MagicBook.LUNAR -> when (spellId) {
                else -> spellPlugin.cast(source, target, this)
            }
        }
        return true
    }

    override fun addExperience(source: Player, target: Actor, hit: Hit, attackStyle: Int, weaponId: Int) {
        throw IllegalStateException("Unable to add experience in magic style without spell parameters")
    }

    override fun getRandomDamage(source: Actor, target: Actor, multiplier: Double): Int {
        return 0
    }

    override fun sendHit(source: Player, target: Actor, maxHit: Int, damage: Int, delay: Int): CombatSwingDetail? {
        return null
    }
    /**
     * Sends the spell to the target
     *
     * @param player
     * The player
     * @param target
     * The target
     * @param plugin
     * The magic spell event
     * @param spellCastTask
     * The task that is executed when the spell is cast
     * @param hitLandTask
     * The task that is executed when the hit lands.
     * @return The amount of damage that landed
     */
    /**
     * Sends the spell to the target
     *
     * @param player
     * The player
     * @param target
     * The target
     * @param plugin
     * The magic spell event
     * @return The damage that landed
     */
    @JvmOverloads
    fun sendSpell(
        player: Player,
        target: Actor,
        plugin: CombatSpellPlugin,
        spellCastTask: Runnable? = null,
        hitLandTask: Runnable? = null
    ): CombatSwingDetail {
        val damage: Int
        var maxHit = plugin.maxHit(player, target)
        val minimum = plugin.minimumHit(player)
        var boost = (1
                + (player.skills.getLevel(MAGIC) - player.skills.getLevelForXp(MAGIC)) * 0.03)
        if (boost > 1) maxHit *= boost.toInt()
        val magicPerc = player.combatDefinitions.bonuses[BonusConstants.MAGIC_DAMAGE_BONUS]
            .toDouble()
        boost = magicPerc / 100 + 1
        maxHit *= boost.toInt()
        damage = if (minimum != -1) {
            CombatRoll.randomizeHit(
                minimum.toDouble(),
                maxHit.toDouble(),
                calculator.getAttackBonus(player),
                calculator.getDefenceBonus(target, 0, 0),
                false
            )
        } else {
            CombatRoll.randomizeHit(
                maxHit.toDouble(),
                calculator.getAttackBonus(player),
                calculator.getDefenceBonus(target, 0, 0)
            )
        }
        // the projectile delay speed
        val projectileDelay = ProjectileManager.getProjectileDelay(player, target)
        // the extra delay calculation
        val delayCalc = ProjectileManager.getDelay(player, target, projectileDelay, 0)
        // the final delay
        val delay = (projectileDelay + delayCalc).toInt()
        val hit = Hit(player, damage, HitSplat.MAGIC_DAMAGE).setMaxHit(maxHit)
        addExperience(player, target, hit, plugin)

        // uses the spells animation
        if (plugin.animationId() != -1) {
            player.nextAnimation = Animation(plugin.animationId())
        }
        if (plugin.castSoundId() != -1) {
            playSingleSound(player, plugin.castSoundId())
        }

        // sends the task that is executed when the spell is used successfully
        if (damage > 0 && spellCastTask != null) {
            spellCastTask.run()
        }
        handleEffects(player, target, hit)
        SystemManager.SCHEDULER.schedule(object : ScheduledTask(1, delay) {
            override fun run() {
                // so we don't have to make a new task for blocking.
                if (delay == 2 && ticksPassed == 0 || ticksPassed == goalTicks - 1) {
                    target.setNextAnimationNoPriority(Animation(CombatAlgorithm.getDefenceEmote(target)))
                } else if (ticksPassed == goalTicks) {
                    target.applyHit(hit)
                    if (damage == 0) {
                        target.setNextGraphics(Graphics(85, 0, 96))
                    } else {
                        if (plugin.hitGfx() != -1) {
                            target.setNextGraphics(Graphics(plugin.hitGfx(), 0, plugin.gfxHeight()))
                        }
                        if (plugin.impactSoundId() != -1) {
                            playAreaSound(player, plugin.impactSoundId())
                        }
                        if (damage > 0 && hitLandTask != null) {
                            hitLandTask.run()
                        }
                    }
                    stop()
                }
            }
        })
        return CombatSwingDetail(player, target, hit)
    }

    /**
     * Adds experience for a spell
     */
    fun addExperience(source: Player, target: Actor, hit: Hit, combatSpellPlugin: CombatSpellPlugin) {
        val magicExp = combatSpellPlugin.exp()
        val damage = hit.damage
        var combatXp = magicExp * 1 + damage / 5
        if (combatXp <= 0) {
            return
        }
        if (source.combatDefinitions.isDefensiveCasting) {
            val defenceXp = (damage / 3).toDouble()
            if (defenceXp > 0) {
                combatXp -= defenceXp
                if (target.isPlayer) {
                    source.skills.addXpNoModifier(DEFENCE, defenceXp)
                } else {
                    source.skills.addXp(DEFENCE, defenceXp)
                }
            }
        }
        if (target.isPlayer) {
            source.skills.addXpNoModifier(MAGIC, combatXp)
        } else {
            source.skills.addXp(MAGIC, combatXp)
        }
        val hpExp = damage / 7.5
        if (hpExp > 0) {
            if (target.isPlayer) {
                source.skills.addXpNoModifier(HITPOINTS, hpExp)
            } else {
                source.skills.addXp(HITPOINTS, hpExp)
            }
        }
    }

    /**
     * Sends a multi spell
     *
     * @param player
     * The player
     * @param target
     * The target
     * @param plugin
     * The event
     * @param spellCastTask
     * The task for when the spell is cast
     * @param hitLandTask
     * The task for when the hit lands
     */
    fun sendMultiSpell(
        player: Player,
        target: Actor?,
        plugin: CombatSpellPlugin,
        spellCastTask: Runnable?,
        hitLandTask: Runnable?
    ): List<CombatSwingDetail> {
        // the list consisting of all the entities to attacak, and the first index being the
        // entity we cast the spell on
        val entityList = CombatAlgorithm.getMultiAttackTargets(player, target)
        // the list of all contexts
        val detailList: MutableList<CombatSwingDetail> = ArrayList()
        if (entityList.size == 0) {
            return detailList
        }
        // if the first hit was a splash we don't want to keep trying
        for (i in entityList.indices) {
            val actor = entityList[i]
            val context = sendSpell(player, actor, plugin, spellCastTask, hitLandTask)
            detailList.add(context)
            if (i == 0 && context.hit.damage == 0) {
                return detailList
            }
        }
        return detailList
    }
}