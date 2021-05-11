package game.entity.actor.npc.impl.others;

import game.content.entity.actor.player.skills.hunter.Hunter.HunterNPC;
import game.entity.actor.npc.NPC;
import game.entity.actor.player.Player;
import game.entity.actor.player.link.OwnedObjectManager;
import game.entity.actor.player.link.OwnedObjectManager.ConvertEvent;
import game.entity.object.WorldObject;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.constants.SkillConstants;

import java.util.List;

@SuppressWarnings("serial")
public class HuntNPC extends NPC {
	
	public HuntNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		List<WorldObject> objects = RegionManager.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (WorldObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object, new WorldObject(info.getTransformObjectId(), 10, 0, this.getX(), this.getY(), this.getPlane()), new ConvertEvent() {
						@Override
						public boolean canConvert(Player player) {
							if (player == null) {
								return false;
							}
							return player.getSkills().getLevel(SkillConstants.HUNTER) >= info.getLevel();
						}
					})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}
}
