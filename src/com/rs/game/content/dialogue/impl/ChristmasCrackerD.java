package com.rs.game.content.dialogue.impl;

import com.rs.game.content.dialogue.Dialogue;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.utility.Misc;

public class ChristmasCrackerD extends Dialogue {
	
	final static Item[] PARTYHATS = { new Item(1038, 1), new Item(1040, 1), new Item(1042, 1), new Item(1044, 1), new Item(1046, 1), new Item(1048, 1) };
	
	final static Item[] EXTRA_ITEMS = { new Item(1969, 1), new Item(2355, Misc.random(1, 2)), new Item(1217, 1), new Item(1635, 1), new Item(441, 5), new Item(441, 10), new Item(1973, 1), new Item(1718, 1), new Item(950, 1), new Item(563, 1), new Item(1987, 1) };
	
	private Player usedOn;
	
	@Override
	public void start() {
		usedOn = (Player) parameters[0];
		sendDialogue(SEND_3_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE, "If you pull the cracker, it will be destroyed.", "That's okay, I might get a party hat!", "Stop. I want to keep my cracker.");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
			case OPTION_1:
				player.getPackets().sendGameMessage("You pull a Christmas cracker...");
				player.getInventory().deleteItem(962, 1);
				usedOn.faceEntity(player);
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
					player.getPackets().sendGameMessage("The person with whom you pull the cracker gets the prize.");
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