package org.redrune.utility.game.entity.actor.player;

import org.redrune.utility.functions.Misc;

public class ChatMessage {
	
	private final String message;
	
	private String filteredMessage;
	
	public ChatMessage(String message) {
		if (!(this instanceof QuickChatMessage)) {
			filteredMessage = Censor.getFilteredMessage(message);
			this.message = Misc.fixChatMessage(message);
		} else {
			this.message = message;
		}
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getMessage(boolean filtered) {
		if (this instanceof QuickChatMessage) {
			return message;
		}
		return filtered ? filteredMessage : message;
	}
	
}