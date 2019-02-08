package plugin.interaction.combat.magic.ancient.shadow;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class ShadowBurstSpellPlugin implements CombatSpellPlugin {
	
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
