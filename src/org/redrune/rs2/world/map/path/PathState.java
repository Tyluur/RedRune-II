package org.redrune.rs2.world.map.path;

import lombok.Getter;
import org.redrune.rs2.world.map.Position;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class PathState {
	
	/**
	 * The queue of points in the state
	 */
	@Getter
	private Deque<Position> points = new ArrayDeque<Position>();
	
	/**
	 * If the route was found
	 */
	@Getter
	private boolean routeFound = true;
	
	/**
	 * Fails the route
	 */
	public void routeFailed() {
		this.routeFound = false;
	}
	
}
