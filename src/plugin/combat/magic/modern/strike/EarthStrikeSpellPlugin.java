package plugin.combat.magic.modern.strike;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
public class EarthStrikeSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics NEXT_GRAPHICS = new Graphics(2713);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 14221;
	}
	
	@Override
	public int hitGfx() {
		return 2723;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 60;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(NEXT_GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2718, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int spellId() {
		return 30;
	}
	
	@Override
	public double exp() {
		return 9.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
