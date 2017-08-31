package com.rs.game.entity.actor.npc.impl.others;

import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.utility.Misc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Tyler
 * 
 */
public class Pets extends NPC {
	/**
	 * Objects
	 */
	private static final long serialVersionUID = 1325133366821220445L;
	private transient Player owner;
	public boolean spawnedPet = false;
	private int npcId;

	public enum Pet {
		SPARKLES(2267, 22973), JAD(3603, 21512);
		private static final Map<Integer, Pet> item = new HashMap<Integer, Pet>();

		static {
			for (Pet itemId : Pet.values()) {
				item.put(itemId.itemId, itemId);
			}
		}

		public static Pet forId(int id) {
			return item.get(id);
		}

		private int npcId;
		private int itemId;

		Pet(int npcId, int itemId) {
			this.npcId = npcId;
			this.itemId = itemId;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getItemId() {
			return itemId;
		}

		public static Map<Integer, Pet> getItem() {
			return item;
		}
	}

	public Pets(int npcId, Player owner, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(npcId, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setRun(true);
		this.owner = owner;
		this.npcId = npcId;
		spawnPet(npcId, owner);
		call(true);
	}

	public void spawnPet(int npcId, Player owner) {
		if (owner.getPet() != null) {
			return;
		}
		spawnedPet = true;
		World.getNPCs().add(this);
		setNextGraphics(new Graphics(getDefinitions().size <= 1 ? 1314 : 1315));
		owner.setPetFollow(owner.getIndex());
		owner.setPet(this);
	}

	public void processNPC() {
		if (isDead())
			return;
		if (!withinDistance(owner, 4)) {
			call(false);
			return;
		}
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (ticks == 2)
				owner.getPackets().sendGameMessage(
						"You have 1 minute before your familiar vanishes.");
			else if (ticks == 1)
				owner.getPackets().sendGameMessage(
						"You have 30 seconds before your familiar vanishes.");
			else if (ticks == 0) {
				dissmissPet(false);
				return;
			}
		}
		sendFollow();
	}

	public void call() {
		if (getAttackedBy() != null
				&& getAttackedByDelay() > System.currentTimeMillis()) {
			owner.getPackets().sendGameMessage(
					"You cant call your familiar while it under combat.");
			return;
		} else {
			call(false);
			return;
		}
	}

	public void call(boolean login) {
		int size = getSize();
		if (login) {
			checkNearDirs = Misc.getCoordOffsetsNear(size);
		} else {
			removeTarget();
		}
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			WorldTile tile = new WorldTile(new WorldTile(owner.getX()
					+ checkNearDirs[0][dir], owner.getY()
					+ checkNearDirs[1][dir], owner.getPlane()));
			if (!World.canMoveNPC(tile.getPlane(), tile.getX(), tile.getY(),
					size))
				continue;
			teleTile = tile;
			break;
		}

		if (login || teleTile != null)
			WorldTasksManager.schedule(new WorldTask() {

				public void run() {
					setNextGraphics(new Graphics(
							getDefinitions().size <= 1 ? 1314 : 1315));
				}

			});
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				owner.getPackets().sendGameMessage(
						"Theres not enough space for your familiar appear.");
				sentRequestMoveMessage = true;
			}
			return;
		} else {
			sentRequestMoveMessage = false;
			setNextWorldTile(teleTile);
			return;
		}
	}

	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceActor(owner);
		if (getFreezeDelay() >= System.currentTimeMillis())
			return;
		int size = getSize();
		int distanceX = owner.getX() - getX();
		int distanceY = owner.getY() - getY();
		if (distanceX < size && distanceX > -1 && distanceY < size
				&& distanceY > -1 && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + 1, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size - 1, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(owner.getX(), getY() + 1)) {
						resetWalkSteps();
						if (!addWalkSteps(owner.getX(), getY() - size - 1))
							return;
					}
				}
			}
			return;
		}
		if (!clipedProjectile(owner, true) || distanceX > size
				|| distanceX < -1 || distanceY > size || distanceY < -1) {
			resetWalkSteps();
			addWalkStepsInteract(owner.getX(), owner.getY(), 2, size, true);
			return;
		} else {
			resetWalkSteps();
			return;
		}
	}

	public void respawnFamiliar(Player owner) {
		this.owner = owner;
		initEntity();
		deserialize();
		owner.setPetFollow(owner.getIndex());
		call(true);
	}

	public void dissmissPet(boolean loggedOut) {
		if (!loggedOut) {
			owner.getInventory().addItem(owner.getPetId(), 1);
			owner.setPetId(0);
			owner.setPet(null);
			finish();
		}
		finish();
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	private int ticks;
	private int trackTimer;
	private transient boolean sentRequestMoveMessage;
	private transient int checkNearDirs[][];
}