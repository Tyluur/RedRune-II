package game.content.plugin.combat.spell.type;

import game.content.plugin.PluginRepository;
import game.content.plugin.combat.spell.SpellPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
public interface RegularSpellPlugin extends SpellPlugin {
	
	@Override
	default void register() {
		PluginRepository.register(this, book(), spellId());
	}
}
