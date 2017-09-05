package com.rs.game.content.skills.slayer;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.SkillConstants;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Slayer is a members-only skill that allows players to kill monsters which are often otherwise immune to damage.
 * Slayer was introduced on 26 January 2005. Players get a Slayer task from one of seven Slayer Masters, and players
 * gain Slayer experience for killing monsters that they are assigned.
 *
 * @author Emperial
 */
public class SlayerTask implements Serializable {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -7184740247844324413L;
	
	/**
	 * The players current assigned task
	 */
	@Getter
	@Setter
	private SlayerTasks currentTask;
	
	/**
	 * The monsters left.
	 */
	private int monstersLeft = -1;
	
	/**
	 * The monsters left to kill
	 */
	public int getTaskMonstersLeft() {
		return monstersLeft;
	}
	
	/**
	 * Sets monsters left to kill
	 */
	public void setMonstersLeft(int i) {
		monstersLeft = i;
	}
	
	/**
	 * Called on npc death if part of task.
	 */
	public void onMonsterDeath(Player player, NPC n) {
		player.getSkills().addXp(SkillConstants.SLAYER, n.getCombatDefinitions().getHitpoints() / 10);
		monstersLeft--;
		int[] checkpoints = new int[] { 2, 3, 4, 5, 10, 15, 25, 50, 75, 100, 125, 150 };
		for (int i : checkpoints) {
			if (monstersLeft == i) {
				player.getPackets().sendGameMessage("You're doing great, Only " + monstersLeft + " " + getCurrentTask().simpleName + " left to slay.");
			}
		}
		if (monstersLeft < 1) {
			player.getPackets().sendGameMessage("You have finished your slayer task, talk to a slayer master for a new one.");
		}
	}
	
}
