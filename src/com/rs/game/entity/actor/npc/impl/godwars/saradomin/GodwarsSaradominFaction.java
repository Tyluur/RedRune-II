package com.rs.game.entity.actor.npc.impl.godwars.saradomin;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.entity.actor.Actor;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.Misc;

@SuppressWarnings("serial")
public class GodwarsSaradominFaction extends NPC {

	public GodwarsSaradominFaction(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Actor> getPossibleTargets() {
		ArrayList<Actor> possibleTarget = new ArrayList<Actor>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = World.getPlayers().get(npcIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player
									.withinDistance(
											this,
											getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
													: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
															: 8)
							|| ((!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this && player
									.getAttackedByDelay() > Misc
									.currentTimeMillis())
							|| !clipedProjectile(player, false))
						continue;
					possibleTarget.add(player);
				}
			}
			List<Integer> npcsIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = World.getNPCs().get(npcIndex);
					if (npc == null
							|| npc == this
							|| npc instanceof GodwarsSaradominFaction
							|| npc.isDead()
							|| npc.hasFinished()
							|| !npc.withinDistance(
									this,
									getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
													: 8)
							|| !npc.getDefinitions().hasAttackOption()
							|| ((!isAtMultiArea() || !npc.isAtMultiArea())
									&& npc.getAttackedBy() != this && npc
									.getAttackedByDelay() > Misc
									.currentTimeMillis())
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		return possibleTarget;
	}

}
