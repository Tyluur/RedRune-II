package org.redrune.rs2.node.item;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.rs2.node.Node;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class Item extends Node {
	
	/**
	 * The item id.
	 */
	@Getter
	@Setter
	private short id;
	
	/**
	 * The item amount.
	 */
	@Getter
	@Setter
	private int amount;
	
	/**
	 * The item definition.
	 */
	@Getter
	@Setter
	private transient ItemDefinition definitions;
	
	protected Item() {
		super(null);
	}
	
	/**
	 * Constructs a new {@code Item} {@code Object}.
	 *
	 * @param id
	 * 		The item id.
	 */
	public Item(int id) {
		this(id, 1);
		this.definitions = ItemDefinitionParser.forId(id);
	}
	
	/**
	 * Constructs a new {@code Item} {@code Object}.
	 *
	 * @param id
	 * 		The item id.
	 * @param amount
	 * 		The amount of this item.
	 */
	public Item(int id, int amount) {
		super(null);
		this.id = (short) id;
		this.definitions = ItemDefinitionParser.forId(id);
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getName() + ", amount=" + amount + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		return o.getClass() == Item.class && ((Item) o).id == id;
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	/**
	 * Gets the name of the item
	 */
	public String getName() {
		return definitions.getName();
	}
}
