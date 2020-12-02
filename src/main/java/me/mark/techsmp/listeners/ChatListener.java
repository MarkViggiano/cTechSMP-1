package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
                if (member.getPlayer() == null) continue;
                if (member.getPlayer().isOnline()) member.getPlayer().sendMessage(message);
            }
            return;
        }

        e.setFormat(message);
        mentionPlayer(message);
    }

    private void mentionPlayer(String message) {
        String parseMessage = message.substring(message.indexOf('>') + 1);
        //Use int i instead of an iterator because its faster (No need to create a new iterator object)
        for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
            Player player = (Player) Bukkit.getOnlinePlayers().toArray()[i];
            if (!parseMessage.contains(player.getName())) continue;
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3, 10);
        }
    }

}
