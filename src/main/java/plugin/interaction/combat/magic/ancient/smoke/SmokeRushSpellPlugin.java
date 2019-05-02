package plugin.interaction.combat.magic.ancient.smoke;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.functions.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class SmokeRushSpellPlugin implements CombatSpellPlugin {
	
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
		return 385;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 150;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 386, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, null).consume(spellContext -> {
			if (spellContext.getHit().getDamage() != 0 && RandomFunction.percentageChance(10)) {
				Actor spellTarget = spellContext.getTarget();
				if (!spellTarget.getPoisonManager().isPoisoned()) {
					spellTarget.getPoisonManager().makePoisoned(20);
				}
			}
		});
	}
	
	@Override
	public int spellId() {
		return 28;
	}
	
	@Override
	public double exp() {
		return 30;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public int castSoundId() {
		return 176;
	}
	
	@Override
	public int impactSoundId() {
		return 177;
	}
}
