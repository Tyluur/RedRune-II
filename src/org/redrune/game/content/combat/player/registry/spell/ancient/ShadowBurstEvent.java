package org.redrune.game.content.combat.player.registry.spell.ancient;

import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.tool.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class ShadowBurstEvent implements CombatSpellEvent {
	
	@Override
	public int delay() {
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
	public int maxHit() {
		return 200;
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
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		context.getSwing().sendMultiSpell(player, context.getTarget(), this, null, null).forEach(spellDetail -> {
			if (spellDetail.getHit().getDamage() != 0) {
				if (spellDetail.getTarget().isPlayer()) {
					Player target = spellDetail.getTarget().toPlayer();
					int attackLevelLeast = (int) (target.getSkills().getLevelForXp(SkillConstants.DEFENCE) * 0.85);
					int currentAttackLevel = target.getSkills().getLevel(SkillConstants.DEFENCE);
					int remainder = currentAttackLevel - attackLevelLeast;
					if (remainder <= 1) {
						remainder = 1;
					}
					if (currentAttackLevel > attackLevelLeast) {
						target.getSkills().drainLevel(SkillConstants.DEFENCE, RandomFunction.random(0, remainder));
					}
				}
			}
		});
	}
	
	
}
