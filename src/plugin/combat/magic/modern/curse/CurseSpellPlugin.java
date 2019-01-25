package plugin.combat.magic.modern.curse;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class CurseSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(108);
	
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
		return 110;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 10;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 109, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this, () -> {
			if (target.isPlayer()) {
				target.toPlayer().getSkills().drainLevel(SkillConstants.DEFENCE, 0.05, 0.05);
			}
		}, null);
	}
	
	@Override
	public int spellId() {
		return 35;
	}
	
	@Override
	public double exp() {
		return 29;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
