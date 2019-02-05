package org.redrune.networking.packet.context.impl;

import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.GameFlags;
import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.player.action.impl.WaterFillingAction;
import org.redrune.game.content.entity.actor.player.skills.cooking.Cooking;
import org.redrune.game.content.entity.actor.player.skills.cooking.Cooking.Cookables;
import org.redrune.game.content.entity.actor.player.skills.crafting.JewelrySmithing;
import org.redrune.game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing.ForgingBar;
import org.redrune.game.content.entity.actor.player.skills.smithing.Smithing.ForgingInterface;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.Item;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.handler.ObjectHandler;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ObjectItemInteractionPacketContext extends PacketContext {
	
	private final WorldObject object;
	
	private final int y;
	
	private final int x;
	
	private final int itemSlot;
	
	private final int interfaceId;
	
	private final int itemId;
	
	private final Item item;
	
	public ObjectItemInteractionPacketContext(WorldObject object, int y, int x, int itemSlot, int interfaceId, int itemId, Item item) {
		this.object = object;
		this.y = y;
		this.x = x;
		this.itemSlot = itemSlot;
		this.interfaceId = interfaceId;
		this.itemId = itemId;
		this.item = item;
	}
	
	@Override
	public void handle(Player player) {
		final WorldTile tile = object.getWorldTile();
		player.stopAll(false); // false
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setRouteEvent(new RouteEvent(tile, () -> {
			player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()), object.getCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()), object.getPlane()));
			if (interfaceId == PlayerInventory.INVENTORY_INTERFACE) { // inventory
				
				if (object.getDefinitions().getName().equals("Anvil")) {
					player.getTemporaryAttributes().put("itemUsed", itemId);
					ForgingBar bar = ForgingBar.forId(itemId);
					if (bar != null) {
						ForgingInterface.sendSmithingInterface(player);
					}
				} else if (itemId == 1438 && object.getId() == 2452) {
					Runecrafting.enterAirAltar(player);
				} else if (itemId == 1440 && object.getId() == 2455) {
					Runecrafting.enterEarthAltar(player);
				} else if (itemId == 1442 && object.getId() == 2456) {
					Runecrafting.enterFireAltar(player);
				} else if (itemId == 1444 && object.getId() == 2454) {
					Runecrafting.enterWaterAltar(player);
				} else if (itemId == 1446 && object.getId() == 2457) {
					Runecrafting.enterBodyAltar(player);
				} else if (itemId == 1448 && object.getId() == 2453) {
					Runecrafting.enterMindAltar(player);
					
				} else if (object.getDefinitions().getName().equals("Furnace")) {
					if (item.getId() == 2357) {
						JewelrySmithing.openInterface(player);
					}
					
				} else if (itemId == 229 || itemId == 1923 || itemId == 1925 || itemId == 1935 || itemId == 3734 || itemId == 5350 && object.getDefinitions().getName().equals("Fountain") || object.getDefinitions().getName().equals("Well") || object.getDefinitions().getName().equals("Sink")) {
					if (WaterFillingAction.isFilling(player, itemId, false)) {
						return;
					}
				} else if (itemId == 536 && object.getDefinitions().getName().equals("Altar")) { //Dragon Bones
					player.getPackets().sendGameMessage("You pray to the gods and they accept your offering.");
					player.getInventory().deleteItem(new Item(536, 1));
					player.getSkills().addXp(SkillConstants.PRAYER, 650);
					player.getPackets().sendSound(2738, 0, 1);
					player.setNextAnimation(new Animation(896));
					player.setNextGraphics(new Graphics(624));
					player.getInventory().refresh();
				} else if (itemId == 18830 && object.getDefinitions().getName().equals("Altar")) { //Frost Dragon bones
					player.getPackets().sendGameMessage("You pray to the gods and they accept your offering.");
					player.getInventory().deleteItem(new Item(18830, 1));
					player.getSkills().addXp(SkillConstants.PRAYER, 1127);
					player.getPackets().sendSound(2738, 0, 1);
					player.setNextAnimation(new Animation(896));
					player.setNextGraphics(new Graphics(624));
					player.getInventory().refresh();
				} else if (itemId == 526 && object.getDefinitions().getName().equals("Altar")) { //Bones
					player.getPackets().sendGameMessage("You pray to the gods and they accept your offering.");
					player.getInventory().deleteItem(new Item(526, 1));
					player.getSkills().addXp(SkillConstants.PRAYER, 186);
					player.getPackets().sendSound(2738, 0, 1);
					player.setNextAnimation(new Animation(896));
					player.setNextGraphics(new Graphics(624));
					player.getInventory().refresh();
				} else if (itemId == 532 && object.getDefinitions().getName().equals("Altar")) { //Big bones
					player.getPackets().sendGameMessage("You pray to the gods and they accept your offering.");
					player.getInventory().deleteItem(new Item(532, 1));
					player.getSkills().addXp(SkillConstants.PRAYER, 249);
					player.getPackets().sendSound(2738, 0, 1);
					player.setNextAnimation(new Animation(896));
					player.setNextGraphics(new Graphics(624));
					player.getInventory().refresh();
				} else if (object.getId() == 733 || object.getId() == 64729) {
					player.setNextAnimation(new Animation(CombatAlgorithm.getWeaponAttackEmote(-1, 0)));
					ObjectHandler.slashWeb(player, object);
				} else if (objectDef.getName().toLowerCase().contains("range") || objectDef.getName().toLowerCase().contains("stove") || object.getId() == 2732) {
					Cookables cook = Cooking.isCookingSkill(item);
					if (cook != null) {
						player.getDialogueManager().startDialogue("CookingD", cook, object);
					}
				} else {
					player.getPackets().sendGameMessage("Nothing interesting happens...");
					if (GameFlags.debugMode) {
						System.out.println("item on object: " + object.getId());
					}
				}
			}
		}));
	}
}
