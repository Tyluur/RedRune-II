package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.functions.Misc;

public class ChristmasCrackerD extends Dialogue {
	
	final static Item[] PARTYHATS = { new Item(1038, 1), new Item(1040, 1), new Item(1042, 1), new Item(1044, 1), new Item(1046, 1), new Item(1048, 1) };
	
	final static Item[] EXTRA_ITEMS = { new Item(1969, 1), new Item(2355, Misc.random(1, 2)), new Item(1217, 1), new Item(1635, 1), new Item(441, 5), new Item(441, 10), new Item(1973, 1), new Item(1718, 1), new Item(950, 1), new Item(563, 1), new Item(1987, 1) };
	
	private Player usedOn;
	
	@Override
	public void start() {
		usedOn = (Player) parameters[0];
		sendOptions("If you pull the cracker<br>it will be destroyed.", "That's okay, I might get a party hat!", "Stop. I want to keep my cracker.");
	}
	
	@Override
	public void run(int interfcaceId, int componentId) {
		switch (componentId) {
			case 1:
				player.getPackets().sendMessage("You pull a Christmas cracker...");
				player.getInventory().deleteItem(962, 1);
				usedOn.faceActor(player);
				player.setNextAnimation(new Animation(15153));
				usedOn.setNextAnimation(new Animation(15153));
				if (Misc.random(100) <= 50) {
					usedOn.setNextGraphics(new Graphics(176));
					player.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
					player.getInventory().addItem(getPartyhats());
					player.getInventory().addItem(getExtraItems());
				} else {
					usedOn.setNextGraphics(new Graphics(176));
					usedOn.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
					usedOn.getInventory().addItem(getPartyhats());
					usedOn.getInventory().addItem(getExtraItems());
					player.getPackets().sendMessage("The person with whom you pull the cracker gets the prize.");
				}
				end();
				break;
			default:
				end();
				break;
		}
	}
	
	static Item getPartyhats() {
		return PARTYHATS[(int) (Math.random() * PARTYHATS.length)];
	}
	
	static Item getExtraItems() {
		return EXTRA_ITEMS[(int) (Math.random() * EXTRA_ITEMS.length)];
	}
	
	@Override
	public void finish() {
	}
	
}