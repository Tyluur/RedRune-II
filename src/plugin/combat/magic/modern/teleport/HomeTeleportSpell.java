package plugin.combat.magic.modern.teleport;

import org.redrune.utility.constants.GameConstants;
import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class HomeTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 0;
	}
	
	@Override
	public int[] runesRequired() {
		return new int[0];
	}
	
	@Override
	public WorldTile destination() {
		return GameConstants.START_PLAYER_LOCATION;
	}
	
	@Override
	public int spellId() {
		return 24;
	}
	
	@Override
	public double exp() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
}
