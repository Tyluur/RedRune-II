package org.redrune.game.content.combat.player.registry;

import lombok.Getter;
import org.redrune.game.content.combat.player.swing.MagicCombatSwing;
import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class MagicSpellContext {
	
	/**
	 * The target of the spell
	 */
	@Getter
	private final Entity target;
	
	/**
	 * The combat swing of the spell
	 */
	@Getter
	private final MagicCombatSwing swing;
	
	public MagicSpellContext(Entity target, MagicCombatSwing swing) {
		this.target = target;
		this.swing = swing;
	}
}
