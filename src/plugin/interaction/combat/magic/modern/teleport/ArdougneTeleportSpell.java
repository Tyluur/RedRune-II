package plugin.interaction.combat.magic.modern.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class ArdougneTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 51;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(WATER_RUNE, 2, LAW_RUNE, 2);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2664, 3305, 0);
	}
	
	@Override
	public int spellId() {
		return 57;
	}
	
	@Override
	public double exp() {
		return 61;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
