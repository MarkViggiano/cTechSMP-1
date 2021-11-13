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
                String.format("%s Left-Click to buy 1 for: %s%s", ChatColor.YELLOW, ChatColor.GREEN, cost),
                String.format("%s Shift + Left-Click to buy 64 for: %s%s", ChatColor.YELLOW, ChatColor.GREEN, 64 * cost),
                String.format("%s Right-Click to sell 1 for: %s%s", ChatColor.YELLOW, ChatColor.RED, sellPrice),
                String.format("%s Shift + Right-Click to sell all", ChatColor.YELLOW)
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
