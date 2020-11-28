package me.mark.techsmp.commands;

import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CordsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;

        Player player = (Player) commandSender;
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) return true;
        if (smpPlayer.getGroup() == null) {
            player.sendMessage(String.format("%s You must be in a group to use this command!", Main.getPrefix()));
            return true;
        }

        Group group = smpPlayer.getGroup();
        String message = String.format("%s %s%s's coords: %s, %s, %s on %s", Main.getPrefix(), smpPlayer.getGroup().getColor(), player.getDisplayName(),
                player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), player.getLocation().getWorld().getEnvironment().name());


        for (SMPPlayer member : group.getMembers()) {
            if (member == null || member.getPlayer() == null) continue;
            if (member.getPlayer().isOnline()) member.getPlayer().sendMessage(message);
        }

        return true;
    }
}
