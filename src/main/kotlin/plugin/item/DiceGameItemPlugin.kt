package plugin.item

import org.redrune.game.content.entity.item.DiceGame
import org.redrune.game.content.plugin.type.ItemPlugin
import org.redrune.game.entity.actor.mask.Animation
import org.redrune.game.entity.actor.mask.Graphics
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.item.Item

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
class DiceGameItemPlugin : ItemPlugin {
    override fun handle(player: Player, item: Item, slotId: Int, option: String): Boolean {
        val itemId = item.id
        when (option) {
            "Private-roll" -> when (itemId) {
                15098 -> {
                    DiceGame.rollDice8(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2075))
                }

                15086 -> {
                    DiceGame.rollDice2(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2072))
                }

                15088 -> {
                    DiceGame.rollDice3(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2074))
                }

                15090 -> {
                    DiceGame.rollDice4(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2071))
                }

                15092 -> {
                    DiceGame.rollDice5(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2070))
                }

                15094 -> {
                    DiceGame.rollDice5(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2073))
                }

                15096 -> {
                    DiceGame.rollDice7(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2068))
                }

                15100 -> {
                    DiceGame.rollDice1(player)
                    player.nextAnimation = Animation(11900)
                    player.setNextGraphics(Graphics(2069))
                }
            }

            else -> return true
        }
        return true
    }

    override fun register() {
        val ids = intArrayOf(15098, 15086, 15088, 15090, 15092, 15094, 15096, 15100)
        for (id in ids) {
            registerItem(id, "Private-Roll")
            registerItem(id, "Friends-Roll")
            registerItem(id, "Choose-dice")
            registerItem(id, "Put-away")
        }
    }
}