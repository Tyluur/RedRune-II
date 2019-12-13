package org.redrune.engine.tick.schedule.impl;

import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.global.punishment.Punishment;
import org.redrune.game.global.punishment.PunishmentRepository;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/13/2017
 */
public class PunishmentProcessorTick extends ScheduledTask {
	
	public PunishmentProcessorTick() {
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
