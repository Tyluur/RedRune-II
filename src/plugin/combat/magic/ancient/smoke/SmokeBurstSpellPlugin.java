package plugin.combat.magic.ancient.smoke;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.constants.MagicConstants.MagicBook;
import com.rs.utility.tools.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class SmokeBurstSpellPlugin extends CombatSpellPlugin {
	
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
		return 389;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 190;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellContext -> {
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
		return 30;
	}
	
	@Override
	public double exp() {
		return 60;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
