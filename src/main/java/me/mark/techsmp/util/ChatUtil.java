package me.mark.techsmp.util;

import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.ranks.Rank;
import org.bukkit.ChatColor;
import javax.annotation.Nonnull;

public class ChatUtil {

    public static String setupMessage(SMPPlayer smpPlayer, String sendMessage) {

        if (smpPlayer == null) return sendMessage;

        String message = String.format("%s%s > %s%s", ChatColor.GRAY, smpPlayer.getPlayer().getName(), ChatColor.WHITE, sendMessage);
        if (smpPlayer.isGroupChatting()) {
            message = String.format("%s%s > %s%s", ChatColor.GRAY, smpPlayer.getPlayer().getName(), smpPlayer.getGroup().getColor(), sendMessage);
        }

        if (smpPlayer.getGroup() != null) {
            message = String.format("%s[%s%s%s] ", ChatColor.GRAY, smpPlayer.getGroup().getColor(), smpPlayer.getGroup().getName(), ChatColor.GRAY) + message;
        }

        if (smpPlayer.getRank() != Rank.NONE) {
            message = String.format("%s%s ", smpPlayer.getRank().getColor(), smpPlayer.getRank().getName()) + message;
        }

        return message;
    }

    public static String setupTab(@Nonnull SMPPlayer smpPlayer) {

        if (smpPlayer.getPlayer() == null)
            return String.format("%sSMP Player", ChatColor.YELLOW);

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
