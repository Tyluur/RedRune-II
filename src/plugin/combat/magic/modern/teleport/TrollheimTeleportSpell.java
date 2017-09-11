package plugin.combat.magic.modern.teleport;

import com.rs.game.entity.WorldTile;
import com.rs.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class TrollheimTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 61;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(FIRE_RUNE, 2, LAW_RUNE, 2);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2888, 3674, 0);
	}
	
	@Override
	public int spellId() {
		return 69;
	}
	
	@Override
	public double exp() {
		return 68;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
