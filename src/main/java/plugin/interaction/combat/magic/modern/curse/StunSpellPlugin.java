package plugin.interaction.combat.magic.modern.curse;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
public class StunSpellPlugin implements CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(173);
	
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
		return 107;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 10;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 174, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this, () -> {
			if (target.isPlayer()) {
				target.toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.10, 0.10);
			}
		}, null);
	}
	
	@Override
	public int spellId() {
		return 82;
	}
	
	@Override
	public double exp() {
		return 90;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public int castSoundId() {
		return 121;
	}
	
	@Override
	public int impactSoundId() {
		return castSoundId() - 1;
	}
}
