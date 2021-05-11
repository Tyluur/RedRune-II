package game.content.entity.actor.player.cutscene.actions;

import game.entity.actor.player.Player;

public class PlayerMusicEffectAction extends CutsceneAction {

	private final int id;

	public PlayerMusicEffectAction(int id, int actionDelay) {
		super(-1, actionDelay);
		this.id = id;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.getPackets().sendMusicEffect(id);
	}

}
