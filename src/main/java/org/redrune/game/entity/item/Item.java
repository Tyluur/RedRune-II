package org.redrune.game.entity.item;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.entity.Entity;

import java.io.Serializable;

/**
 * Represents a single item.
 * <p/>
 *
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item implements Serializable, Entity {
	
	private static final long serialVersionUID = -6485003878697568087L;
	
	/**
	 * The id of the item
	 */
	private int id;
	
	/**
	 * The amount of the item
	 */
	protected int amount;
	
	public Item(int id) {
		this(id, 1);
	}
	
	public Item(int id, int amount) {
		this(id, amount, false);
	}
	
	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}
	
	public Item clone() {
		return new Item(id, amount);
	}
	
	@Override
	public Item toItem() {
		return this;
	}
	
	/**
	 * Gets the name of the item
	 */
	public String getName() {
		return getDefinitions().getName();
	}
	
	/**
	 * Gets the {@code ItemDefinitions} of the item
	 */
	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(id);
	}
	
	@Override
	public String toString() {
		return "Item{" + "id=" + id + ", amount=" + amount + '}';
	}

	public int getId() {
		return this.id;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
