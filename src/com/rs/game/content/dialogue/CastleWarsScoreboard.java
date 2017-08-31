package com.rs.game.content.dialogue;

import com.rs.game.content.minigame.CastleWars;

public class CastleWarsScoreboard extends Dialogue {

	@Override
	public void start() {
		CastleWars.viewScoreBoard(player);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();

	}

	@Override
	public void finish() {

	}

}
