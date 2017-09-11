package plugin.combat.magic.modern.god;

import com.rs.cores.thread.WorldThread;
import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.utility.constants.AttributeKey;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class ChargeSpellPlugin extends CombatSpellPlugin {
	
	private static final Animation ANIMATION = new Animation(811);
	
	private static final Graphics GRAPHICS = new Graphics(6);
	
	@Override
	public int delay(Player player) {
		return 0;
	}
	
	@Override
	public int animationId() {
		return 0;
	}
	
	@Override
	public int hitGfx() {
		return 0;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 0;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.putAttribute(AttributeKey.GOD_CHARGED, WorldThread.getTicksPassed() + 600);
	}
	
	@Override
	public int spellId() {
		return 83;
	}
	
	@Override
	public double exp() {
		return 180;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
