package plugin.combat.magic.ancient.blood;

import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class BloodBarrageSpellPlugin extends CombatSpellPlugin {
	
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
		return 377;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 290;
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
		return 27;
	}
	
	@Override
	public double exp() {
		return 51;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
