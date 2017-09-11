package plugin.combat.magic.modern.curse;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.utility.constants.MagicConstants.MagicBook;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class EntangleSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(179);
	
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
		return 50;
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
			target.freeze(source, TimeUnit.SECONDS.toMillis(15), "You have been frozen!");
		}, null);
	}
	
	@Override
	public int spellId() {
		return 81;
	}
	
	@Override
	public double exp() {
		return 91.1;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
