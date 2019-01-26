package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;

public class FightCavesD extends Dialogue {
	
	public static final int DIALOGUE_WON = 0;
	
	public static final int DIALOGUE_START = 1;
	
	public static final int DIALOGUE_LOST = 2;
	
	int npcId = 2617;
	
	private int dstage = 0;
	
	/**
	 * Starts dialogue,
	 */
	@Override
	public void start() {//
		dstage = (int) parameters[0];
		// if (dstage == DIALOGUE_WON)
		// sendEntityDialogue("TzHaar-Mej-Jal",
		// "You have defeated TzTok-Jad, I am most impressed! Please accept this gift as a reward.",
		// IS_NPC, 2617, 12938); //2617 12936
		// else if (dstage == DIALOGUE_START)
		// sendEntityDialogue("TzHaar-Mej-Jal", "You're on your own now, " +
		// player.getUsername() + ". Prepare to fight for your life!", IS_NPC,
		// 2617, 12944);
		// else if (dstage == DIALOGUE_LOST)
		// sendEntityDialogue("TzHaar-Mej-Jal",
		// "Do not feel bad human, I knew you couldn't do it.", IS_NPC, 2617,
		// 12949);
	}
	
	/**
	 * When it runs, it ends.
	 */
	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}
	
	/**
	 * TODO finish dialogue
	 */
	@Override
	public void finish() {
	
	}
	
}