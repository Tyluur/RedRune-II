package com.rs.game.content.dialogue.impl;

import com.rs.game.content.dialogue.Dialogue;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;

import static com.rs.utility.constants.SkillConstants.*;

public final class LevelUp extends Dialogue {
	
	private static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 37, 37, 37, 37, 37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, 300, 417 };
	
	private int skill;
	
	@Override
	public void start() {
		skill = (Integer) parameters[0];
		int level = player.getSkills().getLevel(skill);
		String name = SKILL_NAME[skill];
		player.putAttribute("leveledUp", skill);
		player.putAttribute("leveledUp[" + skill + "]", Boolean.TRUE);
		
		player.setNextGraphics(new Graphics(199));
		sendChatbox(level, name);
		playMusic();
		sendAnnouncement(level, name);
		switchFlash(player, skill, true);
	}
	
	private void sendChatbox(int level, String name) {
		player.getInterfaceManager().sendChatBoxInterface(740);
		player.getPackets().sendIComponentText(740, 0, "Congratulations, you have just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level!");
		player.getPackets().sendIComponentText(740, 1, "You have now reached level " + level + ".");
		player.getPackets().sendGameMessage("You've just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level! You have reached level " + level + ".");
		player.getPackets().sendConfigByFile(4757, getIconValue(skill));
	}
	
	private void playMusic() {
		int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
		if (musicEffect != -1) {
			player.getPackets().sendMusicEffect(musicEffect);
		}
	}
	
	private void sendAnnouncement(int level, String name) {
		if (level == 99 || level == 120) {
			World.sendWorldMessage("<col=F20505>News: " + player.getDisplayName() + " has achieved " + level + " " + SKILL_NAME[skill] + ".", false);
		}
		if (player.getSkills().getXp(skill) == 200_000_000) {
			for (Player p : World.getPlayers()) {
				p.getPackets().sendGameMessage("<col=F20505>News: " + player.getDisplayName() + " has just achieved 200 million experience in " + name + ".");
			}
		}
	}
	
	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		switch (skill) {
			case ATTACK:
				id = 4732;
				break;
			case STRENGTH:
				id = 4733;
				break;
			case DEFENCE:
				id = 4734;
				break;
			case RANGE:
				id = 4735;
				break;
			case PRAYER:
				id = 4736;
				break;
			case MAGIC:
				id = 4737;
				break;
			case HITPOINTS:
				id = 4738;
				break;
			case AGILITY:
				id = 4739;
				break;
			case HERBLORE:
				id = 4740;
				break;
			case THIEVING:
				id = 4741;
				break;
			case CRAFTING:
				id = 4742;
				break;
			case FLETCHING:
				id = 4743;
				break;
			case MINING:
				id = 4744;
				break;
			case SMITHING:
				id = 4745;
				break;
			case FISHING:
				id = 4746;
				break;
			case COOKING:
				id = 4747;
				break;
			case FIREMAKING:
				id = 4748;
				break;
			case WOODCUTTING:
				id = 4749;
				break;
			case RUNECRAFTING:
				id = 4750;
				break;
			case SLAYER:
				id = 4751;
				break;
			case FARMING:
				id = 4752;
				break;
			case CONSTRUCTION:
				id = 4753;
				break;
			case HUNTER:
				id = 4754;
				break;
			case SUMMONING:
				id = 4755;
				break;
			default:
				id = 7756;
				break;
		}
		player.getPackets().sendConfigByFile(id, on ? 1 : 0);
	}
	
	private static int getIconValue(int skill) {
		switch (skill) {
			case ATTACK:
				return 1;
			case STRENGTH:
				return 2;
			case RANGE:
				return 3;
			case MAGIC:
				return 4;
			case DEFENCE:
				return 5;
			case HITPOINTS:
				return 6;
			case PRAYER:
				return 7;
			case AGILITY:
				return 8;
			case HERBLORE:
				return 9;
			case THIEVING:
				return 10;
			case CRAFTING:
				return 11;
			case RUNECRAFTING:
				return 12;
			case MINING:
				return 13;
			case SMITHING:
				return 14;
			case FISHING:
				return 15;
			case COOKING:
				return 16;
			case FIREMAKING:
				return 17;
			case WOODCUTTING:
				return 18;
			case FLETCHING:
				return 19;
			case SLAYER:
				return 20;
			case FARMING:
				return 21;
			case CONSTRUCTION:
				return 22;
			case SUMMONING:
				return 24;
		}
		return 25;
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	@Override
	public void finish() {
		// player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes
		// random flash
	}
}
