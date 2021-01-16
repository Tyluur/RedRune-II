package org.redrune.game.global.punishment;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.utility.functions.Misc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.redrune.utility.constants.ColorConstants.BLUE;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/17/2017
 */
public class PunishmentHandler {
	
	/**
	 * Adds a punishment
	 *
	 * @param player
	 * 		The player punishing
	 * @param name
	 * 		The punished users name
	 * @param type
	 * 		The type of punishment
	 * @param hours
	 * 		The duration of the punishment
	 */
	public static void addPunishment(Player player, String name, int hours, PunishmentType type) {
		long time = hours == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
		String message = String.format("[<col=%s>Attempting to %s %s for%s</col>.]", BLUE, type.name().toLowerCase(), name, hours == 0 ? "ever" : " " + Misc.format(hours) + " hours");
		player.getPackets().sendMessage(message);
		addPunishment(new Punishment(player.getUsername(), name, type, time));
	}
	
	/**
	 * Requests the removal of a punishment
	 *
	 * @param player
	 * 		The player removing the punishment
	 * @param name
	 * 		The punished users name
	 * @param type
	 * 		The type of punishment
	 */
	public static void removePunishment(Player player, String name, PunishmentType type) {
		// the message for a request
		String message = String.format("[<col=%s>Attempting to remove punishment '%s' from %s</col>.]", BLUE, type.name().toLowerCase(), name);
		player.getPackets().sendMessage(message);
		Player target = World.getPlayer(name);
		
		List<Punishment> punishments = PunishmentRepository.findPunishments(name, target, type);
		System.out.println(punishments);
		System.out.println(target);
		punishments.forEach(PunishmentHandler::deletePunishment);
	}
	
	/**
	 * Gets the message for the punishment
	 *
	 * @param punishment
	 * 		The punishment
	 * @param add
	 * 		If the punishment was added or removed
	 * @param success
	 * 		if the punishment was successful or failed
	 */
	public static String getMessage(Punishment punishment, boolean add, boolean success) {
		return "[" + (success ? "Successfully" : "Failed to") + " " + (add ? "add" + (!success ? "" : "ed") + "" : "remove" + (!success ? "" : "d")) + " punishment '" + punishment.getType().name() + "' " + (add ? "to" : "from") + " '" + punishment.getPunished() + "'.]";
	}
	
	/**
	 * Handles the addition of a punishment
	 *
	 * @param punishment
	 * 		The punishment to ad
	 * @return {@code True} If we could add a punishment, meaning there was a player in the world by that name.
	 */
	public static boolean addPunishment(Punishment punishment) {
		// as long as the punishment as added to the list successfully [ban/mute]
		Player player = World.getPlayer(punishment.getPunished());
		return player != null && punishment.getType().add(player, punishment);
	}
	
	/**
	 * Handles the deletion of a punishment
	 *
	 * @param punishment
	 * 		The punishment to delete
	 */
	public static boolean deletePunishment(Punishment punishment) {
		Player player = World.getPlayer(punishment.getPunished());
		switch (punishment.getType()) {
			case PLAYER_MUTE:
				return punishment.getType().remove(player, punishment);
			case ADDRESS_MUTE:
				return punishment.getType().remove(player, punishment);
			case PLAYER_BAN:
			case ADDRESS_BAN:
				return punishment.getType().remove(null, punishment);
		}
		return false;
	}
	
}
