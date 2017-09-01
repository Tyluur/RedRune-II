package com.rs.game.content.dialogue.impl;

import com.rs.game.content.dialogue.Dialogue;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.world.World;

public final class LevelUp extends Dialogue {

	/*
	 * public static final int[] SKILL_ICON = { 100000000, 400000000, 200000000,
	 * 450000000, 250000000, 500000000, 300000000, 1100000000, 1250000000,
	 * 1300000000, 1050000000, 1200000000, 800000000, 1000000000, 900000000,
	 * 650000000, 600000000, 700000000, 1400000000, 1450000000, 850000000,
	 * 1500000000, 1600000000, 1650000000, 1700000000 };
	 */
	
	public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 37, 37, 37, 37, 37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, 300, 417 };
	
	/*
	 * public static final int[] SKILL_FLASH = { 1, 4, 2, 64, 8, 16, 32, 32768,
	 * 131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128, 512, 524288,
	 * 1048576, 262144, 2097152, 4194304, 8388608, 0, 0 };
	 */
	private int skill;
	
	@Override
	public void start() {
		skill = (Integer) parameters[0];
		int level = player.getSkills().getLevel(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp[" + skill + "]", Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		if (level == 99 || level == 120) {
			player.setNextGraphics(new Graphics(1765));
		}
		player.getInterfaceManager().sendChatBoxInterface(740);
		String name = PlayerSkills.SKILL_NAME[skill];
		player.getPackets().sendIComponentText(740, 0, "Congratulations, you have just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level!");
		player.getPackets().sendIComponentText(740, 1, "You have now reached level " + level + ".");
		player.getPackets().sendGameMessage("You've just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level! You have reached level " + level + ".");
		player.getPackets().sendConfigByFile(4757, getIconValue(skill));
		switchFlash(player, skill, true);
		int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
		if (musicEffect != -1) {
			player.getPackets().sendMusicEffect(musicEffect);
		}
		if (level == 99 || level == 120) {
			World.sendWorldMessage("<col=F20505>News: " + player.getDisplayName() + " has achieved " + level + " " + PlayerSkills.SKILL_NAME[skill] + ".", false);
		}
		if (player.getSkills().getXp(skill) == 200000000) {
			for (Player p : World.getPlayers()) {
				p.getPackets().sendGameMessage("<col=F20505>News: " + player.getDisplayName() + " has just achieved 200 million experience in " + name + ".");
			}
		}
	}
	
	public static int getIconValue(int skill) {
		if (skill == PlayerSkills.ATTACK) {
			return 1;
		}
		if (skill == PlayerSkills.STRENGTH) {
			return 2;
		}
		if (skill == PlayerSkills.RANGE) {
			return 3;
		}
		if (skill == PlayerSkills.MAGIC) {
			return 4;
		}
		if (skill == PlayerSkills.DEFENCE) {
			return 5;
		}
		if (skill == PlayerSkills.HITPOINTS) {
			return 6;
		}
		if (skill == PlayerSkills.PRAYER) {
			return 7;
		}
		if (skill == PlayerSkills.AGILITY) {
			return 8;
		}
		if (skill == PlayerSkills.HERBLORE) {
			return 9;
		}
		if (skill == PlayerSkills.THIEVING) {
			return 10;
		}
		if (skill == PlayerSkills.CRAFTING) {
			return 11;
		}
		if (skill == PlayerSkills.RUNECRAFTING) {
			return 12;
		}
		if (skill == PlayerSkills.MINING) {
			return 13;
		}
		if (skill == PlayerSkills.SMITHING) {
			return 14;
		}
		if (skill == PlayerSkills.FISHING) {
			return 15;
		}
		if (skill == PlayerSkills.COOKING) {
			return 16;
		}
		if (skill == PlayerSkills.FIREMAKING) {
			return 17;
		}
		if (skill == PlayerSkills.WOODCUTTING) {
			return 18;
		}
		if (skill == PlayerSkills.FLETCHING) {
			return 19;
		}
		if (skill == PlayerSkills.SLAYER) {
			return 20;
		}
		if (skill == PlayerSkills.FARMING) {
			return 21;
		}
		if (skill == PlayerSkills.CONSTRUCTION) {
			return 22;
		}
		if (skill == PlayerSkills.SLAYER) {
			return 23;
		}
		if (skill == PlayerSkills.SUMMONING) {
			return 24;
		}
		return 25;
	}
	
	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		if (skill == PlayerSkills.ATTACK) {
			id = 4732;
		} else if (skill == PlayerSkills.STRENGTH) {
			id = 4733;
		} else if (skill == PlayerSkills.DEFENCE) {
			id = 4734;
		} else if (skill == PlayerSkills.RANGE) {
			id = 4735;
		} else if (skill == PlayerSkills.PRAYER) {
			id = 4736;
		} else if (skill == PlayerSkills.MAGIC) {
			id = 4737;
		} else if (skill == PlayerSkills.HITPOINTS) {
			id = 4738;
		} else if (skill == PlayerSkills.AGILITY) {
			id = 4739;
		} else if (skill == PlayerSkills.HERBLORE) {
			id = 4740;
		} else if (skill == PlayerSkills.THIEVING) {
			id = 4741;
		} else if (skill == PlayerSkills.CRAFTING) {
			id = 4742;
		} else if (skill == PlayerSkills.FLETCHING) {
			id = 4743;
		} else if (skill == PlayerSkills.MINING) {
			id = 4744;
		} else if (skill == PlayerSkills.SMITHING) {
			id = 4745;
		} else if (skill == PlayerSkills.FISHING) {
			id = 4746;
		} else if (skill == PlayerSkills.COOKING) {
			id = 4747;
		} else if (skill == PlayerSkills.FIREMAKING) {
			id = 4748;
		} else if (skill == PlayerSkills.WOODCUTTING) {
			id = 4749;
		} else if (skill == PlayerSkills.RUNECRAFTING) {
			id = 4750;
		} else if (skill == PlayerSkills.SLAYER) {
			id = 4751;
		} else if (skill == PlayerSkills.FARMING) {
			id = 4752;
		} else if (skill == PlayerSkills.CONSTRUCTION) {
			id = 4753;
		} else if (skill == PlayerSkills.HUNTER) {
			id = 4754;
		} else if (skill == PlayerSkills.SUMMONING) {
			id = 4755;
		} else {
			id = 7756;
		}
		player.getPackets().sendConfigByFile(id, on ? 1 : 0);
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
