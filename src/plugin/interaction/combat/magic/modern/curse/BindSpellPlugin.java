package plugin.interaction.combat.magic.modern.curse;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class BindSpellPlugin implements CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(181);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 710;
	}
	
	@Override
	public int hitGfx() {
		return -1;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 20;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 178, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// we send the graphics here because we don't always freeze them
			target.setNextGraphics(GRAPHICS);
			// only freeze the player if they are unfreezeable when the spell is cast.
			target.freeze(source, TimeUnit.SECONDS.toMillis(5), "You have been frozen!");
		}, null);
	}
	
	@Override
	public int spellId() {
		return 36;
	}
	
	@Override
	public double exp() {
		return 60.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
