package org.redrune.engine

import org.redrune.cache.Cache
import org.redrune.cache.loaders.ItemDefinitions
import org.redrune.cache.loaders.NPCDefinitions
import org.redrune.cache.loaders.ObjectDefinitions
import org.redrune.engine.cycle.GameCycleWorker
import org.redrune.engine.factory.DecoderThreadFactory
import org.redrune.engine.factory.SlowThreadFactory
import org.redrune.engine.tick.schedule.Scheduler
import org.redrune.engine.tick.schedule.impl.ExchangeTask
import org.redrune.engine.tick.schedule.impl.InformationTabTick
import org.redrune.engine.tick.schedule.impl.PunishmentProcessorTick
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.link.OwnedObjectManager
import org.redrune.game.global.World
import org.redrune.game.global.map.region.RegionManager
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.constants.SkillConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.PlayerSaving.savePlayer
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Manages all system operations.
 *
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 5/21/2017
 */
object SystemManager {
    /**
     * Gets the amount of processors on the computer
     */
    val PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors()

    @JvmField
    val SLOW_EXECUTOR = if (PROCESSOR_COUNT >= 6) Executors.newScheduledThreadPool(
        if (PROCESSOR_COUNT >= 12) 4 else 2,
        SlowThreadFactory()
    ) else Executors.newSingleThreadScheduledExecutor(SlowThreadFactory())
    private val FAST_EXECUTOR = Timer("Fast Executor")
    val SERVER_WORKER_CHANNEL_EXECUTOR = if (PROCESSOR_COUNT >= 6) Executors.newFixedThreadPool(
        PROCESSOR_COUNT - if (PROCESSOR_COUNT >= 12) 7 else 5,
        DecoderThreadFactory()
    ) else Executors.newSingleThreadExecutor(DecoderThreadFactory())
    val SERVER_BOSS_CHANNEL_EXECUTOR = Executors.newSingleThreadExecutor(DecoderThreadFactory())

    @JvmField
    val SCHEDULER = Scheduler()
    var serverWorkersCount = 0

    @Volatile
    var shutdown = false

    @JvmField
    var shutdownStart: Long = 0

    @JvmField
    var shutdownDelay = 0
    private var checkAgility = false
    private val CYCLE_WORKER = GameCycleWorker()
    fun initialize() {
        serverWorkersCount = if (PROCESSOR_COUNT >= 6) PROCESSOR_COUNT - (if (PROCESSOR_COUNT >= 12) 7 else 5) else 1
        registerTasks()
        CYCLE_WORKER.start()
    }

    /**
     * Registers the scheduled tasks
     */
    private fun registerTasks() {
        addAccountsSavingTask()
        addCleanMemoryTask()
        addRestoreRunEnergyTask()
        addRestoreHitPointsTask()
        addRestoreSkillsTask()
        addRestoreSpecialAttackTask()
        addSummoningEffectTask()
        addOwnedObjectsTask()
        addScheduledTasks()
    }

