package org.redrune.game.entity.actor.player.link;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler;
import org.redrune.game.entity.actor.player.Player;

public class DialogueManager {

    private final Player player;

    private Dialogue lastDialogue;

    public DialogueManager(Player player) {
        this.player = player;
    }

    public void startDialogue(Class<?> clazz, Object... parameters) {
        startDialogue(clazz.getSimpleName(), parameters);
    }

    public void startDialogue(Object key, Object... parameters) {
        if (!player.getControllerManager().useDialogueScript(key)) {
            return;
        }
        if (lastDialogue != null) {
            lastDialogue.finish();
        }
        lastDialogue = DialogueHandler.getDialogue(key);
        if (lastDialogue == null) {
            return;
        }
        lastDialogue.parameters = parameters;
        lastDialogue.setPlayer(player);
        lastDialogue.start();
    }

    public void continueDialogue(int interfaceId, int componentId) {
        if (lastDialogue == null) {
            return;
        }
        if (lastDialogue.getStage() == -2) {
            finishDialogue();
            return;
        }
        lastDialogue.run(interfaceId, componentId);
    }

    public void finishDialogue() {
        if (lastDialogue == null) {
            return;
        }
        lastDialogue.finish();
        lastDialogue = null;
        if (player.getInterfaceManager().containsChatBoxInter()) {
            player.getInterfaceManager().closeChatBoxInterface();
        }
    }

    public void updateComponents(int interfaceId) {
        if (lastDialogue == null) {
            return;
        }
        lastDialogue.updateComponents(interfaceId);
    }

}
