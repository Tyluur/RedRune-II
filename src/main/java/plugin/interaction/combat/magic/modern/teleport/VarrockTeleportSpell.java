package plugin.interaction.combat.magic.modern.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/27/2017
 */
public class VarrockTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 25;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(FIRE_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3212, 3424, 0);
	}
	
	@Override
	public int spellId() {
		return 40;
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
