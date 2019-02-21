package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.utility.constants.GameConstants;

public class Banker extends Dialogue {
	
	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, NORMAL, "Good day, how may I help you?");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptions("What would you like to say?", "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to see my collection box.", "What is this place?");
		} else if (stage == 0) {
			if (componentId == first) {
				player.getBank().openBank();
				end();
			} else if (componentId == second) {
				player.getBank().openSetPin();
				end();
			} else if (componentId == third) {
				end();
			} else if (componentId == fourth) {
				stage = 1;
				player(9827, "What is this place?");
			} else {
				end();
			}
		} else if (stage == 1) {
			stage = 2;
			sendNPCDialogue(npcId, NORMAL, "This is a branch of the Bank of " + GameConstants.SERVER_NAME + ". We have", "branches in many towns.");
		} else if (stage == 2) {
			stage = 3;
			sendOptions("What would you like to say?", "And what do you do?", "Didn't you used to be called the Bank of Varrock?");
		} else if (stage == 3) {
			if (componentId == 1) {
				sendPlayerDialogue(NORMAL, "And what do you do?");
				stage = 4;
			} else if (componentId == 2) {
				sendPlayerDialogue(NORMAL, "Didn't you used to be called the Bank of Varrock?");
				stage = 5;
			} else {
				end();
			}
		} else if (stage == 4) {
			stage = -2;
			sendNPCDialogue(npcId, NORMAL, "We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe.");
		} else if (stage == 5) {
			sendNPCDialogue(npcId, NORMAL, "Yes we did, but people kept on coming into our", "signs were wrong. They acted as if we didn't know", "what town we were in or something.");
			stage = -2;
		} else {
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
