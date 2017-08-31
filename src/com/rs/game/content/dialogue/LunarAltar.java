package com.rs.game.content.dialogue;

public class LunarAltar extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Change spellbooks?",
				"Yes, replace my spellbook.", "Never mind.");
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1) {
			if (player.getCombatDefinitions().getSpellBook() != 430) {
				sendDialogue(SEND_2_TEXT_CHAT, "",
						"Your mind clears and you switch",
						"back to the lunar spellbook.");
				player.getCombatDefinitions().setSpellBook(2);
			} else {
				sendDialogue(SEND_2_TEXT_CHAT, "",
						"Your mind clears and you switch",
						"back to the normal spellbook.");
				player.getCombatDefinitions().setSpellBook(0);
			}
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
