package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

public class Spiritspider extends Familiar {

    /**
     *
     */
    private static final long serialVersionUID = 5995661005749498978L;

    public Spiritspider(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
        super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public boolean submitSpecial(Object object) {
        Player player = (Player) object;
        setNextAnimation(new Animation(8267));
        player.setNextAnimation(new Animation(7660));
        player.setNextGraphics(new Graphics(1316));
        WorldTile tile = this;
        // attemps to randomize tile by 4x4 area
        for (int trycount = 0; trycount < Misc.getRandom(10); trycount++) {
            tile = new WorldTile(this, 2);
            if (RegionManager.canMoveNPC(this.getPlane(), tile.getX(), tile.getY(), player.getSize())) {
                return true;
            }
            for (Actor actor : this.getPossibleTargets(true, true)) {
                if (actor instanceof Player) {
                    Player players = (Player) actor;
                    players.getPackets().sendGraphics(new Graphics(1342), tile);
                }
                RegionManager.addGroundItem(new Item(223, 1), tile, player, false, 120, true);
            }
        }
        return true;
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
        return "Egg Spawn";
    }

    @Override
    public String getSpecialDescription() {
        return "Spawns a random amount of red eggs around the familiar.";
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }
}
