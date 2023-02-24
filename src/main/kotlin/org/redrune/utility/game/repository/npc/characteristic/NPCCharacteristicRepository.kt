package org.redrune.utility.game.repository.npc.characteristic

import com.github.michaelbull.rs.RuneScapeAPI
import com.google.gson.GsonBuilder
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.redrune.cache.Cache
import org.redrune.cache.loaders.AnimationDefinitions
import org.redrune.cache.loaders.NPCDefinitions
import org.redrune.game.entity.actor.npc.Drop
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions
import org.redrune.utility.constants.BonusConstants
import org.redrune.utility.functions.Misc
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.sql.SQLException
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
object NPCCharacteristicRepository {

    /**
     * The map of cached npc characteristics, the key is the npc name and the characteristic instance contains all of
     * the characteristics
     */
    private val CACHED_CHARACTERISTICS: MutableMap<String, NPCCharacteristic?> = HashMap()

    /**
     * The default npc definition
     */
    private val DEFAULT_DEFINITION =
        NPCCombatDefinitions(1, -1, -1, -1, 5, 1, 33, 0, BonusConstants.SLASH_ATTACK, -1, -1, 0)

    /**
     * The gson instance
     */
    private val GSON = GsonBuilder().setPrettyPrinting().create()

    /**
     * The location of all npc characteristics
     */
    private const val CHARACTERISTICS_LOCATION = "./data/repository/npc/characteristics/"
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Cache.initialize()
        convertAloticDefinitions()
    }

    private fun dumpBestiaryDefinitions() {
        val api = RuneScapeAPI.createHttp()
        val bestiary = api.bestiary()
        val exceptions: MutableMap<Int, String> = HashMap()
        for (npcId in 0 until Misc.getNPCDefinitionsSize()) {
            val definitions = NPCDefinitions.getNPCDefinitions(npcId)
            if (definitions == null) {
                System.err.println("Unable to get npc definitions for npc $npcId")
                exceptions[npcId] = "definitions unable to parse"
                continue
            }
            val name = definitions.name
            try {
                val optional = bestiary.beastData(npcId)
                if (!optional.isPresent) {
                    System.err.println("Unable to find beast data for npc $npcId [$name]")
                    exceptions[npcId] = "beast data nonexistent"
                    continue
                }
                val beast = optional.get()
                val characteristic = getCharacteristicsNonNull(npcId)
                if (characteristic == null) {
                    System.err.println("Unable to generate characteristics for npc $npcId [$name]")
                    exceptions[npcId] = "characteristics unable to generate"
                    continue
                }
                characteristic.addExamine(npcId, beast.description)
                val combatDefinitions = characteristic.getCombatDefinitionsNonNull(npcId)
                val attackAnimation = beast.getAnimation("attack")
                val deathAnimation = beast.getAnimation("death")
                attackAnimation.ifPresent { attackAnim: Int -> combatDefinitions.attackAnim = attackAnim }
                deathAnimation.ifPresent { deathAnim: Int -> combatDefinitions.deathAnim = deathAnim }
                combatDefinitions.hitpoints = beast.lifePoints / 10
                combatDefinitions.attackLevel = beast.attackLevel
                combatDefinitions.magicLevel = beast.magicLevel
                combatDefinitions.rangeLevel = beast.rangedLevel
                combatDefinitions.defenceLevel = beast.defenceLevel
                combatDefinitions.aggressivenessType = if (beast.isAggressive) 1 else 0
                combatDefinitions.requiredSlayerLevel = beast.requiredSlayerLevel
                beast.slayerCategory.ifPresent { slayerCategory: String? ->
                    combatDefinitions.slayerCategory = slayerCategory
                }
                combatDefinitions.areas.addAll(beast.areas)
                combatDefinitions.experienceReceived = beast.experience
                combatDefinitions.isPoisonous = beast.isPoisonous
                characteristic.addCombatDefinitions(npcId, combatDefinitions)
                saveCharacteristics(npcId, characteristic)
                println("Saved characteristics for npc #$npcId [$name]")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        exceptions.forEach { (key: Int, value: String) -> System.err.println("$key, $value") }
        println("Finished!")
    }

    @Throws(SQLException::class)
    private fun convertAriosDefinitions() {
        val config = HikariConfig()
        var ds: HikariDataSource
        run {
            config.jdbcUrl = "jdbc:mysql://localhost/arios"
            config.username = "debug"
            config.password = "debug"
            config.driverClassName =
                "com.mysql.cj.jdbc.Driver" //alternative is Class.forName("com.mysql.cj.jdbc.Driver")
            config.addDataSourceProperty("cachePrepStmts", "true")
            config.addDataSourceProperty("prepStmtCacheSize", "250")
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
            ds = HikariDataSource(config)
        }
        val query = "SELECT * from `npc_configs`"
        ds.connection.use { con ->
            con.prepareStatement(query).use { pst ->
                pst.executeQuery().use { rs ->
                    while (rs.next()) {
                        val id = rs.getInt("id")
                        val examine = rs.getString("examine")
                        val name = rs.getString("name")
                        val lifePoints = rs.getInt("lifepoints") * 10
                        val attackLevel = rs.getInt("attack_level")
                        val strengthLevel = rs.getInt("strength_level")
                        val defenceLevel = rs.getInt("defence_level")
                        val rangeLevel = rs.getInt("range_level")
                        val magicLevel = rs.getInt("magic_level")
                        val bonusesArray = rs.getString("bonuses")
                        val bonuses = IntArray(18)
                        if (bonusesArray != null) {
                            val split = bonusesArray.trim { it <= ' ' }
                                .split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            val bonusesList: MutableList<Int> = ArrayList()
                            for (s in split) {
                                val digit = s.toInt()
                                bonusesList.add(digit)
                            }
                            bonusesList.add(11, 0)
                            bonusesList.add(12, 0)
                            bonusesList.add(13, 0)
                            val bonusesI = bonusesList.toTypedArray()
                            for (i in bonusesI.indices) {
                                bonuses[i] = bonusesI[i]
                            }
                        }
                        val respawnDelay = rs.getInt("respawn_delay")
                        val attackSpeed = rs.getInt("attack_speed")
                        val meleeAnimation = rs.getInt("melee_animation")
                        val defenceAnimation = rs.getInt("defence_animation")
                        val deathAnimation = rs.getInt("death_animation")
                        val spawnAnimation = rs.getInt("spawn_animation")
                        val magicAnimation = rs.getInt("magic_animation")
                        val rangeAnimation = rs.getInt("range_animation")
                        val attackGfx = rs.getInt("start_gfx")
                        val attackProjectile = rs.getInt("projectile")
                        val endGfx = rs.getInt("end_gfx")
                        val combatStyle = rs.getInt("combat_style")
                        val aggressive = rs.getInt("aggressive")
                        var attackAnimation = meleeAnimation
                        if (attackAnimation == -1) {
                            attackAnimation = rangeAnimation
                        }
                        if (rangeAnimation == -1) {
                            attackAnimation = magicAnimation
                        }
                        val arios = AriosNPCCharacteristics(
                            id,
                            examine,
                            name,
                            lifePoints,
                            attackLevel,
                            strengthLevel,
                            defenceLevel,
                            rangeLevel,
                            magicLevel,
                            bonuses,
                            respawnDelay,
                            attackSpeed,
                            meleeAnimation,
                            defenceAnimation,
                            deathAnimation,
                            spawnAnimation,
                            magicAnimation,
                            rangeAnimation,
                            attackGfx,
                            attackProjectile,
                            endGfx,
                            combatStyle,
                            aggressive
                        )
                        val maxHit = -1
                        val deathDelay = AnimationDefinitions.getAnimationDefinitions(deathAnimation).emoteGameTickets
                        val characteristics = getCharacteristicsNonNull(id)
                        val existent = characteristics!!.getCombatDefinitions(id)
                        //				NPCCombatDefinitions generated = new NPCCombatDefinitions(lifePoints, attackLevel, strengthLevel, defenceLevel, rangeLevel, magicLevel, bonuses, attackAnimation, defenceAnimation, deathAnimation, attackSpeed, deathDelay, respawnDelay, maxHit, combatStyle, attackGfx, attackProjectile, aggressive);
                        existent!!.hitpoints = arios.lifePoints * 10
                        if (arios.attackLevel != 1 && arios.attackLevel != 0) {
                            existent.attackLevel = arios.attackLevel
                        }
                        if (arios.strengthLevel != 1 && arios.strengthLevel != 0) {
                            existent.strengthLevel = arios.strengthLevel
                        }
                        if (arios.defenceLevel != 1 && arios.defenceLevel != 0) {
                            existent.defenceLevel = arios.defenceLevel
                        }
                        if (arios.rangeLevel != 1 && arios.rangeLevel != 0) {
                            existent.rangeLevel = arios.rangeLevel
                        }
                        if (arios.magicLevel != 1 && arios.magicLevel != 0) {
                            existent.magicLevel = arios.magicLevel
                        }
                        existent.bonuses = arios.bonuses
                        existent.attackGfx = arios.attackGfx
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
            }
        }
        println("Finished")
    }

    private fun convertAloticDefinitions() {
        try {
            val LOAD_IDENTIFIERS = arrayOf(
                "id",
                "maxHp",
                "maxDamage",
                "maxDistance",
                "aggressiveLevel",
                "style",
                "type",
                "attackTicks",
                "deathTicks",
                "respawnTicks",
                "attackAnimation",
                "defendAnimation",
                "deathAnimation",
                "meleeAttDef",
                "magicAttDef",
                "rangedAttDef",
                "weakness",
                "legion",
                "slayerRequest",
                "charms",
                "clueType"
            )
            val br =
                BufferedReader(FileReader("D:\\Tyler\\Google Drive\\Me\\3. RSPS\\1. Servers\\#600-699\\Alotic\\world_server\\data\\npcs\\combat.txt"))
            var line: String
            var identifier: String
            var identifierIndex: Byte = 0
            var lastSuccessfulLoad: Short = -1
            var loadFail = false

            /**
             * Definition values
             */
            var ids: ShortArray? = null
            var maxHp = 0
            var maxDamage = 0
            var maxDistance: Byte = 1
            var attackTicks = 5
            var deathTicks = 5
            var respawnTicks = 50
            var aggressiveLevel: Short = 0
            var style: Style? = null
            val type: Any? = null
            var attackAnimation: Short = -1
            var defendAnimation: Short = -1
            var deathAnimation: Short = -1
            var effectiveAttack: Short = 0
            var effectiveDefence: Short = 0
            var effectiveMagic: Short = 0
            var effectiveRanged: Short = 0
            val legion: Any? = null
            val slayerRequest: Byte = 0
            val customDrops: Array<Any>? = null
            val weakness: Array<Any>? = null
            var weaknessModifier: Byte = 100 // NOTE: THIS IS REDUCED *TO*, NOT REDUCED *BY*
            /**
             * Parsing
             */
            while (br.readLine().also { line = it } != null) {
                if (line == "" || line.isEmpty() || line.startsWith("#")) {
                    continue
                }
                identifier = LOAD_IDENTIFIERS[identifierIndex++.toInt()]
                if (!line.startsWith(identifier)) {
                    if (identifier == "weaknessModifier") {
                        weaknessModifier = 75
                        identifier = LOAD_IDENTIFIERS[identifierIndex++.toInt()]
                    } else { // maybe add elseifs here for default values? cant add new identifiers at the end of the array or it will cause problems.
                        // not elegant but i don't see a better solution besides re-editing the whole combat.txt file
                        loadFail = true
                    }
                }
                if (line.startsWith(identifier)) {
                    try {
                        when (identifier) {
                            "id" -> {
                                val lineSplit = line.split("id=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1]
                                if (lineSplit.contains(",")) {
                                    val idSplit =
                                        lineSplit.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    ids = ShortArray(idSplit.size)
                                    var i: Byte = 0
                                    while (i < idSplit.size) {
                                        ids[i.toInt()] = idSplit[i.toInt()].toShort()
                                        i++
                                    }
                                } else {
                                    ids = shortArrayOf(lineSplit.toShort())
                                }
                                println(
                                    "starting to convert " + Arrays.toString(ids) + " (" + NPCDefinitions.getNPCDefinitions(
                                        ids[0].toInt()
                                    ).name + ")"
                                )
                            }

                            "maxHp" -> maxHp = line.split("maxHp=".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[1].toInt()

                            "maxDamage" -> maxDamage = line.split("maxDamage=".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[1].toInt()

                            "maxDistance" -> maxDistance =
                                line.split("maxDistance=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toByte()

                            "aggressiveLevel" -> aggressiveLevel =
                                line.split("aggressiveLevel=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "attackTicks" -> attackTicks =
                                line.split("attackTicks=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toInt()

                            "deathTicks" -> deathTicks =
                                line.split("deathTicks=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toInt()

                            "respawnTicks" -> respawnTicks =
                                line.split("respawnTicks=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toInt()

                            "attackAnimation" -> attackAnimation =
                                line.split("attackAnimation=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "defendAnimation" -> defendAnimation =
                                line.split("defendAnimation=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "deathAnimation" -> deathAnimation =
                                line.split("deathAnimation=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "effectiveAttack" -> effectiveAttack =
                                line.split("effectiveAttack=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "effectiveDefence" -> effectiveDefence =
                                line.split("effectiveDefence=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "effectiveMagic" -> effectiveMagic =
                                line.split("effectiveMagic=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "effectiveRanged" -> effectiveRanged =
                                line.split("effectiveRanged=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toShort()

                            "weaknessModifier" -> weaknessModifier =
                                line.split("weaknessModifier=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1].toByte()

                            "style" -> {
                                val splitLine = line.split("style=".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()[1]
                                style = if (splitLine == "null") {
                                    null
                                } else {
                                    Style.valueOf(splitLine)
                                }
                            }
                        }
                        if (identifierIndex.toInt() == LOAD_IDENTIFIERS.size) {
                            for (id in ids!!) {
                                val characteristic = getCharacteristicsNonNull(id.toInt())
                                if (characteristic == null) {
                                    println(
                                        "Unable to get characteristics for " + id + ", skipping (" + NPCDefinitions.getNPCDefinitions(
                                            id.toInt()
                                        ).name + ")..."
                                    )
                                    continue
                                }
                                var definitions = characteristic.getCombatDefinitions(id.toInt())
                                if (definitions == null) {
                                    println("Unable to find stored definitions for $id, generating new...")
                                    definitions = NPCCombatDefinitions()
                                }
                                definitions.hitpoints = maxHp
                                definitions.attackAnim = attackAnimation.toInt()
                                definitions.defenceAnim = defendAnimation.toInt()
                                definitions.deathAnim = deathAnimation.toInt()
                                definitions.attackDelay = attackTicks
                                definitions.respawnDelay = respawnTicks
                                definitions.deathDelay = deathTicks
                                if (style != null) {
                                    definitions.attackStyle = style.attackStyle
                                }
                                saveCharacteristics(id.toInt(), characteristic)
                                println("Saved animations for npc " + id + " [" + NPCDefinitions.getNPCDefinitions(id.toInt()).name + "]")
                                identifierIndex = 0
                                lastSuccessfulLoad = id
                            }
                            ids = null
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        loadFail = true
                    }
                }
                if (loadFail) {
                    if (ids == null) {
                        System.err.println("Error loading npc combat definitions!")
                        if (lastSuccessfulLoad.toInt() != -1) {
                            System.err.println("Last successful combat definition loaded: $lastSuccessfulLoad")
                        }
                    } else {
                        System.err.println("Error loading combat definition for npc " + ids[0] + "!")
                    }
                    System.err.println(
                        "Next expected definition identifier: $identifier=value, got: " + line.split("=".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0])
                    break
                }
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Gets the `NPCCharacteristic` instance from the file
     *
     * @param name The name of the npc we will use to find the file
     */
    private fun getCharacteristicsFromFile(name: String): NPCCharacteristic? {
        val fileLocation = getFileLocation(name)
        val file = File(fileLocation)
        if (!file.exists()) {
            return null
        }
        val text = Misc.getText(fileLocation)
        return GSON.fromJson(text, NPCCharacteristic::class.java)
    }

    /**
     * @param name The name of the npc
     */
    private fun getFileLocation(name: String): String {
        return CHARACTERISTICS_LOCATION + name + ".json"
    }

    fun convertNPCCombatDefinitions() {
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

    private fun getNPCIdFromText(text: String): Int {
        val split = getSQLSplittedText(text)
        return split[0]!!.replace('(', ' ').trim { it <= ' ' }.toInt()
    }

    private fun getSQLSplittedText(text: String): Array<String?> {
        val split = text.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val output = arrayOfNulls<String>(split.size)
        for (i in split.indices) {
            val data = split[0]
            val out = if (data.lowercase(Locale.getDefault()).contains("null")) "-1" else data
            output[i] = out
        }
        return output
    }

    fun convertNPCDrops() {
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

    fun convertNPCBonuses() {
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

    fun convertNPCExamines() {
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
    private fun saveCharacteristics(npcId: Int, characteristic: NPCCharacteristic) {
        val npcDefinitions = NPCDefinitions.getNPCDefinitions(npcId)
        if (npcDefinitions == null) {
            println("No definitions for #$npcId")
            return
        }
        val name = npcDefinitions.name
        Misc.saveToJsonFile(getFileLocation(name), characteristic)
    }

    /**
     * Gets the drops of an npc by its id
     */
    @JvmStatic
    fun getDrops(npcId: Int): List<Drop>? {
        val characteristic = getCharacteristics(npcId) ?: return null
        return characteristic.getDrops(npcId)
    }

    /**
     * Gets the characteristics of an npc by its id
     *
     * @param npcId The id of the npc
     */
    private fun getCharacteristics(npcId: Int): NPCCharacteristic? {
        val definitions = NPCDefinitions.getNPCDefinitions(npcId)
        if (definitions == null) {
            println("Unable to find definitions for npc $npcId")
            return null
        }
        val name = definitions.name
        var cached = CACHED_CHARACTERISTICS[name]
        return if (!CACHED_CHARACTERISTICS.containsKey(name)) {
            cached = getCharacteristicsFromFile(name)
            if (cached == null) {
                CACHED_CHARACTERISTICS[name] = null
                return null
            }
            CACHED_CHARACTERISTICS[name] = cached
            cached
        } else {
            cached
        }
    }

    /**
     * Returns an instance of the npc's characteristics unless the npc does not exist in the cache
     */
    private fun getCharacteristicsNonNull(npcId: Int): NPCCharacteristic? {
        val definitions = NPCDefinitions.getNPCDefinitions(npcId)
        if (definitions == null) {
            println("Unable to find definitions for npc $npcId")
            return null
        }
        val name = definitions.name
        val characteristic = getCharacteristicsFromFile(name)
        return characteristic ?: NPCCharacteristic()
    }

    /**
     * Gets the combat definitions of an npc by its id
     */
    @JvmStatic
    fun getCombatDefinitions(npcId: Int, nullable: Boolean): NPCCombatDefinitions? {
        val characteristic = getCharacteristics(npcId)
        if (nullable && characteristic == null) {
            return null
        }
        return if (characteristic == null) {
            DEFAULT_DEFINITION
        } else characteristic.getCombatDefinitions(npcId)
            ?: return DEFAULT_DEFINITION
    }

    /**
     * Gets the bonuses of an npc by its id
     */
    @JvmStatic
    fun getBonuses(npcId: Int): IntArray? {
        val characteristic = getCharacteristics(npcId) ?: return null
        return characteristic.getBonuses(npcId)
    }

    /**
     * Gets the examine of an npc by its id
     */
    fun getExamine(npcId: Int): String {
        val characteristic = getCharacteristics(npcId) ?: return "It's an npc."
        return characteristic.getExamine(npcId) ?: return "It's an npc."
    }

    /**
     * Created by yak.
     */
    enum class Style {
        STAB, SLASH, CRUSH, RANGED, DECENT_OF_DRAGONS, DECENT_OF_DARKNESS, DOWN_TO_EARTH, CLEAR_MIND, LIFE_LEECH, KORASI_SPEC, MAGIC, DRAGON_FIRE, SARADOMIN_LIGHTNING, OTHER;

        val isMelee: Boolean
            get() = this == STAB || this == SLASH || this == CRUSH
        val isRanged: Boolean
            get() = this == RANGED || this == DECENT_OF_DRAGONS || this == DECENT_OF_DARKNESS || this == DOWN_TO_EARTH || this == CLEAR_MIND || this == LIFE_LEECH
        val isMagic: Boolean
            get() = this == MAGIC
        val attackStyle: Int
            get() = when (this) {
                STAB -> BonusConstants.STAB_ATTACK
                SLASH -> BonusConstants.SLASH_ATTACK
                CRUSH -> BonusConstants.CRUSH_ATTACK
                MAGIC -> BonusConstants.MAGIC_ATTACK
                RANGED -> BonusConstants.RANGE_ATTACK
                else -> throw IllegalStateException("Unable to parse attack style")
            }
    }
}