package org.redrune.game.content.entity.actor.player.event;

import org.redrune.game.content.entity.actor.player.event.Event.EventState;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public class EventManager {

    /**
     * The player whose class this is an instance for
     */
    private final Player player;

    /**
     * The queue of events the player is awaiting to perform
     */
    private Event event;

    public EventManager(Player player) {
        this.player = player;
    }

    /**
     * Processes the event
     */
    public void process() {
        try {
            if (event != null) {
                switch (event.getState()) {
                    case CREATED:
                        event.firePolicy(player);
                        event.run(player);
                        event.setState(EventState.STARTED);
                        break;
                    case STARTED:
                        event.setState(EventState.FINISHED);
                        break;
                    case FINISHED:
                        event = null;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            event = null;
        }
    }

    /**
     * Starts an event
     *
     * @param event The event to start
     */
    public void start(Event event) {
        this.event = event;
        fire();
    }

    /**
     * This method fires an event for the player
     */
    private void fire() {
        if (event == null) {
            return;
        }
        if (event.getState() != EventState.CREATED) {
            return;
        }
        event.firePolicy(player);
        event.run(player);
        event.setState(Event.EventState.STARTED);
    }

}
