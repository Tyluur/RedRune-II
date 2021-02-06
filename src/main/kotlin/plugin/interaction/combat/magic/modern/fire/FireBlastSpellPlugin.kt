package plugin.interaction.combat.magic.modern.fire;

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
public class FireBlastSpellPlugin implements CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(2728);
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 14223;
	}
	
	@Override
	public int hitGfx() {
		return 2739;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 160;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 2733, 30, 26, 52, 0, 0));
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int spellId() {
		return 63;
	}
	
	@Override
	public double exp() {
		return 34.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public int castSoundId() {
		return 155;
	}
	
	@Override
	public int impactSoundId() {
		return castSoundId() + 1;
	}
}
