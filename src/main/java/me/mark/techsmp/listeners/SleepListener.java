package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepListener extends BaseListener {
    public SleepListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerSleepEvent(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(getMain(), () -> {
            Bukkit.broadcastMessage(String.format("%s %s skipped the night!", Main.getPrefix(), event.getPlayer().getDisplayName()));
            event.getPlayer().getWorld().setTime(0);
        }, 50);
    }
}
