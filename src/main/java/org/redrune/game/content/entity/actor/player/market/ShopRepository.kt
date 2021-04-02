package org.redrune.game.content.entity.actor.player.market

import com.github.michaelbull.logging.InlineLogger
import com.google.gson.reflect.TypeToken
import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.file.JsonFileManager
import org.redrune.utility.functions.GsonFunctions
import org.redrune.utility.functions.Misc
import java.io.File
import java.util.*
import java.util.function.Consumer

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 6/15/2017
 */
object ShopRepository {
    /**
     * The map of all shops, the key is the shop identifier
     */
    private val SHOPS: MutableMap<Int, Shop> = HashMap()

    /**
     * The map of all shop currencies
     */
    private val SHOP_CURRENCIES: MutableMap<String, ShopCurrency> = HashMap()

    /**
     * The location of the shop file
     */
    private const val SHOP_FILE_LOCATION = "./data/repository/item/shops.json"

    /**
     * All data from external sources in relevance to shops is loaded in this method.
     */
    @JvmStatic
    fun registerAll() {
        val shops = loadShops()
        if (shops == null) {
            logger.error { "Unable to load shops from " + SHOP_FILE_LOCATION + "!" }
            return
        }
        Misc.getClasses(ShopRepository::class.java.getPackage().name + ".currency").stream()
            .filter { obj: Any? -> ShopCurrency::class.java.isInstance(obj) }
            .forEach { clazz: Any ->
                val currency = clazz as ShopCurrency
                SHOP_CURRENCIES[currency.javaClass.simpleName] = currency
            }
        shops.forEach(Consumer { obj: Shop -> loadShopCurrency(obj) })
        logger.info { "Loaded " + SHOPS.size + " game shops, and " + SHOP_CURRENCIES.size + " currencies..." }
    }

    /**
     * Loads the currency of a shop
     *
     * @param shop
     * The shop
     */
    private fun loadShopCurrency(shop: Shop) {
        val currencyName = shop.currencyName + "Currency"
        val currency = SHOP_CURRENCIES[currencyName]
        if (currency == null) {
            println("Unable to find currency {" + currencyName + "} for shop {" + shop.name + "}")
            return
        }
        shop.currency = currency
        if (!SHOPS.containsKey(shop.identifier)) {
            SHOPS[shop.identifier] = shop
        } else {
            println(
                "Unable to load shop #" + shop.identifier + " - " + SHOPS[shop.identifier]!!
                    .name + " was using it already..."
            )
        }
    }

    /**
     * Opens a shop
     *
     * @param player
     * The player opening the shop
     * @param identifier
     * The identifier of the shop
     */
    fun open(player: Player?, identifier: Int) {
        val shop = SHOPS[identifier]
        if (shop == null) {
            println("Unable to find shop #$identifier")
            return
        }
        shop.open(player)
    }

    /**
     * Gets the collection of shops loaded into a map
     */
    @JvmStatic
    val shops: Map<Int, Shop>
        get() = SHOPS

    /**
     * This method saves a collection of shops to the file
     */
    @JvmStatic
    fun saveShops(shopList: List<Shop>): Boolean {
        return JsonFileManager.save(shopList, SHOP_FILE_LOCATION)
    }

    /**
     * Gets the characteristic instance from a file
     */
    private fun loadShops(): List<Shop>? {
        val file = File(SHOP_FILE_LOCATION)
        if (!file.exists()) {
            return null
        }
        val text = Misc.getText(SHOP_FILE_LOCATION)
        return GsonFunctions.GSON.fromJson(text, object : TypeToken<List<Shop?>?>() {}.type)
    }

    private val logger = InlineLogger()
}