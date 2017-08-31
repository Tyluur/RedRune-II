package com.rs.game.content.controler.impl;

import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.controler.Controler;

public class CorpBeastControler extends Controler {

	@Override
	public void start() {

	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 37929) {
			removeControler();
			player.stopAll();
			player.setNextWorldTile(new WorldTile(2970, 4384, 0));
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
	}

	@Override
	public boolean sendDeath() {
		removeControler();
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
