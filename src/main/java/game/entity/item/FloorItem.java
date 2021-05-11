package game.entity.item;

import game.entity.actor.player.Player;
import game.global.WorldTile;

/**
 * This class represents an item that exists on the floor
 *
 * @author Matrix Team
 * @author Tyluur <itstyluur@icloud.com>
 */
@SuppressWarnings("serial")
public class FloorItem extends Item {
	
	/**
	 * The tile of the item
	 */
	private WorldTile tile;
	
	/**
	 * The owner of the item
	 */
	private Player owner;
	
	/**
	 * If the item is invisible
	 */
	private boolean invisible;
	
	/**
	 * If the item is a grave
	 */
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

    public WorldTile getTile() {
        return this.tile;
    }

    public Player getOwner() {
        return this.owner;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public boolean isGrave() {
        return this.grave;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
}
