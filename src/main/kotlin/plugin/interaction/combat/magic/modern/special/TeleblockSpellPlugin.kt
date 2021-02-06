package plugin.interaction.combat.magic.modern.special;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.projectile.ProjectileManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
public class TeleblockSpellPlugin implements CombatSpellPlugin {

    private static final Graphics GRAPHICS = new Graphics(1841);

    @Override
    public int delay(Player player) {
        return 4;
    }

    @Override
    public int animationId() {
        return 10503;
    }

    @Override
    public int hitGfx() {
        return 1843;
    }

    @Override
    public int maxHit(Player player, Actor target) {
        return 30;
    }

    @Override
    public void cast(Player source, Actor target, MagicCombatStyle style) {
        if (target instanceof NPC) {
            source.getPackets().sendMessage("You cannot cast teleport block on monsters.");
            return;
        }
        Player p2 = (Player) target;
        final boolean hasImmunity = target.hasTeleblockImmunity();
        final boolean isTeleblocked = target.isTeleblocked();
        if (hasImmunity || isTeleblocked) {
            source.getPackets().sendMessage("This player is already affected by teleport block.");
            return;
        }
        ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 1842, 18, 9, 52, 15, 0));
        style.sendSpell(source, target, this, () -> {
            target.setNextGraphics(GRAPHICS);
            //TODO: get protect from magic prayers
            int teleblockTime = (p2.getPrayer().usingPrayer(0, 0) ? 150 : 300);
            target.teleblock(source, TimeUnit.SECONDS.toMillis(teleblockTime));
        }, null);
    }

    @Override
    public int spellId() {
        return 86;
    }

    @Override
    public double exp() {
        return 80.0;
    }

    @Override
    public MagicBook book() {
        return MagicBook.REGULAR;
    }

}
