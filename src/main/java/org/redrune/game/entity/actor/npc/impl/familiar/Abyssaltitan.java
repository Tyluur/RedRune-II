package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class Abyssaltitan extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 7635947578932404484L;

    public Abyssaltitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public boolean submitSpecial(Object object) {
        if (getOwner().getBank().hasBankSpace()) {
            if (getBob().getBeastItems().getUsedSlots() == 0) {
                getOwner().getPackets().sendMessage("You clearly have no essence.");
                return false;
            }
            getOwner().getBank().depositAllBob(false);
            getOwner().setNextGraphics(new Graphics(1316));
            getOwner().setNextAnimation(new Animation(7660));
            return true;
        }
        return false;
    }

    @Override
    public int getBOBSize() {
        return 7;
    }

    @Override
    public int getSpecialAmount() {
        return 6;
    }

    @Override
    public String getSpecialName() {
        return "Essence Shipment";
    }

    @Override
    public String getSpecialDescription() {
        return "Sends all your inventory and beast's essence to your bank.";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }
}
