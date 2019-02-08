package plugin.item;

import org.redrune.game.content.entity.item.DiceGame;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class DiceGameItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		int itemId = item.getId();
		switch(option) {
			case "Private-roll":
				if (itemId == 15098) {
					DiceGame.rollDice8(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2075));
				} else if (itemId == 15086) {
					DiceGame.rollDice2(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2072));
				} else if (itemId == 15088) {
					DiceGame.rollDice3(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2074));
				} else if (itemId == 15090) {
					DiceGame.rollDice4(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2071));
				} else if (itemId == 15092) {
					DiceGame.rollDice5(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2070));
				} else if (itemId == 15094) {
					DiceGame.rollDice5(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2073));
				} else if (itemId == 15096) {
					DiceGame.rollDice7(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2068));
				} else if (itemId == 15100) {
					DiceGame.rollDice1(player);
					player.setNextAnimation(new Animation(11900));
					player.setNextGraphics(new Graphics(2069));
				}
				break;
			default:
				return true;
		}
		return true;
	}
	
	@Override
	public void register() {
		int[] ids = { 15098, 15086, 15088, 15090, 15092, 15094, 15096, 15100 };
		for (int id : ids) {
			registerItem(id, "Private-Roll");
			registerItem(id, "Friends-Roll");
			registerItem(id, "Choose-dice");;
			registerItem(id, "Put-away");
		}
		
	}
}
