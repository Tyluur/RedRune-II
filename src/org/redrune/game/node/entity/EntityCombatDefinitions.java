package org.redrune.game.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatType;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class EntityCombatDefinitions {
	
	/**
	 * The attack style the entity is using. Melee for npcs is always on stab [0].
	 */
	@Getter
	private byte attackStyle = 0;
	
	/**
	 * The amount of special energy we have
	 */
	@Getter
	private byte specialEnergy = 100;
	
	/**
	 * If we are to fight back the player who hits us
	 */
	@Getter
	private boolean retaliating = true;
	
	/**
	 * Sets the special activated flag
	 */
	@Getter
	private transient boolean specialActivated = false;
	
	/**
	 * The entity whose definitions these are for
	 */
	@Setter
	private transient Entity entity;
	
	/**
	 * Sends the login refreshing
	 */
	public void sendLogin() {
		refreshAttackStyle();
		refreshRetaliate();
		refreshSpecialEnergy();
	}
	
	/**
	 * Refreshes the attack style
	 */
	private void refreshAttackStyle() {
		final Player player = entity.toPlayer();
		player.getTransmitter().send(new ConfigPacketBuilder(43, attackStyle).build(player));
	}
	
	/**
	 * Refreshes the retaliate button
	 */
	private void refreshRetaliate() {
		final Player player = entity.toPlayer();
		player.getTransmitter().send(new ConfigPacketBuilder(172, retaliating ? 0 : 1).build(player));
	}
	
	/**
	 * Refreshes the special energy
	 */
	private void refreshSpecialEnergy() {
		final Player player = entity.toPlayer();
		player.getTransmitter().send(new ConfigPacketBuilder(300, specialEnergy * 10).build(player));
	}
	
	/**
	 * Changes the attack style
	 *
	 * @param attackStyle
	 * 		The attack style
	 */
	public void changeAttackStyle(byte attackStyle) {
		Player player = entity.toPlayer();
		byte maxSize = 3;
		int weaponId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON);
		CombatType type = StaticCombatFormulae.getCombatType(player);
		String name = weaponId == -1 ? "" : ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		// whips, halberds, range, and magic combat styles only have 3 styles.
		if (weaponId == -1 || type != CombatType.MELEE || name.contains("whip") || name.contains("halberd")) {
			maxSize = 2;
		}
		if (attackStyle > maxSize) {
			attackStyle = maxSize;
		}
		if (this.attackStyle != attackStyle) {
			this.attackStyle = attackStyle;
			refreshAttackStyle();
		}
	}
	
	/**
	 * Toggles the retaliate button
	 */
	public void toggleAutoRetaliate() {
		retaliating = !retaliating;
		entity.toPlayer().stop(true, true, true, false);
		refreshRetaliate();
	}
	
	/**
	 * Reduces the special attack energy by the given amount. This also verifies that we never have < 0 special energy.
	 *
	 * @param amount
	 * 		The amount to reduce it by.
	 */
	public void reduceSpecial(int amount) {
		this.specialEnergy -= amount;
		if (this.specialEnergy <= 0) {
			this.specialEnergy = 0;
		}
		refreshSpecialEnergy();
	}
	
	/**
	 * Sets if the special attack is activated or not
	 *
	 * @param specialActivated
	 * 		The special attack being activated
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
		refreshSpecialActivated();
	}
	
	/**
	 * Refreshes the special attack bar, sending it on or off to the client.
	 */
	private void refreshSpecialActivated() {
		final Player player = entity.toPlayer();
		player.getTransmitter().send(new ConfigPacketBuilder(301, specialActivated ? 1 : 0).build(player));
	}
	
	/**
	 * Sets the amount of special energy we have
	 *
	 * @param specialEnergy
	 * 		The amount
	 */
	public void setSpecialEnergy(byte specialEnergy) {
		this.specialEnergy = specialEnergy;
		refreshSpecialEnergy();
	}
}
