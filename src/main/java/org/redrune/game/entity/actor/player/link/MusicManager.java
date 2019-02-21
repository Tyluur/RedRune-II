package org.redrune.game.entity.actor.player.link;

import org.redrune.cache.loaders.ClientScriptMap;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;
import java.util.ArrayList;

public final class MusicManager implements Serializable {
	
	private static final long serialVersionUID = 1020415702861567375L;
	
	private static final int[] CONFIG_IDS = new int[] { 20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1104, 1136, 1180, 1202, 1381, 1394, 1434, 1596, 1618, 1619, 1620, 1865, 1864, 2246, 2019 };
	
	private static final int[] PLAY_LIST_CONFIG_IDS = new int[] { 1621, 1622, 1623, 1624, 1625, 1626 };
	
	private ArrayList<Integer> unlockedMusics;
	
	private ArrayList<Integer> playList;
	
	private transient Player player;
	
	private transient int playingMusic;
	
	private transient long playingMusicDelay;
	
	private transient boolean settedMusic;
	
	private transient boolean playListOn;
	
	private transient int nextPlayListMusic;
	
	private transient boolean shuffleOn;
	
	public MusicManager() {
		unlockedMusics = new ArrayList<Integer>();
		playList = new ArrayList<Integer>(12);
		// auto unlocked musics
		unlockedMusics.add(62);
		unlockedMusics.add(400);
		unlockedMusics.add(16);
		unlockedMusics.add(466);
		unlockedMusics.add(321);
		unlockedMusics.add(547);
		unlockedMusics.add(621);
		unlockedMusics.add(207);
		unlockedMusics.add(401);
		unlockedMusics.add(147);
		unlockedMusics.add(457);
		unlockedMusics.add(552);
		unlockedMusics.add(858);
	}
	
	public static void loadMusicIds(Region region) {
		int musicId1 = MusicManager.getMusicId(MusicManager.getMusicName1(region.getRegionId()));
		if (musicId1 != -1) {
			int musicId2 = MusicManager.getMusicId(MusicManager.getMusicName2(region.getRegionId()));
			if (musicId2 != -1) {
				int musicId3 = MusicManager.getMusicId(MusicManager.getMusicName3(region.getRegionId()));
				if (musicId3 != -1) {
					region.setMusicIds(new int[] { musicId1, musicId2, musicId3 });
				} else {
					region.setMusicIds(new int[] { musicId1, musicId2 });
				}
			} else {
				region.setMusicIds(new int[] { musicId1 });
			}
		}
	}
	
	public static final int getMusicId(String musicName) {
		if (musicName == null) {
			return -1;
		}
		if (musicName.equals("")) {
			return -2;
		}
		if (musicName.equals("Skyfall")) {
			return 2000;
		}
		if (musicName.equals("Stronger (What Doesn't Kill You)")) {
			return 2001;
		}
		int musicIndex = (int) ClientScriptMap.getMap(1345).getKeyForValue(musicName);
		return ClientScriptMap.getMap(1351).getIntValue(musicIndex);
	}
	
	public static final String getMusicName2(int regionId) {
		switch (regionId) {
			case 12342: // edge
				return "Stronger (What Doesn't Kill You)";
			case 13152: // crucible
				return "I Can See You";
			case 13151: // crucible
				return "You Will Know Me";
			case 12895: // crucible
				return "Steady";
			case 12896: // crucible
				return "Hunted";
			case 12853:
				return "Cellar Song";
			case 11573: // taverley
				return "Taverley Enchantment";
			
			case 11575: // burthope
				return "Taverley Adventure";
			/*
			 * kalaboss
			 */
			case 13626:
			case 13627:
			case 13882:
			case 13881:
				return "Daemonheim Fremenniks";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City II";
			case 18255: // fight pits
				return "Tzhaar Supremacy II";
			case 14948:
				return "Dominion Lobby II";
			default:
				return null;
		}
	}
	
