package org.redrune.game.content.dialogue.impl.subscribe;

import org.redrune.game.GameConstants;
import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.content.dialogue.DialogueSubscription;
import org.redrune.game.content.dialogue.messages.NPCDialogueMessage;
import org.redrune.game.content.dialogue.messages.OptionDialogueMessage;
import org.redrune.game.content.dialogue.messages.PlayerDialogueMessage;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
@DialogueSubscription(names = { "Banker" })
public class BankerNPCDialogue extends Dialogue {
	
	@Override
	public void constructMessages(Player player) {
		construct(new NPCDialogueMessage(this.chattingId = parameter(0), QUESTIONS, "Good day, How may I help you?"));
		construct(new OptionDialogueMessage("What would you like to say?", new String[] { "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to see my collection box.", "What is this place?" }, () -> {
			player.getBank().openBank();
			end(player);
		}, () -> {
			construct(new PlayerDialogueMessage(NORMAL, "I like to set a bank pin."));
			construct(new NPCDialogueMessage(chattingId, NORMAL, "Sorry, bank pins are not yet ready."));
			construct(new PlayerDialogueMessage(NORMAL, "I will return another day."));
		}, () -> {
			construct(new PlayerDialogueMessage(NORMAL, "I would like to view my collection box."));
			construct(new NPCDialogueMessage(chattingId, NORMAL, "Sorry, the grand exchange is not ready."));
		}, () -> {
			construct(new PlayerDialogueMessage(NORMAL, "What is this place?"));
			construct(new NPCDialogueMessage(chattingId, NORMAL, "This is a branch of the Bank of " + GameConstants.SERVER_NAME + ". We have", "branches in many towns."));
			construct(new OptionDialogueMessage("What would you like to say?", new String[] { "And what do you do?", "Didnt you used to be called the Bank of Varrock?" }, () -> {
				construct(new PlayerDialogueMessage(NORMAL, "And what do you do?"));
				construct(new NPCDialogueMessage(chattingId, NORMAL, "We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe."));
			}, () -> {
				construct(new PlayerDialogueMessage(NORMAL, "Didnt you used to be called the Bank of Varrock?"));
				construct(new NPCDialogueMessage(chattingId, NORMAL, "Yes we did, but people kept on coming into our", "signs were wrong. They acted as if we didn't know", "what town we were in or something."));
			}));
		}));
	}
	
}