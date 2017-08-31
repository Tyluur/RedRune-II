package com.rs.game.content.dialogue.varrock;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.PlayerLook;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.utility.game.player.ShopsHandler;


public class Thessalia extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
				"Would you like to buy any fine clothes?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch(stage) {
		case -1:
			stage = 0;
			sendDialogue(SEND_2_OPTIONS, "Select an Option", "What do you have?", "No, thank you.");
			break;
		case 0:
			if(componentId == OPTION_2) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"No, thank you." }, IS_PLAYER, player.getIndex(), 9827);
			}else {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"What do you have?" }, IS_PLAYER, player.getIndex(), 9827);
			}
			break;
		case 1:
			stage = -2;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, please return if you change your mind."
					}, IS_NPC, npcId, 9827);
			break;
		case 2:
			stage = 3;
			sendEntityDialogue(SEND_3_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, I have a number of fine pieces of clothing on",
					"sale or, if you prefer, I can offer you an exclusive,",
					"total clothing makeover?"
					}, IS_NPC, npcId, 9827);
			break;
		case 3:
			stage = 4;
			sendDialogue(SEND_2_OPTIONS, "Select an Option", "Tell me more about this makeover.", "I'd just like to buy some clothes.");
			break;
		case 4:
			if(componentId == OPTION_2) {
				ShopsHandler.openShop(player, 18);
				end();
			}else {
				stage = 5;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"Tell me more about this makeover." }, IS_PLAYER, player.getIndex(), 9827);
			}
			break;
		case 5:
			stage = 6;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Certainly!"
					}, IS_NPC, npcId, 9827);
			break;
		case 6:
			stage = 7;
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Here at Thessalia's Fine Clothing Boutique we offer a",
					"unique server, where we totally revamp your outfit to your",
					"Choosing. Tired of always wearing the same outfit, day-in,",
					"day-out? Then this is the service for you!"
					}, IS_NPC, npcId, 9827);
			break;
		case 7:
			stage = 8;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"So, what do you say? Interested?"
					}, IS_NPC, npcId, 9827);
			ShopsHandler.openShop(player, 18);
			break;
		case 8:
			stage = 9;
			sendDialogue(SEND_3_OPTIONS, "Select an Option", "I'd like to change my outfit, please.", "I'd just like to buy some cloths.", "No, thank you.");
			break;
		case 9:
			if(componentId == OPTION_3) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"No, thank you." }, IS_PLAYER, player.getIndex(), 9827);
			}else if(componentId == OPTION_2) {
				ShopsHandler.openShop(player, 18);
				end();
			}else {
				stage = 10;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"I'd like to change my outfit, please." }, IS_PLAYER, player.getIndex(), 9827);
			}
			break;
		case 10:
			if(player.getEquipment().wearingArmour()) {
				stage = -2;
				sendEntityDialogue(SEND_2_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"You can't try them on while wearing armour. Take it off",
						"and speak to me again."
						}, IS_NPC, npcId, 9827);
			}else{
				stage = 11;
				sendEntityDialogue(SEND_2_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Wonderful. Feel free to try on some items and see",
						"if there's anything you would like."
						}, IS_NPC, npcId, 9827);
			}
			break;
		case 11:
			stage = 12;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
			"Okay, thanks." }, IS_PLAYER, player.getIndex(), 9827);
			break;
		case 12:
			PlayerLook.openThessaliasMakeOver(player);
			end();
			break;
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
