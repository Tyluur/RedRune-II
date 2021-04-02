package org.redrune.game.content.entity.actor.player.market.currency;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.player.market.ShopCurrency;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/15/2017
 */
public class CoinCurrency implements ShopCurrency {

    @Override
    public String name() {
        return "coins";
    }

    @Override
    public int getCurrencyAmount(Player player) {
        return player.getInventory().getItems().getNumberOf(COINS);
    }

    @Override
    public void reduceCurrency(Player player, int amount) {
        player.getInventory().deleteItem(COINS, amount);
    }

    @Override
    public int getBuyPrice(int itemId) {
        return ItemDefinitions.getItemDefinitions(itemId).getValue();
    }

    @Override
    public int stockAmount(int itemId) {
        switch (itemId) {
            case 15273:
                return 10;
        }
        return 1;
    }

    @Override
    public int itemId() {
        return COINS;
    }
}