	public static final String getMusicName3(int regionId) {
		switch (regionId) {
			case 13152: // crucible
				return "Steady";
			case 13151: // crucible
				return "Hunted";
			case 12895: // crucible
				return "Target";
			case 12896: // crucible
				return "I Can See You";
			case 11575: // burthope
				return "Spiritual";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City III";
			case 18255: // fight pits
				return "Tzhaar Supremacy III";
			case 14948:
				return "Dominion Lobby III";
			default:
				return null;
		}
	}
	
	public void passMusics(Player p) {
		for (int musicId : p.getMusicManager().unlockedMusics) {
			if (!unlockedMusics.contains(musicId)) {
				unlockedMusics.add(musicId);
			}
		}
	}
	
	public boolean hasMusic(int id) {
		return unlockedMusics.contains(id);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		playingMusic = RegionManager.getRegion(player.getRegionId()).getMusicId();
	}
	
	public void switchShuffleOn() {
		if (shuffleOn) {
			playListOn = false;
			refreshPlayListConfigs();
		}
		shuffleOn = !shuffleOn;
	}
	
	public void refreshPlayListConfigs() {
		int[] configValues = new int[PLAY_LIST_CONFIG_IDS.length];
		for (int i = 0; i < configValues.length; i++) {
			configValues[i] = -1;
		}
		for (int i = 0; i < playList.size(); i += 2) {
			Integer musicId1 = playList.get(i);
			Integer musicId2 = (i + 1) >= playList.size() ? null : playList.get(i + 1);
			if (musicId1 == null && musicId2 == null) {
				break;
			}
			int musicIndex = (int) ClientScriptMap.getMap(1351).getKeyForValue(musicId1);
			int configValue;
			if (musicId2 != null) {
				int musicIndex2 = (int) ClientScriptMap.getMap(1351).getKeyForValue(musicId2);
				configValue = musicIndex | musicIndex2 << 15;
			} else {
				configValue = musicIndex | -1 << 15;
			}
			configValues[i / 2] = configValue;
		}
		for (int i = 0; i < PLAY_LIST_CONFIG_IDS.length; i++) {
			player.getPackets().sendConfig(PLAY_LIST_CONFIG_IDS[i], configValues[i]);
		}
	}
	
	public void clearPlayList() {
		if (playList.isEmpty()) {
			return;
		}
		playList.clear();
		refreshPlayListConfigs();
	}
	
	public void addPlayingMusicToPlayList() {
		addToPlayList((int) ClientScriptMap.getMap(1351).getKeyForValue(playingMusic));
	}
	
