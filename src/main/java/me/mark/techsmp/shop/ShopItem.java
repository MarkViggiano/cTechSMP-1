package me.mark.techsmp.shop;

import me.mark.techsmp.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;

public class ShopItem {

    private final ItemStack item;
    private final int sellPrice;
    private final int cost;

    public ShopItem(ItemStack item, int sellPrice, int cost) {
        this.item = ItemUtil.createItem(item, Arrays.asList(
                String.format("%s Left-Click to buy %s for: %s%s", ChatColor.YELLOW, item.getAmount(), ChatColor.GREEN, cost),
                String.format("%s Right-Click to sell %s for: %s%s", ChatColor.YELLOW, item.getAmount(), ChatColor.RED, sellPrice)
        ));
        this.sellPrice = sellPrice;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
