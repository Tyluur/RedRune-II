package org.redrune.game.content.skills.crafting;

import org.redrune.game.content.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Gircat <gircat101@gmail.com> Created on Jul 28, 2014 at 12:51:21 PM.
 */
public class JewelrySmithing {
	
	public static final int INTERFACE = 675;
	
	private static final int[] GEMS = { 2357, 1607, 1605, 1603, 1601, 1615, 6573 };
	
	private static final int[] MOLDS = { 1592, 1597, 1595, 11065 };
	
	private static final int[][] ITEMS = { { 1635, 1637, 1639, 1641, 1643, 1645, 1647 }, { 1654, 1656, 1658, 1660, 1662, 1664, 1666 }, { 1673, 1675, 1677, 1679, 1681, 1685 }, { 11069, 11072, 11076, 11085, 11092, 11115, 11067 } };
	
	private static final int[] COMPONENTS_BASE = { 20, 39, 58, 77 };
	
	private static final double[][] EXPERIENCE = { { 15, 40, 55, 70, 85, 100, 115 }, { 20, 55, 60, 75, 90, 105, 120 }, { 25, 60, 65, 80, 95, 110, 125 }, { 30, 65, 70, 85, 100, 150, 165 } };
	
	private static final byte[][] LEVEL = { { 5, 20, 27, 34, 43, 55, 67 }, { 6, 22, 29, 40, 56, 72, 82 }, { 7, 23, 30, 42, 58, 74, 84 }, { 8, 24, 31, 50, 70, 80, 90 } };
	
	private static final int[] ONYX = { 6575, 6577, 6579, 11130 };
	
	private static final int[] EGG_IDS = { 3689, 3690, 3691, 3692, 3693, 3694 };
	
	public static void openInterface(Player player) {
		player.getInterfaceManager().sendInterface(INTERFACE);
		callCS2(player);
		for (int primaryIndex = 0; primaryIndex < MOLDS.length; primaryIndex++) {
			player.getPackets().sendIComponentText(INTERFACE, 16 + (primaryIndex * 19), "");
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				player.getPackets().sendItems((299 + (primaryIndex * 14) + secondaryIndex), new Item[] { new Item(ITEMS[primaryIndex][secondaryIndex]) });
			}
		}
	}
	
	private static void callCS2(Player player) {
		for (int primaryIndex = 0; primaryIndex < COMPONENTS_BASE.length; primaryIndex++) {
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				player.getPackets().sendInterSetItemsOptionsScript(INTERFACE, (COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2), (299 + (primaryIndex * 14) + secondaryIndex), false, 6, 4, "Make 1", "Make 5", "Make 10");
				player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE, (COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2), 0, 28, 0, 1, 2, 3);
			}
		}
	}
	
	private static void resetIndex(Player player, int primaryIndex) {
		for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
			player.getPackets().sendItems((299 + (primaryIndex * 14) + secondaryIndex), new Item[] { });
		}
	}
	
	public static void handleButtonClick(Player player, int componentId, final int tick) {
		for (int primaryIndex = 0; primaryIndex < COMPONENTS_BASE.length; primaryIndex++) {
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				if (componentId == (COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2)) {
					final int actionPrimaryIndex = primaryIndex, actionSecondaryIndex = secondaryIndex;
					player.closeInterfaces();
					player.getActionManager().setAction(new Action() {
						
						int ticks;
						
						@Override
						public boolean start(Player player) {
							this.ticks = tick;
							return process(player);
						}
						
						@Override
						public boolean process(Player player) {
							if (ticks <= 0) {
								return false;
							}
							int level = LEVEL[actionPrimaryIndex][actionSecondaryIndex];
							if (player.getSkills().getLevel(SkillConstants.CRAFTING) < level) {
								player.getPackets().sendGameMessage("You need a Crafting level of " + level + ".");
								return false;
							} else if (!player.getInventory().containsItem(2357, 1)) {
								player.getPackets().sendGameMessage("You need a gold bar in order to create jewellery.");
								return false;
							} else if (actionSecondaryIndex != 0 && !player.getInventory().containsItem(GEMS[actionSecondaryIndex], 1)) {
								player.getPackets().sendGameMessage("You are missing the requried items in order to create this type of jewellery.");
								return false;
							}
							return true;
						}
						
						@Override
						public int processWithDelay(Player player) {
							ticks--;
							player.setNextAnimation(new Animation(3243));
							player.getInventory().deleteItem(2357, 1);
							player.getInventory().deleteItem(GEMS[actionSecondaryIndex], 1);
							player.getInventory().addItem(actionSecondaryIndex == 6 ? ONYX[actionPrimaryIndex] : ITEMS[actionPrimaryIndex][actionSecondaryIndex], 1);
							player.getSkills().addXp(SkillConstants.CRAFTING, EXPERIENCE[actionPrimaryIndex][actionSecondaryIndex]);
							return 2;
						}
						
						@Override
						public void stop(Player player) {
							setActionDelay(player, 3);
						}
					});
				}
			}
		}
	}
	
	public static void ringTransformation(Player player, final int itemId) {
		if (player.getActionManager().getAction() != null) {
			player.getPackets().sendGameMessage("Please finish what you are doing before transforming.");
			return;
		}
		player.getActionManager().setAction(new Action() {
			
			@Override
			public boolean start(Player player) {
				player.stopAll();
				int transformationId = EGG_IDS[Misc.random(EGG_IDS.length)];
				if (itemId == 6583) {
					transformationId = 2626;
				}
				player.getAppearance().transformIntoNPC(transformationId);
				player.getInterfaceManager().sendInventoryInterface(375);
				return true;
			}
			
			@Override
			public boolean process(Player player) {
				return true;
			}
			
			@Override
			public int processWithDelay(Player player) {
				return 0;
			}
			
			@Override
			public void stop(Player player) {
				setActionDelay(player, 3);
				resetTransformation(player);
			}
		});
	}
	
	public static void resetTransformation(Player player) {
		player.closeInterfaces();
		player.getInventory().init();
		player.setNextAnimation(new Animation(14884));
		player.getAppearance().transformIntoNPC(-1);
	}
}