package game.content.entity.item;

import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.player.Player;

import java.util.Random;

public class Dicing {
	
	public static void handleRoll(final Player player, final int itemId, int graphic, final int lowest, final int highest) {
		player.getPackets().sendMessage("Rolling...", true);
		player.getInventory().deleteItem(itemId, 1);
		player.setNextAnimation(new Animation(11900));
		player.setNextGraphics(new Graphics(graphic));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInventory().addItem(itemId, 1);
				player.getPackets().sendMessage("Clan Chat channel-mate <col=db3535>" + player.getDisplayName() + "</col> rolled <col=db3535>" + getRandom(lowest, highest) + "</col> on " + diceText(itemId) + " die.", true);
			}
		}, 1);
	}
	
	public static int getRandom(int lowest, int highest) {
		Random r = new Random();
		if (lowest > highest) {
			return -1;
		}
		long range = (long) highest - (long) lowest + 1;
		long fraction = (long) (range * r.nextDouble());
		int numberRolled = (int) (fraction + lowest);
		return numberRolled;
	}
	
	public static String diceText(int id) {
		switch (id) {
			case 15086:
				return "a six-sided";
			case 15088:
				return "two six-sided";
			case 15090:
				return "an eight-sided";
			case 15092:
				return "a ten-sided";
			case 15094:
				return "a twelve-sided";
			case 15096:
				return "a a twenty-sided";
			case 15098:
				return "the percentile";
			case 15100:
				return "a four-sided";
		}
		return "";
	}
	
}
