package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class Thornysnail extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = -1147053487269627345L;

    public Thornysnail(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public boolean submitSpecial(Object object) {
        return false;
    }

    @Override
    public int getBOBSize() {
        return 3;
    }

    @Override
    public int getSpecialAmount() {
        return 0;
    }

    @Override
    public String getSpecialName() {
        return "Slime Spray";
    }

    @Override
    public String getSpecialDescription() {
        return "Inflicts up to 80 damage against your opponent.";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }
}
