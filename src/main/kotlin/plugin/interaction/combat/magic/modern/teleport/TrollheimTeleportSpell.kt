package plugin.interaction.combat.magic.modern.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
public class TrollheimTeleportSpell implements TeleportSpellPlugin {
	
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
