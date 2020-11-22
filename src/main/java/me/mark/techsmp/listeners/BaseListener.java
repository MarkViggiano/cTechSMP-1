package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {

    private Main main;

    public BaseListener(Main plugin) {
        this.main = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info(String.format("[LISTENER] Registered listener: %s", this.getClass().getSimpleName()));
    }

    public Main getMain() {
        return main;
    }
}
