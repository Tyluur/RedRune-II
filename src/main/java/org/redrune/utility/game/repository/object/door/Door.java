package org.redrune.utility.game.repository.object.door;

/**
 * Represents a door.
 *
 * @author Emperor
 */
public class Door {

    /**
     * The door's object id.
     */
    private final int id;

    /**
     * The door's replace object id.
     */
    private int replaceId;

    /**
     * If the player should automaticly walk through it.
     */
    private boolean autoWalk;

    /**
     * Constructs a new {@code DoorManager} {@code Object}.
     */
    public Door(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Door{" + "id=" + id + ", replaceId=" + replaceId + ", autoWalk=" + autoWalk + '}';
    }

    public boolean isAutoWalk() {
        return autoWalk;
    }

    public int getReplaceId() {
        return replaceId;
    }

    public int getId() {
        return id;
    }

    public void setReplaceId(int replaceId) {
        this.replaceId = replaceId;
    }

}
