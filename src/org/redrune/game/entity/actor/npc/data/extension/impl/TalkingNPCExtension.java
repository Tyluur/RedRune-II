package org.redrune.game.entity.actor.npc.data.extension.impl;

import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.data.extension.NPCExtension;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class TalkingNPCExtension extends NPCExtension {
	
	/**
	 * The message to speak
	 */
	private final String message;
	
	/**
	 * The delay between each speaking time
	 */
	private final long delay;
	
	/**
	 * The last time the chat was ran
	 */
	private long lastRan;
	
	public TalkingNPCExtension(String message, long delay) {
		this.message = message;
		this.delay = delay;
	}
	
	@Override
	public void process(NPC npc) {
		if (Misc.timeHasPassed(lastRan, delay)) {
			npc.setNextForceTalk(new ForceTalk(message));
			lastRan = System.currentTimeMillis();
		}
	}
}
