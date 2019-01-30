package plugin.combat.magic.ancient.shadow;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class ShadowBlitzSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1978;
	}
	
	@Override
	public int hitGfx() {
		return 381;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 240;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 380, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, null).consume(spellDetail -> {
			if (spellDetail.getHit().getDamage() != 0 && spellDetail.getTarget().isPlayer()) {
				spellDetail.getTarget().toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.05, 0.15);
			}
		});
	}
	
	@Override
	public int spellId() {
		return 33;
	}
	
	@Override
	public double exp() {
		return 45;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
