package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class Giantchinchompa extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -7708802901929527088L;

    public Giantchinchompa(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public boolean submitSpecial(Object object) {
        return false;
    }

    @Override
    public int getBOBSize() {
        return 0;
    }

    @Override
    public int getSpecialAmount() {
        return 3;
    }

    @Override
    public String getSpecialName() {
        return "Explode";
    }

    @Override
    public String getSpecialDescription() {
        return "Explodes, damaging nearby enemies.";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

}
