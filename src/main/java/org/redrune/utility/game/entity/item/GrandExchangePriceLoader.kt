package org.redrune.utility.game.entity.item

import com.github.michaelbull.logging.InlineLogger
import org.koin.dsl.module
import org.redrune.utility.functions.Misc
import java.nio.file.Paths

class GrandExchangePriceLoader {

    init {
        load()
    }

    private fun load() {
        val lines = Misc.getFileText(path.toString())
        for ((index, line) in lines.withIndex()) {
            var itemId = -1
            var price = -1

            try {
                val split = line.split(" - ")
                itemId = Integer.parseInt(split[0])
                price = Integer.parseInt(split[1])
            } catch (e: Exception) {
                logger.error { "Unable to parse grand exchange price! [line[$index]=$line]" }
            }
            map[itemId] = price
        }
        logger.info { "${map.size} Grand exchange prices completed loading" }
    }

    fun getPrice(itemId: Int): Int? {
        return map[itemId]
    }

    companion object {

        private val map = hashMapOf<Int, Int>()

        private val logger = InlineLogger()

        private val path = Paths.get("data/repository/item/grand_exchange_prices_08-05-2010.txt")

    }

}

val priceLoaderModule = module { single(createdAtStart = true) { GrandExchangePriceLoader() } }