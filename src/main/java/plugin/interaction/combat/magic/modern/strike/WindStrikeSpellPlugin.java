package plugin.interaction.combat.magic.modern.strike;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public class WindStrikeSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int spellId() {
		return 25;
	}
	
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
		return 2700;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 20;
	}
	
	@Override
	public double exp() {
		return 5.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2699, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this);
	}
}
