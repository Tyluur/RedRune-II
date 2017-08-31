package com.rs.game.content.dialogue.varrock;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.utility.game.player.ShopsHandler;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Aug 12, 2014 at 10:43:18 PM.
 */

public class Horvik extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Hello young warrior. You must be interested in", "some of my fine armour. What would you like to do?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendDialogue(SEND_4_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE, "Who are you?", "What are you selling?", "What kind of name is Horvik?", "Nevermind.");
			break;
		case 0:
			if (componentId == 1) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Who are you?" }, IS_PLAYER, player.getIndex(), 9827);
				break;
			} else if (componentId == 2) {
				stage = 3;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "What are you selling?" }, IS_PLAYER, player.getIndex(), 9827);
				break;
			} else if (componentId == 3) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "What kind of name is Horvik?" }, IS_PLAYER, player.getIndex(), 9827);
				break;
			} else if (componentId == 4) {
				stage = 100;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Nevermind." }, IS_PLAYER, player.getIndex(), 9827);
				break;
			}
		case 1:
			stage = 2;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "My name is Horvik. I wonder around this area trying", "to sell some of the most affordable armour any player", "can buy when they need their first set. It is my job", "to protect you from the dangerous world." }, IS_NPC, npcId, 9827);
			break;
		case 2:
			stage = 100;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { player.getDisplayName(), "Oh, fair enough.", "Thank you." }, IS_PLAYER, player.getIndex(), 9827);
			break;
		case 3:
			end();
			ShopsHandler.openShop(player, 4);
			break;
		case 4:
			stage = 5;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Are you kidding me?" }, IS_NPC, npcId, 9827);
			break;
		case 5:
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "What kind of name is " + player.getDisplayName() + "?" }, IS_NPC, npcId, 9827);
			break;
		case 100:
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
