package game.content.entity.actor.player.action.impl

import game.content.entity.actor.combat.function.Magic
import game.content.entity.actor.player.action.Action
import game.entity.actor.mask.Animation
import game.entity.actor.mask.Graphics
import game.entity.actor.player.Player
import game.global.WorldTile
import game.global.map.region.RegionManager
import utility.constants.GameConstants
import utility.constants.MagicConstants
import utility.functions.Misc

class HomeTeleportAction : Action() {
    private var currentTime = 0
    private var tile: WorldTile? = null
    override fun start(player: Player): Boolean {
        tile = GameConstants.HOME_TILE.worldTile
        return if (!player.controllerManager.processMagicTeleport(tile)) {
            false
        } else process(player)
    }

    override fun process(player: Player): Boolean {
        if (player.attackedByDelay + 10000 > Misc.currentTimeMillis()) {
            player.packets.sendMessage("You can't home teleport until 10 seconds after the end of combat.")
            return false
        }
        return true
    }

    override fun processWithDelay(player: Player): Int {
        player.walkSteps.clear()
        if (currentTime++ == 0) {
            player.nextAnimation = Animation(HOME_ANIMATION)
            player.setNextGraphics(Graphics(HOME_GRAPHIC))
        } else if (currentTime == 17) {
            var teleTile = tile
            // attemps to randomize tile by 4x4 area
            for (trycount in 0..9) {
                teleTile = WorldTile(tile, 2)
                if (RegionManager.canMoveNPC(tile!!.plane, teleTile.x, teleTile.y, player.size)) {
                    break
                }
                teleTile = tile
            }
            player.setNextWorldTile(teleTile)
            player.nextAnimation = Animation(HOME_ANIMATION + 1)
            player.setNextGraphics(Graphics(HOME_GRAPHIC + 1))
            player.controllerManager.magicTeleported(MagicConstants.MAGIC_TELEPORT)
            if (player.controllerManager.controller == null) {
                Magic.teleControllersCheck(player, teleTile)
            }
            // return 0;
        } else if (currentTime == 21) {
            player.nextAnimation = Animation(-1)
            player.setNextGraphics(Graphics(-1))
            return -1
        }
        return 0
    }

    override fun stop(player: Player) {}

    companion object {
        protected const val HOME_ANIMATION = 16385
        protected const val HOME_GRAPHIC = 3017
        protected const val DONE_ANIMATION = 16386
    }
}