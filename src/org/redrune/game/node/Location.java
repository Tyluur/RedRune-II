package org.redrune.game.node;

import lombok.Getter;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Location {
	
	/**
	 * Represents the viewport sizes.
	 */
	public final static int[] VIEWPORT_SIZES = { 104, 120, 136, 168 };
	
	/**
	 * The amount of sectors per region.
	 */
	public static final int SECTORS_PER_REGION = 8;
	
	/**
	 * The sector length.
	 */
	public static final int SECTOR_LENGTH = 4;
	
	/**
	 * The x-coordinate.
	 */
	@Getter
	private final int x;
	
	/**
	 * The y-coordinate.
	 */
	@Getter
	private final int y;
	
	/**
	 * The plane-coordinate.
	 */
	@Getter
	private final int plane;
	
	/**
	 * Constructs a new {@code Location} {@code Object}.
	 *
	 * @param x
	 * 		The x-coordinate.
	 * @param y
	 * 		The y-coordinate.
	 */
	public Location(int x, int y) {
		this(x, y, 0);
	}
	
	/**
	 * Constructs a new {@code Location} {@code Object}.
	 *
	 * @param x
	 * 		The x-coordinate.
	 * @param y
	 * 		The y-coordinate.
	 * @param plane
	 * 		The z-coordinate.
	 */
	public Location(int x, int y, int plane) {
		this.x = x;
		this.y = y;
		this.plane = plane;
	}
	
	public Location(Location other) {
		this.x = other.x;
		this.y = other.y;
		this.plane = other.plane;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location location = (Location) obj;
			return location.getX() == x && location.getY() == y && location.getPlane() == plane;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + ", plane=" + plane + ", id=" + getRegionId() + ", rX=" + getRegionX() + ", rY=" + getRegionY() + "]";
	}
	
	/**
	 * The region ID of the location you're in.
	 */
	
	public int getRegionId() {
		return (getRegionY() >> 3) | ((getRegionX() >> 3) << 8);
	}
	
	/**
	 * Gets the region y-coordinate.
	 *
	 * @return The region y-coordinate.
	 */
	public int getRegionY() {
		return y >> 3;
	}
	
	/**
	 * Gets the region x-coordinate.
	 *
	 * @return The region x-coordinate.
	 */
	public int getRegionX() {
		return x >> 3;
	}
	
	/**
	 * Gets a delta location.
	 *
	 * @param l
	 * 		The location.
	 * @param o
	 * 		The other location.
	 * @return The delta location.
	 */
	public static Location getDelta(Location l, Location o) {
		return new Location(o.x - l.x, o.y - l.y, o.plane - l.plane);
	}
	
	/**
	 * Gets the id of a region from the x coordinate and the y coordinate
	 *
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 */
	public static int getRegionId(int x, int y) {
		return ((y >> 3) >> 3) | (((x >> 3) >> 3)) << 8;
	}
	
	public static Location GetDelta(Location from, Location to) {
		return Location.create((short) (to.x - from.x), (short) (to.y - from.y), (byte) (to.plane - from.plane));
	}
	
	/**
	 * Returns a location.
	 *
	 * @param diffX
	 * 		The amount to increase the current x-coordinate with.
	 * @param diffY
	 * 		The amount to increase the current y-coordinate with.
	 * @param diffZ
	 * 		The amount to increase the current height with.
	 * @return The location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return create(x + diffX, y + diffY, plane + diffZ);
	}
	
	/**
	 * Constructs a new {@code Location} {@code Object} with modified coordinates
	 *
	 * @param x
	 * 		The x change
	 * @param y
	 * 		The y change
	 * @param z
	 * 		The z change
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}
	
	/**
	 * Returns a location calculated by increasing this coordinates with the
	 * given location's coordinates..
	 *
	 * @param l
	 * 		The delta location.
	 * @return The location.
	 */
	public Location transform(Location l) {
		return create(x + l.x, y + l.y, plane + l.plane);
	}
	
	/**
	 * Gets the viewport x.
	 *
	 * @param depth
	 * 		The depth.
	 * @return The viewport x.
	 */
	public int getViewportX(int depth) {
		return getViewportX(this, depth);
	}
	
	/**
	 * Gets the viewport x.
	 *
	 * @param base
	 * 		The base location.
	 * @param depth
	 * 		The depth.
	 * @return The viewport x.
	 */
	public int getViewportX(Location base, int depth) {
		depth = VIEWPORT_SIZES[depth];
		return x - (SECTORS_PER_REGION * (base.getRegionX() - (depth >> SECTOR_LENGTH)));
	}
	
	/**
	 * Gets the viewport y.
	 *
	 * @param depth
	 * 		The depth.
	 * @return The viewport y.
	 */
	public int getViewportY(int depth) {
		return getViewportY(this, depth);
	}
	
	/**
	 * Gets the viewport y.
	 *
	 * @param base
	 * 		The base location.
	 * @param depth
	 * 		The depth.
	 * @return The viewport y.
	 */
	public int getViewportY(Location base, int depth) {
		depth = VIEWPORT_SIZES[depth];
		return y - (SECTORS_PER_REGION * (base.getRegionY() - (depth >> SECTOR_LENGTH)));
	}
	
	/**
	 * Gets the distance between this location and the given location.
	 *
	 * @param location
	 * 		The location argued.
	 * @return The distance.
	 */
	public int getDistance(Location location) {
		return Misc.getDistance(getX(), getY(), location.getX(), location.getY());
	}
	
	/**
	 * Gets the 12 bits hash of this location.
	 *
	 * @return The hash.
	 */
	public int get12BitsHash() {
		return (0x1f & getLocalY()) | (getPlane() << 10) | (0x3e5 & ((getLocalX() << 5)));
	}
	
	/**
	 * Gets the local y-coordinate of this location.
	 *
	 * @return The local y-coordinate.
	 */
	public int getLocalY() {
		return y - ((getRegionY() - 6) << 3);
	}
	
	/**
	 * Gets the local x-coordinate of this location.
	 *
	 * @return The local x-coordinate.
	 */
	public int getLocalX() {
		return x - ((getRegionX() - 6) << 3);
	}
	
	/**
	 * Gets the 18 bits hash of this location.
	 *
	 * @return The hash.
	 */
	public int get18BitsHash() {
		int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
		return (((regionId & 0xff) * 64) >> 6) | (getPlane() << 16) | ((((regionId >> 8) * 64) >> 6) << 8);
	}
	
	/**
	 * Gets the 30 bits hash of this location.
	 *
	 * @return The hash.
	 */
	public int get30BitsHash() {
		return y | plane << 28 | x << 14;
	}
	
	/**
	 * Checks if the location is within distance ( 14 tiles) of the other location.
	 *
	 * @param location
	 * 		The location.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Location location) {
		return withinDistance(location, 14);
	}
	
	/**
	 * Checks if the location is within a given amount of distance to us
	 *
	 * @param location
	 * 		The other location
	 * @param distance
	 * 		The amount of distance
	 */
	public boolean withinDistance(Location location, int distance) {
		if (location.plane != plane) {
			return false;
		}
		int deltaX = location.x - x, deltaY = location.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}
	
	public int getLocalX(Location lastRegion) {
		return x - ((lastRegion.getRegionX() - (VIEWPORT_SIZES[plane] >> 4)) * 8);
	}
	
	public int getLocalY(Location lastRegion) {
		return y - ((lastRegion.getRegionY() - (VIEWPORT_SIZES[plane] >> 4)) * 8);
	}
	
	public int getXInRegion() {
		return x & 0x3F;
	}
	
	public int getYInRegion() {
		return y & 0x3F;
	}
	
	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}
	
	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}
	
	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}
	
	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	/**
	 * Checks if the location is a multi area
	 */
	public boolean isMultiArea() {
		int destX = getX();
		int destY = getY();
		return (destX >= 3462 && destX <= 3511 && destY >= 9481 && destY <= 9521 && getPlane() == 0) // kalphite
				       // queen
				       // lair
				       || (destX >= 4540 && destX <= 4799 && destY >= 5052 && destY <= 5183 && getPlane() == 0) // thzaar
				       // city
				       || getRegionId() == 11051 || getRegionId() == 16729 // glacors
				       || getRegionId() == 11589 // dags
				       || getRegionId() == 10894 // monkey skeles
				       || getRegionId() == 11573 // sea troll queen
				       || getRegionId() == 10554 || getRegionId() == 10810 // rock crabs
				       || (destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249) // mole
				       || (destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903)// wild
				       || (destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720) || (destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970) || (destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759) || (destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937) || (destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720) || (destX >= 2450 && destX <= 3520 && destY >= 9450 && destY <= 9550) || (destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710) || (destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646) || (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)// wild
				       || (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				       || (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				       || (destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714) // chaos dwarf battlefield
				       // godwars
				       || (destX >= 2250 && destX <= 2292) && (destY >= 4675 && destY <= 4710) // kbd
				       || (getX() >= 2560 && getX() <= 2630) && (getY() >= 5710 && getY() <= 5753) // tormenteds
				       || (getX() >= 3083 && getX() <= 3120) && (getY() >= 5522 && getY() <= 5550) // Bork's area
				       || getRegionId() == 12590 || (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				       || (destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY)) || (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				       || (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				       || (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				       // out
				       || (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532) // castlewars
				       || (destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631) // Risk
				       // ffa.
				       || (destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631) // Safe
				       // ffa
				       || getRegionId() == 1089 || getRegionId() == 12341 || (getX() >= 3011 && getX() <= 3132 && getY() >= 10052 && getY() <= 10175 && (getY() >= 10066 || getX() >= 3094)); // forinthry dungeon
				
	}
	
}