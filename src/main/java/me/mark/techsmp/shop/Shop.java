package me.mark.techsmp.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import java.util.HashMap;

public class Shop {

    private final Inventory inventory;
    private final HashMap<Material, ShopItem> items;

    public Shop (String title, int rows) {
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.items = new HashMap<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addItem(ShopItem shopItem) {
        this.items.putIfAbsent(shopItem.getItem().getType(), shopItem);
        this.inventory.addItem(shopItem.getItem());
    }

    public ShopItem getShopItemByMaterial(Material material) {
        return this.items.getOrDefault(material, null);
    }

    public HashMap<Material, ShopItem> getItems() {
        return items;
    }
}
