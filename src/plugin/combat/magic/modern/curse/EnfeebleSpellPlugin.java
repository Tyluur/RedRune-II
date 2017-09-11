package plugin.combat.magic.modern.curse;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.utility.constants.MagicConstants.MagicBook;
import com.rs.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class EnfeebleSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(170);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 729;
	}
	
	@Override
	public int hitGfx() {
		return 172;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 10;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 171, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this, () -> {
			if (target.isPlayer()) {
				target.toPlayer().getSkills().drainLevel(SkillConstants.STRENGTH, 0.10, 0.10);
			}
		}, null);
	}
	
	@Override
	public int spellId() {
		return 78;
	}
	
	@Override
	public double exp() {
		return 83;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
