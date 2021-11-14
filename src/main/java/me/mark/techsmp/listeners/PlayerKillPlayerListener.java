package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillPlayerListener extends BaseListener {

    public PlayerKillPlayerListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerKillPlayer(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer != null) getMain().getDatabaseManager().addKillLog(killer.getUniqueId(), victim.getUniqueId());
    }

}
