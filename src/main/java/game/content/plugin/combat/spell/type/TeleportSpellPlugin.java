package game.content.plugin.combat.spell.type;

import game.content.plugin.PluginRepository;
import game.content.plugin.combat.spell.SpellPlugin;
import game.entity.actor.Actor;
import game.entity.actor.player.Player;
import game.global.WorldTile;
import utility.constants.MagicConstants;

import static game.content.entity.actor.combat.function.Magic.sendTeleportSpell;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/8/2017
 */
public interface TeleportSpellPlugin extends SpellPlugin {
	
	@Override
	default void register() {
		PluginRepository.register(this, book(), spellId());
	}
	
	@Override
	default void cast(Player player, Actor target) {
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
	WorldTile destination();
	
	/**
	 * The level required to use the spell
	 */
	int levelRequired();
	
	/**
	 * If the teleport spell should randomize the coordinates to arrive at
	 */
	default boolean randomize() {
		return true;
	}
	
	/**
	 * The runes that are required
	 */
	int[] runesRequired();
	
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
