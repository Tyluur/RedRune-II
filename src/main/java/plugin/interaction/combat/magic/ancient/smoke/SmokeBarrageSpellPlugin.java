package plugin.interaction.combat.magic.ancient.smoke;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.functions.RandomFunction;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
public class SmokeBarrageSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1979;
	}
	
	@Override
	public int hitGfx() {
		return 391;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 270;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellContext -> {
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
		return 31;
	}
	
	@Override
	public double exp() {
		return 48;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public int castSoundId() {
		return 183;
	}
	
	@Override
	public int impactSoundId() {
		return 185;
	}
}
