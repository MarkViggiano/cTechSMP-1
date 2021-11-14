package me.mark.techsmp.util;

import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static String setupMessage(SMPPlayer smpPlayer, Player player, String sendMessage) {

        if (smpPlayer == null) smpPlayer = SMPPlayer.createSMPPlayer(player);

        String message = String.format("%s%s > %s%s", ChatColor.GRAY, smpPlayer.getPlayer().getName(), ChatColor.WHITE, sendMessage);
        if (smpPlayer.isGroupChatting()) {
            if (smpPlayer.getGroup() != null) {
                message = String.format("%s%s > %s%s", ChatColor.GRAY, smpPlayer.getPlayer().getName(), smpPlayer.getGroup().getColor(), sendMessage);
            } else {
                smpPlayer.toggleGroupChat();
            }
        }

        if (smpPlayer.getGroup() != null) {
            message = String.format("%s[%s%s%s] ", ChatColor.GRAY, smpPlayer.getGroup().getColor(), smpPlayer.getGroup().getName(), ChatColor.GRAY) + message;
        }

        if (smpPlayer.getRank() != Rank.NONE) {
            message = String.format("%s%s ", smpPlayer.getRank().getColor(), smpPlayer.getRank().getName()) + message;
        }

        return message;
    }

    public static String setupTab(SMPPlayer smpPlayer, Player player) {
        if (smpPlayer == null)
            smpPlayer = SMPPlayer.createSMPPlayer(player);

        String message = String.format("%s%s", ChatColor.YELLOW, smpPlayer.getPlayer().getName());

        if (smpPlayer.getGroup() != null) {
            message = String.format("%s[%s%s%s] ", ChatColor.GRAY, smpPlayer.getGroup().getColor(), smpPlayer.getGroup().getName(), ChatColor.GRAY) + message;
        }

        if (smpPlayer.getRank() != Rank.NONE) {
            message = String.format("%s%s ", smpPlayer.getRank().getColor(), smpPlayer.getRank().getName()) + message;
        }

        return message;
    }

}
