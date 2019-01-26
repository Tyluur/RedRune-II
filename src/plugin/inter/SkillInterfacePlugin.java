package plugin.inter;

import org.redrune.game.content.entity.actor.player.dialogue.impl.LevelUp;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class SkillInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 320) {
			player.stopAll();
			int lvlupSkill = -1;
			int skillMenu = -1;
			switch (componentId) {
				case 200: // Attack
					skillMenu = 1;
					if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 1);
					} else {
						lvlupSkill = 0;
						player.getPackets().sendConfig(1230, 10);
					}
					break;
				case 11: // Strength
					skillMenu = 2;
					if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 2);
					} else {
						lvlupSkill = 2;
						player.getPackets().sendConfig(1230, 20);
					}
					break;
				case 28: // Defence
					skillMenu = 5;
					if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 5);
					} else {
						lvlupSkill = 1;
						player.getPackets().sendConfig(1230, 40);
					}
					break;
				case 52: // Ranged
					skillMenu = 3;
					if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 3);
					} else {
						lvlupSkill = 4;
						player.getPackets().sendConfig(1230, 30);
					}
					break;
				case 76: // Prayer
					if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
						skillMenu = 7;
						player.getPackets().sendConfig(965, 7);
					} else {
						lvlupSkill = 5;
						player.getPackets().sendConfig(1230, 60);
					}
					break;
				case 93: // Magic
					if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
						skillMenu = 4;
						player.getPackets().sendConfig(965, 4);
					} else {
						lvlupSkill = 6;
						player.getPackets().sendConfig(1230, 33);
					}
					break;
				case 110: // Runecrafting
					if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
						skillMenu = 12;
						player.getPackets().sendConfig(965, 12);
					} else {
						lvlupSkill = 20;
						player.getPackets().sendConfig(1230, 100);
					}
					break;
				case 134: // Construction
					skillMenu = 22;
					if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 22);
					} else {
						lvlupSkill = 21;
						player.getPackets().sendConfig(1230, 698);
					}
					break;
				case 193: // Hitpoints
					skillMenu = 6;
					if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 6);
					} else {
						lvlupSkill = 3;
						player.getPackets().sendConfig(1230, 50);
					}
					break;
				case 19: // Agility
					skillMenu = 8;
					if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 8);
					} else {
						lvlupSkill = 16;
						player.getPackets().sendConfig(1230, 65);
					}
					break;
				case 36: // Herblore
					skillMenu = 9;
					if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 9);
					} else {
						lvlupSkill = 15;
						player.getPackets().sendConfig(1230, 75);
					}
					break;
				case 60: // Thieving
					skillMenu = 10;
					if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 10);
					} else {
						lvlupSkill = 17;
						player.getPackets().sendConfig(1230, 80);
					}
					break;
				case 84: // Crafting
					skillMenu = 11;
					if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 11);
					} else {
						lvlupSkill = 12;
						player.getPackets().sendConfig(1230, 90);
					}
					break;
				case 101: // Fletching
					skillMenu = 19;
					if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 19);
					} else {
						lvlupSkill = 9;
						player.getPackets().sendConfig(1230, 665);
					}
					break;
				case 118: // Slayer
					skillMenu = 20;
					if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 20);
					} else {
						lvlupSkill = 18;
						player.getPackets().sendConfig(1230, 673);
					}
					break;
				case 142: // Hunter
					skillMenu = 23;
					if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 23);
					} else {
						lvlupSkill = 22;
						player.getPackets().sendConfig(1230, 689);
					}
					break;
				case 186: // Mining
					skillMenu = 13;
					if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 13);
					} else {
						lvlupSkill = 14;
						player.getPackets().sendConfig(1230, 110);
					}
					break;
				case 179: // Smithing
					skillMenu = 14;
					if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 14);
					} else {
						lvlupSkill = 13;
						player.getPackets().sendConfig(1230, 115);
					}
					break;
				case 44: // Fishing
					skillMenu = 15;
					if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 15);
					} else {
						lvlupSkill = 10;
						player.getPackets().sendConfig(1230, 120);
					}
					break;
				case 68: // Cooking
					skillMenu = 16;
					if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 16);
					} else {
						lvlupSkill = 7;
						player.getPackets().sendConfig(1230, 641);
					}
					break;
				case 172: // Firemaking
					skillMenu = 17;
					if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 17);
					} else {
						lvlupSkill = 11;
						player.getPackets().sendConfig(1230, 649);
					}
					break;
				case 165: // Woodcutting
					skillMenu = 18;
					if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 18);
					} else {
						lvlupSkill = 8;
						player.getPackets().sendConfig(1230, 660);
					}
					break;
				case 126: // Farming
					skillMenu = 21;
					if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 21);
					} else {
						lvlupSkill = 19;
						player.getPackets().sendConfig(1230, 681);
					}
					break;
				case 150: // Summoning
					skillMenu = 24;
					if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 24);
					} else {
						lvlupSkill = 23;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				case 158: // Dung
					skillMenu = 25;
					if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 25);
					} else {
						lvlupSkill = 24;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
			}
			
			player.getInterfaceManager().sendInterface(lvlupSkill != -1 ? 741 : 499);
			if (lvlupSkill != -1) {
				LevelUp.switchFlash(player, lvlupSkill, false);
			}
			if (skillMenu != -1) {
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
			}
		} else if (interfaceId == 499) {
			int skillMenu = -1;
			if (player.getTemporaryAttributtes().get("skillMenu") != null) {
				skillMenu = (Integer) player.getTemporaryAttributtes().get("skillMenu");
			}
			switch (componentId) {
				case 10:
					player.getPackets().sendConfig(965, skillMenu);
					break;
				case 11:
					player.getPackets().sendConfig(965, 1024 + skillMenu);
					break;
				case 12:
					player.getPackets().sendConfig(965, 2048 + skillMenu);
					break;
				case 13:
					player.getPackets().sendConfig(965, 3072 + skillMenu);
					break;
				case 14:
					player.getPackets().sendConfig(965, 4096 + skillMenu);
					break;
				case 15:
					player.getPackets().sendConfig(965, 5120 + skillMenu);
					break;
				case 16:
					player.getPackets().sendConfig(965, 6144 + skillMenu);
					break;
				case 17:
					player.getPackets().sendConfig(965, 7168 + skillMenu);
					break;
				case 18:
					player.getPackets().sendConfig(965, 8192 + skillMenu);
					break;
				case 19:
					player.getPackets().sendConfig(965, 9216 + skillMenu);
					break;
				case 20:
					player.getPackets().sendConfig(965, 10240 + skillMenu);
					break;
				case 21:
					player.getPackets().sendConfig(965, 11264 + skillMenu);
					break;
				case 22:
					player.getPackets().sendConfig(965, 12288 + skillMenu);
					break;
				case 23:
					player.getPackets().sendConfig(965, 13312 + skillMenu);
					break;
				case 29: // close inter
					player.stopAll();
					break;
			}
			
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(320, 499);
	}
}
