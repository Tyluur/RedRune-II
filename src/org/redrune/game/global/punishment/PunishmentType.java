package org.redrune.game.global.punishment;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public enum PunishmentType {
	PLAYER_MUTE {
		@Override
		public boolean add(Player player, Punishment punishment) {
			return PunishmentRepository.addToQueue(punishment);
		}
		
		@Override
		public boolean remove(Player player, Punishment punishment) {
			return PunishmentRepository.delete(punishment, true);
		}
	},
	PLAYER_BAN {
		@Override
		public boolean add(Player player, Punishment punishment) {
			boolean addToQueue = PunishmentRepository.addToQueue(punishment);
			if (addToQueue) {
				punishment.setAdditionEvent(player::forceOffline);
			}
			return addToQueue;
		}
		
		@Override
		public boolean remove(Player player, Punishment punishment) {
			return PunishmentRepository.delete(punishment, true);
		}
	},
	ADDRESS_MUTE {
		@Override
		public boolean add(Player player, Punishment punishment) {
			PunishmentType type = punishment.getType();
			if (type == PunishmentType.ADDRESS_MUTE || type == PunishmentType.ADDRESS_BAN) {
				punishment.putParameter("ip", player.getSession().getIp());
				punishment.putParameter("mac", player.getSession().getMacAddress());
			}
			return PunishmentRepository.addToQueue(punishment);
		}
		
		@Override
		public boolean remove(Player player, Punishment punishment) {
			return PunishmentRepository.delete(punishment, true);
		}
	},
	ADDRESS_BAN {
		@Override
		public boolean add(Player player, Punishment punishment) {
			PunishmentType type = punishment.getType();
			if (type == PunishmentType.ADDRESS_MUTE || type == PunishmentType.ADDRESS_BAN) {
				punishment.putParameter("ip", player.getSession().getIp());
				punishment.putParameter("mac", player.getSession().getMacAddress());
			}
			boolean addToQueue = PunishmentRepository.addToQueue(punishment);
			if (addToQueue) {
				Runnable task = () -> {
					for (Player p : World.getPlayers()) {
						if (p == null) {
							continue;
						}
						if (p.getSession().getIp().equals(player.getSession().getIp())) {
							p.forceOffline();
						}
						if (p.getSession().getMacAddress().equals(player.getSession().getMacAddress())) {
							p.forceOffline();
						}
					}
				};
				punishment.setAdditionEvent(task);
			}
			return addToQueue;
		}
		
		@Override
		public boolean remove(Player player, Punishment punishment) {
			return PunishmentRepository.delete(punishment, true);
		}
	};
	
	/**
	 * Handles the addition of a punishment
	 */
	public abstract boolean add(Player player, Punishment punishment);
	
	/**
	 * Handles the removal of a punishment
	 */
	public abstract boolean remove(Player player, Punishment punishment);
	
}
