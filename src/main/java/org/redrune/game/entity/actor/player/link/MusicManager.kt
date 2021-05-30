package org.redrune.game.entity.actor.player.link

import org.redrune.cache.loaders.ClientScriptMap
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.map.region.Region
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.functions.Misc
import java.io.Serializable

class MusicManager : Serializable {
    private val unlockedMusics: ArrayList<Int>
    private val playList: ArrayList<Int>

    @Transient
    private var player: Player? = null

    @Transient
    private var playingMusic = 0

    @Transient
    private var playingMusicDelay: Long = 0

    @Transient
    private var settedMusic = false

    @Transient
    private var playListOn = false

    @Transient
    private var nextPlayListMusic = 0

    @Transient
    private var shuffleOn = false
    fun passMusics(p: Player) {
        for (musicId in p.musicManager.unlockedMusics) {
            if (!unlockedMusics.contains(musicId)) {
                unlockedMusics.add(musicId)
            }
        }
    }

    fun hasMusic(id: Int): Boolean {
        return unlockedMusics.contains(id)
    }

    fun setPlayer(player: Player) {
        this.player = player
        playingMusic = RegionManager.getRegion(player.regionId).musicId
    }

    fun switchShuffleOn() {
        if (shuffleOn) {
            playListOn = false
            refreshPlayListConfigs()
        }
        shuffleOn = !shuffleOn
    }

    fun refreshPlayListConfigs() {
        val configValues = IntArray(PLAY_LIST_CONFIG_IDS.size)
        for (i in configValues.indices) {
            configValues[i] = -1
        }
        run {
            var i = 0
            while (i < playList.size) {
                val musicId1 = playList[i]
                val musicId2 = if (i + 1 >= playList.size) null else playList[i + 1]
                if (musicId1 == null && musicId2 == null) {
                    break
                }
                val musicIndex = ClientScriptMap.getMap(1351).getKeyForValue(musicId1).toInt()
                var configValue: Int
                configValue = if (musicId2 != null) {
                    val musicIndex2 = ClientScriptMap.getMap(1351).getKeyForValue(musicId2).toInt()
                    musicIndex or musicIndex2 shl 15
                } else {
                    musicIndex or -1 shl 15
                }
                configValues[i / 2] = configValue
                i += 2
            }
        }
        for (i in PLAY_LIST_CONFIG_IDS.indices) {
            player!!.packets.sendConfig(PLAY_LIST_CONFIG_IDS[i], configValues[i])
        }
    }

    fun clearPlayList() {
        if (playList.isEmpty()) {
            return
        }
        playList.clear()
        refreshPlayListConfigs()
    }

    fun addPlayingMusicToPlayList() {
        addToPlayList(ClientScriptMap.getMap(1351).getKeyForValue(playingMusic).toInt())
    }

