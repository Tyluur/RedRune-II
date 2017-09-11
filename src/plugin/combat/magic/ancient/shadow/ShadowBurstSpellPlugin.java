package plugin.combat.magic.ancient.shadow;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.constants.MagicConstants.MagicBook;
import com.rs.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class ShadowBurstSpellPlugin extends CombatSpellPlugin {
	
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
		return 382;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 200;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellDetail -> {
			if (spellDetail.getHit().getDamage() != 0) {
				if (spellDetail.getTarget().isPlayer()) {
					spellDetail.getTarget().toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.05, 0.10);
				}
			}
		});
	}
	
	@Override
	public int spellId() {
		return 34;
	}
	
	@Override
	public double exp() {
		return 37;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
