package com.rs.cores.schedule.impl;

import com.rs.cores.schedule.ScheduledTask;
import com.rs.game.world.punishment.Punishment;
import com.rs.game.world.punishment.PunishmentRepository;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class PunishmentTask extends ScheduledTask {
	
	public PunishmentTask() {
		super(5, -1);
	}
	
	@Override
	public void run() {
		Queue<Punishment> awaiting = PunishmentRepository.getQueue();
		Punishment punishment;
		while ((punishment = awaiting.poll()) != null) {
			PunishmentRepository.add(punishment, awaiting.isEmpty());
		}
		Queue<Punishment> deleting = new LinkedBlockingQueue<>();
		for (Punishment p : PunishmentRepository.getPunishments()) {
			if (p.hasExpired()) {
				deleting.add(p);
			}
		}
		while((punishment = deleting.poll()) != null) {
			PunishmentRepository.delete(punishment, deleting.isEmpty());
		}
	}
}