    fun addToPlayList(musicIndex: Int) {
        if (playList.size == 12) {
            return
        }
        val musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex.toLong())
        if (musicId != -1 && unlockedMusics.contains(musicId) && !playList.contains(musicId)) {
            playList.add(musicId)
            if (playListOn) {
                switchPlayListOn()
            } else {
                refreshPlayListConfigs()
            }
        }
    }

    fun switchPlayListOn() {
        if (playListOn) {
            playListOn = false
            shuffleOn = false
            refreshPlayListConfigs()
        } else {
            playListOn = true
            nextPlayListMusic = 0
            replayMusic()
        }
    }

    fun replayMusic() {
        if (playListOn && playList.size > 0) {
            if (shuffleOn) {
                playingMusic = playList[Misc.getRandom(playList.size - 1)]
            } else {
                if (nextPlayListMusic >= playList.size) {
                    nextPlayListMusic = 0
                }
                playingMusic = playList[nextPlayListMusic++]
            }
        } else if (unlockedMusics.size > 0) // random music
        {
            playingMusic = unlockedMusics[Misc.getRandom(unlockedMusics.size - 1)]
        }
        playMusic(playingMusic)
    }

    fun playMusic(musicId: Int) {
        if (!player!!.hasStarted()) {
            return
        }
        playingMusicDelay = Misc.currentTimeMillis()
        if (musicId == -2) {
            playingMusic = musicId
            player!!.packets.sendMusic(-1)
            player!!.packets.sendIComponentText(187, 4, "")
            return
        }
        player!!.packets.sendMusic(musicId, if (playingMusic == -1) 0 else 100, 255)
        playingMusic = musicId
        val musicIndex = ClientScriptMap.getMap(1351).getKeyForValue(musicId).toInt()
        if (musicIndex != -1) {
            var musicName = ClientScriptMap.getMap(1345).getStringValue(musicIndex.toLong())
            if (musicName == " ") {
                musicName = getMusicName1(player!!.regionId)
            }
            player!!.packets.sendIComponentText(187, 4, musicName ?: "")
            if (!unlockedMusics.contains(musicId)) {
                addMusic(musicId)
                if (musicName != null) {
                    player!!.packets.sendMessage("<col=ff0000>You have unlocked a new music track: $musicName.", true)
                }
            }
        }
    }

    fun addMusic(musicId: Int) {
        unlockedMusics.add(musicId)
        refreshListConfigs()
        if (unlockedMusics.size >= 70) {
            player!!.emotesManager.unlockEmote(41)
        }
    }

    fun refreshListConfigs() {
        val configValues = IntArray(CONFIG_IDS.size)
        for (musicId in unlockedMusics) {
            val musicIndex = ClientScriptMap.getMap(1351).getKeyForValue(musicId).toInt()
            if (musicIndex == -1) {
                continue
            }
            val index = getConfigIndex(musicIndex)
            if (index >= CONFIG_IDS.size) {
                continue
            }
            configValues[index] = configValues[index] or (1 shl musicIndex - index * 32)
        }
        for (i in CONFIG_IDS.indices) {
            if (configValues[i] != 0) {
                player!!.packets.sendConfig(CONFIG_IDS[i], configValues[i])
            }
        }
    }

    fun getConfigIndex(musicId: Int): Int {
        return (musicId + 1) / 32
    }

    fun removeFromPlayList(musicIndex: Int) {
        val musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex.toLong())
        if (musicId != -1 && unlockedMusics.contains(musicId) && playList.contains(musicId)) {
            playList.remove(musicId)
            if (playListOn) {
                switchPlayListOn()
            } else {
                refreshPlayListConfigs()
            }
        }
    }

    fun unlockMusicPlayer() {
        player!!.packets.sendUnlockIComponentOptionSlots(187, 1, 0, ClientScriptMap.getMap(1351).size * 2, 0, 2, 3)
    }

    fun init() {
        // unlock music inter all options
        if (playingMusic >= 0) {
            playMusic(playingMusic)
        }
        refreshListConfigs()
        refreshPlayListConfigs()
    }

    fun musicEnded(): Boolean {
        return playingMusic != -2 && playingMusicDelay + 180000 < Misc.currentTimeMillis()
    }

    fun forcePlayMusic(musicId: Int) {
        settedMusic = true
        playMusic(musicId)
    }

    fun reset() {
        settedMusic = false
        player!!.musicManager.checkMusic(RegionManager.getRegion(player!!.regionId).musicId)
    }

    fun checkMusic(requestMusicId: Int) {
        if (playListOn || settedMusic && playingMusicDelay + 180000 >= Misc.currentTimeMillis()) {
            return
        }
        settedMusic = false
        if (playingMusic != requestMusicId) {
            playMusic(requestMusicId)
        }
    }

    fun playAnotherMusic(musicIndex: Int) {
        val musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex.toLong())
        if (musicId != -1 && unlockedMusics.contains(musicId)) {
            settedMusic = true
            if (playListOn) {
                switchPlayListOn()
            }
            playMusic(musicId)
        }
    }

    companion object {
        private const val serialVersionUID = 1020415702861567375L
        private val CONFIG_IDS = intArrayOf(
            20,
            21,
            22,
            23,
            24,
            25,
            298,
            311,
            346,
            414,
            464,
            598,
            662,
            721,
            906,
            1009,
            1104,
            1136,
            1180,
            1202,
            1381,
            1394,
            1434,
            1596,
            1618,
            1619,
            1620,
            1865,
            1864,
            2246,
            2019
        )
        private val PLAY_LIST_CONFIG_IDS = intArrayOf(1621, 1622, 1623, 1624, 1625, 1626)

        @JvmStatic
        fun loadMusicIds(region: Region) {
            val musicId1 = getMusicId(getMusicName1(region.regionId))
            if (musicId1 != -1) {
                val musicId2 = getMusicId(getMusicName2(region.regionId))
                if (musicId2 != -1) {
                    val musicId3 = getMusicId(getMusicName3(region.regionId))
                    if (musicId3 != -1) {
                        region.setMusicIds(intArrayOf(musicId1, musicId2, musicId3))
                    } else {
                        region.setMusicIds(intArrayOf(musicId1, musicId2))
                    }
                } else {
                    region.setMusicIds(intArrayOf(musicId1))
                }
            }
        }

        fun getMusicId(musicName: String?): Int {
            if (musicName == null) {
                return -1
            }
            if (musicName == "") {
                return -2
            }
            if (musicName == "Skyfall") {
                return 2000
            }
            if (musicName == "Stronger (What Doesn't Kill You)") {
                return 2001
            }
            val musicIndex = ClientScriptMap.getMap(1345).getKeyForValue(musicName).toInt()
            return ClientScriptMap.getMap(1351).getIntValue(musicIndex.toLong())
        }

        fun getMusicName2(regionId: Int): String? {
            return when (regionId) {
                12342 -> "Stronger (What Doesn't Kill You)"
                13152 -> "I Can See You"
                13151 -> "You Will Know Me"
                12895 -> "Steady"
                12896 -> "Hunted"
                12853 -> "Cellar Song"
                11573 -> "Taverley Enchantment"
                11575 -> "Taverley Adventure"
                13626, 13627, 13882, 13881 -> "Daemonheim Fremenniks"
                18512, 18511, 19024 -> "Tzhaar City II"
                18255 -> "Tzhaar Supremacy II"
                14948 -> "Dominion Lobby II"
                else -> null
            }
        }

        fun getMusicName3(regionId: Int): String? {
            return when (regionId) {
                13152 -> "Steady"
                13151 -> "Hunted"
                12895 -> "Target"
                12896 -> "I Can See You"
                11575 -> "Spiritual"
                18512, 18511, 19024 -> "Tzhaar City III"
                18255 -> "Tzhaar Supremacy III"
                14948 -> "Dominion Lobby III"
                else -> null
            }
        }

        fun getMusicName1(regionId: Int): String? {
            return when (regionId) {
                8774 -> "Taverley Lament"
                11576 -> "Kingdom"
                11320 -> "Tremble"
                12616 -> "Undead Dungeon"
                10388 -> "Cavern"
                12107 -> "Into the Abyss"
                11164 -> "The Slayer"
                10908, 10907 -> "Masquerade"
                4707, 4451, 5221, 5220, 5219, 4453, 4709 -> "Hunting Dragons"
                12115 -> "Dimension X"
                8527 -> "Aye Car Rum Ba"
                8528 -> "Blistering Barnacles"
                13206 -> "The Lost Tribe"
                12949, 12950 -> "Cave of the Goblins"
                12948 -> "The Power of Tears"
                11416 -> "Underground"
                14638 -> "In the Brine"
                14637, 14894 -> "Life's a Beach!"
                14494 -> "Little Cave of Horrors"
                11673 -> "Courage"
                11672 -> "Dunjun"
                11417 -> "Arabique"
                11671 -> "Royale"
                13977 -> "Stillness"
                13622 -> "Morytania"
                13722 -> "Mausoleum"
                10906 -> "Twilight"
                12181 -> "Woe of the Wyvern"
                11925 -> "Starlight"
                13617 -> "Citharede Requiem"
                13361 -> "Valerio's Song"
                13910, 13654 -> "Rest for the Weary"
                13656 -> "The Muspah's Tomb"
                11057 -> "High Seas"
                10802 -> "Jungly2"
                10801 -> "Landlubber"
                11058 -> "Jolly-R"
                10901 -> "Pathways"
                10645, 10644, 10900 -> "7th Realm"
                11315, 11314 -> "The Shadow"
                11414, 11413 -> "Dangerous Road"
                7505 -> "Dogs of War"
                8017 -> "Food for Thought"
                8530 -> "Malady"
                9297 -> "Dance of Death"
                10040 -> "Lighthouse"
                10140 -> "Out of the Deep"
                9797 -> "Crystal Cave"
                9541 -> "Faerie"
                11927 -> "Cave Background"
                10301 -> "Skyfall"
                14646 -> "The Other Side"
                14746 -> "Phasmatys"
                14747 -> "Brew Hoo Hoo"
                15967 -> "Runespan"
                15711 -> "Runearia"
                15710 -> "Runebreath"
                13152 -> "Hunted"
                13151 -> "Target"
                12895 -> "I Can See You"
                12896 -> "You Will Know Me"
                12597 -> "Spirit"
                13109 -> "Medieval"
                13110 -> "Honkytonky Parade"
                10658 -> "Espionage"
                13899 -> "Zealot"
                10039 -> "Legion"
                11319 -> "Warriors' Guild"
                11575 -> "Spiritual"
                11573 -> "Taverley Ambience"
                7473 -> "The Waiting Game"
                18512, 18511, 19024 -> "Tzhaar City I"
                18255 -> "Tzhaar Supremacy I"
                14672, 14671, 14415, 14416 -> "Living Rock"
                11157 -> "Aztec"
                15446, 15957, 15958 -> "Dead and Buried"
                12848 -> "Arabian3"
                12954, 12442, 12441 -> "Scape Cave"
                12185, 11929 -> "Dwarf Theme"
                12184 -> "Workshop"
                6992, 6993 -> "The Mad Mole"
                9776 -> "Melodrama"
                10029, 10285 -> "Jungle Hunt"
                14231 -> "Dangerous Way"
                12856 -> "Faithless"
                13104, 12847, 13359, 13102 -> "Desert Voyage"
                13103 -> "Lonesome"
                12589 -> "The Desert"
                18517, 18516, 18773, 18775, 13407, 13360 -> ""
                14948 -> "Dominion Lobby I"
                11836 -> "Attack3"
                12091 -> "Wilderness2"
                12092 -> "Wild Side"
                9781 -> "Gnome Village"
                11339 -> "Serene"
                11083 -> "Miracle Dance"
                10827 -> "Zealot"
                10571 -> "Down to Earth"
                10315 -> "Quest"
                8523 -> "Stratosphere"
                9035 -> "Complication"
                8779 -> "La Mort"
                10059 -> "Heart and Mind"
                9803 -> "Righteousness"
                9547 -> "Understanding"
                9804 -> "Bloodbath"
                13107 -> "Arabian2"
                13105 -> "Al Kharid"
                12342 -> "Forever"
                10806 -> "Overture"
                10899 -> "Karamja Jam"
                13623 -> "The Terrible Tower"
                12374 -> "The Route of All Evil"
                9802 -> "Undead Dungeon"
                10809 -> "Borderland"
                10553 -> "Rellekka"
                10552 -> "Saga"
                10296 -> "Lullaby"
                10828 -> "Legend"
                9275 -> "Volcanic Vikings"
                11061, 11317 -> "Fishing"
                9551 -> "TzHaar!"
                12345 -> "Eruption"
                12089 -> "Dark"
                12446, 12445 -> "Wilderness"
                12343 -> "Dangerous"
                14131 -> "Dance of the Undead"
                11844, 11588 -> "The Vacant Abyss"
                13363 -> "Shine"
                13362 -> "Duel Arena"
                12082 -> "Sea Shanty2"
                12081 -> "Tomorrow"
                11602 -> "Strength of Saradomin"
                12590 -> "Bandit Camp"
                10329 -> "The Sound of Guthix"
                9033 -> "Attack5"
                11603 -> "Zamorak Zoo"
                11346 -> "Armadyl Alliance"
                11347 -> "Armageddon"
                13114 -> "Wilderness"
                12086 -> "Knightmare"
                9552 -> "Fire and Brimstone"
                13972 -> "Insect Queen"
                11094 -> "Clan Wars"
                12336 -> "Newbie Melody"
                14644 -> "Darkmeyer"
                13626, 13627, 13882, 13881 -> "Daemonheim Entrance"
                11574 -> "Splendour"
                12851 -> "Autumn Voyage"
                12338 -> "Unknown Land"
                12339 -> "Start"
                12340 -> "Spooky"
                12850 -> "Harmony"
                12849 -> "Yesteryear"
                12593 -> "Book of Spells"
                12594 -> "Dream"
                12595 -> "Flute Salad"
                12854 -> "Adventure"
                12853 -> "Garden"
                12852 -> "Expanse"
                13108 -> "Still Night"
                12083 -> "Wander"
                11828 -> "Fanfare"
                11829 -> "Scape Soft"
                11577 -> "Mad Eadgar"
                10293 -> "Mellow"
                11824 -> "Mudskipper Melody"
                11570 -> "Wandar"
                12341 -> "Barbarianims"
                12855 -> "Crystal Sword"
                12344 -> "Dark"
                12599 -> "Doorways"
                12598 -> "The Trade Parade"
                11318 -> "Ice Melody"
                12600 -> "Scape Wild"
                10032 -> "Big Chords"
                10288 -> "Magic Dance"
                11826 -> "Long Way Home"
                11825 -> "Attention"
                11827 -> "Nightfall"
                11062, 10805 -> "Camelot"
                10550 -> "Talking Forest"
                10549 -> "Lasting"
                10548 -> "Wonderous"
                10547 -> "Baroque"
                10291, 10292 -> "Knightly"
                11571 -> "Miles Away"
                11595 -> "Rune Essence"
                10294 -> "Theme"
                12349 -> "Mage Arena"
                13365 -> "Venture"
                13364 -> "Medieval"
                13878 -> "Village"
                13877 -> "Waterlogged"
                9516 -> "Command Centre"
                12596 -> "Greatness"
                10804 -> "Trinity"
                11601 -> "Zaros Zeitgeist" // zaros godwars
                else -> null
            }
        }
    }

    init {
        unlockedMusics = ArrayList()
        playList = ArrayList(12)
        // auto unlocked musics
        unlockedMusics.add(62)
        unlockedMusics.add(400)
        unlockedMusics.add(16)
        unlockedMusics.add(466)
        unlockedMusics.add(321)
        unlockedMusics.add(547)
        unlockedMusics.add(621)
        unlockedMusics.add(207)
        unlockedMusics.add(401)
        unlockedMusics.add(147)
        unlockedMusics.add(457)
        unlockedMusics.add(552)
        unlockedMusics.add(858)
    }
}