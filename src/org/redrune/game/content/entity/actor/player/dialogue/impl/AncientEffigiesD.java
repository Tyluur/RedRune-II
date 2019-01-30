package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.content.entity.item.AncientEffigies;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.SkillConstants;

/**
 * Ancient effifies dialogue handling.
 *
 * @author Raghav/Own4g3 <Raghav_ftw@hotmail.com>
 * @author Gircat <gircat101@gmail.com> Modified on Jul 29, 2014 at 10:58:28 PM.
 */
public class AncientEffigiesD extends Dialogue {
	
	int itemId;
	
	int skill1; // this might needs to be saved
	
	int skill2;
	
	@Override
	public void start() {
		itemId = (Integer) parameters[0];
		sendDialogue(SEND_3_TEXT_INFO, "As you inspect the ancient effigy, you begin to feel a", "strange sensation of the relic searching your mind,", "drawing on your knowledge.");
		int random = Misc.getRandom(7);
		skill1 = AncientEffigies.SKILL_1[random];
		skill2 = AncientEffigies.SKILL_2[random];
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_TEXT_INFO, "Images from your experiences of " + AncientEffigies.getMessage(skill1), "fill your mind.");
			stage = 0;
		} else if (stage == 0) {
			player.getTemporaryAttributes().put("skill1", skill1);
			player.getTemporaryAttributes().put("skill2", skill2);
			sendDialogue(SEND_2_LARGE_OPTIONS, "Choose an image.", "" + SkillConstants.SKILL_NAME[skill1], "" + SkillConstants.SKILL_NAME[skill2]);
			stage = 1;
		} else if (stage == 1 && componentId == 2) {
			if (player.getSkills().getLevel((Integer) player.getTemporaryAttributes().get("skill1")) < AncientEffigies.getRequiredLevel(itemId)) {
				sendDialogue(SEND_3_TEXT_INFO, "The images in your mind fade; the ancient effigy seems", "to desire knowledge of experiences you have not yet", "had.");
				player.getPackets().sendGameMessage("You're required at least " + AncientEffigies.getRequiredLevel(itemId) + " " + SkillConstants.SKILL_NAME[(Integer) player.getTemporaryAttributes().get("skill1")] + " to investigate this ancient effigy.");
			} else {
				player.getTemporaryAttributes().put("skill", skill1);
				sendDialogue(SEND_2_TEXT_INFO, "As you focus on your memories, you can almost hear a", "voice in the back of your mind whispering to you...");
				stage = 2;
			}
		} else if (stage == 1 && componentId == 3) {
			if (player.getSkills().getLevel((Integer) player.getTemporaryAttributes().get("skill2")) < AncientEffigies.getRequiredLevel(itemId)) {
				sendDialogue(SEND_3_TEXT_INFO, "The images in your mind fade; the ancient effigy seems", "to desire knowledge of experiences you have not yet", "had.");
				player.getPackets().sendGameMessage("You're required at least " + AncientEffigies.getRequiredLevel(itemId) + " " + SkillConstants.SKILL_NAME[(Integer) player.getTemporaryAttributes().get("skill1")] + " to investigate this ancient effigy.");
			} else {
				player.getTemporaryAttributes().put("skill", skill2);
				sendDialogue(SEND_2_TEXT_INFO, "As you focus on your memories, you can almost hear a", "voice in the back of your mind whispering to you...");
				stage = 2;
			}
		} else if (stage == 2) {
			player.getPackets().sendGameMessage("You have gained " + AncientEffigies.getExp(itemId) + " " + SkillConstants.SKILL_NAME[(Integer) player.getTemporaryAttributes().get("skill")] + " experience!");
			AncientEffigies.effigyInvestigation(player, itemId);
			sendDialogue(SEND_3_TEXT_INFO, "The ancient effigy glows briefly; it seems changed", "somehow and no longer responds to the same memories", "as before.");
			stage = 3;
		} else if (stage == 3) {
			sendDialogue(SEND_2_TEXT_INFO, "A sudden bolt of inspiration flashes through your mind,", "revealing new insight into your experiences!");
			stage = -2;
		} else {
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
