package org.redrune.game.node.entity.npc;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.Directions.Direction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class NPC extends Entity {
	
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
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public NPC(int id, Location location, Direction direction) {
		super(location);
		this.id = id;
		this.faceDirection = direction.getValue();
	}
	
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
		getRegion().addEntity(this);
		
		setRenderable(true);
	}
	
	@Override
	public void deregister() {
		getRegion().removeEntity(this);
		World.get().getNpcs().remove(this);
		
		setRenderable(false);
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
	public NPC toNPC() {
		return this;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getDefinitions().getName() + ", location=" + getLocation() + ", renderable=" + isRenderable() + "]";
	}
	
	/**
	 * Starts an interaction with the player by facing them
	 *
	 * @param player
	 * 		The player
	 */
	public void startPlayerInteraction(Player player) {
		putAttribute(AttributeKey.INTERACTING_PLAYER, player);
		player.putAttribute(AttributeKey.INTERACTING_NPC, this);
		
		turnTo(player);
	}
	
	/**
	 * Gets the middle world tile
	 */
	public Location getMiddleWorldTile() {
		int size = getSize();
		return new Location(getLocation().getCoordFaceX(size), getLocation().getCoordFaceY(size), getLocation().getPlane());
	}
	
	/**
	 * Ends the interaction with the player. If we're still interacting with them it will stop facing them. If we have
	 * moved onto somebody else, it will not update.
	 *
	 * @param player
	 * 		The player
	 */
	public void endPlayerInteraction(Player player) {
		Player interactingWith = getAttribute(AttributeKey.INTERACTING_PLAYER, null);
		if (interactingWith.equals(player)) {
			turnTo(null);
		}
	}
	
	@Override
	public boolean attackable(Player player) {
		return super.attackable(player);
	}
}
