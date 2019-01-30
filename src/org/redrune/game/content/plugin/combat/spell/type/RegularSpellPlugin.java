package org.redrune.game.content.plugin.combat.spell.type;

import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public interface RegularSpellPlugin extends SpellPlugin {
	
	@Override
	default void register() {
		PluginRepository.register(this, book(), spellId());
	}
}
