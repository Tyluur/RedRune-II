package org.redrune.game.content.entity.actor.player.controller;

import org.redrune.game.content.entity.item.Foods.Food;
import org.redrune.game.content.entity.item.Pots.Pot;
import org.redrune.game.entity.Entity;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.game.ClickOption;

public abstract class Controller {

    public abstract void start();

    protected Player player;

    public final Object[] getArguments() {
        return player.getControllerManager().getLastControllerArguments();
    }

    /**
     * Sets the arguments of the controller
     */
    public final void setArguments(Object[] objects) {
        player.getControllerManager().setLastControllerArguments(objects);
    }

    /**
     * Removes the controller without calling {@link #forceClose()}
     */
    public final void removeController() {
        player.getControllerManager().removeControllerWithoutCheck();
    }

    /**
     * Checks if we can eat a food
     */
    public boolean canEat(Food food) {
        return true;
    }

    /**
     * Checks if we can drink a potion
     */
    public boolean canPot(Pot pot) {
        return true;
    }

    /**
     * If we can't keep fighting an actor, this is false. Called during the combat cycle.
     */
    public boolean keepCombating(Actor target) {
        return true;
    }

    /**
     * Checks if we can equip an item in a slot
     */
    public boolean canEquip(int slotId, int itemId) {
        return true;
    }

    /**
     * This is the validator for when we attempt to attack an actor.
     *
     * @param target The target
     */
    public boolean canAttack(Actor target) {
        return true;
    }

    public void trackXP(int skillId, int addedXp) {

    }

    public boolean canDeleteInventoryItem(int itemId, int amount) {
        return true;
    }

    public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
        return true;
    }

    public boolean canAddInventoryItem(int itemId, int amount) {
        return true;
    }

    /**
     * Used for checking if the controller has custom hitting validation
     *
     * @param actor The actor we wish to hit
     */
    public boolean canHit(Actor actor) {
        return true;
    }

    /**
     * This method is called every time the world cycles
     */
    public void process() {

    }

    /**
     * When the player moves, this method is called
     */
    public void moved() {

    }

    /**
     * After a teleport has been complete, this is called
     *
     * @param type The type of teleport that was complete
     */
    public void magicTeleported(int type) {

    }

    /**
     * When the player's interfaces are sent [login/client orientation change], this method is called
     */
    public void sendInterfaces() {

    }

    /*
     * return can use script
     */
    public boolean useDialogueScript(Object key) {
        return true;
    }

    /*
     * return can teleport
     */
    public boolean processMagicTeleport(WorldTile toTile) {
        return true;
    }

    /*
     * return can teleport
     */
    public boolean processItemTeleport(WorldTile toTile) {
        return true;
    }

    /*
     * return can teleport
     */
    public boolean processObjectTeleport(WorldTile toTile) {
        return true;
    }

    /*
     * return process normaly
     */
    public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
        return true;
    }

    /**
     * Handles the death of a player, if we return true we send a regular death.
     */
    public boolean sendDeath() {
        return true;
    }

    /**
     * If the controller doesn't let the player walk to a direction, we return false here
     *
     * @param direction The direction
     */
    public boolean canMove(int direction) {
        return true;
    }

    /**
     * If the controller doesn't let the player walk to a coordinate, we return false here.
     */
    public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
        return true;
    }

    /**
     * If the controller should be removed on login, we return true here
     */
    public boolean login() {
        return true;
    }

    /**
     * If the controller should be removed on logout, we return true here
     */
    public boolean logout() {
        return true;
    }

    /**
     * This is called when the controller is forcefully closed
     */
    public void forceClose() {
    }

    /**
     * Handles the usage of an item on an npc
     *
     * @param npc  The npc the item is used on
     * @param item The item that is used
     */
    public boolean processItemOnNPC(NPC npc, Item item) {
        return true;
    }

    /**
     * Handles the usage of an item on a player
     *
     * @param player The npc the item is used on
     * @param item   The item that is used
     */
    public boolean processItemOnPlayer(Player player, Item item) {
        return true;
    }

    /**
     * Handles the click on an entity. If the controller handles it customly we return false.
     *
     * @param entity The entity
     * @param option The option clicked
     */
    public boolean canEntityClick(Entity entity, ClickOption option) {
        return true;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
