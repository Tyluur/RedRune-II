package plugin.combat.magic.ancient.teleport;

import com.rs.game.GameConstants;
import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

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
		return 48;
	}
	
	@Override
	public double exp() {
		return 0;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
