package plugin.interaction.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/28/2017
 */
public class CatherbyTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 87;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 10);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2804, 3433, 0);
	}
	
	@Override
	public int spellId() {
		return 44;
	}
	
	@Override
	public double exp() {
		return 93;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
