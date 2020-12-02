package me.mark.techsmp.listeners;

import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class PlayerConnectionListener extends BaseListener {
    public PlayerConnectionListener(Main plugin) {
        super(plugin);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.sendMessage(String.format("%s Welcome to the cTech SMP!", Main.getPrefix()));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3, 2);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            onlinePlayer.setPlayerListFooter(String.format("%s%sPlayers Online: %s%s", ChatColor.WHITE, ChatColor.BOLD, ChatColor.RED, Bukkit.getOnlinePlayers().size()));

        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) smpPlayer = new SMPPlayer(player.getUniqueId(), player, false);

        smpPlayer.setPlayer(player);
        player.setPlayerListHeader(String.format("%s%scTech SMP", ChatColor.RED, ChatColor.BOLD));
        String listName = ChatUtil.setupTab(smpPlayer);
        player.setPlayerListName(listName);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            for (Map.Entry<String, Group> groupEntry : Main.getInstance().getGroupManager().getGroups().entrySet()) {
                Group group = groupEntry.getValue();
                group.updateGroupMemberTabs();
            }
        }, 20);

        player.setDisplayName(ChatUtil.setupTab(smpPlayer));

    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e) {
        //subtract one because the player isn't removed from the online players list until the end of the listener method.
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            onlinePlayer.setPlayerListFooter(String.format("%s%sPlayers Online: %s%s", ChatColor.WHITE, ChatColor.BOLD, ChatColor.RED, Bukkit.getOnlinePlayers().size() - 1));
    }
}
