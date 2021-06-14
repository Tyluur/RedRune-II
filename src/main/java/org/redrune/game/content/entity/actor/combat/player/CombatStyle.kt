package org.redrune.game.content.entity.actor.combat.player

import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle
import org.redrune.game.content.entity.actor.combat.player.style.MeleeCombatStyle
import org.redrune.game.content.entity.actor.combat.player.style.RangeCombatStyle
import org.redrune.game.content.plugin.PluginRepository.getSpellPlugin
import org.redrune.game.content.plugin.combat.spell.SpellPlugin
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin
import org.redrune.game.entity.actor.player.Player
import java.util.*

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/4/2017
 */
enum class CombatStyle(
    /**
     * The style
     */
    val style: AbstractCombatStyle
) {
    MELEE(MeleeCombatStyle()) {
        override fun getDelay(player: Player): Int {
            val weaponId = player.equipment.weaponId
            val weaponName =
                if (weaponId == -1) "unarmed" else ItemDefinitions.getItemDefinitions(weaponId).name.lowercase(Locale.getDefault())
            // interval 0.6
            if (weaponId == 9703) {
                return 1
            }
            // Interval 1.8
            if (weaponName.contains("saradomin sword") || weaponName.contains(" whip") || weaponName == "zamorakian spear" || weaponName == "korasi's sword") {
                return 3
            }
            // Interval 3.0
            if (weaponName.contains("spear") || weaponName.contains(" sword") || weaponName.contains("longsword") || weaponName.contains(
                    "light"
                ) || weaponName.contains("hatchet") || weaponName.contains("pickaxe ") || weaponName.contains("mace") || weaponName.contains(
                    "hasta"
                ) || weaponName.contains("warspear") || weaponName.contains("flail") || weaponName.contains("hammers")
            ) {
                return 4
            }
            // Interval 3.6
            if (weaponName.contains("godsword") || weaponName.contains("warhammer") || weaponName.contains("battleaxe") || weaponName.contains(
                    "maul"
                ) || weaponName == "dominion sword"
            ) {
                return 5
            }
            // Interval 4.2
            return if (weaponName.contains("greataxe") || weaponName.contains("halberd") || weaponName.contains("2h sword") || weaponName.contains(
                    "two handed sword"
                ) || weaponName.contains("katana") || weaponName == "thok's sword"
            ) {
                6
            } else when (weaponId) {
                6527 -> 4
                10887 -> 5
                15403, 20084, 6528 -> 6
                else -> 3
            }
        }
    },
    RANGE(RangeCombatStyle()) {
        override fun getDelay(player: Player): Int {
            val weaponId = player.equipment.weaponId
            val attackStyle = player.combatDefinitions.attackStyle
            var delay: Int
            val weaponName =
                if (weaponId == -1) "unarmed" else ItemDefinitions.getItemDefinitions(weaponId).name.lowercase(Locale.getDefault())
            delay =
                if (weaponName.contains("shortbow") || weaponName.contains("karil's crossbow") || weaponName.contains("sling")) {
                    3
                } else if (weaponName.contains("crossbow")) {
                    5
                } else if (weaponName.contains("dart") || weaponName.contains("knife")) {
                    2
                } else if (weaponName.contains("chinchompa") || weaponName.contains("crystal bow")) {
                    4
                } else if (weaponName.contains("toktz-xil-ul")) {
                    3
                } else {
                    when (weaponId) {
                        15241 -> 7
                        11235, 15701, 15702, 15703, 15704 -> 9
                        20171 -> 4
                        else -> 6
                    }
                }
            if (attackStyle == 1) {
                delay--
            } else if (attackStyle == 2) {
                delay++
            }
            return delay
        }
    },
    MAGIC(MagicCombatStyle()) {
        override fun getDelay(player: Player): Int {
            val optional = getSpellPlugin(player.combatDefinitions.magicBook, player.combatDefinitions.realSpellId)
            return optional.map { spellPlugin: SpellPlugin? ->
                if (spellPlugin is CombatSpellPlugin) {
                    return@map spellPlugin.delay(player)
                } else {
                    return@map -1
                }
            }.orElse(-1)
        }
    };

    /**
     * Gets the delay for the player to swing this style
     */
    abstract fun getDelay(player: Player): Int
}