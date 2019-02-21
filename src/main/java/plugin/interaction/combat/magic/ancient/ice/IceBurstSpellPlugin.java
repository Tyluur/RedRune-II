package plugin.interaction.combat.magic.ancient.ice;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class IceBurstSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1979;
	}
	
	@Override
	public int hitGfx() {
		return 363;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 220;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 366, 43, 21, 52, 15, 0));
		style.sendMultiSpell(source, target, this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			target.freeze(source, TimeUnit.SECONDS.toMillis(10), "You have been frozen!");
		}, null);
	}
	
	@Override
	public int gfxHeight() {
		return 0;
	}
	
	@Override
	public int spellId() {
		return 22;
	}
	
	@Override
	public double exp() {
		return 46;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
