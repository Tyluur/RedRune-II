package plugin.combat.magic.ancient.smoke;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.tools.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class SmokeBarrageSpellPlugin extends CombatSpellPlugin {
	
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
	
}
