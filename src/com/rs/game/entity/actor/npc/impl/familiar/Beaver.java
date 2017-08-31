package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.skills.woodcutting.Woodcutting;
import com.rs.game.content.skills.summoning.Summoning.Pouches;
import com.rs.game.content.skills.woodcutting.Woodcutting.TreeDefinitions;

public class Beaver extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9181393770444014076L;

	public Beaver(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Multichop";
	}

	@Override
	public String getSpecialDescription() {
		return "Chops a tree, giving the owner its logs. There is also a chance that random logs may be produced.";
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
		return SpecialAttack.OBJECT;
	}

	@Override
	public boolean submitSpecial(Object context) {
		WorldObject object = (WorldObject) context;
		getOwner().getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
		return true;
	}
}
