package plugin.interaction.combat.magic.ancient.teleport;

import org.redrune.game.global.WorldTile;
import org.redrune.game.content.plugin.combat.spell.type.TeleportSpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class AnnakarlTeleportSpell implements TeleportSpellPlugin {
	
	@Override
	public int levelRequired() {
		return 60;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, BLOOD_RUNE, 2);
	}
	
	@Override
	public WorldTile destination() {
		return new WorldTile(3288, 3886, 0);
	}
	
	@Override
	public int spellId() {
		return 46;
	}
	
	@Override
	public double exp() {
		return 100;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
