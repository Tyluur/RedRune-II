package org.redrune.game.plugin.combat.spell.type;

import org.redrune.game.plugin.PluginRepository;
import org.redrune.game.plugin.combat.spell.SpellPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public abstract class RegularSpellPlugin extends SpellPlugin {
	
	@Override
	public void register() {
		PluginRepository.register(this, book(), spellId());
	}
}
