package org.redrune.game.entity.item;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an item that exists on the floor
 *
 * @author Matrix Team
 * @author Tyluur <itstyluur@gmail.com>
 */
@SuppressWarnings("serial")
public class FloorItem extends Item {
	
	/**
	 * The tile of the item
	 */
	@Getter
	private WorldTile tile;
	
	/**
	 * The owner of the item
	 */
	@Getter
	private Player owner;
	
	/**
	 * If the item is invisible
	 */
	@Getter
	@Setter
	private boolean invisible;
	
	/**
	 * If the item is a grave
	 */
	@Getter
	private boolean grave;
	
	public FloorItem(int id) {
		super(id);
	}
	
	public FloorItem(Item item, WorldTile tile, Player owner, boolean grave, boolean invisible) {
		super(item.getId(), item.getAmount());
		this.tile = tile;
		this.owner = owner;
		this.grave = grave;
		this.invisible = invisible;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * If we have an owner
	 */
	public boolean hasOwner() {
		return owner != null;
	}
	
}
