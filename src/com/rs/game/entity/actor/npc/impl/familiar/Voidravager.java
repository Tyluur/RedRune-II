package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.skills.summoning.Summoning.Pouches;

public class Voidravager extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3950385081972248371L;

	public Voidravager(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Call To Arms";
	}

	@Override
	public String getSpecialDescription() {
		return "Teleports the player to Void Outpost.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextGraphics(new Graphics(1316));
		player.setNextAnimation(new Animation(7660));
		// Magic.sendTeleportSpell(player, upEmoteId, downEmoteId, upGraphicId,
		// downGraphicId, 0, 0, tile, 3, true, Magic.OBJECT_TELEPORT);
		return true;
	}
}
