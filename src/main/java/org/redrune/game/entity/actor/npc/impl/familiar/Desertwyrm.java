package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class Desertwyrm extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 678861520073043877L;

    public Desertwyrm(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
        return 6;
    }

    @Override
    public String getSpecialName() {
        return "Electric Lash";
    }

    @Override
    public String getSpecialDescription() {
        return "Attacks the player's opponent inflicting up to 50 damage instead of 40 damage. ";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

}