    private fun addAccountsSavingTask() {
        SLOW_EXECUTOR.scheduleWithFixedDelay({
            try {
                saveFiles()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, 15, 15, TimeUnit.SECONDS)
    }

    private fun addCleanMemoryTask() {
        SLOW_EXECUTOR.scheduleWithFixedDelay({
            try {
                cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, 0, 10, TimeUnit.MINUTES)
    }

    private fun addRestoreRunEnergyTask() {
        FAST_EXECUTOR.schedule(object : TimerTask() {
            override fun run() {
                try {
                    for (player in World.getPlayers()) {
                        if (player == null || player.isDead || !player.isRunning || checkAgility && player.skills.getLevel(
                                SkillConstants.AGILITY
                            ) < 70
                        ) {
                            continue
                        }
                        player.attributes.restoreRunEnergy()
                    }
                    checkAgility = !checkAgility
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }, 0, 1000)
    }

    private fun addRestoreHitPointsTask() {
        FAST_EXECUTOR.schedule(object : TimerTask() {
            override fun run() {
                try {
                    for (player in World.getPlayers()) {
                        if (player == null || player.isDead || !player.isRunning) {
                            continue
                        }
                        player.restoreHitPoints()
                    }
                    for (npc in World.getNPCs()) {
                        if (npc == null || npc.isDead || npc.isFinished) {
                            continue
                        }
                        npc.restoreHitPoints()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }, 0, 6000)
    }

    private fun addRestoreSkillsTask() {
        FAST_EXECUTOR.schedule(object : TimerTask() {
            override fun run() {
                try {
                    for (player in World.getPlayers()) {
                        if (player == null || !player.isRunning) {
                            continue
                        }
                        var ammountTimes = if (player.prayer.usingPrayer(0, 8)) 2 else 1
                        if (player.attributes.isResting) {
                            ammountTimes += 1
                        }
                        val berserker = player.prayer.usingPrayer(1, 5)
                        for (skill in 0..24) {
                            if (skill == SkillConstants.SUMMONING) {
                                continue
                            }
                            for (time in 0 until ammountTimes) {
                                val currentLevel = player.skills.getLevel(skill)
                                val normalLevel = player.skills.getLevelForXp(skill)
                                if (currentLevel > normalLevel) {
                                    if (skill == SkillConstants.ATTACK || skill == SkillConstants.STRENGTH || skill == SkillConstants.DEFENCE || skill == SkillConstants.RANGE || skill == SkillConstants.MAGIC) {
                                        if (berserker && Misc.getRandom(100) <= 15) {
                                            continue
                                        }
                                    }
                                    player.skills[skill] = currentLevel - 1
                                } else if (currentLevel < normalLevel) {
                                    player.skills[skill] = currentLevel + 1
                                } else {
                                    break
                                }
                            }
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }, 0, 30000)
    }

    private fun addRestoreSpecialAttackTask() {
        FAST_EXECUTOR.schedule(object : TimerTask() {
            override fun run() {
                try {
                    for (player in World.getPlayers()) {
                        if (player == null || player.isDead || !player.isRunning) {
                            continue
                        }
                        player.combatDefinitions.restoreSpecialAttack()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }, 0, 30000)
    }

    private fun addSummoningEffectTask() {
        SLOW_EXECUTOR.scheduleWithFixedDelay({
            try {
                for (player in World.getPlayers()) {
                    if (player.familiar == null || player.isDead || !player.isFinished) {
                        continue
                    }
                    if (player.familiar.originalId == 6814) {
                        player.heal(20)
                        player.setNextGraphics(Graphics(1507))
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, 0, 15, TimeUnit.SECONDS)
    }

    private fun addOwnedObjectsTask() {
        SLOW_EXECUTOR.scheduleWithFixedDelay({
            try {
                OwnedObjectManager.processAll()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun addScheduledTasks() {
        SCHEDULER.schedule(ExchangeTask())
        SCHEDULER.schedule(PunishmentProcessorTick())
        SCHEDULER.schedule(InformationTabTick())
    }

    private fun saveFiles() {
        for (player in World.getPlayers()) {
            if (player == null || !player.hasStarted() || player.isFinished) {
                continue
            }
            savePlayer(player)
        }
    }

    private fun cleanMemory(force: Boolean) {
        if (force) {
            ItemDefinitions.clearItemsDefinitions()
            NPCDefinitions.clearNPCDefinitions()
            ObjectDefinitions.clearObjectDefinitions()
            for (region in RegionManager.getRegions().values) {
                region.removeMapFromMemory()
            }
        }
        for (index in Cache.STORE.indexes) {
            index.resetCachedFiles()
        }
        FAST_EXECUTOR.purge()
        System.gc()
    }

    fun safeShutdown(delay: Int) {
        if (shutdownStart != 0L) {
            return
        }
        shutdownStart = Misc.currentTimeMillis()
        shutdownDelay = delay
        for (player in World.getPlayers()) {
            if (player == null || !player.hasStarted() || player.isFinished) {
                continue
            }
            player.packets.sendSystemUpdate(delay)
        }
        SLOW_EXECUTOR.schedule({
            try {
                for (player in World.getPlayers()) {
                    if (player == null || !player.hasStarted()) {
                        continue
                    }
                    player.realFinish()
                }
                closeServices()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, delay.toLong(), TimeUnit.SECONDS)
    }

    private fun closeServices() {
        shutdown()
    }

    private fun shutdown() {
        SERVER_WORKER_CHANNEL_EXECUTOR.shutdown()
        SERVER_BOSS_CHANNEL_EXECUTOR.shutdown()
        FAST_EXECUTOR.cancel()
        SLOW_EXECUTOR.shutdown()
        shutdown = true
    }
}