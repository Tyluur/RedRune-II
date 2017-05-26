package org.redrune.rs2.node.entity.npc;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.world.World;
import org.redrune.rs2.world.map.Location;
import org.redrune.rs2.world.map.region.RegionManager;
import org.redrune.utility.rs.FaceDirection;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class NPC extends Entity {
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public NPC(int id, Location location) {
		super(location);
		this.id = id;
		this.faceDirection = FaceDirection.NORTH.getValue();
	}
	
	/**
	 * The id of the npc
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * The direction the npc is facing
	 */
	@Getter
	@Setter
	private int faceDirection;
	
	/**
	 * The cache definitions of the npc
	 */
	private NPCDefinition definitions;
	
	@Override
	public int getHitpoints() {
		return 0;
	}
	
	@Override
	public int getMaxHitpoints() {
		return 0;
	}
	
	@Override
	public void tick() {
	
	}
	
	@Override
	public void register() {
		super.registerTransients();
		RegionManager.getRegion(getLocation().getX(), getLocation().getY()).addEntity(this);
		setRenderable(true);
	}
	
	@Override
	public void deregister() {
		setRenderable(false);
		
		RegionManager.getRegion(getLocation().getX(), getLocation().getY()).removeEntity(this);
		World.get().getNpcs().remove(this);
	}
	
	@Override
	public int getSize() {
		return getDefinitions().getSize();
	}
	
	/**
	 * Gets the {@link NPCDefinition}s of this npc
	 */
	public NPCDefinition getDefinitions() {
		if (definitions == null) {
			definitions = NPCDefinitionParser.forId(id);
		}
		return definitions;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getDefinitions().getName() + ", renderable=" + isRenderable() + "]";
	}
	
	@Override
	public NPC toNPC() {
		return this;
	}
}
