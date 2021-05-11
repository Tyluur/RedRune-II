package game.content.entity.actor.player.dialogue.impl;

import cache.codec.loaders.NPCDefinitions;
import game.content.entity.actor.player.dialogue.Dialogue;
import game.content.entity.actor.player.skills.slayer.Slayer.Master;
import game.content.entity.actor.player.skills.slayer.Slayer.SlayerTask;
import utility.constants.GameConstants;

public class EnchantedGemDialouge extends Dialogue {
	
	@Override
	public void start() {
		Master master = (Master) player.getTemporaryAttributes().get("SlayerMaster");
		if (master == null) {
			player.getTemporaryAttributes().put("SlayerMaster", Master.SPRIA);
			master = (Master) player.getTemporaryAttributes().get("SlayerMaster");
		}
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).getName(), "Good day, How may I help you?" }, IS_NPC, master.getMaster(), 9827);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		Master master = (Master) player.getTemporaryAttributes().get("SlayerMaster");
		if (stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_4_OPTIONS, new String[] { DEFAULT_OPTION, "How many monsters do I have left?", "Where are you located in the land of " + GameConstants.SERVER_NAME + "?", "Give me a tip.", "Nothing, Nevermind." }, IS_PLAYER, player.getIndex(), 9827);
		} else if (stage == 0) {
			if (componentId == 1) {
				SlayerTask task = (SlayerTask) player.getTemporaryAttributes().get("SlayerTask");
				if (task != null) {
					
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).getName(), "You're current assigned to kill " + task.getName().toLowerCase() + " only " + task.getAmount() + " more to go." }, IS_NPC, master.getMaster(), 9827);
				} else {
					sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).getName(), "You currently don't have a task, see me to get one." }, IS_NPC, master.getMaster(), 9827);
				}
				stage = -1;
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).getName(), "" + master.getDialouge() + "." }, IS_NPC, master.getMaster(), 9827);
				stage = -1;
			} else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(master.getMaster()).getName(), "I currently dont have any tips for you now." }, IS_NPC, master.getMaster(), 9827);
				stage = -1;
			} else {
				end();
			}
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
