package me.mark.techsmp.commands;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (args.length < 2) {
            commandSender.sendMessage(String.format("%s Missing arguments! Must be: /setrole <player name> <role name>", Main.getPrefix()));
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can perform this command!");
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(String.format("%s Only OPs can set roles!", Main.getPrefix()));
            return true;
        }
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) smpPlayer = new SMPPlayer(player.getUniqueId(), player, false);

        String playerName = args[0];
        String rankName = args[1];

        Player rankPlayer = Bukkit.getPlayer(playerName);
        if (rankPlayer == null) {
            commandSender.sendMessage(String.format("%s Player not found!", Main.getPrefix()));
            return true;
        }
        SMPPlayer smpRankedPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(rankPlayer);
        if (smpRankedPlayer == null) {
            commandSender.sendMessage(String.format("%s Player must be online!", Main.getPrefix()));
            return true;
        }

        switch (rankName.toLowerCase()) {
            case "owner":
                smpRankedPlayer.setRank(Rank.OWNER, false);
                break;

            case "dev":
            case "developer":
                smpRankedPlayer.setRank(Rank.DEVELOPER, false);
                break;
            case "admin":
                smpRankedPlayer.setRank(Rank.ADMIN, false);
                break;
            case "mod":
                smpRankedPlayer.setRank(Rank.MOD, false);
                break;
            default:
                smpRankedPlayer.setRank(Rank.NONE, false);
                break;
        }

        commandSender.sendMessage(String.format("%s %s's role has been updated to: %s%s%s!",
                 Main.getPrefix(), playerName, smpRankedPlayer.getRank().getColor(), smpRankedPlayer.getRank().getName(), ChatColor.WHITE));

        smpRankedPlayer.getPlayer().sendMessage(String.format("%s Your role has been set to: %s%s%s!",
                Main.getPrefix(), smpRankedPlayer.getRank().getColor(), smpRankedPlayer.getRank().getName(), ChatColor.WHITE));

        return true;
    }
}
