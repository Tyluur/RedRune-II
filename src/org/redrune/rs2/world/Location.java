package org.redrune.rs2.world;

import lombok.Getter;

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
	 * The z-coordinate.
	 */
	@Getter
	private final int z;
	
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
	 * @param z
	 * 		The z-coordinate.
	 */
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "x: " + x + ", y: " + y + ", z: " + z;
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
		return locate(x + diffX, y + diffY, z + diffZ);
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
	private Location locate(int x, int y, int z) {
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
		return locate(x + l.x, y + l.y, z + l.z);
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
	 * Gets the region x-coordinate.
	 *
	 * @return The region x-coordinate.
	 */
	public int getRegionX() {
		return x >> 3;
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
	 * Gets the region y-coordinate.
	 *
	 * @return The region y-coordinate.
	 */
	public int getRegionY() {
		return y >> 3;
	}
	
	/**
	 * The region ID of the location you're in.
	 */
	
	public int getRegionId() {
		return (getRegionY() >> 3) | ((getRegionX() >> 3) << 8);
	}
	
	/**
	 * Gets the region location.
	 *
	 * @return The region location.
	 */
	public Location getRegionLocation() {
		return new Location(x >> 3 + (x - ((x >> 3) << 3)), y >> 3 + (y - ((y >> 3) << 3)), z);
	}
	
	/**
	 * Gets the distance between this location and the given location.
	 *
	 * @param location
	 * 		The location argued.
	 * @return The distance.
	 */
	public int getDistance(Location location) {
		return (int) Math.sqrt(Math.pow(location.x - x, 2) + Math.pow(location.y - y, 2));
	}
	
	/**
	 * Gets the distance between this location and the given location.
	 *
	 * @param l
	 * 		The given location.
	 * @return The distance in double format.
	 */
	public double distance(Location l) {
		return Math.sqrt(Math.pow(l.x - x, 2) + Math.pow(l.y - y, 2));
	}
	
	/**
	 * Gets the 12 bits hash of this location.
	 *
	 * @return The hash.
	 */
	public int get12BitsHash() {
		return (0x1f & getLocalY()) | (getZ() << 10) | (0x3e5 & ((getLocalX() << 5)));
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
		return (((regionId & 0xff) * 64) >> 6) | (getZ() << 16) | ((((regionId >> 8) * 64) >> 6) << 8);
	}
	
	/**
	 * Gets the 30 bits hash of this location.
	 *
	 * @return The hash.
	 */
	public int get30BitsHash() {
		return y | z << 28 | x << 14;
	}
	
	/**
	 * Checks if the location is within distance of the other location.
	 *
	 * @param location
	 * 		The location.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Location location) {
		if (z != location.z) {
			return false;
		}
		int deltaX = location.x - x, deltaY = location.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}
}