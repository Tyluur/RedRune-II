package plugin.combat.magic.ancient.blood;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class BloodBurstSpellPlugin extends CombatSpellPlugin {
	
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
		return 376;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 210;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellContext -> {
			if (spellContext.getHit().getDamage() != 0) {
				source.getPackets().sendGameMessage("You drain some of your opponents' life points.");
				source.heal(spellContext.getHit().getDamage() / 4);
			}
		});
	}
	@Override
	public int spellId() {
		return 26;
	}
	
	@Override
	public double exp() {
		return 39;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
