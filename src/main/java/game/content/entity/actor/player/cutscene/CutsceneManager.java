package game.content.entity.actor.player.cutscene;

import game.entity.actor.player.Player;

public final class CutsceneManager {
	
	private final Player player;
	
	private Cutscene cutscene;
	
	public CutsceneManager(Player player) {
		this.player = player;
	}
	
	public void process() {
		if (cutscene == null) {
			return;
		}
		if (cutscene.process(player)) {
			return;
		}
		cutscene = null;
	}
	
	public void logout() {
		if (hasCutscene()) {
			cutscene.logout(player);
		}
	}
	
	public boolean hasCutscene() {
		return cutscene != null;
	}
	
	public boolean play(Object key) {
		if (hasCutscene()) {
			return false;
		}
		Cutscene cutscene = (Cutscene) (key instanceof Cutscene ? key : CutscenesHandler.getCutscene(key));
		if (cutscene == null) {
			return false;
		}
		cutscene.createCache(player);
		this.cutscene = cutscene;
		return true;
	}
	
}
