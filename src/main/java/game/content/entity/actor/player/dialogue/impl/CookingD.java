package game.content.entity.actor.player.dialogue.impl;

import game.content.entity.actor.player.dialogue.Dialogue;
import game.content.entity.actor.player.skills.cooking.Cooking;
import game.content.entity.actor.player.skills.cooking.Cooking.Cookables;
import game.entity.object.WorldObject;

public class CookingD extends Dialogue {
	
	private Cookables cooking;
	
	private WorldObject object;
	
	@Override
	public void start() {
		this.cooking = (Cookables) parameters[0];
		this.object = (WorldObject) parameters[1];
		
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.COOK, "Choose how many you wish to cook,<br>then click on the item to begin.", player.getInventory().getItems().getNumberOf(cooking.getRawItem()), new int[] { cooking.getProduct().getId() }, null);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new Cooking(object, cooking.getRawItem(), SkillsDialogue.getQuantity(player)));
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
