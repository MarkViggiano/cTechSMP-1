package me.mark.techsmp.commands;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        SMPPlayer smpPlayer = SMPPlayer.createSMPPlayer(player);
        player.sendMessage(String.format("%s Your balance is: %s%s%s coins", Main.getPrefix(), ChatColor.GREEN, smpPlayer.getCoins(), ChatColor.WHITE));
        return true;
    }
}
