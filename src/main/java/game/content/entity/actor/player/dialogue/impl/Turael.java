package game.content.entity.actor.player.dialogue.impl;

import cache.codec.loaders.NPCDefinitions;
import game.content.entity.actor.player.dialogue.Dialogue;
import game.content.entity.actor.player.skills.slayer.Slayer;
import game.content.entity.actor.player.skills.slayer.SlayerMaster;

public class Turael extends Dialogue {
	
	private int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue((short) 241, new String[] { NPCDefinitions.getNPCDefinitions(npcId).getName(), "Hello, brave warrior. What would you like?" }, (byte) 1, npcId, 9827);
	}
	
	/**
	 * Runs the dialogue
	 */
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue((short) 236, "What would you like to say?", "I would like a slayer task.", "What is my current slayer task?");
		} else if (stage == 0) {
			if (componentId == 1) {
				if (player.getAttributes().getSlayerTask().getTaskMonstersLeft() < 1) {
					Slayer.assignTask(player, SlayerMaster.TURAEL);
					sendEntityDialogue((short) 241, new String[] { NPCDefinitions.getNPCDefinitions(8461).getName(), "Your slayer task is to kill " + player.getAttributes().getSlayerTask().getTaskMonstersLeft() + " " + player.getAttributes().getSlayerTask().getCurrentTask().simpleName }, (byte) 1, 8461, 9827);
				} else {
					sendEntityDialogue((short) 243, new String[] { NPCDefinitions.getNPCDefinitions(8461).getName(), "You already have a slayer task!", "You need to kill " + player.getAttributes().getSlayerTask().getTaskMonstersLeft() + " " + player.getAttributes().getSlayerTask().getCurrentTask().simpleName, "Would you like a new slayer task?" }, (byte) 1, 8461, 9827);
				}
				stage = 1;
			} else if (componentId == 2) {
				if (player.getAttributes().getSlayerTask().getTaskMonstersLeft() > 0) {
					sendEntityDialogue((short) 242, new String[] { NPCDefinitions.getNPCDefinitions(8461).getName(), "You have a short memory, don't you?", "You need to kill " + player.getAttributes().getSlayerTask().getTaskMonstersLeft() + " " + player.getAttributes().getSlayerTask().getCurrentTask() }, (byte) 1, 8461, 9827);
				} else {
					sendEntityDialogue((short) 241, new String[] { NPCDefinitions.getNPCDefinitions(8461).getName(), "Foolish warrior. You don't have a slayer task!" }, (byte) 1, 8461, 9827);
				}
				stage = -1;
			} else if (componentId == 3) {
				end();
			} else if (componentId == 4) {
				stage = 1;
			} else {
				end();
			}
		} else if (stage == 1) {
			sendDialogue((short) 236, "Would you like a new Slayer task?", "Yes", "No");
			stage = 2;
		} else if (stage == 2) {
			player.getPackets().sendMessage("" + componentId);
			if (componentId == 1) {
				Slayer.assignTask(player, SlayerMaster.TURAEL);
				sendEntityDialogue((short) 241, new String[] { NPCDefinitions.getNPCDefinitions(8461).getName(), "Your slayer task is to kill " + player.getAttributes().getSlayerTask().getTaskMonstersLeft() + " " + player.getAttributes().getSlayerTask().getCurrentTask().simpleName }, (byte) 1, 8461, 9827);
			} else {
				end();
				stage = -1;
			}
			stage = 3;
		} else if (stage == 3) {
			if (componentId == 1) {
				stage = 4;
			} else if (componentId == 2) {
				stage = 5;
			} else {
				end();
			}
		} else if (stage == 4) {
			stage = -2;
		} else if (stage == 5) {
			stage = -2;
		} else {
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
}