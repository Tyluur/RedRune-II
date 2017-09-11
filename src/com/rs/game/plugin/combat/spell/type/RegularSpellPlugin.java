package com.rs.game.plugin.combat.spell.type;

import com.rs.game.plugin.PluginRepository;
import com.rs.game.plugin.combat.spell.SpellPlugin;

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
