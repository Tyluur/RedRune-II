package org.redrune.game.content.entity.actor.player.controller;

import org.redrune.game.content.entity.item.Foods.Food;
import org.redrune.game.content.entity.item.Pots.Pot;
import org.redrune.game.entity.Entity;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.GameConstants;
import org.redrune.utility.game.ClickOption;

import java.io.Serializable;

public final class ControllerManager implements Serializable {
	
	private static final long serialVersionUID = 2084691334731830796L;
	
	private Object[] lastControllerArguments;
	
	private String lastController;
	
	private transient Player player;
	
	private transient Controller controller;
	
	private transient boolean inited;
	
	public ControllerManager() {
		lastController = GameConstants.DEFAULT_CONTROLLER;
	}
	
	public void startController(String key, Object... parameters) {
		if (controller != null) {
			forceStop();
		}
		controller = ControllerHandler.getController(key);
		if (controller == null) {
			return;
		}
		controller.setPlayer(player);
		lastControllerArguments = parameters;
		lastController = key;
		controller.start();
		inited = true;
	}
	
	public void forceStop() {
		if (controller != null) {
			controller.forceClose();
			controller = null;
		}
		lastControllerArguments = null;
		lastController = null;
		inited = false;
	}
	
	public void login() {
		if (lastController == null) {
			return;
		}
		controller = ControllerHandler.getController(lastController);
		if (controller == null) {
			forceStop();
			return;
		}
		controller.setPlayer(player);
		if (controller.login()) {
			forceStop();
		} else {
			inited = true;
		}
	}
	
	public void logout() {
		if (controller == null) {
			return;
		}
		if (controller.logout()) {
			forceStop();
		}
	}
	
	public boolean canMove(int dir) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canMove(dir);
	}
	
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.checkWalkStep(lastX, lastY, nextX, nextY);
	}
	
	public boolean keepCombating(Actor target) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.keepCombating(target);
	}
	
	public boolean canEquip(int slotId, int itemId) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canEquip(slotId, itemId);
	}
	
	public boolean canAddInventoryItem(int itemId, int amount) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canAddInventoryItem(itemId, amount);
	}
	
	public void trackXP(int skillId, int addedXp) {
		if (controller == null || !inited) {
			return;
		}
		controller.trackXP(skillId, addedXp);
	}
	
	public boolean canDeleteInventoryItem(int itemId, int amount) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canDeleteInventoryItem(itemId, amount);
	}
	
	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canUseItemOnItem(itemUsed, usedWith);
	}
	
	public boolean canAttack(Actor actor) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canAttack(actor);
	}
	
	/**
	 * Checks if we can click on an entity
	 *
	 * @param entity
	 * 		The entity
	 * @param option
	 * 		The option
	 */
	public boolean canEntityClick(Entity entity, ClickOption option) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canEntityClick(entity, option);
	}
	
	public boolean canHit(Actor actor) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canHit(actor);
	}
	
	public void moved() {
		if (controller == null || !inited) {
			return;
		}
		controller.moved();
	}
	
	public void magicTeleported(int type) {
		if (controller == null || !inited) {
			return;
		}
		controller.magicTeleported(type);
	}
	
	public void sendInterfaces() {
		if (controller == null || !inited) {
			return;
		}
		controller.sendInterfaces();
	}
	
	public void process() {
		if (controller == null || !inited) {
			return;
		}
		controller.process();
	}
	
	public boolean sendDeath() {
		if (controller == null || !inited) {
			return true;
		}
		return controller.sendDeath();
	}
	
	public boolean canEat(Food food) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canEat(food);
	}
	
	public boolean canPot(Pot pot) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.canPot(pot);
	}
	
	public boolean useDialogueScript(Object key) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.useDialogueScript(key);
	}
	
	public boolean processMagicTeleport(WorldTile toTile) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.processMagicTeleport(toTile);
	}
	
	public boolean processItemTeleport(WorldTile toTile) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.processItemTeleport(toTile);
	}
	
	public boolean processObjectTeleport(WorldTile toTile) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.processObjectTeleport(toTile);
	}
	
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.processButtonClick(interfaceId, componentId, slotId, packetId);
	}
	
	public boolean processItemOnNPC(NPC npc, Item item) {
		if (controller == null || !inited) {
			return true;
		}
		return controller.processItemOnNPC(npc, item);
	}
	
	public boolean processItemOnPlayer(Player player, Item item) {
		if (controller == null | !inited) {
			return true;
		}
		return controller.processItemOnPlayer(player, item);
	}
	
	public void removeControllerWithoutCheck() {
		controller = null;
		lastControllerArguments = null;
		lastController = null;
		inited = false;
	}
	
	public boolean handleItemOption1(Player playerr, int slotId, int itemId, Item item) {
		if (itemId != item.getId()) {
			return false;
		}
		switch (itemId) {
			case -1:
				return false;
		}
		return true;
	}

    public Object[] getLastControllerArguments() {
        return this.lastControllerArguments;
    }

    public String getLastController() {
        return this.lastController;
    }

    public Controller getController() {
        return this.controller;
    }

    public void setLastControllerArguments(Object[] lastControllerArguments) {
        this.lastControllerArguments = lastControllerArguments;
    }

    public void setLastController(String lastController) {
        this.lastController = lastController;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
