package org.redrune.game.content.entity.actor.player.market;

import com.google.gson.reflect.TypeToken;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.file.JsonFileManager;
import org.redrune.utility.functions.Misc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.redrune.utility.functions.GsonFunctions.GSON;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/15/2017
 */
public final class ShopRepository {
	
	/**
	 * The map of all shops, the key is the shop identifier
	 */
	private static final Map<Integer, Shop> SHOPS = new HashMap<>();
	
	/**
	 * The map of all shop currencies
	 */
	private static final Map<String, ShopCurrency> SHOP_CURRENCIES = new HashMap<>();
	
	/**
	 * The location of the shop file
	 */
	private static final String SHOP_FILE_LOCATION = "./data/repository/item/shops.json";
	
	/**
	 * All data from external sources in relevance to shops is loaded in this method.
	 */
	public static void registerAll() {
		List<Shop> shops = loadShops();
		if (shops == null) {
			System.out.println("Unable to load shops from " + SHOP_FILE_LOCATION + "!");
			return;
		}
		Misc.getClasses(ShopRepository.class.getPackage().getName() + ".currency").stream().filter(ShopCurrency.class::isInstance).forEach(clazz -> {
			ShopCurrency currency = (ShopCurrency) clazz;
			SHOP_CURRENCIES.put(currency.getClass().getSimpleName(), currency);
		});
		shops.forEach(ShopRepository::loadShopCurrency);
		System.out.println("Loaded " + SHOPS.size() + " game shops, and " + SHOP_CURRENCIES.size() + " currencies...");
	}
	
	/**
	 * Loads the currency of a shop
	 *
	 * @param shop
	 * 		The shop
	 */
	private static void loadShopCurrency(Shop shop) {
		String currencyName = shop.getCurrencyName() + "Currency";
		ShopCurrency currency = SHOP_CURRENCIES.get(currencyName);
		if (currency == null) {
			System.out.println("Unable to find currency {" + currencyName + "} for shop {" + shop.getName() + "}");
			return;
		}
		shop.setCurrency(currency);
		if (!SHOPS.containsKey(shop.getIdentifier())) {
			SHOPS.put(shop.getIdentifier(), shop);
		} else {
			System.out.println("Unable to load shop #" + shop.getIdentifier() + " - " + SHOPS.get(shop.getIdentifier()).getName() + " was using it already...");
		}
	}
	
	/**
	 * Opens a shop
	 *
	 * @param player
	 * 		The player opening the shop
	 * @param identifier
	 * 		The identifier of the shop
	 */
	public static void open(Player player, int identifier) {
		Shop shop = SHOPS.get(identifier);
		if (shop == null) {
			System.out.println("Unable to find shop #" + identifier);
			return;
		}
		shop.open(player);
	}
	
	/**
	 * Gets the collection of shops loaded into a map
	 */
	public static Map<Integer, Shop> getShops() {
		return SHOPS;
	}
	
	/**
	 * This method saves a collection of shops to the file
	 */
	public static boolean saveShops(List<Shop> shopList) {
		return JsonFileManager.save(shopList, SHOP_FILE_LOCATION);
	}
	
	/**
	 * Gets the characteristic instance from a file
	 */
	private static List<Shop> loadShops() {
		File file = new File(SHOP_FILE_LOCATION);
		if (!file.exists()) {
			return null;
		}
		String text = Misc.getText(SHOP_FILE_LOCATION);
		return GSON.fromJson(text, new TypeToken<List<Shop>>() {
		}.getType());
	}
}
