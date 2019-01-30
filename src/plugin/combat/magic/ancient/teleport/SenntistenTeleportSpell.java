package plugin.combat.magic.ancient.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class SenntistenTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 60;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, SOUL_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3360, 3387, 0);
	}
	
	@Override
	public int spellId() {
		return 41;
	}
	
	@Override
	public double exp() {
		return 70;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
