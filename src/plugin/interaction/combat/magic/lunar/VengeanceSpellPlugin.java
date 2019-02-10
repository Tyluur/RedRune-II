package plugin.interaction.combat.magic.lunar;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.RegularSpellPlugin;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.constants.SkillConstants;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public class VengeanceSpellPlugin implements RegularSpellPlugin {
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 112;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
	
	@Override
	public void cast(Player player, Actor target) {
		Long lastTimeCast = player.getTemporaryAttribute("last_veng_time", -1L);
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < 94) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return;
		} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
			return;
		} else if (!CombatAlgorithm.checkRunes(player, true, MagicConstants.ASTRAL_RUNE, 4, MagicConstants.DEATH_RUNE, 2, MagicConstants.EARTH_RUNE, 10)) {
			return;
		}
		player.setNextGraphics(new Graphics(726, 0, 100));
		player.setNextAnimation(new Animation(4410));
		player.putAttribute("cast_veng", true);
		player.putAttribute("last_veng_time", System.currentTimeMillis());
	}
}
