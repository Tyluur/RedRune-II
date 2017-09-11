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
public class ShadowBarrageSpellPlugin extends CombatSpellPlugin {
	
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
		return 383;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 280;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellDetail -> {
			if (spellDetail.getTarget().isPlayer()) {
				spellDetail.getTarget().toPlayer().getSkills().drainLevel(SkillConstants.ATTACK, 0.05, 0.15);
			}
		});
	}
	
	@Override
	public int spellId() {
		return 35;
	}
	
	@Override
	public double exp() {
		return 49;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
