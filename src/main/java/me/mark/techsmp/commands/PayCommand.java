package me.mark.techsmp.commands;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        SMPPlayer smpPlayer = SMPPlayer.createSMPPlayer(player);
        if (args.length != 2) {
            player.sendMessage(String.format("%s Proper usage is: /pay <name> <amount> || Example: /pay Flaymed 100", Main.getPrefix()));
            return true;
        }

        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(String.format("%s That player does not exist or is not online!", Main.getPrefix()));
            return true;
        }
        SMPPlayer smpReceivingPlayer = SMPPlayer.createSMPPlayer(receiver);
        int senderAmount = smpPlayer.getCoins();
        int amount = Integer.parseInt(args[1]);
        if (amount > senderAmount) {
            player.sendMessage(String.format("%s You don't have enough coins for that! Use `/bal` to check your coins.", Main.getPrefix()));
            return true;
        }
        smpPlayer.removeCoins(amount);
        smpReceivingPlayer.addCoins(amount);
        player.sendMessage(String.format("%s You have sent %s%s%s coins to %s%s%s.", Main.getPrefix(), ChatColor.GREEN, amount, ChatColor.WHITE, ChatColor.GREEN, args[0], ChatColor.WHITE));
        receiver.sendMessage(String.format("%s You have received %s%s%s from %s%s%s.", Main.getPrefix(), ChatColor.GREEN, amount, ChatColor.WHITE, ChatColor.GREEN, player.getName(), ChatColor.WHITE));
        return true;
    }
}
