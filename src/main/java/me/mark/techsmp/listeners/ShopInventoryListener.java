package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.shop.Shop;
import me.mark.techsmp.shop.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ShopInventoryListener extends BaseListener {
    public ShopInventoryListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        SMPPlayer smpPlayer = SMPPlayer.createSMPPlayer((Player) e.getWhoClicked());
        if (e.getClickedInventory() == getMain().getGroupShop().getInventory()) handleGroupShop(e, smpPlayer);
        if (e.getClickedInventory() == getMain().getItemShop().getInventory()) handleItemShop(e, smpPlayer);
    }

    private void handleGroupShop(InventoryClickEvent e, SMPPlayer smpPlayer) {
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;
        Shop groupShop = getMain().getGroupShop();
        ShopItem shopItem = groupShop.getShopItemByMaterial(clickedItem.getType());
        int cost = shopItem.getCost();
        Group group = smpPlayer.getGroup();
        Player player = smpPlayer.getPlayer();
        /*
        Left-Click to Buy 1
        Shift + Left-Click to Buy 64
        Right Click to Sell 1
        Shift + Right Click to sell 64 (or whatever is remaining of a stack)
         */
        switch (e.getClick()) {
            case LEFT:
                if (group.getCoins() < cost) {
                    player.sendMessage(String.format("%s Your group cannot afford that item!", Main.getPrefix()));
                    return;
                }

                group.removeCoins(cost);
                player.getInventory().addItem(new ItemStack(shopItem.getItem().getType(), 1));
                player.sendMessage(String.format("%s%s -%s", Main.getPrefix(), ChatColor.RED, cost));
                break;
            case SHIFT_LEFT:
                int buyCost = (cost / shopItem.getItem().getAmount()) * 64;
                if (group.getCoins() < buyCost) {
                    player.sendMessage(String.format("%s Your group cannot afford that item!", Main.getPrefix()));
                    return;
                }

                group.removeCoins(buyCost);
                player.getInventory().addItem(new ItemStack(shopItem.getItem().getType(), 64));
                player.sendMessage(String.format("%s%s -%s", Main.getPrefix(), ChatColor.RED, buyCost));
                break;
            case RIGHT:
            case SHIFT_RIGHT:
                smpPlayer.getPlayer().sendMessage(String.format("%s You cannot sell items bought in the group shop!", Main.getPrefix()));
                break;

        }
    }

    private void handleItemShop(InventoryClickEvent e, SMPPlayer smpPlayer) {
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;
        Shop groupShop = getMain().getGroupShop();
        ShopItem shopItem = groupShop.getShopItemByMaterial(clickedItem.getType());
        int cost = shopItem.getCost();
        Player player = smpPlayer.getPlayer();
        /*
        Left-Click to Buy 1
        Shift + Left-Click to Buy 64
        Right Click to Sell 1
        Shift + Right Click to sell 64 (or whatever is remaining of a stack)
         */
        switch (e.getClick()) {
            case LEFT:
                if (smpPlayer.getCoins() < cost) {
                    player.sendMessage(String.format("%s You cannot afford that item!", Main.getPrefix()));
                    return;
                }

                smpPlayer.removeCoins(cost);
                player.getInventory().addItem(new ItemStack(clickedItem.getType(), clickedItem.getAmount()));
                player.sendMessage(String.format("%s%s -%s", Main.getPrefix(), ChatColor.RED, cost));
                break;

            case SHIFT_LEFT:
                int buyCost = (cost / shopItem.getItem().getAmount()) * 64;
                if (smpPlayer.getCoins() < buyCost) {
                    player.sendMessage(String.format("%s You cannot afford that item!", Main.getPrefix()));
                    return;
                }

                smpPlayer.removeCoins(buyCost);
                player.getInventory().addItem(new ItemStack(clickedItem.getType(), 64));
                player.sendMessage(String.format("%s%s -%s", Main.getPrefix(), ChatColor.RED, buyCost));
                break;

            case RIGHT:

                int singleSellCost = shopItem.getSellPrice() / clickedItem.getAmount();
                if (!player.getInventory().contains(clickedItem.getType())) {
                    player.sendMessage(String.format("%s You do not have any of that item to sell!", Main.getPrefix()));
                    return;
                }

                smpPlayer.addCoins(singleSellCost);
                player.getInventory().remove(new ItemStack(clickedItem.getType(), 1));
                player.updateInventory();
                player.sendMessage(String.format("%s%s +%s", Main.getPrefix(), ChatColor.GREEN, singleSellCost));
                break;

            case SHIFT_RIGHT:
                int stackSellCost = (shopItem.getSellPrice() / clickedItem.getAmount()) * 64;
                if (!player.getInventory().contains(clickedItem.getType())) {
                    player.sendMessage(String.format("%s You do not have any of that item to sell!", Main.getPrefix()));
                    return;
                }

                smpPlayer.addCoins(stackSellCost);
                player.getInventory().remove(new ItemStack(clickedItem.getType(), 64));
                player.updateInventory();
                player.sendMessage(String.format("%s%s +%s", Main.getPrefix(), ChatColor.GREEN, stackSellCost));
                break;

        }
    }

}
