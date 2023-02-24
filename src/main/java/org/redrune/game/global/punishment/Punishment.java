package org.redrune.game.global.punishment;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;

import java.util.*;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/17/2017
 */
public final class Punishment {

    /**
     * The name of the player carried out the punishment
     */
    private final String punisher;

    /**
     * The name of the player who was punished
     */
    private final String punished;

    /**
     * The type of punishment this was
     */
    private final PunishmentType type;

    /**
     * The time the punishment will be over at
     */
    private final long time;

    /**
     * The time the punishment was created, in simple date format
     */
    private final String punishedAt;

    /**
     * The time the punishment was created, in simple date format
     */
    private final String overAt;

    /**
     * The event executed when the punishment is added
     */
    private transient Runnable additionEvent;

    /**
     * Additional parameters, used for storing things like mac address/ip address
     */
    private final Map<String, String> parameters = new HashMap<>();

    public Punishment(String punisher, String punished, PunishmentType type, long time) {
        this(punisher, punished, type, time, null);
    }

    public Punishment(String punisher, String punished, PunishmentType type, long time, Runnable additionEvent) {
        this.punisher = punisher;
        this.punished = punished;
        this.type = type;
        this.time = time;
        this.additionEvent = additionEvent;
        this.punishedAt = new Date().toLocaleString();
        this.overAt = new Date(time).toLocaleString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Punishment)) {
            return false;
        }
        Punishment p = (Punishment) obj;
        return Objects.equals(punished, p.punished) && Objects.equals(punisher, p.punisher) && Objects.equals(type, p.type);
    }

    /**
     * If the duration for the punishment has expired
     */
    public boolean hasExpired() {
        return System.currentTimeMillis() > time;
    }

    @Override
    public String toString() {
        return "Punishment{" + "punisher='" + punisher + '\'' + ", punished='" + punished + '\'' + ", type=" + type + ", time=" + time + '}';
    }

    /**
     * Notifies the {@link #punisher} of the change of states in the punishment
     */
    public void notify(boolean addition, boolean success) {
        Player player = World.getPlayer(punisher);
        if (player == null) {
            return;
        }
        String message = PunishmentHandler.getMessage(this, addition, success);
        player.getPackets().sendMessage(message);
    }

    /**
     * Gets the ip of the punishment
     */
    public Optional<String> getIp() {
        return Optional.ofNullable(parameters.get("ip"));
    }

    /**
     * Gets the mac of the punishment
     */
    public Optional<String> getMac() {
        return Optional.ofNullable(parameters.get("mac"));
    }

    /**
     * Puts a parameter
     */
    public void putParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String getPunisher() {
        return this.punisher;
    }

    public String getPunished() {
        return this.punished;
    }

    public PunishmentType getType() {
        return this.type;
    }

    public long getTime() {
        return this.time;
    }

    public String getPunishedAt() {
        return this.punishedAt;
    }

    public String getOverAt() {
        return this.overAt;
    }

    public Runnable getAdditionEvent() {
        return this.additionEvent;
    }

    public void setAdditionEvent(Runnable additionEvent) {
        this.additionEvent = additionEvent;
    }
}
