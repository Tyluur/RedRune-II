package org.redrune.game.node.entity.npc;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.npc.data.NPCCharacteristics;
import org.redrune.game.node.entity.npc.data.NPCCombatDefinitions;
import org.redrune.game.node.entity.npc.link.CombatManager;
import org.redrune.game.node.entity.npc.link.DropManager;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Drop;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.repository.npc.characteristic.NPCCharacteristicRepository;
import org.redrune.utility.rs.constant.Directions.Direction;
import org.redrune.utility.tool.Misc;
import org.redrune.utility.tool.RandomFunction;

import java.util.Collections;
import java.util.List;

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
	 * The amount of health points the npc has
	 */
	@Getter
	private int healthPoints = 0;
	
	/**
	 * The cache definitions of the npc
	 */
	private NPCDefinition definitions;
	
	/**
	 * The characteristics of the npc
	 */
	@Getter
	@Setter
	private NPCCharacteristics characteristics;
	
	/**
	 * If the npc respawns
	 */
	@Getter
	@Setter
	private boolean respawnable = true;
	
	/**
	 * The location we were spawned at
	 */
	@Getter
	private final Location spawnLocation;
	
	/**
	 * The instance of the combat manager
	 */
	@Getter
	private final CombatManager combatManager;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public NPC(int id, Location location, Direction direction) {
		super(location);
		this.id = id;
		this.spawnLocation = location;
		this.faceDirection = direction.ordinal();
		this.characteristics = NPCCharacteristicRepository.getCharacteristics(this);
		this.combatManager = new CombatManager(this);
		// we won't ever be in combat with an npc that doesn't have an attack option
		if (getDefinitions().hasAttackOption()) {
			setHealthPoints(getMaxHealth());
		}
	}
	
	@Override
	public int getMaxHealth() {
		return getCombatDefinitions().getHitpoints();
	}
	
	@Override
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}
	
	@Override
	public void receiveHit(Hit hit) {
		// only hit splats we care about are combat ones
		if (!hit.getSplat().isDefaultCombatSplat()) {
			return;
		}
		StaticCombatFormulae.autoRetaliate(hit.getSource(), this);
		// adjust hit so we don't hit too high
		if (hit.getDamage() > getHealthPoints()) {
			hit.setDamage(getHealthPoints());
		}
		// if our hp is so low that we're dead
		boolean dead = (healthPoints = healthPoints - hit.getDamage()) <= 0;
		// fires death event
		if (dead) {
			checkDeathEvent();
		}
	}
	
	@Override
	public boolean fighting() {
		return combatManager.getCombat().fighting();
	}
	
	@Override
	protected void checkDeathEvent() {
		// we won't ever die if we don't have an attack option
		if (!combatManager.isHasAttackOption()) {
			return;
		}
		super.checkDeathEvent();
	}
	
	@Override
	public void fireDeathEvent() {
		System.out.println("We are dying! [" + this + "]");
		sendAnimation(getCombatDefinitions().getDeathAnim());
		getMovement().resetWalkSteps();
		int goalTicks = getCombatDefinitions().getDeathDelay();
		if (goalTicks <= 1) {
			goalTicks = 2;
		}
		SystemManager.getScheduler().schedule(new ScheduledTask(1, goalTicks) {
			
			@Override
			public void run() {
				System.out.println(this.toString());
				if (getTicksPassed() == 1) {
					sendAnimation(getCombatDefinitions().getDeathAnim());
				} else if (getTicksPassed() == getGoalTicks()) {
					deregister();
					sendDrops();
					restoreAll();
					// only fire the respawn event for a respawnable npc
					if (isRespawnable()) {
						fireRespawnEvent(getId(), getCombatDefinitions().getRespawnDelay(), spawnLocation, Direction.values()[faceDirection]);
					}
				}
			}
		});
	}
	
	/**
	 * Sends the item drops to the ground. We find the player who did the most damage to us in order to find who to
	 * display the drops to.
	 */
	private void sendDrops() {
		Entity mostDamageEntity = getHitMap().getMostDamageEntity();
		// the killer wasn't set for some reason
		if (mostDamageEntity == null) {
			return;
		}
		// TODO: familiar did the damage so we set the killer to the owner's familiar
		if (!(mostDamageEntity.isPlayer())) {
			System.out.println("mostDamage = " + mostDamageEntity);
			return;
		}
		Player killer = mostDamageEntity.toPlayer();
		List<Drop> dropList = DropManager.generateDrops(killer, this, characteristics.getDrops());
		// TODO: send drops to clan
		dropList.forEach(drop -> sendDrop(killer, drop));
		// sends the charm drop
		dropCharms(killer);
	}
	
	/**
	 * Drops a random charm
	 *
	 * @param killer
	 * 		The killer
	 */
	private void dropCharms(Player killer) {
		List<Drop> charms = characteristics.getCharmDrops();
		if (charms.isEmpty()) {
			return;
		}
		Collections.shuffle(charms);
		double random = (RandomFunction.getRandomDouble(99) + 1) / 100;
		for (Drop charm : charms) {
			if (random <= charm.getRate()) {
				sendDrop(killer, charm);
				break;
			}
		}
	}
	
	/**
	 * Sends the drop to the floor for the killer
	 *
	 * @param killer
	 * 		The killer
	 * @param drop
	 * 		The drop
	 */
	private void sendDrop(Player killer, Drop drop) {
		Item item;
		// if the item is stack-able we generate a different amount
		if (ItemDefinitionParser.forId(drop.getItemId()).isStackable()) {
			item = new Item(drop.getItemId(), Misc.random(drop.getMinAmount(), drop.getMaxAmount()));
		} else {
			item = new Item(drop.getItemId(), drop.getMinAmount() + Misc.getRandom(drop.getExtraAmount()));
		}
		RegionManager.addFloorItem(item.getId(), item.getAmount(), 200, getDropTile(), killer.getDetails().getUsername());
	}
	
	/**
	 * Gets the tile we should drop the items on
	 */
	private Location getDropTile() {
		return new Location(getLocation().getCoordFaceX(getSize()), getLocation().getCoordFaceY(getSize()), getLocation().getPlane());
	}
	
	/**
	 * Fires the respawn event
	 *
	 * @param id
	 * 		The id of the npc respawning
	 * @param delay
	 * 		The delay to wait
	 * @param location
	 * 		The location to respawn them
	 * @param direction
	 * 		The direction to face when respawned
	 */
	private static void fireRespawnEvent(int id, int delay, Location location, Direction direction) {
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				World.get().addNPC(id, location, direction);
			}
		});
	}
	
	@Override
	public void restoreAll() {
		setHealthPoints(getMaxHealth());
		sendAnimation(-1);
		removeAttribute("dying");
	}
	
	@Override
	public void tick() {
		// before anything else we must make sure we aren't dead
		if (isDead() && isDying()) {
			return;
		}
		super.tick();
		// we aren't in combat
		combatManager.getCombat().process();
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getDefinitions().getName() + ", location=" + getLocation() + ", renderable=" + isRenderable() + "]";
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
	public boolean attackable(Entity entity) {
		// if we don't have an attack option
		if (!combatManager.isHasAttackOption()) {
			return false;
		}
		// TODO implement slayer requirements in the npc classes
		return true;
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
	public NPC toNPC() {
		return this;
	}
	
	@Override
	public int getSize() {
		return getDefinitions().getSize();
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
	
	/**
	 * Gets the combat definitions of the npc. <p>This value is never null because on construct we always set a npc
	 * combat definition regardless of whether they exist in file.</p>
	 */
	public NPCCombatDefinitions getCombatDefinitions() {
		return characteristics.getCombatDefinitions().get(getId());
	}
	
	/**
	 * Gets the bonus at an index. <p>To find out the sorting of the bonuses list, see {@link
	 * org.redrune.utility.rs.constant.BonusConstants} order. NPC bonuses only reach the 10th index [range defence]. The
	 * bonuses are always defined because on construct we set the bonuses regardless of whether they exist in file.</p>
	 *
	 * @param index
	 * 		The index
	 */
	public int getBonus(int index) {
		int[] bonuses = getBonuses();
		// verifying that we're in bounds
		if (index < 0 || index >= bonuses.length) {
			return 0;
		} else {
			return bonuses[index];
		}
	}
	
	/**
	 * Gets the bonuses array, maxed length of 10.
	 */
	public int[] getBonuses() {
		return characteristics.getBonuses().get(getId());
	}
}
