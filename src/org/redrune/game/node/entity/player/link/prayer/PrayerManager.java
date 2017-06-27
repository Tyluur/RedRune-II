package org.redrune.game.node.entity.player.link.prayer;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.network.rs666.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigFilePacketBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.HeadIcons.PrayerIcon;
import org.redrune.utility.rs.constant.PrayerConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static org.redrune.game.node.entity.player.link.prayer.Prayer.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public final class PrayerManager implements SkillConstants {
	
	/**
	 * The list of prayers that are active
	 */
	private final CopyOnWriteArraySet<Prayer> activePrayers = new CopyOnWriteArraySet<>();
	
	/**
	 * The set of quick prayers
	 */
	private final CopyOnWriteArraySet<Prayer> quickPrayers = new CopyOnWriteArraySet<>();
	
	/**
	 * The stat adjustments of the prayers
	 */
	private final int[] adjustments = new int[11];
	
	/**
	 * The book the player is using
	 */
	private PrayerBook book = PrayerBook.REGULAR;
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * If quick prayers are being set
	 */
	private transient boolean settingQuickPrayers;
	
	/**
	 * The id of the head icon
	 */
	@Getter
	private transient PrayerIcon icon = PrayerIcon.NONE;
	
	/**
	 * The next drain information per prayer
	 */
	private transient long[] nextDrain;
	
	/**
	 * If we're boosted because of leeches
	 */
	@Getter
	@Setter
	private transient boolean boostedLeech;
	
	/**
	 * Sends login configurations for prayer books
	 */
	public void sendLoginConfigurations() {
		this.nextDrain = new long[30];
		sendPrayerSelection();
		sendBook();
		resetStatAdjustments();
	}
	
	/**
	 * Sets the book the player is using
	 *
	 * @param book
	 * 		The book
	 */
	public void setBook(PrayerBook book) {
		this.book = book;
		sendBook();
	}
	
	/**
	 * Sends the prayer book we're on
	 */
	private void sendBook() {
		player.getTransmitter().send(new ConfigPacketBuilder(1584, book == PrayerBook.CURSES ? 1 : 0).build(player));
	}
	
	/**
	 * Handles the setting of prayers
	 *
	 * @param componentId
	 * 		The component clicked
	 * @param slotId
	 * 		The slot clicked
	 */
	public void handlePrayerSettings(int componentId, int slotId) {
		if (componentId == 8) {
			Optional<Prayer> optional = findPrayerBySlot(slotId, book);
			if (!optional.isPresent()) {
				System.out.println("Unable to find prayer... [" + componentId + "," + slotId + "]");
				return;
			}
			if (getPrayerPoints() <= 0) {
				player.getTransmitter().sendMessage("You have ran out of prayer points.", false);
				return;
			}
			Prayer prayer = optional.get();
			if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < prayer.getPrayerLevelRequired()) {
				player.getTransmitter().sendMessage("You need a prayer level of " + prayer.getPrayerLevelRequired() + " to use " + prayer.getName() + ".", false);
				return;
			}
			if (!prayer.canActivate(player)) {
				return;
			}
			// deactivates if active, otherwise returns false
			// and we turn on the prayer
			boolean activate = false;
			if (!deactivatePrayer(prayer)) {
				closePrayers(prayer, false);
				fireAdjustmentListeners(prayer);
				activePrayers.add(prayer);
				activate = true;
			}
			// so the prayer was activated
			if (activate) {
				prayer.activate(player);
				resetDrainPrayer(prayer.getSlotId());
			}
			resetStatAdjustments();
			refreshActivatedConfigs();
			updateHeadIcon();
		} else if (componentId == 42) {
			Optional<Prayer> optional = findPrayerBySlot(slotId, book);
			if (!optional.isPresent()) {
				System.out.println("Unable to find prayer... [" + componentId + "," + slotId + "]");
				return;
			}
			Prayer prayer = optional.get();
			if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < prayer.getPrayerLevelRequired()) {
				player.getTransmitter().sendMessage("You need a prayer level of " + prayer.getPrayerLevelRequired() + " to use " + prayer.getName() + ".", false);
				return;
			}
			if (!prayer.canActivate(player)) {
				return;
			}
			// if we're not removing it, we're adding it
			if (!removeQuickPrayer(prayer)) {
				closePrayers(prayer, true);
				quickPrayers.add(prayer);
			}
			refreshActivatedConfigs();
		} else if (componentId == 43) {
			settingQuickPrayers = false;
			sendPrayerSelection();
			if (settingQuickPrayers) {
				player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
			}
			sendAccessMasks();
		}
	}
	
	/**
	 * Fires the adjustment modifications for the prayer
	 *
	 * @param prayer
	 * 		The prayer
	 */
	private void fireAdjustmentListeners(Prayer prayer) {
		int prayerId = prayer.getSlotId();
		if (prayerId == 1) {
			if (adjustments[0] > 0) {
				player.getTransmitter().sendMessage("Your Attack is now unaffected by sap and leech curses.", true);
			}
			adjustStat(0, 0);
			adjustStat(1, 0);
			adjustStat(2, 0);
			adjustments[0] = 0;
		} else if (prayerId == 2) {
			if (adjustments[1] > 0) {
				player.getTransmitter().sendMessage("Your Range is now unaffected by sap and leech curses.", true);
			}
			adjustStat(2, 0);
			adjustStat(4, 0);
			adjustments[1] = 0;
		} else if (prayerId == 3) {
			if (adjustments[2] > 0) {
				player.getTransmitter().sendMessage("Your Magic is now unaffected by sap and leech curses.", true);
			}
			adjustStat(2, 0);
			adjustStat(5, 0);
			adjustments[2] = 0;
		} else if (prayerId == 10) {
			if (adjustments[3] > 0) {
				player.getTransmitter().sendMessage("Your Attack is now unaffected by sap and leech curses.", true);
			}
			adjustStat(0, 0);
			adjustments[3] = 0;
		} else if (prayerId == 11) {
			if (adjustments[4] > 0) {
				player.getTransmitter().sendMessage("Your Ranged is now unaffected by sap and leech curses.", true);
			}
			adjustStat(4, 0);
			adjustments[4] = 0;
		} else if (prayerId == 12) {
			if (adjustments[5] > 0) {
				player.getTransmitter().sendMessage("Your Magic is now unaffected by sap and leech curses.", true);
			}
			adjustStat(5, 0);
			adjustments[5] = 0;
		} else if (prayerId == 13) {
			if (adjustments[6] > 0) {
				player.getTransmitter().sendMessage("Your Defence is now unaffected by sap and leech curses.", true);
			}
			adjustStat(2, 0);
			adjustments[6] = 0;
		} else if (prayerId == 14) {
			if (adjustments[7] > 0) {
				player.getTransmitter().sendMessage("Your Strength is now unaffected by sap and leech curses.", true);
			}
			adjustStat(1, 0);
			adjustments[7] = 0;
		} else if (prayerId == 19) {
			adjustments[8] = 0;
			adjustments[9] = 0;
			adjustments[10] = 0;
			adjustStat(0, 0);
			adjustStat(1, 0);
			adjustStat(2, 0);
		}
	}
	
	/**
	 * Refreshes the stat adjustment panel beneath the prayers
	 */
	private void resetStatAdjustments() {
		for (int i = 0; i < 5; i++) {
			adjustStat(i, 0);
		}
	}
	
	/**
	 * Modifies the stat adjustment on the client
	 *
	 * @param stat
	 * 		The stat
	 * @param percentage
	 * 		The percentage
	 */
	public void adjustStat(int stat, int percentage) {
		player.getTransmitter().send(new ConfigFilePacketBuilder(6857 + stat, 30 + percentage).build(player));
	}
	
	/**
	 * Increases the leech bonus
	 *
	 * @param bonus
	 * 		The amount to leech by
	 */
	public void increaseLeechBonus(int bonus, Player from) {
		PrayerManager fromP = from.getManager().getPrayers();
		
		// reducing the bonuses for the player we leech from
		fromP.adjustments[bonus]--;
		
		adjustments[bonus]++;
		if (bonus == 0) {
			for (int i = 0; i <= 2; i++) {
				adjustStat(i, adjustments[bonus]);
			}
			for (int i = 0; i <= 2; i++) {
				fromP.adjustStat(i, fromP.adjustments[bonus]);
			}
		} else if (bonus == 1) {
			adjustStat(2, adjustments[bonus]);
			adjustStat(3, adjustments[bonus]);
			
			fromP.adjustStat(2, fromP.adjustments[bonus]);
			fromP.adjustStat(3, fromP.adjustments[bonus]);
		} else if (bonus == 2) {
			adjustStat(2, adjustments[bonus]);
			adjustStat(4, adjustments[bonus]);
			
			fromP.adjustStat(2, fromP.adjustments[bonus]);
			fromP.adjustStat(4, fromP.adjustments[bonus]);
		} else if (bonus == 3) {
			adjustStat(0, adjustments[bonus]);
			
			fromP.adjustStat(0, fromP.adjustments[bonus]);
		} else if (bonus == 4) {
			adjustStat(3, adjustments[bonus]);
			
			fromP.adjustStat(3, fromP.adjustments[bonus]);
		} else if (bonus == 5) {
			adjustStat(4, adjustments[bonus]);
			
			fromP.adjustStat(4, fromP.adjustments[bonus]);
		} else if (bonus == 6) {
			adjustStat(2, adjustments[bonus]);
			
			fromP.adjustStat(2, fromP.adjustments[bonus]);
		} else if (bonus == 7) {
			adjustStat(1, adjustments[bonus]);
			
			fromP.adjustStat(1, fromP.adjustments[bonus]);
		}
	}
	
	/**
	 * Increases the players turmoil bonuses
	 *
	 * @param target
	 * 		The player we're fighting against
	 */
	public void increaseTurmoilBonus(Player target) {
		adjustments[8] = (int) ((100 * Math.floor(0.15 * target.getSkills().getLevelForXp(SkillConstants.ATTACK))) / target.getSkills().getLevelForXp(SkillConstants.ATTACK));
		adjustments[9] = (int) ((100 * Math.floor(0.15 * target.getSkills().getLevelForXp(SkillConstants.DEFENCE))) / target.getSkills().getLevelForXp(SkillConstants.DEFENCE));
		adjustments[10] = (int) ((100 * Math.floor(0.1 * target.getSkills().getLevelForXp(SkillConstants.STRENGTH))) / target.getSkills().getLevelForXp(SkillConstants.STRENGTH));
		adjustStat(0, adjustments[8]);
		adjustStat(1, adjustments[10]);
		adjustStat(2, adjustments[9]);
	}
	
	/**
	 * If the stat adjustments for turmoil reached their max values
	 *
	 * @param bonus
	 * 		The bonus slot
	 */
	public boolean reachedMax(int bonus) {
		return bonus != 8 && bonus != 9 && bonus != 10 && adjustments[bonus] >= 20;
	}
	
	/**
	 * Gets the amount of prayer points
	 */
	public int getPrayerPoints() {
		return player.getVariables().getPrayerPoints();
	}
	
	/**
	 * Deactivates a prayer if it is active
	 *
	 * @param prayer
	 * 		The prayer
	 * @return {@code Boolean.TRUE} if it was active and we deactivated it.
	 */
	private boolean deactivatePrayer(Prayer prayer) {
		if (prayerOn(prayer)) {
			activePrayers.remove(prayer);
			return true;
		}
		return false;
	}
	
	/**
	 * If the quick prayer was removed
	 *
	 * @param prayer
	 * 		The prayer to remove
	 */
	private boolean removeQuickPrayer(Prayer prayer) {
		return quickPrayers.remove(prayer);
	}
	
	/**
	 * Closes the prayers that this prayer requires closed
	 *
	 * @param prayer
	 * 		The prayer
	 * @param useQuickPrayers
	 * 		If we are closing quick prayers
	 */
	private void closePrayers(Prayer prayer, boolean useQuickPrayers) {
		int[][] close = prayer.getPrayersToClose();
		Set<Prayer> prayerSet = new TreeSet<>();
		for (int[] closeArray : close) {
			for (int closeId : closeArray) {
				Optional<Prayer> optional = findPrayerBySlot(closeId, prayer.getBook());
				if (!optional.isPresent()) {
					System.out.println("Unable to find prayer by slot " + closeId);
					continue;
				}
				prayerSet.add(optional.get());
			}
		}
		if (prayerSet.isEmpty()) {
			return;
		}
		for (Prayer toClose : prayerSet) {
			((useQuickPrayers ? quickPrayers : activePrayers)).remove(toClose);
		}
	}
	
	/**
	 * Resets the prayer drain statistics
	 *
	 * @param index
	 * 		The index of the prayer
	 */
	private void resetDrainPrayer(int index) {
		long duration = (long) (System.currentTimeMillis() + (PrayerConstants.DRAIN_RATES[book.ordinal()][index] * 1000));
		double bonus = player.getEquipment().getBonus(BonusConstants.PRAYER_BONUS);
		nextDrain[index] = (long) (duration + (bonus * 50));
	}
	
	/**
	 * Refreshes the configs for activated prayers. This will show the colour behind the prayer indicating that it's on.
	 */
	private void refreshActivatedConfigs() {
		int value = 0;
		for (Prayer prayer : (settingQuickPrayers ? quickPrayers : activePrayers)) {
			if (prayer.getBook() != book) {
				continue;
			}
			value += prayer.getActivationConfig();
		}
		int configId = book == PrayerBook.CURSES ? (settingQuickPrayers ? 1587 : 1582) : (settingQuickPrayers ? 1397 : 1395);
		player.getTransmitter().send(new ConfigPacketBuilder(configId, value).build(player));
		// sets the orb to on/off
		if (!settingQuickPrayers) {
			player.getTransmitter().send(new CS2ConfigBuilder(182, value == 0 ? 0 : 1).build(player));
		}
	}
	
	/**
	 * Updates the head icon
	 */
	private void updateHeadIcon() {
		if (book == PrayerBook.REGULAR) {
			if (prayerOn(PROTECT_FROM_SUMMONING)) {
				if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.MAGIC_SUMMONING);
					return;
				} else if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.RANGE_SUMMONING);
					return;
				} else if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.MELEE_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.SUMMONING);
					return;
				}
			} else {
				if (prayerOn(PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.PROTECT_FROM_MAGIC);
					return;
				} else if (prayerOn(PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.PROTECT_FROM_RANGE);
					return;
				} else if (prayerOn(PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.PROTECT_FROM_MELEE);
					return;
				} else if (prayerOn(RETRIBUTION)) {
					setIcon(PrayerIcon.RETRIBUTION);
					return;
				} else if (prayerOn(REDEMPTION)) {
					setIcon(PrayerIcon.REDEMPTION);
					return;
				} else if (prayerOn(SMITE)) {
					setIcon(PrayerIcon.SMITE);
					return;
				}
			}
		} else {
			if (prayerOn(DEFLECT_SUMMONING)) {
				if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE_AND_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.DEFLECT_SUMMONING);
					return;
				}
			} else {
				if (prayerOn(DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGIC);
					return;
				} else if (prayerOn(DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE);
					return;
				} else if (prayerOn(DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE);
					return;
				} else if (prayerOn(WRATH)) {
					setIcon(PrayerIcon.WRATH);
					return;
				} else if (prayerOn(SOULSPLIT)) {
					setIcon(PrayerIcon.SOULSPLIT);
					return;
				}
			}
		}
		setIcon(PrayerIcon.NONE);
	}
	
	/**
	 * Checks if a prayer is currently on
	 *
	 * @param prayer
	 * 		The prayer
	 */
	public boolean prayerOn(Prayer prayer) {
		return activePrayers.contains(prayer);
	}
	
	/**
	 * Checks if a list of prayers are active
	 *
	 * @param prayers
	 * 		The prayers
	 */
	public boolean prayersAreActive(Prayer... prayers) {
		for (Prayer prayer : prayers) {
			if (!prayerOn(prayer)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the icon
	 *
	 * @param icon
	 * 		The icon to set
	 */
	public void setIcon(PrayerIcon icon) {
		this.icon = icon;
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	/**
	 * This is used to process the draining of prayers
	 */
	public void process() {
		if (getPrayersActiveCount() == 0) {
			return;
		}
		long time = System.currentTimeMillis();
		int drain = 0;
		int bonus = player.getEquipment().getBonus(BonusConstants.PRAYER_BONUS);
		int hatId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_HAT);
		if (hatId >= 18744 && hatId <= 18746) {
			bonus += 15;
		}
		for (Prayer prayer : activePrayers) {
			int index = prayer.getSlotId();
			long drainTimer = nextDrain[index];
			if (drainTimer == 0 || drainTimer > time) {
				continue;
			}
			int rate = (int) ((PrayerConstants.DRAIN_RATES[book.ordinal()][index] * 1000) + (bonus * 50));
			int passedTime = (int) (time - drainTimer);
			drain++;
			int count = 0;
			while (passedTime >= rate && count++ < 10) {
				drain++;
				passedTime -= rate;
			}
			nextDrain[index] = (time + rate) - passedTime;
		}
		if (drain > 0) {
			drainPrayer(drain);
		}
		setBoostedLeech(false);
	}
	
	/**
	 * Gets the amount of prayers that are active
	 */
	public int getPrayersActiveCount() {
		return activePrayers.size();
	}
	
	/**
	 * Gets the amount of quick prayers we have
	 */
	public int getQuickPrayersCount() {
		return quickPrayers.size();
	}
	
	/**
	 * Drains the prayer by the amount
	 *
	 * @param amount
	 * 		The amount to drain
	 * @return {@code Boolean.TRUE} if the prayers were all closed
	 */
	private boolean drainPrayer(int amount) {
		int newAmount = getPrayerPoints() - amount;
		if (newAmount < 0) {
			newAmount = 0;
		}
		player.getVariables().setPrayerPoints(newAmount);
		refreshPrayerPoints();
		if (newAmount == 0) {
			activePrayers.forEach(this::deactivatePrayer);
			player.getTransmitter().sendMessage("You have ran out of prayer points.");
			updateHeadIcon();
			refreshActivatedConfigs();
			return true;
		}
		return false;
	}
	
	/**
	 * Refreshes the players prayer points
	 */
	private void refreshPrayerPoints() {
		player.getTransmitter().send(new ConfigPacketBuilder(2382, player.getVariables().getPrayerPoints()).build(player));
	}
	
	/**
	 * Toggles the quick prayer setting
	 */
	public void toggleQuickPrayers() {
		if (getPrayerPoints() <= 0) {
			player.getTransmitter().sendMessage("You have ran out of prayer points.");
			return;
		}
		if (getPrayersActiveCount() != 0) {
			activePrayers.forEach(this::deactivatePrayer);
			updateHeadIcon();
			refreshActivatedConfigs();
			return;
		}
		if (getQuickPrayersCount() == 0) {
			player.getTransmitter().sendMessage("You have no quick prayers to activate.");
			return;
		}
		List<Prayer> quickPrayers = this.quickPrayers.stream().filter(prayer -> prayer.getBook().equals(book)).collect(Collectors.toList());
		quickPrayers.forEach(prayer -> {
			closePrayers(prayer, false);
			activePrayers.add(prayer);
			prayer.activate(player);
			resetDrainPrayer(prayer.getSlotId());
			refreshActivatedConfigs();
			updateHeadIcon();
		});
	}
	
	/**
	 * Handles the select quick prayers button
	 */
	public void selectQuickPrayers() {
		settingQuickPrayers = !settingQuickPrayers;
		sendPrayerSelection();
		if (settingQuickPrayers) // switchs tab to prayer
		{
			player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
		}
		sendAccessMasks();
	}
	
	/**
	 * Sends the prayer selection tab, this is based on a config.
	 */
	private void sendPrayerSelection() {
		player.getTransmitter().send(new CS2ConfigBuilder(181, settingQuickPrayers ? 1 : 0).build(player));
	}
	
	/**
	 * Sends the prayer book access masks
	 */
	private void sendAccessMasks() {
		if (settingQuickPrayers) {
			player.getTransmitter().send(new AccessMaskBuilder(271, 42, 0, 2, 0, 29).build(player));
		} else {
			player.getTransmitter().send(new AccessMaskBuilder(271, 8, 0, 2, 0, 30).build(player));
		}
	}
	
	/**
	 * Gets the prayer boost for a skill
	 *
	 * @param skill
	 * 		The skill
	 */
	public double getBoost(int skill) {
		double bonus = 1.0;
		double modif = 0;
		switch (skill) {
			case ATTACK:
				if (prayerOn(CLARITY_OF_THOUGHT)) {
					bonus += 0.05;
				} else if (prayerOn(IMPROVED_REFLEXES)) {
					bonus += 0.10;
				} else if (prayerOn(INCREDIBLE_REFLEXES)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.15;
				} else if (prayerOn(PIETY)) {
					bonus += 0.20;
				} else if (prayerOn(SAP_WARRIOR)) {
					modif = (adjustments[0]);
					bonus += modif / 100;
				} else if (prayerOn(LEECH_ATTACK)) {
					modif = (5 + adjustments[3]);
					bonus += modif / 100;
				} else if (prayerOn(TURMOIL)) {
					modif = (15 + adjustments[8]);
					bonus += modif / 100;
				}
				break;
			case STRENGTH:
				if (prayerOn(BURST_OF_STRENGTH)) {
					bonus += 0.05;
				} else if (prayerOn(SUPERHUMAN_STRENGTH)) {
					bonus += 0.10;
				} else if (prayerOn(ULTIMATE_STRENGTH)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.18;
				} else if (prayerOn(PIETY)) {
					bonus += 0.23;
				} else if (prayerOn(SAP_WARRIOR)) {
					double d = (adjustments[0]);
					bonus += d / 100;
				} else if (prayerOn(LEECH_STRENGTH)) {
					modif = (5 + adjustments[7]);
					bonus += modif / 100;
				} else if (prayerOn(TURMOIL)) {
					modif = (23 + adjustments[10]);
					bonus += modif / 100;
				}
				break;
			case DEFENCE:
				if (prayerOn(THICK_SKIN)) {
					bonus += 0.05;
				} else if (prayerOn(ROCK_SKIN)) {
					bonus += 0.10;
				} else if (prayerOn(STEEL_SKIN)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.15;
				} else if (prayerOn(PIETY)) {
					bonus += 0.20;
				} else if (prayerOn(SAP_WARRIOR)) {
				
				}
				// sap
				modif = (adjustments[0]);
				bonus += modif / 100;
				
				// leech
				modif = (5 + adjustments[3]);
				bonus += modif / 100;
				
				// turmil
				modif = (15 + adjustments[8]);
				bonus += modif / 100;
				break;
			case RANGE:
				if (prayerOn(SHARP_EYE)) {
					bonus += 0.05;
				} else if (prayerOn(HAWK_EYE)) {
					bonus += 0.10;
				} else if (prayerOn(EAGLE_EYE)) {
					bonus += 0.15;
				} else if (prayerOn(SAP_RANGER)) {
					modif = (adjustments[1]);
					bonus += modif / 100;
				} else if (prayerOn(LEECH_RANGED)) {
					modif = (adjustments[1]);
					bonus += modif / 100;
				}
				break;
			case MAGIC:
				if (prayerOn(MYSTIC_WILL)) {
					bonus += 0.05;
				} else if (prayerOn(MYSTIC_LORE)) {
					bonus += 0.10;
				} else if (prayerOn(MYSTIC_MIGHT)) {
					bonus += 0.15;
				}
				
				// saps
				modif = (adjustments[2]);
				bonus += modif / 100;
				
				// leech
				modif = (5 + adjustments[5]);
				bonus += modif / 100;
				break;
		}
		return bonus;
	}
	
	/**
	 * Handles the prayer aspect of a hit being received
	 *
	 * @param hit
	 * 		The hit object
	 */
	public void handleHit(Hit hit) {
		if (hit.getSource().isPlayer() && hit.getSource().toPlayer().getManager().getPrayers().prayerOn(SMITE)) { // smite
			int drain = hit.getDamage() / 4;
			if (drain > 0) {
				drainPrayer(drain);
			}
		} else {
			if (hit.getDamage() == 0) {
				return;
			}
			handleDeflects(hit);
			handleLeeches(hit);
		}
	}
	
	/**
	 * Gets the hit after the prayer multiplier
	 *
	 * @param player
	 * 		If the receiver is a player
	 * @param damage
	 * 		The amount of damage
	 */
	private int getHitPrayerMultiplier(boolean player, int damage) {
		return (int) (damage * (player ? 0.6D : 0D));
	}
	
	/**
	 * Handles the deflection of combat prayers
	 *
	 * @param hit
	 * 		The hit
	 */
	private void handleDeflects(Hit hit) {
		Entity hitter = hit.getSource();
		switch (hit.getSplat()) {
			case MELEE_DAMAGE:
				if (prayerOn(PROTECT_FROM_MELEE)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
				} else if (prayerOn(DEFLECT_MELEE)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendGraphics((2230));
						player.sendAnimation(12573);
					}
				}
				break;
			case RANGE_DAMAGE:
				if (prayerOn(PROTECT_FROM_MISSILES)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
				} else if (prayerOn(DEFLECT_MISSILES)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendGraphics((2229));
						player.sendAnimation(12573);
					}
				}
				break;
			case MAGIC_DAMAGE:
				if (prayerOn(PROTECT_FROM_MAGIC)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					break;
				} else if (prayerOn(DEFLECT_MAGIC)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendGraphics((2228));
						player.sendAnimation(12573);
					}
				}
				break;
		}
	}
	
	/**
	 * Handles leech prayers
	 *
	 * @param hit
	 * 		The hit
	 */
	private void handleLeeches(Hit hit) {
		// leeches only apply to players
		if (!hit.getSource().isPlayer()) {
			return;
		}
		// the instance of the source [to player object]
		Player source = hit.getSource().toPlayer();
		// the instance of the sources prayer
		PrayerManager sourcePrayer = source.getManager().getPrayers();
		
		System.out.println(source + ", " + hit + ", " + sourcePrayer.isBoostedLeech());
		if (!sourcePrayer.isBoostedLeech()) {
			if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
				if (sourcePrayer.prayerOn(TURMOIL)) {
					if (Misc.getRandom(4) == 0) {
						sourcePrayer.increaseTurmoilBonus(player);
						sourcePrayer.setBoostedLeech(true);
						return;
					}
				} else if (sourcePrayer.prayerOn(SAP_WARRIOR)) { // sap att
					if (Misc.getRandom(4) == 0) {
						if (sourcePrayer.reachedMax(0)) {
							source.getTransmitter().sendMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
						} else {
							sourcePrayer.increaseLeechBonus(0, player);
							source.getTransmitter().sendMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
						}
						source.sendAnimation((12569));
						source.sendGraphics((2214));
						sourcePrayer.setBoostedLeech(true);
						sendPrayerProjectile(source, player, 2215);
						SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
							@Override
							public Runnable getTask() {
								return () -> player.sendGraphics(2216);
							}
						});
						return;
					}
				} else {
					if (sourcePrayer.prayerOn(LEECH_ATTACK)) {
						if (Misc.getRandom(7) == 0) {
							if (sourcePrayer.reachedMax(3)) {
								source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								sourcePrayer.increaseLeechBonus(3, player);
								source.getTransmitter().sendMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
							}
							source.sendAnimation((12575));
							sourcePrayer.setBoostedLeech(true);
							sendPrayerProjectile(source, player, 2231);
							SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
								@Override
								public Runnable getTask() {
									return () -> player.sendGraphics(2232);
								}
							});
							return;
						}
					}
					if (sourcePrayer.prayerOn(LEECH_STRENGTH)) {
						if (Misc.getRandom(7) == 0) {
							if (sourcePrayer.reachedMax(7)) {
								source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
							} else {
								sourcePrayer.increaseLeechBonus(7, player);
								source.getTransmitter().sendMessage("Your curse drains Strength from the enemy, boosting your Strength.", true);
							}
							source.sendAnimation((12575));
							sourcePrayer.setBoostedLeech(true);
							sendPrayerProjectile(source, player, 2248);
							SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
								@Override
								public Runnable getTask() {
									return () -> player.sendGraphics(2250);
								}
							});
							return;
						}
					}
					
				}
			}
			if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
				if (sourcePrayer.prayerOn(SAP_RANGER)) { // sap range
					if (Misc.getRandom(4) == 0) {
						if (sourcePrayer.reachedMax(1)) {
							source.getTransmitter().sendMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
						} else {
							sourcePrayer.increaseLeechBonus(1, player);
							source.getTransmitter().sendMessage("Your curse drains Range from the enemy, boosting your Range.", true);
						}
						source.sendAnimation((12569));
						source.sendGraphics((2217));
						sourcePrayer.setBoostedLeech(true);
						sendPrayerProjectile(source, player, 2218);
						SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
							@Override
							public Runnable getTask() {
								return () -> player.sendGraphics(2219);
							}
						});
						return;
					}
				} else if (sourcePrayer.prayerOn(LEECH_RANGED)) {
					if (Misc.getRandom(7) == 0) {
						if (sourcePrayer.reachedMax(4)) {
							source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
						} else {
							sourcePrayer.increaseLeechBonus(4, player);
							source.getTransmitter().sendMessage("Your curse drains Range from the enemy, boosting your Range.", true);
						}
						source.sendAnimation((12575));
						sourcePrayer.setBoostedLeech(true);
						sendPrayerProjectile(source, player, 2236);
						SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
							@Override
							public Runnable getTask() {
								return () -> player.sendGraphics(2238);
							}
						});
						return;
					}
				}
			}
			if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
				if (sourcePrayer.prayerOn(SAP_MAGE)) { // sap mage
					if (Misc.getRandom(4) == 0) {
						if (sourcePrayer.reachedMax(2)) {
							source.getTransmitter().sendMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
						} else {
							sourcePrayer.increaseLeechBonus(2, player);
							source.getTransmitter().sendMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
						}
						source.sendAnimation((12569));
						source.sendGraphics((2220));
						sourcePrayer.setBoostedLeech(true);
						sendPrayerProjectile(source, player, 2221);
						SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
							@Override
							public Runnable getTask() {
								return () -> player.sendGraphics(2222);
							}
						});
						return;
					}
				} else if (sourcePrayer.prayerOn(LEECH_MAGIC)) {
					if (Misc.getRandom(7) == 0) {
						if (sourcePrayer.reachedMax(5)) {
							source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
						} else {
							sourcePrayer.increaseLeechBonus(5, player);
							source.getTransmitter().sendMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
						}
						source.sendAnimation((12575));
						sourcePrayer.setBoostedLeech(true);
						sendPrayerProjectile(source, player, 2240);
						SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
							@Override
							public Runnable getTask() {
								return () -> player.sendGraphics(2242);
							}
						});
						return;
					}
				}
			}
			
			if (sourcePrayer.prayerOn(LEECH_DEFENCE)) { // leech defence
				if (Misc.getRandom(10) == 0) {
					if (sourcePrayer.reachedMax(6)) {
						source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
					} else {
						sourcePrayer.increaseLeechBonus(6, player);
						source.getTransmitter().sendMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
					}
					source.sendAnimation((12575));
					sourcePrayer.setBoostedLeech(true);
					sendPrayerProjectile(source, player, 2244);
					SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
						@Override
						public Runnable getTask() {
							return () -> player.sendGraphics(2246);
						}
					});
					return;
				}
			}
			
			if (sourcePrayer.prayerOn(LEECH_ENERGY)) {
				if (Misc.getRandom(10) == 0) {
					if (player.getVariables().getRunEnergy() <= 0) {
						source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
					} else {
						source.getVariables().setRunEnergy(source.getVariables().getRunEnergy() > 90 ? 100 : source.getVariables().getRunEnergy() + 10);
						player.getVariables().setRunEnergy(source.getVariables().getRunEnergy() > 10 ? player.getVariables().getRunEnergy() - 10 : 0);
					}
					source.sendAnimation((12575));
					sourcePrayer.setBoostedLeech(true);
					sendPrayerProjectile(source, player, 2256);
					SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
						@Override
						public Runnable getTask() {
							return () -> player.sendGraphics(2258);
						}
					});
					return;
				}
			}
			
			if (sourcePrayer.prayerOn(LEECH_SPECIAL_ATTACK)) {
				if (Misc.getRandom(10) == 0) {
					if (player.getCombatDefinitions().getSpecialEnergy() <= 0) {
						source.getTransmitter().sendMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
					} else {
						// so if we have less than 10%, we guy who hit us doesnt get 10% energy
						// they get only the energy we had left.
						int amount = (player.getCombatDefinitions().getSpecialEnergy() >= 10 ? -10 : player.getCombatDefinitions().getSpecialEnergy());
						source.getCombatDefinitions().modifySpecial(amount);
						player.getCombatDefinitions().modifySpecial(-amount);
					}
					source.sendAnimation((12575));
					sourcePrayer.setBoostedLeech(true);
					sendPrayerProjectile(source, player, 2252);
					SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
						@Override
						public Runnable getTask() {
							return () -> player.sendGraphics(2254);
						}
					});
					return;
				}
			}
			
			if (sourcePrayer.prayerOn(SAP_SPIRIT)) { // sap spec
				if (Misc.getRandom(10) == 0) {
					source.sendAnimation(12569);
					source.sendGraphics((2223));
					sourcePrayer.setBoostedLeech(true);
					if (player.getCombatDefinitions().getSpecialEnergy() <= 0) {
						source.getTransmitter().sendMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
					} else {
						player.getCombatDefinitions().modifySpecial(-10);
					}
					sendPrayerProjectile(source, player, 2224);
					SystemManager.getScheduler().schedule(new ScheduledTask(1, false) {
						@Override
						public Runnable getTask() {
							return () -> player.sendGraphics(2225);
						}
					});
				}
			}
		}
	}
	
	/**
	 * Sends the prayer projectile to the target
	 *
	 * @param source
	 * 		The source
	 * @param target
	 * 		The target
	 * @param projectileId
	 * 		The id of the projectile
	 */
	private void sendPrayerProjectile(Entity source, Entity target, int projectileId) {
		player.getRegion().sendProjectile(new Projectile(source, target, projectileId, 0, 10, 0, ProjectileManager.getSpeedModifier(source, target) / 2, 0, 0));
	}
}