package org.redrune.rs2.node.entity.player;

import org.redrune.network.packet.PacketRepository;
import org.redrune.network.packet.PacketSender;
import org.redrune.network.packet.event.PacketEvent;
import org.redrune.network.protocol.login.LoginReadEvent.LoginRequest;
import org.redrune.network.session.Session;
import org.redrune.network.session.impl.GameSession;
import org.redrune.rs2.GameConstants;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.player.components.PlayerDetails;
import org.redrune.rs2.node.entity.player.components.PlayerEquipment;
import org.redrune.rs2.node.entity.player.components.PlayerRenderData;
import org.redrune.rs2.node.entity.player.components.PlayerSkills;
import org.redrune.rs2.node.entity.player.components.PlayerVariables;
import org.redrune.rs2.node.entity.player.components.managers.InterfaceManager;
import org.redrune.rs2.node.entity.player.render.PlayerRendering;
import org.redrune.rs2.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.rs2.world.Location;
import org.redrune.rs2.world.SequencialUpdate;
import org.redrune.rs2.world.World;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.SkillConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * The player that renderable in the game.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Player extends Entity {
	
	/**
	 * The credentials of the player
	 */
	@Getter
	private final PlayerDetails details;
	
	/**
	 * The Interface Manager
	 */
	@Getter
	private InterfaceManager interfaceManager;
	
	/**
	 * The Packet Sender.
	 */
	@Getter
	private PacketSender packetSender;
	
	/**
	 * The variables of the player that are saved
	 */
	@Getter
	private final PlayerVariables variables;
	
	/**
	 * The skills of the player
	 */
	@Getter
	private final PlayerSkills skills;
	
	/**
	 * The equipment of the player
	 */
	@Getter
	private final PlayerEquipment equipment = new PlayerEquipment();
	
	/**
	 * The networkSession attached to the player
	 */
	@Getter
	@Setter
	private GameSession session;
	
	
	/**
	 * The render information object
	 */
	@Getter
	private transient PlayerRenderData renderData;
	
	public Player(String username, String password) {
		super(new Location(3333, 3333));
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username, password);
		this.variables = new PlayerVariables();
//		this.setSession(session);
//		this.getSession().setPlayer(this);
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		if (getAttribute(AttributeKey.MAP_REGION_CHANGED, false)) {
			getPacketSender().sendRegion(true);
		}
		getPacketSender().sendPlayerUpdate();
//		getTransmitter().send(new PlayerRendering().build(this));
		getUpdateMasks().register(new AppearanceUpdate(this));
	}
	
	
	public void sendLogin(Session session, LoginRequest request) {
		this.session = (GameSession) session;
		switch (request) {
		case REQUEST_LOBBY:
			packetSender.sendConfig(1749, 3977);
			packetSender.sendConfig(1754, 20);
			packetSender.sendConfig(2863, 117440512);
			packetSender.sendConfig(2897, -805091453);
			packetSender.sendConfig(3184, 1);
			break;
		case REQUEST_WORLD:
			register();
//			init();
//			refreshRegion(true);
//			GameSettings.getWorld().getRegionManager().refreshRegionForEntity(this);
//			GameSettings.getWorld().getRegionManager().refreshSpawnedObjects(this);


			// inventory.addItem(new Item(26589, 1));
			// inventory.addItem(new Item(20769, 1));
			// inventory.addItem(new Item(26591, 1));
			// inventory.addItem(new Item(26124, 1));
			// inventory.addItem(new Item(26126, 1));
			// inventory.addItem(new Item(26136, 1));
			// inventory.addItem(new Item(26140, 1));
			// inventory.addItem(new Item(590, 1));
			// inventory.addItem(new Item(1513, 1));
			// inventory.addItem(new Item(6739, 1));
			// inventory.addItem(new Item(946, 1));
//			if (account.getRights().equals(Rights.ADMINISTRATOR)) {
//				skillManager.masterLevels();
//			}
//
//			packetSender.sendConfig(1295, 1000);// TUTORIAL
//			inventory.refreshInventory();
//			equipment.refreshEquipment();
//			skillManager.refreshSkills();
//			emotesManager.refreshEmotes();
//			actionBar.refresh();
//			abilityBook.refreshBook();
//			moneyPouch.refreshMoneyPouch();
//			propertiesManager.refreshRunEnergy();
//			combatProperties.setMaxHealth(((skillManager.getLevel(Skill.CONSTITUTION) * 40) * 2)
//					+ combatProperties.getBonuses().getLifepoints());
//			combatProperties.refreshHealth();
//			packetSender.sendConfig(3274, (skillManager.getLevel(Skill.PRAYER) * 100));
			packetSender.sendPlayerOption(1, "Follow");
			packetSender.sendPlayerOption(2, "Trade");
			packetSender.sendPlayerOption(3, "Req Assist");
			packetSender.sendOnlineStatus();
			packetSender.sendUnlockFriendsList();
			interfaceManager.sendInterfaces();
//			renderProcessor.getAppearance().renderBlock(null);
//			setNextLocation(new Location(3043, 4860, 0));
			packetSender.sendGlobalConfig(823, 1);// EXTRAS CONFIG
			packetSender.sendCS2Script(1211, new Object[] { "Welcome to " + GameConstants.SERVER_NAME + ".", 0, -120, 0 });
			packetSender.sendGameMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
			break;
		default:
			break;
		}
	}
	
	public void addPlayerToWorld(LoginRequest request) {
		switch (request) {
		case REQUEST_LOBBY:
			break;
		case REQUEST_WORLD:
			World.get().getPlayers().add(this);
			break;
		}
	}
	
	
	@Override
	public void register() {
		registerTransients();
		World.get().getPlayers().add(this);
		SequencialUpdate.getRenderablePlayers().add(this);
		getInterfaceManager().sendInterfaces();
//		getPacketSender()
//		transmitter.sendLoginComponents();
		equipment.sendFullContainer();
		skills.refreshAll();
		setRenderable(true);
		
		System.out.println("Player registered:\t" + this);
	}
	
	@Override
	public void deregister() {
		World.get().getPlayers().remove(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		setRenderable(false);
		
		System.out.println("Player deregistered:\t" + this);
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	/**
	 * Generates the transient objects (objects which will not save)
	 */
	@Override
	public void registerTransients() {
		super.registerTransients();
		this.packetSender = new PacketSender(this);
		this.renderData = new PlayerRenderData(this);
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
	}
	
	@Override
	public Player toPlayer() {
		return this;
	}
	
	@Override
	public int getHitpoints() {
		return variables.getHealthPoints();
	}
	
	@Override
	public int getMaxHitpoints() {
		return skills.getLevelForXp(SkillConstants.HITPOINTS) * 10;
	}
	
	@Override
	public void tick() {
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", index=" + getIndex() + ", right=" + details.getDominantRight() + "]";
	}
	
	public void writePacket(Class<?> clazz, PacketEvent event) {
		session.write(clazz, PacketRepository.writePacket(event));
	}
}