package org.redrune.utility.game.repository.npc.characteristic;

import com.github.michaelbull.rs.RuneScapeAPI;
import com.github.michaelbull.rs.bestiary.Beast;
import com.github.michaelbull.rs.bestiary.Bestiary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.redrune.cache.Cache;
import org.redrune.cache.loaders.AnimationDefinitions;
import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.entity.actor.npc.Drop;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.functions.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class NPCCharacteristicRepository {

    /**
     * The map of cached npc characteristics, the key is the npc name and the characteristic instance contains all of
     * the characteristics
     */
    private static final Map<String, NPCCharacteristic> CACHED_CHARACTERISTICS = new HashMap<>();

    /**
     * The default npc definition
     */
    private final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(1, -1, -1, -1, 5, 1, 33, 0, BonusConstants.SLASH_ATTACK, -1, -1, 0);

    /**
     * The gson instance
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The location of all npc characteristics
     */
    private static final String CHARACTERISTICS_LOCATION = "./data/repository/npc/characteristics/";

    public static void main(String[] args) throws IOException {
        Cache.initialize();
        convertAloticDefinitions();
    }

    private static void dumpBestiaryDefinitions() {
        RuneScapeAPI api = RuneScapeAPI.createHttp();
        Bestiary bestiary = api.bestiary();
        Map<Integer, String> exceptions = new HashMap<>();
        for (int npcId = 0; npcId < Misc.getNPCDefinitionsSize(); npcId++) {
            NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(npcId);
            if (definitions == null) {
                System.err.println("Unable to get npc definitions for npc " + npcId);
                exceptions.put(npcId, "definitions unable to parse");
                continue;
            }
            String name = definitions.getName();
            try {
                Optional<Beast> optional = bestiary.beastData(npcId);
                if (!optional.isPresent()) {
                    System.err.println("Unable to find beast data for npc " + npcId + " [" + name + "]");
                    exceptions.put(npcId, "beast data nonexistent");
                    continue;
                }
                Beast beast = optional.get();
                NPCCharacteristic characteristic = getCharacteristicsNonNull(npcId);
                if (characteristic == null) {
                    System.err.println("Unable to generate characteristics for npc " + npcId + " [" + name + "]");
                    exceptions.put(npcId, "characteristics unable to generate");
                    continue;
                }
                characteristic.addExamine(npcId, beast.getDescription());

                NPCCombatDefinitions combatDefinitions = characteristic.getCombatDefinitionsNonNull(npcId);

                OptionalInt attackAnimation = beast.getAnimation("attack");
                OptionalInt deathAnimation = beast.getAnimation("death");
                attackAnimation.ifPresent(combatDefinitions::setAttackAnim);
                deathAnimation.ifPresent(combatDefinitions::setDeathAnim);
                combatDefinitions.setHitpoints(beast.getLifePoints() / 10);
                combatDefinitions.setAttackLevel(beast.getAttackLevel());
                combatDefinitions.setMagicLevel(beast.getMagicLevel());
                combatDefinitions.setRangeLevel(beast.getRangedLevel());
                combatDefinitions.setDefenceLevel(beast.getDefenceLevel());
                combatDefinitions.setAggressivenessType(beast.isAggressive() ? 1 : 0);
                combatDefinitions.setRequiredSlayerLevel(beast.getRequiredSlayerLevel());
                beast.getSlayerCategory().ifPresent(combatDefinitions::setSlayerCategory);
                combatDefinitions.getAreas().addAll(beast.getAreas());
                combatDefinitions.setExperienceReceived(beast.getExperience());
                combatDefinitions.setPoisonous(beast.isPoisonous());

                characteristic.addCombatDefinitions(npcId, combatDefinitions);

                saveCharacteristics(npcId, characteristic);
                System.out.println("Saved characteristics for npc #" + npcId + " [" + name + "]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exceptions.forEach((key, value) -> System.err.println(key + ", " + value));

        System.out.println("Finished!");
    }

    private static void convertAriosDefinitions() throws SQLException {

        HikariConfig config = new HikariConfig();
        HikariDataSource ds;

        {
            config.setJdbcUrl("jdbc:mysql://localhost/arios");
            config.setUsername("debug");
            config.setPassword("debug");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver"); //alternative is Class.forName("com.mysql.cj.jdbc.Driver")
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);
        }
        String query = "SELECT * from `npc_configs`";

        try (Connection con = ds.getConnection(); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String examine = rs.getString("examine");
                String name = rs.getString("name");
                int lifePoints = rs.getInt("lifepoints") * 10;
                int attackLevel = rs.getInt("attack_level");
                int strengthLevel = rs.getInt("strength_level");
                int defenceLevel = rs.getInt("defence_level");
                int rangeLevel = rs.getInt("range_level");
                int magicLevel = rs.getInt("magic_level");
                String bonusesArray = rs.getString("bonuses");
                int[] bonuses = new int[18];
                if (bonusesArray != null) {
                    String[] split = bonusesArray.trim().split(",");
                    List<Integer> bonusesList = new ArrayList<>();
                    for (String s : split) {
                        Integer digit = Integer.parseInt(s);
                        bonusesList.add(digit);
                    }
                    bonusesList.add(11, 0);
                    bonusesList.add(12, 0);
                    bonusesList.add(13, 0);
                    Integer[] bonusesI = bonusesList.toArray(new Integer[bonusesList.size()]);
                    for (int i = 0; i < bonusesI.length; i++) {
                        bonuses[i] = bonusesI[i];
                    }
                }
                int respawnDelay = rs.getInt("respawn_delay");
                int attackSpeed = rs.getInt("attack_speed");
                int meleeAnimation = rs.getInt("melee_animation");
                int defenceAnimation = rs.getInt("defence_animation");
                int deathAnimation = rs.getInt("death_animation");
                int spawnAnimation = rs.getInt("spawn_animation");
                int magicAnimation = rs.getInt("magic_animation");
                int rangeAnimation = rs.getInt("range_animation");
                int attackGfx = rs.getInt("start_gfx");
                int attackProjectile = rs.getInt("projectile");
                int endGfx = rs.getInt("end_gfx");
                int combatStyle = rs.getInt("combat_style");
                int aggressive = rs.getInt("aggressive");

                int attackAnimation = meleeAnimation;
                if (attackAnimation == -1) {
                    attackAnimation = rangeAnimation;
                }
                if (rangeAnimation == -1) {
                    attackAnimation = magicAnimation;
                }
                AriosNPCCharacteristics arios = new AriosNPCCharacteristics(id, examine, name, lifePoints, attackLevel, strengthLevel, defenceLevel, rangeLevel, magicLevel, bonuses, respawnDelay, attackSpeed, meleeAnimation, defenceAnimation, deathAnimation, spawnAnimation, magicAnimation, rangeAnimation, attackGfx, attackProjectile, endGfx, combatStyle, aggressive);

                int maxHit = -1;

                int deathDelay = AnimationDefinitions.getAnimationDefinitions(deathAnimation).getEmoteGameTickets();
                NPCCharacteristic characteristics = getCharacteristicsNonNull(id);
                NPCCombatDefinitions existent = characteristics.getCombatDefinitions(id);
                //				NPCCombatDefinitions generated = new NPCCombatDefinitions(lifePoints, attackLevel, strengthLevel, defenceLevel, rangeLevel, magicLevel, bonuses, attackAnimation, defenceAnimation, deathAnimation, attackSpeed, deathDelay, respawnDelay, maxHit, combatStyle, attackGfx, attackProjectile, aggressive);

                existent.setHitpoints(arios.getLifePoints() * 10);
                if (arios.getAttackLevel() != 1 && arios.getAttackLevel() != 0) {
                    existent.setAttackLevel(arios.getAttackLevel());
                }
                if (arios.getStrengthLevel() != 1 && arios.getStrengthLevel() != 0) {
                    existent.setStrengthLevel(arios.getStrengthLevel());
                }
                if (arios.getDefenceLevel() != 1 && arios.getDefenceLevel() != 0) {
                    existent.setDefenceLevel(arios.getDefenceLevel());
                }
                if (arios.getRangeLevel() != 1 && arios.getRangeLevel() != 0) {
                    existent.setRangeLevel(arios.getRangeLevel());
                }
                if (arios.getMagicLevel() != 1 && arios.getMagicLevel() != 0) {
                    existent.setMagicLevel(arios.getMagicLevel());
                }
                existent.setBonuses(arios.getBonuses());
                existent.setAttackGfx(arios.getAttackGfx());
                //existent.set
/*				if (existent != null) {
					if (existent.getHitpoints() > out.getHitpoints()) {
						out.setHitpoints(existent.getHitpoints());
					}
					if (existent.getAttackAnim() != 0 && existent.getAttackAnim() != 65535) {
						out.setAttackAnim(existent.getAttackAnim());
					}
					if (existent.getDefenceAnim() != 0 && existent.getDefenceAnim() != 65535) {
						out.setDefenceAnim(existent.getDefenceAnim());
					}
					if (existent.getDeathAnim() != 0 && existent.getDeathAnim() != 65535) {
						out.setDeathAnim(existent.getDeathAnim());
					}
					if (existent.getAttackGfx() != 0 && existent.getAttackGfx() != 65535) {
						out.setAttackGfx(existent.getAttackGfx());
					}
					if (existent.getAttackProjectile() != 0 && existent.getAttackProjectile() != 65535) {
						out.setAttackProjectile(existent.getAttackProjectile());
					}
				}*/
/*				{
					if (characteristics.getBonuses(id) != null) {
						out.setBonuses(characteristics.getBonuses(id));
					}
				}
				System.out.println("Generated new npc combat definitions! " + out);
				characteristics.addCombatDefinitions(id, out);
				characteristics.addExamine(id, examine);
				saveCharacteristics(id, characteristics);*/
            }
        }
        System.out.println("Finished");
    }

    private static void convertAloticDefinitions() {
        try {
            final String[] LOAD_IDENTIFIERS = {"id", "maxHp", "maxDamage", "maxDistance", "aggressiveLevel", "style", "type", "attackTicks", "deathTicks", "respawnTicks", "attackAnimation", "defendAnimation", "deathAnimation", "meleeAttDef", "magicAttDef", "rangedAttDef", "weakness", "legion", "slayerRequest", "charms", "clueType"};
            BufferedReader br = new BufferedReader(new FileReader("D:\\Tyler\\Google Drive\\Me\\3. RSPS\\1. Servers\\#600-699\\Alotic\\world_server\\data\\npcs\\combat.txt"));
            String line, identifier;
            byte identifierIndex = 0;
            short lastSuccessfulLoad = -1;
            boolean loadFail = false;
            /**
             * Definition values
             */
            short[] ids = null;
            int maxHp = 0, maxDamage = 0;
            byte maxDistance = 1;
            int attackTicks = 5, deathTicks = 5, respawnTicks = 50;
            short aggressiveLevel = 0;
            Style style = null;
            Object type = null;
            short attackAnimation = -1, defendAnimation = -1, deathAnimation = -1;
            short effectiveAttack = 0, effectiveDefence = 0, effectiveMagic = 0, effectiveRanged = 0;
            Object legion = null;
            byte slayerRequest = 0;
            Object[] customDrops = null;
            Object[] weakness = null;
            byte weaknessModifier = 100; // NOTE: THIS IS REDUCED *TO*, NOT REDUCED *BY*
            /**
             * Parsing
             */
            while ((line = br.readLine()) != null) {
                if (line.equals("") || line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                identifier = LOAD_IDENTIFIERS[identifierIndex++];
                if (!line.startsWith(identifier)) {
                    if (identifier.equals("weaknessModifier")) {
                        weaknessModifier = 75;
                        identifier = LOAD_IDENTIFIERS[identifierIndex++];
                    } else { // maybe add elseifs here for default values? cant add new identifiers at the end of the array or it will cause problems.
                        // not elegant but i don't see a better solution besides re-editing the whole combat.txt file
                        loadFail = true;
                    }
                }
                if (line.startsWith(identifier)) {
                    try {
                        switch (identifier) {
                            case "id":
                                String lineSplit = line.split("id=")[1];
                                if (lineSplit.contains(",")) {
                                    String[] idSplit = lineSplit.split(",");
                                    ids = new short[idSplit.length];
                                    for (byte i = 0; i < idSplit.length; i++) {
                                        ids[i] = Short.parseShort(idSplit[i]);
                                    }
                                } else {
                                    ids = new short[]{Short.parseShort(lineSplit)};
                                }
                                System.out.println("starting to convert " + Arrays.toString(ids) + " (" + NPCDefinitions.getNPCDefinitions(ids[0]).getName() + ")");
                                break;
                            case "maxHp":
                                maxHp = Integer.parseInt(line.split("maxHp=")[1]);
                                break;
                            case "maxDamage":
                                maxDamage = Integer.parseInt(line.split("maxDamage=")[1]);
                                break;
                            case "maxDistance":
                                maxDistance = Byte.parseByte(line.split("maxDistance=")[1]);
                                break;
                            case "aggressiveLevel":
                                aggressiveLevel = Short.parseShort(line.split("aggressiveLevel=")[1]);
                                break;
                            case "attackTicks":
                                attackTicks = Integer.parseInt(line.split("attackTicks=")[1]);
                                break;
                            case "deathTicks":
                                deathTicks = Integer.parseInt(line.split("deathTicks=")[1]);
                                break;
                            case "respawnTicks":
                                respawnTicks = Integer.parseInt(line.split("respawnTicks=")[1]);
                                break;
                            case "attackAnimation":
                                attackAnimation = Short.parseShort(line.split("attackAnimation=")[1]);
                                break;
                            case "defendAnimation":
                                defendAnimation = Short.parseShort(line.split("defendAnimation=")[1]);
                                break;
                            case "deathAnimation":
                                deathAnimation = Short.parseShort(line.split("deathAnimation=")[1]);
                                break;
                            case "effectiveAttack":
                                effectiveAttack = Short.parseShort(line.split("effectiveAttack=")[1]);
                                break;
                            case "effectiveDefence":
                                effectiveDefence = Short.parseShort(line.split("effectiveDefence=")[1]);
                                break;
                            case "effectiveMagic":
                                effectiveMagic = Short.parseShort(line.split("effectiveMagic=")[1]);
                                break;
                            case "effectiveRanged":
                                effectiveRanged = Short.parseShort(line.split("effectiveRanged=")[1]);
                                break;
                            case "weaknessModifier":
                                weaknessModifier = Byte.parseByte(line.split("weaknessModifier=")[1]);
                                break;
                            case "style": {
                                String splitLine = line.split("style=")[1];
                                if (splitLine.equals("null")) {
                                    style = null;
                                } else {
                                    style = Style.valueOf(splitLine);
                                }
                                break;
                            }
                        }
                        if (identifierIndex == LOAD_IDENTIFIERS.length) {
                            for (short id : ids) {

                                NPCCharacteristic characteristic = getCharacteristicsNonNull(id);
                                if (characteristic == null) {
                                    System.out.println("Unable to get characteristics for " + id + ", skipping (" + NPCDefinitions.getNPCDefinitions(id).getName() + ")...");
                                    continue;
                                }
                                NPCCombatDefinitions definitions = characteristic.getCombatDefinitions(id);
                                if (definitions == null) {
                                    System.out.println("Unable to find stored definitions for " + id + ", generating new...");
                                    definitions = new NPCCombatDefinitions();
                                }
                                definitions.setHitpoints(maxHp);
                                definitions.setAttackAnim(attackAnimation);
                                definitions.setDefenceAnim(defendAnimation);
                                definitions.setDeathAnim(deathAnimation);
                                definitions.setAttackDelay(attackTicks);
                                definitions.setRespawnDelay(respawnTicks);
                                definitions.setDeathDelay(deathTicks);
                                if (style != null) {
                                    definitions.setAttackStyle(style.getAttackStyle());
                                }
                                saveCharacteristics(id, characteristic);
                                System.out.println("Saved animations for npc " + id + " [" + NPCDefinitions.getNPCDefinitions(id).getName() + "]");

                                identifierIndex = 0;
                                lastSuccessfulLoad = id;
                            }
                            ids = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        loadFail = true;
                    }
                }
                if (loadFail) {
                    if (ids == null) {
                        System.err.println("Error loading npc combat definitions!");
                        if (lastSuccessfulLoad != -1) {
                            System.err.println("Last successful combat definition loaded: " + lastSuccessfulLoad);
                        }
                    } else {
                        System.err.println("Error loading combat definition for npc " + ids[0] + "!");
                    }
                    System.err.println("Next expected definition identifier: " + identifier + "=value, got: " + line.split("=")[0]);
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by yak.
     */
    public enum Style {

        STAB,
        SLASH,
        CRUSH,
        RANGED,
        DECENT_OF_DRAGONS,
        DECENT_OF_DARKNESS,
        DOWN_TO_EARTH,
        CLEAR_MIND,
        LIFE_LEECH,
        KORASI_SPEC,
        MAGIC,
        DRAGON_FIRE,
        SARADOMIN_LIGHTNING,
        OTHER;

        public boolean isMelee() {
            return this == STAB || this == SLASH || this == CRUSH;
        }

        public boolean isRanged() {
            return this == RANGED || this == DECENT_OF_DRAGONS || this == DECENT_OF_DARKNESS || this == DOWN_TO_EARTH || this == CLEAR_MIND || this == LIFE_LEECH;
        }

        public boolean isMagic() {
            return this == MAGIC;
        }

        public int getAttackStyle() {
            switch (this) {
                case STAB:
                    return BonusConstants.STAB_ATTACK;
                case SLASH:
                    return BonusConstants.SLASH_ATTACK;
                case CRUSH:
                    return BonusConstants.CRUSH_ATTACK;
                case MAGIC:
                    return BonusConstants.MAGIC_ATTACK;
                case RANGED:
                    return BonusConstants.RANGE_ATTACK;
                default:
                    throw new IllegalStateException("Unable to parse attack style");
            }
        }

    }

    /**
     * Gets the {@code NPCCharacteristic} instance from the file
     *
     * @param name The name of the npc we will use to find the file
     */
    private static NPCCharacteristic getCharacteristicsFromFile(String name) {
        String fileLocation = getFileLocation(name);
        File file = new File(fileLocation);
        if (!file.exists()) {
            return null;
        }
        String text = Misc.getText(fileLocation);
        return GSON.fromJson(text, NPCCharacteristic.class);
    }

    /**
     * @param name The name of the npc
     */
    private static String getFileLocation(String name) {
        return CHARACTERISTICS_LOCATION + name + ".json";
    }

    public static void convertNPCCombatDefinitions() {
		/*HashMap<Integer, NPCCombatDefinitions> defs = NPCCombatDefinitionsL.getNpcCombatDefinitions();
		System.out.println("Defs=" + defs.size());
		for (Entry<Integer, NPCCombatDefinitions> entry : defs.entrySet()) {
			int npcId = entry.getKey();
			NPCCombatDefinitions definitions = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addCombatDefinitions(npcId, definitions);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
    }

    private static int getNPCIdFromText(String text) {
        String[] split = getSQLSplittedText(text);
        return Integer.parseInt(split[0].replace('(', ' ').trim());
    }

    private static String[] getSQLSplittedText(String text) {
        String[] split = text.split(", ");
        String[] output = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String data = split[0];
            String out = data.toLowerCase().contains("null") ? "-1" : data;
            output[i] = out;
        }
        return output;
    }

    public static void convertNPCDrops() {
		/*HashMap<Integer, Drop[]> drops = NPCDrops.getDropMap();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, Drop[]> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			Drop[] dropArray = entry.getValue();
			List<Drop> dropList = Arrays.asList(dropArray);
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addDrops(npcId, dropList);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
    }

    public static void convertNPCBonuses() {
		/*HashMap<Integer, int[]> drops = NPCBonuses.getNpcBonuses();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, int[]> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			int[] bonuses = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addBonuses(npcId, bonuses);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
    }

    public static void convertNPCExamines() {
		/*Map<Integer, String> drops = NPCExamines.getEXAMINES();
		System.out.println("drops=" + drops.size());
		for (Entry<Integer, String> entry : drops.entrySet()) {
			int npcId = entry.getKey();
			String examine = entry.getValue();
			NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
			if (npcDefinitions == null) {
				System.out.println("No definitions for #" + npcId + "");
				continue;
			}
			String name = npcDefinitions.getName();
			NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
			if (characteristic == null) {
				characteristic = new NPCCharacteristic();
			}
			characteristic.addExamine(npcId, examine);
			Misc.saveToJsonFile(getFileLocation(name), characteristic);
			System.out.println("Saved [" + name + "][" + characteristic + "]");
		}*/
    }

    /**
     * Saves the characteristics of an npc
     *
     * @param npcId          The id of the npc
     * @param characteristic The characteristics of the npc
     */
    private static void saveCharacteristics(int npcId, NPCCharacteristic characteristic) {
        NPCDefinitions npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId);
        if (npcDefinitions == null) {
            System.out.println("No definitions for #" + npcId + "");
            return;
        }
        String name = npcDefinitions.getName();
        Misc.saveToJsonFile(getFileLocation(name), characteristic);
    }

    /**
     * Gets the drops of an npc by its id
     */
    public static List<Drop> getDrops(int npcId) {
        NPCCharacteristic characteristic = getCharacteristics(npcId);
        if (characteristic == null) {
            return null;
        }
        return characteristic.getDrops(npcId);
    }

    /**
     * Gets the characteristics of an npc by its id
     *
     * @param npcId The id of the npc
     */
    private static NPCCharacteristic getCharacteristics(int npcId) {
        NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(npcId);
        if (definitions == null) {
            System.out.println("Unable to find definitions for npc " + npcId);
            return null;
        }
        String name = definitions.getName();
        NPCCharacteristic cached = CACHED_CHARACTERISTICS.get(name);
        if (!CACHED_CHARACTERISTICS.containsKey(name)) {
            cached = getCharacteristicsFromFile(name);
            if (cached == null) {
                CACHED_CHARACTERISTICS.put(name, null);
                return null;
            }
            CACHED_CHARACTERISTICS.put(name, cached);
            return cached;
        } else {
            return cached;
        }
    }

    /**
     * Returns an instance of the npc's characteristics unless the npc does not exist in the cache
     */
    private static NPCCharacteristic getCharacteristicsNonNull(int npcId) {
        NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(npcId);
        if (definitions == null) {
            System.out.println("Unable to find definitions for npc " + npcId);
            return null;
        }
        String name = definitions.getName();
        NPCCharacteristic characteristic = getCharacteristicsFromFile(name);
        return characteristic == null ? new NPCCharacteristic() : characteristic;
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    public static NPCCombatDefinitions getCombatDefinitions(int npcId, boolean nullable) {
        NPCCharacteristic characteristic = getCharacteristics(npcId);
        if (nullable && characteristic == null) {
            return null;
        }
        if (characteristic == null) {
            return DEFAULT_DEFINITION;
        }
        NPCCombatDefinitions combatDefinitions = characteristic.getCombatDefinitions(npcId);
        if (combatDefinitions == null) {
            return DEFAULT_DEFINITION;
        }
        return combatDefinitions;
    }

    /**
     * Gets the bonuses of an npc by its id
     */
    public static int[] getBonuses(int npcId) {
        NPCCharacteristic characteristic = getCharacteristics(npcId);
        if (characteristic == null) {
            return null;
        }
        return characteristic.getBonuses(npcId);
    }

    /**
     * Gets the examine of an npc by its id
     */
    public static String getExamine(int npcId) {
        NPCCharacteristic characteristic = getCharacteristics(npcId);
        if (characteristic == null) {
            return "It's an npc.";
        }
        String examine = characteristic.getExamine(npcId);
        if (examine == null) {
            return "It's an npc.";
        }
        return examine;
    }

}
