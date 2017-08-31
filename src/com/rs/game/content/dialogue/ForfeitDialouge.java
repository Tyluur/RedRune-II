package com.rs.game.content.dialogue;

public class ForfeitDialouge extends Dialogue {

	@Override
	public void start() {
		this.sendDialogue(SEND_2_OPTIONS, "Forfeit Duel?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
		case 1:
			if (!player.getDuelConfigurations().getRule(7)) {
				player.getDuelConfigurations().endDuel(player, false, false);
				player.getDuelConfigurations().endDuel(
						player.getDuelConfigurations().getOther(player), false,
						false);
			} else {
				sendDialogue(SEND_1_TEXT_INFO,
						"You can't forfeit during this duel.");
			}
			break;
		case 2:
			break;
		}
		end();
	}

	@Override
	public void finish() {

	}

}
