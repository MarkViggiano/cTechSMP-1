package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends BaseListener {
    public ChatListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        SMPPlayer smpPlayer = getMain().getSmpPlayerManager().getSMPPlayerFromPlayer(e.getPlayer());

        String message = ChatUtil.setupMessage(smpPlayer, e.getMessage());

        if (smpPlayer.isGroupChatting()) {
            e.setCancelled(true);

            for (SMPPlayer member : smpPlayer.getGroup().getMembers()) {
                if (member.getPlayer().isOnline()) member.getPlayer().sendMessage(message);
            }
            return;
        }

        e.setFormat(message);
    }

}
