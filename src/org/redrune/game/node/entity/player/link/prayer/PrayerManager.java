package org.redrune.game.node.entity.player.link.prayer;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.network.rs666.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.HeadIcons.PrayerIcon;
import org.redrune.utility.rs.constant.PrayerConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public final class PrayerManager {
	
	/**
	 * The list of prayers that are active
	 */
	private final CopyOnWriteArraySet<Prayer> activePrayers = new CopyOnWriteArraySet<>();
	
	/**
	 * The set of quick prayers
	 */
	private final CopyOnWriteArraySet<Prayer> quickPrayers = new CopyOnWriteArraySet<>();
	
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
	 * Sends login configurations for prayer books
	 */
	public void sendLoginConfigurations() {
		this.nextDrain = new long[30];
		player.getTransmitter().send(new CS2ConfigBuilder(181, settingQuickPrayers ? 1 : 0).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(1584, book == PrayerBook.CURSES ? 1 : 0).build(player));
		setBook(PrayerBook.CURSES);
	}
	
	/**
	 * Sets the book the player is using
	 *
	 * @param book
	 * 		The book
	 */
	public void setBook(PrayerBook book) {
		this.book = book;
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
		System.out.println("componentId = [" + componentId + "], slotId = [" + slotId + "]");
		if (componentId == 8) {
			Optional<Prayer> optional = Prayer.findPrayerBySlot(slotId, book);
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
				closePrayers(prayer);
				activePrayers.add(prayer);
				activate = true;
			}
			// so the prayer was activated
			if (activate) {
				prayer.activate(player);
				resetDrainPrayer(prayer.getSlotId());
			}
			refreshActivatedConfigs();
			updateHeadIcon();
		} else if (componentId == 42) {
			Optional<Prayer> optional = Prayer.findPrayerBySlot(slotId, book);
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
			// TODO: finish this
		} else if (componentId == 43) {
			settingQuickPrayers = false;
			player.getTransmitter().send(new CS2ConfigBuilder(181, settingQuickPrayers ? 1 : 0).build(player));
			if (settingQuickPrayers) {
				player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
			}
			sendAccessMasks();
		}
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
	public boolean deactivatePrayer(Prayer prayer) {
		if (isPrayerOn(prayer)) {
			activePrayers.remove(prayer);
			return true;
		}
		return false;
	}
	
	/**
	 * Closes the prayers that this prayer requires closed
	 *
	 * @param prayer
	 * 		The prayer
	 */
	private boolean closePrayers(Prayer prayer) {
		int[][] close = prayer.getPrayersToClose();
		Set<Prayer> prayerSet = new TreeSet<>();
		for (int[] closeArray : close) {
			for (int closeId : closeArray) {
				Optional<Prayer> optional = Prayer.findPrayerBySlot(closeId, prayer.getBook());
				if (!optional.isPresent()) {
					System.out.println("Unable to find prayer by slot " + closeId);
					continue;
				}
				prayerSet.add(optional.get());
			}
		}
		if (prayerSet.isEmpty()) {
			return false;
		}
		int closed = 0;
		for (Prayer toClose : prayerSet) {
			if (activePrayers.remove(toClose)) {
				closed++;
			}
		}
		return closed != 0;
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
		for (Prayer prayer : activePrayers) {
			value += prayer.getActivationConfig();
		}
		int configId = book == PrayerBook.CURSES ? (settingQuickPrayers ? 1587 : 1582) : (settingQuickPrayers ? 1397 : 1395);
		player.getTransmitter().send(new ConfigPacketBuilder(configId, value).build(player));
		//System.out.println("config=" + configId + ":value=" + value);
	}
	
	/**
	 * Updates the head icon
	 */
	private void updateHeadIcon() {
		if (book == PrayerBook.REGULAR) {
			if (isPrayerOn(Prayer.PROTECT_FROM_SUMMONING)) {
				if (prayersAreActive(Prayer.PROTECT_FROM_SUMMONING, Prayer.PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.MAGIC_SUMMONING);
					return;
				} else if (prayersAreActive(Prayer.PROTECT_FROM_SUMMONING, Prayer.PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.RANGE_SUMMONING);
					return;
				} else if (prayersAreActive(Prayer.PROTECT_FROM_SUMMONING, Prayer.PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.MELEE_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.SUMMONING);
					return;
				}
			} else {
				if (isPrayerOn(Prayer.PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.PROTECT_FROM_MAGIC);
					return;
				} else if (isPrayerOn(Prayer.PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.PROTECT_FROM_RANGE);
					return;
				} else if (isPrayerOn(Prayer.PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.PROTECT_FROM_MELEE);
					return;
				} else if (isPrayerOn(Prayer.RETRIBUTION)) {
					setIcon(PrayerIcon.RETRIBUTION);
					return;
				} else if (isPrayerOn(Prayer.REDEMPTION)) {
					setIcon(PrayerIcon.REDEMPTION);
					return;
				} else if (isPrayerOn(Prayer.SMITE)) {
					setIcon(PrayerIcon.SMITE);
					return;
				}
			}
		} else {
			if (isPrayerOn(Prayer.DEFLECT_SUMMONING)) {
				if (prayersAreActive(Prayer.DEFLECT_SUMMONING, Prayer.DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(Prayer.DEFLECT_SUMMONING, Prayer.DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(Prayer.DEFLECT_SUMMONING, Prayer.DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE_AND_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.DEFLECT_SUMMONING);
					return;
				}
			} else {
				if (isPrayerOn(Prayer.DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGIC);
					return;
				} else if (isPrayerOn(Prayer.DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE);
					return;
				} else if (isPrayerOn(Prayer.DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE);
					return;
				} else if (isPrayerOn(Prayer.WRATH)) {
					setIcon(PrayerIcon.WRATH);
					return;
				} else if (isPrayerOn(Prayer.SOULSPLIT)) {
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
	public boolean isPrayerOn(Prayer prayer) {
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
			if (!isPrayerOn(prayer)) {
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
	public void refreshPrayerPoints() {
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
		quickPrayers.forEach(prayer -> {
			prayer.activate(player);
			resetDrainPrayer(prayer.getSlotId());
			refreshActivatedConfigs();
			updateHeadIcon();
		});
		System.out.println("Activated Prayers: " + quickPrayers);
	}
	
	/**
	 * Handles the select quick prayers button
	 */
	public void selectQuickPrayers() {
		settingQuickPrayers = !settingQuickPrayers;
		player.getTransmitter().send(new CS2ConfigBuilder(181, settingQuickPrayers ? 1 : 0).build(player));
		if (settingQuickPrayers) // switchs tab to prayer
		{
			player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
		}
		sendAccessMasks();
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
}