	public void addToPlayList(int musicIndex) {
		if (playList.size() == 12) {
			return;
		}
		int musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId) && !playList.contains(musicId)) {
			playList.add(musicId);
			if (playListOn) {
				switchPlayListOn();
			} else {
				refreshPlayListConfigs();
			}
		}
	}
	
	public void switchPlayListOn() {
		if (playListOn) {
			playListOn = false;
			shuffleOn = false;
			refreshPlayListConfigs();
		} else {
			playListOn = true;
			nextPlayListMusic = 0;
			replayMusic();
		}
	}
	
	public void replayMusic() {
		if (playListOn && playList.size() > 0) {
			if (shuffleOn) {
				playingMusic = playList.get(Misc.getRandom(playList.size() - 1));
			} else {
				if (nextPlayListMusic >= playList.size()) {
					nextPlayListMusic = 0;
				}
				playingMusic = playList.get(nextPlayListMusic++);
			}
		} else if (unlockedMusics.size() > 0) // random music
		{
			playingMusic = unlockedMusics.get(Misc.getRandom(unlockedMusics.size() - 1));
		}
		playMusic(playingMusic);
	}
	
	public void playMusic(int musicId) {
		if (!player.hasStarted()) {
			return;
		}
		playingMusicDelay = Misc.currentTimeMillis();
		if (musicId == -2) {
			playingMusic = musicId;
			player.getPackets().sendMusic(-1);
			player.getPackets().sendIComponentText(187, 4, "");
			return;
		}
		player.getPackets().sendMusic(musicId, playingMusic == -1 ? 0 : 100, 255);
		playingMusic = musicId;
		int musicIndex = (int) ClientScriptMap.getMap(1351).getKeyForValue(musicId);
		if (musicIndex != -1) {
			String musicName = ClientScriptMap.getMap(1345).getStringValue(musicIndex);
			if (musicName.equals(" ")) {
				musicName = getMusicName1(player.getRegionId());
			}
			player.getPackets().sendIComponentText(187, 4, musicName != null ? musicName : "");
			if (!unlockedMusics.contains(musicId)) {
				addMusic(musicId);
				if (musicName != null) {
					player.getPackets().sendMessage("<col=ff0000>You have unlocked a new music track: " + musicName + ".");
				}
			}
		}
	}
	
	public static final String getMusicName1(int regionId) {
		switch (regionId) {
			case 8774: //taverly slayer dungeon
				return "Taverley Lament";
			case 11576:
				return "Kingdom";
			case 11320:
				return "Tremble";
			case 12616: //tarns lair
				return "Undead Dungeon";
			case 10388:
				return "Cavern";
			case 12107:
				return "Into the Abyss";
			case 11164:
				return "The Slayer";
			case 10908:
			case 10907:
				return "Masquerade";
			case 4707:
			case 4451:
			case 5221:
			case 5220:
			case 5219:
			case 4453:
			case 4709:
				return "Hunting Dragons";
			case 12115:
				return "Dimension X";
			case 8527: //braindeath island
				return "Aye Car Rum Ba";
			case 8528: //braindeath mountain
				return "Blistering Barnacles";
			case 13206: //goblin mines under lumby
				return "The Lost Tribe";
			case 12949:
			case 12950:
				return "Cave of the Goblins";
			case 12948:
				return "The Power of Tears";
			case 11416: //dramen tree
				return "Underground";
			case 14638: //mosleharms
				return "In the Brine";
			case 14637:
			case 14894:
				return "Life's a Beach!";
			case 14494: //mosleharms cave
				return "Little Cave of Horrors";
			case 11673: //taverly dungeon musics
				return "Courage";
			case 11672:
				return "Dunjun";
			case 11417:
				return "Arabique";
			case 11671:
				return "Royale";
			case 13977:
				return "Stillness";
			case 13622:
				return "Morytania";
			case 13722:
				return "Mausoleum";
			case 10906:
				return "Twilight";
			case 12181: //Asgarnian Ice Dungeon's wyvern area
				return "Woe of the Wyvern";
			case 11925: //Asgarnian Ice Dungeon
				return "Starlight";
			case 13617: //abbey
				return "Citharede Requiem";
			case 13361: //desert verms
				return "Valerio's Song";
			case 13910: //The Tale of the Muspah cave entrance
			case 13654:
				return "Rest for the Weary";
			case 13656: //The Tale of the Muspah cave ice verms area
				return "The Muspah's Tomb";
			case 11057: //brimhaven and arroundd
				return "High Seas";
			case 10802:
				return "Jungly2";
			case 10801:
				return "Landlubber";
			case 11058:
				return "Jolly-R";
			case 10901: //brimhaven dungeon entrance
				return "Pathways";
			case 10645: //brimhaven dungeon
			case 10644:
			case 10900:
				return "7th Realm";
			case 11315: //crandor
			case 11314:
				return "The Shadow";
			case 11414: //karanja underground
			case 11413:
				return "Dangerous Road";
			case 7505: //strongholf of security war
				return "Dogs of War";
			case 8017: //strongholf of security famine
				return "Food for Thought";
			case 8530: //strongholf of security pestile
				return "Malady";
			case 9297: //strongholf of security death
				return "Dance of Death";
			case 10040:
				return "Lighthouse";
			case 10140: // inside lighthouse
				return "Out of the Deep";
			case 9797:
				return "Crystal Cave";
			case 9541:
				return "Faerie";
			case 11927: // gamers grotto
				return "Cave Background";
			case 10301: // dz
				return "Skyfall";
			case 14646:// Port Phasmatys
				return "The Other Side";
			case 14746:// Ectofuntus
				return "Phasmatys";
			case 14747:// Port Phasmatys brewery
				return "Brew Hoo Hoo";
			case 15967:// Runespan
				return "Runespan";
			case 15711:// Runespan
				return "Runearia";
			case 15710:// Runespan
				return "Runebreath";
			case 13152: // crucible
				return "Hunted";
			case 13151: // crucible
				return "Target";
			case 12895: // crucible
				return "I Can See You";
			case 12896: // crucible
				return "You Will Know Me";
			case 12597:
				return "Spirit";
			case 13109:
				return "Medieval";
			case 13110:
				return "Honkytonky Parade";
			case 10658:
				return "Espionage";
			case 13899: // water altar
				return "Zealot";
			case 10039:
				return "Legion";
			case 11319: // warriors guild
				return "Warriors' Guild";
			case 11575: // burthope
				return "Spiritual";
			case 11573: // taverley
				return "Taverley Ambience";
			case 7473:
				return "The Waiting Game";
			case 18512:
			case 18511:
			case 19024:
				return "Tzhaar City I";
			case 18255: // fight pits
				return "Tzhaar Supremacy I";
			case 14672:
			case 14671:
			case 14415:
			case 14416:
				return "Living Rock";
			case 11157: // Brimhaven Agility Arena
				return "Aztec";
			case 15446:
			case 15957:
			case 15958:
				return "Dead and Buried";
			case 12848:
				return "Arabian3";
			case 12954:
			case 12442:
			case 12441:
				return "Scape Cave";
			case 12185:
			case 11929:
				return "Dwarf Theme";
			case 12184:
				return "Workshop";
			case 6992:
			case 6993: // mole lair
				return "The Mad Mole";
			case 9776: // castle wars
				return "Melodrama";
			case 10029:
			case 10285:
				return "Jungle Hunt";
			case 14231: // barrows under
				return "Dangerous Way";
			case 12856: // chaos temple
				return "Faithless";
			case 13104:
			case 12847: // arround desert camp
			case 13359:
			case 13102:
				return "Desert Voyage";
			case 13103:
				return "Lonesome";
			case 12589: // granite mine
				return "The Desert";
			case 18517: //polipore dungeon
			case 18516:
			case 18773:
			case 18775:
			case 13407: // crucible entrance
			case 13360: // dominion tower outside
				return "";
			case 14948:
				return "Dominion Lobby I";
			case 11836: // lava maze near kbd entrance
				return "Attack3";
			case 12091: // lava maze west
				return "Wilderness2";
			case 12092: // lava maze north
				return "Wild Side";
			case 9781:
				return "Gnome Village";
			case 11339: // air altar
				return "Serene";
			case 11083: // mind altar
				return "Miracle Dance";
			case 10827: // water altar
				return "Zealot";
			case 10571: // earth altar
				return "Down to Earth";
			case 10315: // fire altar
				return "Quest";
			case 8523: // cosmic altar
				return "Stratosphere";
			case 9035: // chaos altar
				return "Complication";
			case 8779: // death altar
				return "La Mort";
			case 10059: // body altar
				return "Heart and Mind";
			case 9803: // law altar
				return "Righteousness";
			case 9547: // nature altar
				return "Understanding";
			case 9804: // blood altar
				return "Bloodbath";
			case 13107:
				return "Arabian2";
			case 13105:
				return "Al Kharid";
			case 12342: // edge
				return "Forever";
			case 10806:
				return "Overture";
			case 10899:
				return "Karamja Jam";
			case 13623:
				return "The Terrible Tower";
			case 12374:
				return "The Route of All Evil";
			case 9802:
				return "Undead Dungeon";
			case 10809: // east rellekka
				return "Borderland";
			case 10553: // Rellekka
				return "Rellekka";
			case 10552: // south
				return "Saga";
			case 10296: // south west
				return "Lullaby";
			case 10828: // south east
				return "Legend";
			case 9275:
				return "Volcanic Vikings";
			case 11061:
			case 11317:
				return "Fishing";
			case 9551:
				return "TzHaar!";
			case 12345:
				return "Eruption";
			case 12089:
				return "Dark";
			case 12446:
			case 12445:
				return "Wilderness";
			case 12343:
				return "Dangerous";
			case 14131:
				return "Dance of the Undead";
			case 11844:
			case 11588:
				return "The Vacant Abyss";
			case 13363: // duel arena hospital
				return "Shine";
			case 13362: // duel arena
				return "Duel Arena";
			case 12082: // port sarim
				return "Sea Shanty2";
			case 12081: // port sarim south
				return "Tomorrow";
			case 11602:
				return "Strength of Saradomin";
			case 12590:
				return "Bandit Camp";
			case 10329:
				return "The Sound of Guthix";
			case 9033:
				return "Attack5";
			// godwars
			case 11603:
				return "Zamorak Zoo";
			case 11346:
				return "Armadyl Alliance";
			case 11347:
				return "Armageddon";
			case 13114:
				return "Wilderness";
			// black kngihts fortess
			case 12086:
				return "Knightmare";
			// tzaar
			case 9552:
				return "Fire and Brimstone";
			// kq
			case 13972:
				return "Insect Queen";
			// clan wars free for all:
			case 11094:
				return "Clan Wars";
			/*
			 * tutorial island
			 */
			case 12336:
				return "Newbie Melody";
			/*
			 * darkmeyer
			 */
			case 14644:
				return "Darkmeyer";
			/*
			 * kalaboss
			 */
			case 13626:
			case 13627:
			case 13882:
			case 13881:
				return "Daemonheim Entrance";
			/*
			 * Lumbridge, falador and region.
			 */
			case 11574: // heroes guild
				return "Splendour";
			case 12851:
				return "Autumn Voyage";
			case 12338: // draynor and market
				return "Unknown Land";
			case 12339: // draynor up
				return "Start";
			case 12340: // draynor mansion
				return "Spooky";
			case 12850: // lumbry castle
				return "Harmony";
			case 12849: // east lumbridge swamp
				return "Yesteryear";
			case 12593: // at Lumbridge Swamp.
				return "Book of Spells";
			case 12594: // on the path between Lumbridge and Draynor.
				return "Dream";
			case 12595: // at the Lumbridge windmill area.
				return "Flute Salad";
			case 12854: // at Varrock Palace.
				return "Adventure";
			case 12853: // at varrock center
				return "Garden";
			case 12852: // varock mages
				return "Expanse";
			case 13108:
				return "Still Night";
			case 12083:
				return "Wander";
			case 11828:
				return "Fanfare";
			case 11829:
				return "Scape Soft";
			case 11577:
				return "Mad Eadgar";
			case 10293: // at the Fishing Guild.
				return "Mellow";
			case 11824:
				return "Mudskipper Melody";
			case 11570:
				return "Wandar";
			case 12341:
				return "Barbarianims";
			case 12855:
				return "Crystal Sword";
			case 12344:
				return "Dark";
			case 12599:
				return "Doorways";
			case 12598:
				return "The Trade Parade";
			case 11318:
				return "Ice Melody";
			case 12600:
				return "Scape Wild";
			case 10032: // west yannile:
				return "Big Chords";
			case 10288: // east yanille
				return "Magic Dance";
			case 11826: // Rimmington
				return "Long Way Home";
			case 11825: // rimmigton coast
				return "Attention";
			case 11827: // north rimmigton
				return "Nightfall";
			/*
			 * Camelot and region.
			 */
			case 11062:
			case 10805:
				return "Camelot";
			case 10550:
				return "Talking Forest";
			case 10549:
				return "Lasting";
			case 10548:
				return "Wonderous";
			case 10547:
				return "Baroque";
			case 10291:
			case 10292:
				return "Knightly";
			case 11571: // crafting guild
				return "Miles Away";
			case 11595: // ess mine
				return "Rune Essence";
			case 10294:
				return "Theme";
			case 12349:
				return "Mage Arena";
			case 13365: // digsite
				return "Venture";
			case 13364: // exams center
				return "Medieval";
			case 13878: // canifis
				return "Village";
			case 13877: // canafis south
				return "Waterlogged";
			/*
			 * Mobilies Armies.
			 */
			case 9516:
				return "Command Centre";
			case 12596: // champions guild
				return "Greatness";
			case 10804: // legends guild
				return "Trinity";
			case 11601:
				return "Zaros Zeitgeist"; // zaros godwars
			default:
				return null;
		}
	}
	
	public void addMusic(int musicId) {
		unlockedMusics.add(musicId);
		refreshListConfigs();
		if (unlockedMusics.size() >= 70) {
			player.getEmotesManager().unlockEmote(41);
		}
	}
	
	public void refreshListConfigs() {
		int[] configValues = new int[CONFIG_IDS.length];
		for (int musicId : unlockedMusics) {
			int musicIndex = (int) ClientScriptMap.getMap(1351).getKeyForValue(musicId);
			if (musicIndex == -1) {
				continue;
			}
			int index = getConfigIndex(musicIndex);
			if (index >= CONFIG_IDS.length) {
				continue;
			}
			configValues[index] |= 1 << (musicIndex - (index * 32));
		}
		for (int i = 0; i < CONFIG_IDS.length; i++) {
			if (configValues[i] != 0) {
				player.getPackets().sendConfig(CONFIG_IDS[i], configValues[i]);
			}
		}
	}
	
	public int getConfigIndex(int musicId) {
		return (musicId + 1) / 32;
	}
	
	public void removeFromPlayList(int musicIndex) {
		Integer musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId) && playList.contains(musicId)) {
			playList.remove(musicId);
			if (playListOn) {
				switchPlayListOn();
			} else {
				refreshPlayListConfigs();
			}
		}
	}
	
	public void unlockMusicPlayer() {
		player.getPackets().sendUnlockIComponentOptionSlots(187, 1, 0, ClientScriptMap.getMap(1351).getSize() * 2, 0, 2, 3);
	}
	
	public void init() {
		// unlock music inter all options
		if (playingMusic >= 0) {
			playMusic(playingMusic);
		}
		refreshListConfigs();
		refreshPlayListConfigs();
	}
	
	public boolean musicEnded() {
		return playingMusic != -2 && playingMusicDelay + (180000) < Misc.currentTimeMillis();
	}
	
	public void forcePlayMusic(int musicId) {
		settedMusic = true;
		playMusic(musicId);
	}
	
	public void reset() {
		settedMusic = false;
		player.getMusicManager().checkMusic(RegionManager.getRegion(player.getRegionId()).getMusicId());
	}
	
	public void checkMusic(int requestMusicId) {
		if (playListOn || settedMusic && playingMusicDelay + (180000) >= Misc.currentTimeMillis()) {
			return;
		}
		settedMusic = false;
		if (playingMusic != requestMusicId) {
			playMusic(requestMusicId);
		}
	}
	
	public void playAnotherMusic(int musicIndex) {
		int musicId = ClientScriptMap.getMap(1351).getIntValue(musicIndex);
		if (musicId != -1 && unlockedMusics.contains(musicId)) {
			settedMusic = true;
			if (playListOn) {
				switchPlayListOn();
			}
			playMusic(musicId);
		}
		
	}
	
}
