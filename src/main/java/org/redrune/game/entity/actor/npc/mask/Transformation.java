package org.redrune.game.entity.actor.npc.mask;

public final class Transformation {

    private final int toNPCId;

    public Transformation(int toNPCId) {
        this.toNPCId = toNPCId;
    }

    public int getToNPCId() {
        return toNPCId;
    }
}
