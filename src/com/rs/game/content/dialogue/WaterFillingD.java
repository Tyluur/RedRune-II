package com.rs.game.content.dialogue;

import com.rs.game.content.action.impl.WaterFillingAction;
import com.rs.game.content.action.impl.WaterFillingAction.Fill;
import com.rs.game.content.SkillsDialogue;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Jul 30, 2014 at 2:36:07 AM.
 */
public class WaterFillingD extends Dialogue {

	private Fill fill;

	@Override
	public void start() {
		this.fill = (Fill) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Choose how many you wish to fill, <br> then click on the item to begin.", player.getInventory().getItems().getNumberOf(fill.getEmpty()), new int[] { fill.getEmpty() }, null);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new WaterFillingAction(fill, SkillsDialogue.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {

	}

}
