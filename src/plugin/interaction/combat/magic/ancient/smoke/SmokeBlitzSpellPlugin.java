package plugin.interaction.combat.magic.ancient.smoke;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.functions.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class SmokeBlitzSpellPlugin implements CombatSpellPlugin {
	
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
		return 387;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 230;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 386, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, null).consume(spellContext -> {
			if (spellContext.getHit().getDamage() != 0 && RandomFunction.percentageChance(10)) {
				Actor spellTarget = spellContext.getTarget();
				if (!spellTarget.getPoisonManager().isPoisoned()) {
					spellTarget.getPoisonManager().makePoisoned(40);
				}
			}
		});
	}
	
	@Override
	public int spellId() {
		return 29;
	}
	
	@Override
	public double exp() {
		return 42;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
