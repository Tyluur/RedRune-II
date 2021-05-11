package game.global.map.route.strategy;

import game.entity.actor.Actor;
import game.global.map.route.RouteStrategy;

public class ActorStrategy extends RouteStrategy {

	/**
	 * Entity position x.
	 */
	private final int x;

	/**
	 * Entity position y.
	 */
	private final int y;

	/**
	 * Entity size.
	 */
	private final int size;

	/**
	 * Access block flag, see RouteStrategy static final values.
	 */
	private final int accessBlockFlag;

	public ActorStrategy(Actor actor) {
		this(actor, 0);
	}

	public ActorStrategy(Actor actor, int accessBlockFlag) {
		this.x = actor.getX();
		this.y = actor.getY();
		this.size = actor.getSize();
		this.accessBlockFlag = accessBlockFlag;
	}

	@Override
	public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
		return checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY, sizeXY, x - clipBaseX, y - clipBaseY, size, size, accessBlockFlag);
	}

	@Override
	public int getApproxDestinationX() {
		return x;
	}

	@Override
	public int getApproxDestinationY() {
		return y;
	}

	@Override
	public int getApproxDestinationSizeX() {
		return size;
	}

	@Override
	public int getApproxDestinationSizeY() {
		return size;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ActorStrategy)) {
			return false;
		}
		ActorStrategy strategy = (ActorStrategy) other;
		return x == strategy.x && y == strategy.y && size == strategy.size && accessBlockFlag == strategy.accessBlockFlag;
	}

}
