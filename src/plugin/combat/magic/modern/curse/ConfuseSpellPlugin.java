package plugin.combat.magic.modern.curse;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class ConfuseSpellPlugin extends CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 716;
	}
	
	@Override
	public int hitGfx() {
		return 104;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 10;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(new Graphics(102));
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 103, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this, () -> {
			if (target.isPlayer()) {
				target.toPlayer().getSkills().drainLevel(ATTACK, 0.05, 0.05);
			}
		}, null);
	}
	
	@Override
	public int spellId() {
		return 26;
	}
	
	@Override
	public double exp() {
		return 13;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
