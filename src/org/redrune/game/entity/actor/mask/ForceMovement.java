package org.redrune.game.entity.actor.mask;

import org.redrune.game.global.WorldTile;

public final class ForceMovement {
	
	public static final int NORTH = 0;
	
	public static final int EAST = 1;
	
	public static final int SOUTH = 2;
	
	public static final int WEST = 3;
	
	private WorldTile toFirstTile;
	
	private WorldTile toSecondTile;
	
	private int firstTileTicketDelay;
	
	private int secondTileTicketDelay;
	
	private int direction;
	
	/*
	 * USE: moves to firsttile firstTileTicketDelay: the delay in game tickets
	 * between your tile and first tile the direction
	 */
	public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, int direction) {
		this(toFirstTile, firstTileTicketDelay, null, 0, direction);
	}
	
	/*
	 * USE: moves to firsttile and from first tile to second tile
	 * firstTileTicketDelay: the delay in game tickets between your tile and
	 * first tile secondTileTicketDelay: the delay in game tickets between first
	 * tile and second tile the direction
	 */
	public ForceMovement(WorldTile toFirstTile, int firstTileTicketDelay, WorldTile toSecondTile, int secondTileTicketDelay, int direction) {
		this.toFirstTile = toFirstTile;
		this.firstTileTicketDelay = firstTileTicketDelay;
		this.toSecondTile = toSecondTile;
		this.secondTileTicketDelay = secondTileTicketDelay;
		this.direction = direction;
	}
	
	public WorldTile getToFirstTile() {
		return toFirstTile;
	}
	
	public WorldTile getToSecondTile() {
		return toSecondTile;
	}
	
	public int getFirstTileTicketDelay() {
		return firstTileTicketDelay;
	}
	
	public int getSecondTileTicketDelay() {
		return secondTileTicketDelay;
	}
	
	public int getDirection() {
		return direction;
	}
	
}
