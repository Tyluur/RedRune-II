package com.rs.game.plugin.combat.spell.type;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.plugin.combat.spell.SpellPlugin;
import com.rs.utility.constants.MagicConstants;

import static com.rs.game.content.Magic.sendTeleportSpell;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/8/2017
 */
public abstract class TeleportSpellPlugin extends SpellPlugin {
	
	@Override
	public void register() {
		PluginRepository.register(this, book(), spellId());
	}
	
	@Override
	public void cast(Player player, Actor target) {
		switch (book()) {
			case REGULAR:
				sendModernTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
			case ANCIENTS:
				sendAncientsTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
			case LUNAR:
				sendLunarTeleport(player, destination(), levelRequired(), exp(), randomize(), runesRequired());
				break;
		}
	}
	
	/**
	 * Sends a modern teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendModernTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, destination, 3, randomize, MagicConstants.MAGIC_TELEPORT, runes);
	}
	
	/**
	 * The destination of the teleportation
	 */
	public abstract WorldTile destination();
	
	/**
	 * The level required to use the spell
	 */
	public abstract int levelRequired();
	
	/**
	 * If the teleport spell should randomize the coordinates to arrive at
	 */
	public boolean randomize() {
		return true;
	}
	
	/**
	 * The runes that are required
	 */
	public abstract int[] runesRequired();
	
	/**
	 * Sends an ancients teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendAncientsTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, destination, 5, randomize, MagicConstants.MAGIC_TELEPORT, runes);
	}
	
	/**
	 * Sends a lunar teleport spell
	 *
	 * @param player
	 * 		The player
	 * @param destination
	 * 		The destination
	 */
	static void sendLunarTeleport(Player player, WorldTile destination, int level, double xp, boolean randomize, int... runes) {
		sendTeleportSpell(player, 9606, -1, 1685, -1, level, xp, destination, 6, randomize, MagicConstants.MAGIC_TELEPORT, runes);
	}
}
