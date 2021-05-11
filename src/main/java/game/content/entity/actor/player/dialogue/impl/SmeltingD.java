package game.content.entity.actor.player.dialogue.impl;

import game.content.entity.actor.player.dialogue.Dialogue;
import game.content.entity.actor.player.dialogue.impl.SkillsDialogue.ItemNameFilter;
import game.content.entity.actor.player.skills.smithing.Smelting;
import game.content.entity.actor.player.skills.smithing.Smelting.SmeltingBar;
import game.entity.object.WorldObject;
import utility.constants.SkillConstants;

public class SmeltingD extends Dialogue {
	
	private WorldObject object;
	
	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		int[] ids = new int[SmeltingBar.values().length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SmeltingBar.values()[i].getProducedBar().getId();
		}
		SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.", 28, ids, new ItemNameFilter() {
			int count = 0;
			
			@Override
			public String rename(String name) {
				SmeltingBar bar = SmeltingBar.values()[count++];
				if (player.getSkills().getLevel(SkillConstants.SMITHING) < bar.getLevelRequired()) {
					name = "<col=ff0000>" + name + "<br><col=ff0000>Level " + bar.getLevelRequired();
				}
				return name;
				
			}
		});
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		player.getActionManager().setAction(new Smelting(SkillsDialogue.getItemSlot(componentId), object, SkillsDialogue.getQuantity(player)));
		end();
	}
	
	@Override
	public void finish() {
	}
}
