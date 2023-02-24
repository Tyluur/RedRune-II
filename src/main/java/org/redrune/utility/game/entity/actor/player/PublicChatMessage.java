package org.redrune.utility.game.entity.actor.player;

public class PublicChatMessage extends ChatMessage {

    private final int effects;

    public PublicChatMessage(String message, int effects) {
        super(message);
        this.effects = effects;
    }

    public int getEffects() {
        return effects;
    }

}
