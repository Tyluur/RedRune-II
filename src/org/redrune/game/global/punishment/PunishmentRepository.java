package org.redrune.game.global.punishment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.file.JsonFileManager;
import org.redrune.utility.file.SerializableFilesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class PunishmentRepository {
	
	/**
	 * The punishments that exist in the world
	 */
	private static final List<Punishment> PUNISHMENTS = new ArrayList<>();
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location of all item characteristics
	 */
	private static final String PUNISHMENTS_FILE_LOCATION = "./data/saves/punishments.json";
	
	/**
	 * The queue of punishments awaiting addition
	 */
	private static final Queue<Punishment> AWAITING_ADDITION = new ConcurrentLinkedQueue<>();
	
	/**
	 * Loads all punishments
	 */
	public static void loadAll() {
		List<Punishment> punishments = getPunishmentsFromFile();
		if (punishments == null) {
			punishments = new ArrayList<>();
		}
		PUNISHMENTS.addAll(punishments);
		System.out.println("Loaded " + PUNISHMENTS.size() + " punishments from file " + new File(PUNISHMENTS_FILE_LOCATION).getPath());
	}
	
	/**
	 * Adds a punishment to the queue awaiting addition
	 */
	public static boolean addToQueue(Punishment punishment) {
		return AWAITING_ADDITION.add(punishment);
	}
	
	/**
	 * Gets the queue of punishments awaiting addition
	 */
	public static Queue<Punishment> getQueue() {
		return AWAITING_ADDITION;
	}
	
	/**
	 * Adds a punishment to the collection of punishments {@link #PUNISHMENTS}
	 *
	 * @param save
	 * 		If we should save to the file
	 */
	public static void add(Punishment punishment, boolean save) {
		boolean added = PUNISHMENTS.add(punishment);
		if (save) {
			JsonFileManager.save(PUNISHMENTS, PUNISHMENTS_FILE_LOCATION);
		}
		punishment.notify(true, added);
		if (added && punishment.getAdditionEvent() != null) {
			punishment.getAdditionEvent().run();
		}
	}
	
	/**
	 * Deletes a punishment and updates the file if requested
	 */
	public static boolean delete(Punishment punishment, boolean save) {
		boolean removed = PUNISHMENTS.removeIf(p -> p.equals(punishment));
		if (save) {
			JsonFileManager.save(PUNISHMENTS, PUNISHMENTS_FILE_LOCATION);
		}
		punishment.notify(false, removed);
		return removed;
	}
	
	/**
	 * Gets the characteristic instance from a file
	 */
	private static List<Punishment> getPunishmentsFromFile() {
		File file = new File(PUNISHMENTS_FILE_LOCATION);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(PUNISHMENTS_FILE_LOCATION);
		return GSON.fromJson(text, new TypeToken<List<Punishment>>() {
		}.getType());
	}
	
	/**
	 * Checks if the player has any of the following punishments
	 */
	public static boolean isPunished(Player player, PunishmentType... types) {
		List<PunishmentType> typeList = new ArrayList<>(Arrays.asList(types));
		List<Punishment> applicable = PUNISHMENTS.stream().filter(punishment -> typeList.contains(punishment.getType())).collect(Collectors.toList());
		for (Punishment punishment : applicable) {
			switch (punishment.getType()) {
				case PLAYER_MUTE:
					if (punishment.getPunished().equals(player.getUsername())) {
						return true;
					}
					break;
				case PLAYER_BAN:
					if (punishment.getPunished().equals(player.getUsername())) {
						return true;
					}
					break;
				case ADDRESS_MUTE:
					if (punishment.getIp().orElse("n/a").equals(player.getSession().getIp()) || punishment.getMac().orElse("n/a").equals(player.getSession().getMacAddress())) {
						return true;
					}
					break;
				case ADDRESS_BAN:
					if (punishment.getIp().orElse("n/a").equals(player.getSession().getIp()) || punishment.getMac().orElse("n/a").equals(player.getSession().getMacAddress())) {
						return true;
					}
					break;
			}
		}
		return false;
	}
	
	/**
	 * Gets all the punishments
	 */
	public static List<Punishment> getPunishments() {
		return PUNISHMENTS;
	}
	
	/**
	 * Finds all punishments that match the name and type
	 */
	public static List<Punishment> findPunishments(String name, Player target, PunishmentType type) {
		List<Punishment> punishments = new ArrayList<>();
		for (Punishment punishment : PUNISHMENTS) {
			if (punishment.getType() != type) {
				continue;
			}
			switch (punishment.getType()) {
				case PLAYER_MUTE:
				case PLAYER_BAN:
					if (punishment.getPunished().equals(name)) {
						punishments.add(punishment);
					}
					break;
				case ADDRESS_MUTE:
					if (target != null && (target.getSession().getIp().equals(punishment.getIp().orElse("n/a")) || target.getSession().getMacAddress().equals(punishment.getMac().orElse("n/a")))) {
						punishments.add(punishment);
					}
					break;
				case ADDRESS_BAN:
					target = SerializableFilesManager.loadPlayer(name);
					if (target != null && (target.getLastIP().equals(punishment.getIp().orElse("n/a")) || target.getLastMac().equals(punishment.getMac().orElse("n/a")))) {
						punishments.add(punishment);
					}
					break;
			}
		}
		return punishments;
	}
}
