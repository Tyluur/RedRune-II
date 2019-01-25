package plugin.combat.magic.modern.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class MobilisingArmiesTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 10;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 1, WATER_RUNE, 1, AIR_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2413, 2848, 0);
	}
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 19;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
