package org.redrune.game.entity.actor.link;

import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public class InteractionManager {

    /**
     * The actor this class is an instance for
     */
    private final Actor owner;

    /**
     * The actor being interacted with
     */
    private final Set<Actor> interactorList = new LinkedHashSet<>();

    public InteractionManager(Actor owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "InteractionManager{" + "owner=" + owner + ", interactorList=" + interactorList + '}';
    }

    /**
     * This is processed every tick
     */
    public void process() {
        // only npcs process their interacting with partner, players don't have any necessary processing task
        if (!isInteracting()) {
            return;
        }
        cancelAllInteractions();
    }

    /**
     * If the actor is in an interaction
     */
    public boolean isInteracting() {
        return !interactorList.isEmpty();
    }

    /**
     * Handles the end of an interaction
     */
    private void cancelAllInteractions() {
        System.out.println("cancelled all interactions");
        interactorList.forEach(partner -> partner.getInteractionManager().reset(true));
        reset(true);
    }

    /**
     * Resets all interaction necessities
     */
    public void reset(boolean emptyList) {
        owner.setNextFaceActor(null);
        if (emptyList) {
            interactorList.clear();
        }
    }

    /**
     * Ends the interaction with a single actor
     */
    @SuppressWarnings("unused")
    public void cancelActorInteraction() {
        if (owner.isPlayer()) {
            for (Actor a : interactorList) {
                if (a.isNPC()) {
                    NPC npc = a.toNPC();
                    InteractionManager npcInteractionManager = npc.getInteractionManager();
                    boolean removed = npcInteractionManager.removeInteraction(owner);
                    if (npcInteractionManager.interactorList.isEmpty()) {
                        npcInteractionManager.reset(true);
                    } else {
                        Actor alternative = npcInteractionManager.getClosestAlternative();
                        npc.setNextFaceActor(alternative);
                    }
                }
            }
        }
        reset(true);
    }

    /**
     * Removes an interacting actor from the list
     *
     * @param toRemove The actor to remove
     */
    private boolean removeInteraction(Actor toRemove) {
        for (Iterator<Actor> iterator = interactorList.iterator(); iterator.hasNext(); ) {
            Actor actor = iterator.next();
            if (actor.equals(toRemove)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the closest alternative actor
     */
    private Actor getClosestAlternative() {
        Actor closest = null;
        int distance = -1;
        for (Actor actor : interactorList) {
            if (actor == null) {
                continue;
            }
            int actorD = actor.getDistance(owner);
            if (distance == -1 || actorD < distance) {
                closest = actor;
                distance = actorD;
            }
        }
        return closest;
    }

    /**
     * Starts an interaction
     *
     * @param interactingWith The actor the owner is interacting with
     */
    public void startInteraction(Actor interactingWith) {
        this.interactorList.add(interactingWith);
        if (owner.isPlayer()) {
            owner.setNextFaceActor(interactingWith);
            interactingWith.setNextFaceActor(owner);
            interactingWith.getInteractionManager().startInteraction(owner);
        }
    }

    public Set<Actor> getInteractorList() {
        return this.interactorList;
    }
}
