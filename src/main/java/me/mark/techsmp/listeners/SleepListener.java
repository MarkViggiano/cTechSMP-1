package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepListener extends BaseListener {
    public SleepListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerSleepEvent(PlayerBedEnterEvent event) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(String.format("%sIt's a new day!", ChatColor.YELLOW));
        }
        event.getPlayer().getWorld().setTime(0);
    }
}
