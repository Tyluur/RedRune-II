package org.redrune.game.content.activity.type;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.world.area.Shape;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public abstract class AreaActivity extends Activity {
	
	/**
	 * The shape of the area
	 */
	public abstract Shape getShape();
	
	@Override
	public boolean isAtActivity() {
		return getShape().inside(player.getLocation());
	}
}
