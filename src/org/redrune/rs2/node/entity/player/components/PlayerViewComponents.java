package org.redrune.rs2.node.entity.player.components;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class PlayerViewComponents {
	
	/**
	 * The mode of the screen
	 */
	@Getter
	@Setter
	private int screenSizeMode;
	
	/**
	 * The size of the screen in x
	 */
	@Getter
	@Setter
	private int screenSizeX;
	
	/**
	 * The size of the screen in y
	 */
	@Getter
	@Setter
	private int screenSizeY;
	
	/**
	 * The display mode of the client
	 */
	@Getter
	@Setter
	private int displayMode;
	
}
