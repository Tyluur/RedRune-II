package org.redrune.game.content.controller.impl;

import org.redrune.game.content.controller.Controller;
import org.redrune.game.entity.Entity;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.game.ClickOption;

public class CorpBeastController extends Controller {

	@Override
	public void start() {

	}

	@Override
	public void magicTeleported(int type) {
		removeController();
	}
	
	@Override
	public boolean canEntityClick(Entity entity, ClickOption option) {
		if (entity.isObject() && entity.toObject().getId() == 37929 && option == ClickOption.FIRST) {
			removeController();
			player.stopAll();
			player.setNextWorldTile(new WorldTile(2970, 4384, 0));
			return false;
		}
		return super.canEntityClick(entity, option);
	}
	
	@Override
	public boolean sendDeath() {
		removeController();
		return true;
	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

}
