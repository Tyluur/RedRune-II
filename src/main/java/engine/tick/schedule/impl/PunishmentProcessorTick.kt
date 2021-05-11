package engine.tick.schedule.impl

import engine.tick.schedule.ScheduledTask
import game.global.punishment.Punishment
import game.global.punishment.PunishmentRepository.add
import game.global.punishment.PunishmentRepository.delete
import game.global.punishment.PunishmentRepository.punishments
import game.global.punishment.PunishmentRepository.queue
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 9/13/2017
 */
class PunishmentProcessorTick : ScheduledTask(5, -1) {

    override fun run() {
        val awaiting = queue
        var punishment: Punishment?
        while (awaiting.poll().also { punishment = it } != null) {
            add(punishment!!, awaiting.isEmpty())
        }
        val deleting: Queue<Punishment> = LinkedBlockingQueue()
        for (p in punishments) {
            if (p.hasExpired()) {
                deleting.add(p)
            }
        }
        while (deleting.poll().also { punishment = it } != null) {
            delete(punishment!!, deleting.isEmpty())
        }
    }
}