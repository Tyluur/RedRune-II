package org.redrune.game.entity.actor.mask;

import lombok.Getter;
import org.redrune.game.global.WorldTile;

public final class ForceMovement {
	
	public static final int NORTH = 0;
	
	public static final int EAST = 1;
	
	public static final int SOUTH = 2;
	
	public static final int WEST = 3;
	
	@Getter
	private WorldTile toFirstTile;
	
	@Getter
	private WorldTile toSecondTile;
	
	@Getter
	private int firstTileTicketDelay;
	
	@Getter
	private int secondTileTicketDelay;
	
	@Getter
	private int direction;
	
	/*
	 * USE: moves to firsttile firstTileTicketDelay: the delay in game tickets
	 * between your tile and first tile the direction
	 */
	public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, int direction) {
		this(toFirstTile, firstTileTicketDelay, null, 0, direction);
	}
	
	/**
	 * Constructs a new force movement mask
	 *
	 * @param toFirstTile
	 * 		The tile we start at
	 * @param firstTileTicketDelay
	 * 		The delay before we arrive at the first tile
	 * @param toSecondTile
	 * 		The tile we arrive at
	 * @param secondTileTicketDelay
	 * 		The delay before arriving at this tile
	 * @param direction
	 * 		The direction to face after we arrive at the tile
	 */
	public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, WorldTile toSecondTile, int secondTileTicketDelay, int direction) {
		this.toFirstTile = toFirstTile;
		this.firstTileTicketDelay = firstTileTicketDelay;
		this.toSecondTile = toSecondTile;
		this.secondTileTicketDelay = secondTileTicketDelay;
		this.direction = direction;
	}
	
}
