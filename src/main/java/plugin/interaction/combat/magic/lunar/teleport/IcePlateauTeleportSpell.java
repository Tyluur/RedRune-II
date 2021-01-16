package plugin.interaction.combat.magic.lunar.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/28/2017
 */
public class IcePlateauTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 89;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(ASTRAL_RUNE, 3, LAW_RUNE, 3, WATER_RUNE, 8);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(2972, 3873, 0);
	}
	
	@Override
	public int spellId() {
		return 51;
	}
	
	@Override
	public double exp() {
		return 96;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
}
