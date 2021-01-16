package plugin.interaction.combat.magic.ancient.shadow;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/1/2017
 */
public class ShadowBarrageSpellPlugin implements CombatSpellPlugin {
	
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
	
	@Override
	public int castSoundId() {
		return 181;
	}
	
	@Override
	public int impactSoundId() {
		return 185;
	}
}
