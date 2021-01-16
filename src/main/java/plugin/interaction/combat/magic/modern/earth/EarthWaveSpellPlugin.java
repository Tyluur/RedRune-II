package plugin.interaction.combat.magic.modern.earth;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/7/2017
 */
public class EarthWaveSpellPlugin implements CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2716);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 14222;
	}
	
	@Override
	public int hitGfx() {
		return 2726;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 190;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2721, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int spellId() {
		return 77;
	}
	
	@Override
	public double exp() {
		return 42.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public int castSoundId() {
		return 134;
	}
	
	@Override
	public int impactSoundId() {
		return castSoundId() + 1;
	}
}
