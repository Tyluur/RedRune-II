package com.rs.game.content.controller.impl;

import com.rs.game.content.controller.Controller;
import com.rs.game.entity.Entity;
import com.rs.game.entity.WorldTile;
import com.rs.utility.game.ClickOption;

import static com.rs.utility.game.ClickOption.FIRST;

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
		if (entity.isObject() && entity.toObject().getId() == 37929 && option == FIRST) {
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
