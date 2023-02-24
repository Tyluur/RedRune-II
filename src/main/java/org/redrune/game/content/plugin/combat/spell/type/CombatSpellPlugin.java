package org.redrune.game.content.plugin.combat.spell.type;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
public interface CombatSpellPlugin extends SpellPlugin {

    /**
     * The delay on the spell, only used to find the next combat swing time
     */
    int delay(Player player);

    /**
     * The animation id for the spell
     */
    int animationId();

    /**
     * The id of the graphics applied when the hit lands
     */
    int hitGfx();

    /**
     * The base damage of the spell
     *
     * @param player The player casting
     * @param target The target of the spell
     */
    int maxHit(Player player, Actor target);

    /**
     * Handles the casting of the spell
     */
    void cast(Player source, Actor target, MagicCombatStyle style);

    @Override
    default void cast(Player player, Actor target) {
        throw new RuntimeException("Unable to cast a spell without the style");
    }

    @Override
    default void register() {
        PluginRepository.register(this, book(), spellId());
    }

    /**
     * The minimum damage the spell will do. If the spell splashes it must do atleast this damage.
     *
     * @param player The player
     */
    default int minimumHit(Player player) {
        return -1;
    }

    /**
     * The default height of the hit land gfx
     */
    default int gfxHeight() {
        return 96;
    }

    /**
     * The id of the sound played when the spell is cast
     */
    default int castSoundId() {
        return -1;
    }

    /**
     * The id of the sound played when the spell impact lands
     */
    default int impactSoundId() {
        return -1;
    }
}
