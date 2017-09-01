package com.rs.game.entity.item;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;

@SuppressWarnings("serial")
public class FloorItem extends Item {
	
	private WorldTile tile;
	
	private Player owner;
	
	private boolean invisible;
	
	private boolean grave;
	
	public FloorItem(int id) {
		super(id);
	}
	
	public FloorItem(Item item, WorldTile tile, Player owner, boolean underGrave, boolean invisible) {
		super(item.getId(), item.getAmount());
		this.tile = tile;
		this.owner = owner;
		grave = underGrave;
		this.invisible = invisible;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public WorldTile getTile() {
		return tile;
	}
	
	public boolean isGrave() {
		return grave;
	}
	
	public boolean isInvisible() {
		return invisible;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	public Player getOwner() {
		return owner;
	}
	
}
