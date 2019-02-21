package plugin.item;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.player.dialogue.impl.SimpleMessage;
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing;
import org.redrune.game.content.plugin.type.ItemOnItemPlugin;
import org.redrune.game.content.plugin.type.ItemOnObjectPlugin;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.constants.SkillConstants;
import plugin.item.GodswordInteractionItemPlugin.GodswordComponents.Constants;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class GodswordInteractionItemPlugin implements ItemPlugin, ItemOnItemPlugin, ItemOnObjectPlugin {
	
	/**
	 * The item id of a godsword blade
	 */
	private static final int GODSWORD_BLADE = 11690;
	
	/**
	 * The animation to perform when starting on the anvil
	 */
	private static final Animation ANVIL_ANIMATION = new Animation(898);
	
	public static void dismantleGS(Player player, Item item, int slot) {
		int gs = (item.getId() - 11694) / 2;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendMessage("Not enough space in your inventory.");
			return;
		}
		item.setId(11690);
		player.getInventory().addItem(11702 + gs * 2, 1);
		player.getInventory().refresh(slot);
		player.getPackets().sendMessage("You dismantle the godsword");
	}
	
	@Override
	public boolean handleItemOnItem(Player player, Item used, Item with) {
		String usedName = used.getName().toLowerCase();
		Item hilt = usedName.contains("hilt") ? used : with;
		int swordId = getSwordByHilt(hilt.getId());
		if (swordId == -1) {
			return false;
		}
		player.getInventory().deleteItem(used);
		player.getInventory().deleteItem(with);
		player.getInventory().addItem(swordId, 1);
		String name = ItemDefinitions.getItemDefinitions(swordId).getName();
		player.getDialogueManager().startDialogue(SimpleMessage.class, "You attach the hilt to the blade and make a" + (name.toLowerCase().startsWith("a") ? "n" : "") + " " + name + ".");
		return true;
	}
	
	private int getSwordByHilt(int hiltId) {
		switch (hiltId) {
			case 11702:
				return 11694;
			case 11704:
				return 11696;
			case 11706:
				return 11698;
			case 11708:
				return 11700;
		}
		return -1;
	}
	
	@Override
	public boolean handle(Player player, Item item, WorldObject object) {
		if (!player.getInventory().containsItems(GodswordComponents.SHARDS.itemIds, new int[] { 1, 1, 1 })) {
			player.getDialogueManager().startDialogue(SimpleMessage.class, "You don't have all the godsword shards necessary to build a blade.");
			return true;
		}
		if (!player.getInventory().containsItem(Smithing.HAMMER, 1)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a hammer in order to work with shards.");
			return true;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < 80) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a Smithing level of 80 to forge godsword shards.");
			return true;
		}
		player.getLocks().lock();
		player.setNextAnimation(ANVIL_ANIMATION);
		player.getSkills().addXp(SkillConstants.SMITHING, 200);
		player.getDialogueManager().startDialogue(SimpleMessage.class, "You set to work, trying to fix the ancient sword.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getInventory().deleteItem(Constants.GODSWORD_SHARD_1, 1);
				player.getInventory().deleteItem(Constants.GODSWORD_SHARD_2, 1);
				player.getInventory().deleteItem(Constants.GODSWORD_SHARD_3, 1);
				player.getInventory().addItem(11690, 1);
				player.getDialogueManager().startDialogue(SimpleMessage.class, "Even as an experienced smith it is not an easy task, but eventually", "it is done.");
				player.getLocks().unlock();
			}
		}, 3);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(GodswordComponents.GODSWORDS.itemIds).forEach(sword -> registerItem(sword, "Dismantle"));
		Arrays.stream(GodswordComponents.HILTS.itemIds).forEach(hilt -> registerItemOnItemIds(hilt, GODSWORD_BLADE));
		Arrays.stream(GodswordComponents.SHARDS.itemIds).forEach(shard -> {
			registerItemOnObjectPlugins(shard, 2782);
			registerItemOnObjectPlugins(shard, 2783);
		});
	}
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		dismantleGS(player, item, slotId);
		return true;
	}
	
	enum GodswordComponents {
		GODSWORDS(new int[] { 11694, 11696, 11698, 11700 }),
		HILTS(new int[] { 11702, 11704, 11706, 11708 }),
		SHARDS(new int[] { Constants.GODSWORD_SHARD_1, Constants.GODSWORD_SHARD_2, Constants.GODSWORD_SHARD_3 });
		
		private final int[] itemIds;
		
		GodswordComponents(int[] itemIds) {
			this.itemIds = itemIds;
		}
		
		class Constants {
			
			private static final int GODSWORD_SHARD_1 = 11710;
			
			private static final int GODSWORD_SHARD_2 = 11712;
			
			private static final int GODSWORD_SHARD_3 = 11714;
		}
	}
}
