package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.PortalCreateEvent;

public class CreatePortalListener extends BaseListener {
    public CreatePortalListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerCreatePortal(PortalCreateEvent e) {
        e.setCancelled(true);
        if (e.getEntity() != null) e.getEntity().sendMessage("%s Creating portals to other dimensions isn't cool...", Main.getPrefix());
    }
}
