package org.redrune.game.content.action.combat.player;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.action.combat.player.registry.BowFireEvent;
import org.redrune.game.content.action.combat.player.registry.CombatRegistryEvent;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.utility.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The registry for all combat data that is stored. This is for special attacks and magic spells.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class CombatRegistry {
	
	/**
	 * The map of special attacks, the key being the weapon name.
	 */
	private static final Map<String, SpecialAttackEvent> SPECIALS = new HashMap<>();
	
	/**
	 * The map of bows, the key being the weapon name.
	 */
	private static final Map<String, BowFireEvent> BOWS = new HashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(CombatRegistry.class);
	
	/**
	 * Registers all special attacks and magic spells
	 */
	public static void registerAll() {
		Misc.getClassesInDirectory(CombatRegistryEvent.class.getPackage().getName() + ".special").stream().filter(SpecialAttackEvent.class::isInstance).forEach(clazz -> {
			SpecialAttackEvent special = (SpecialAttackEvent) clazz;
			for (String name : special.applicableNames()) {
				if (SPECIALS.containsKey(name)) {
					LOGGER.info("Attempted duplicate registration of special for '" + name + "'");
					continue;
				}
				SPECIALS.put(name, special);
			}
		});
		Misc.getClassesInDirectory(CombatRegistryEvent.class.getPackage().getName() + ".range").stream().filter(BowFireEvent.class::isInstance).forEach(clazz -> {
			BowFireEvent bow = (BowFireEvent) clazz;
			for (String name : bow.bowNames()) {
				if (BOWS.containsKey(name)) {
					LOGGER.info("Attempted duplicate registration of bow for '" + name + "'");
					continue;
				}
				BOWS.put(name, bow);
			}
		});
		LOGGER.info("Prepared " + SPECIALS.size() + " special attack weapons, and " + BOWS.size() + " bow listeners.");
	}
	
	/**
	 * Gets an optional of a special attack event by the id of the weapon used. The id is then translated into a name
	 * and we loop through the map to find the right one.
	 *
	 * @param weaponId
	 * 		The id of the weapon used.
	 */
	public static Optional<SpecialAttackEvent> getSpecial(int weaponId) {
		String name = weaponId == -1 ? "unarmed" : ItemDefinitionParser.forId(weaponId).getName();
		for (Entry<String, SpecialAttackEvent> entry : SPECIALS.entrySet()) {
			String specialName = entry.getKey();
			if (name.toLowerCase().contains(specialName.toLowerCase())) {
				return Optional.of(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets an optional of a bow fire event by the id of the weapon used. The id is then translated into a name
	 * and we loop through the map to find the right one.
	 *
	 * @param weaponId
	 * 		The id of the weapon used.
	 */
	public static Optional<BowFireEvent> getBow(int weaponId) {
		String name = weaponId == -1 ? "unarmed" : ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		for (Entry<String, BowFireEvent> entry : BOWS.entrySet()) {
			String specialName = entry.getKey();
			String regex = specialName.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			
			if (matcher.find()) {
				return Optional.of(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
}
