package com.rs.game.entity.item;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.entity.Entity;
import lombok.Getter;
import lombok.Setter;

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
	@Getter
	@Setter
	private int id;
	
	/**
	 * The amount of the item
	 */
	@Getter
	@Setter
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
}
