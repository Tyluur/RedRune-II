package plugin.combat.magic.ancient.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class CarrallangarTeleportSpell extends TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 84;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, SOUL_RUNE, 2);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3217, 3677, 0);
	}
	
	@Override
	public int spellId() {
		return 45;
	}
	
	@Override
	public double exp() {
		return 94;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
