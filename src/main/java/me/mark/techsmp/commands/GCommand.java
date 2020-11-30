package me.mark.techsmp.commands;

import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) commandSender.sendMessage("Only players can do this!");

        Player player = (Player) commandSender;
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) smpPlayer = new SMPPlayer(player.getUniqueId(), player, false);

        if (args.length == 0) {
            sendInvalidArgumentsMessage(player);
            return true;
        }

        String initialParam = args[0];

        switch (initialParam.toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing group name!", Main.getPrefix()));
                    break;
                }
                String createGroupName = args[1];
                createGroup(createGroupName, player, smpPlayer);
                break;

            case "delete":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing group name!", Main.getPrefix()));
                    break;
                }
                String deleteGroupName = args[1];
                deleteGroup(deleteGroupName, player);
                break;

            case "invite":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing player name!", Main.getPrefix()));
                    break;
                }
                String playerName = args[1];
                invitePlayer(playerName, player);
                break;

            case "leave":
                leaveGroup(player);
                break;

            case "join":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing group name!", Main.getPrefix()));
                    break;
                }
                String groupName = args[1];
                joinGroup(player, groupName);
                break;

            case "kick":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing player name!", Main.getPrefix()));
                    break;
                }
                kickPlayer(player, args[1]);
                break;

            case "color":
                if (args.length < 2) {
                    player.sendMessage(String.format("%s Invalid arguments! Missing color!", Main.getPrefix()));
                    break;
                }
                setGroupColor(player, args[1].toUpperCase());
                break;

            case "chat":
                toggleGroupChat(player);
                break;

            case "help":
            default:
                sendInvalidArgumentsMessage(player);
                break;

        }

        return true;
    }

    private void sendInvalidArgumentsMessage(Player player) {
        player.sendMessage(String.format("%s %s Invalid arguments! Proper commands:", Main.getPrefix(), ChatColor.RED));
        player.sendMessage(String.format("%s /g create <name> | Create a group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g delete <name> | Delete a group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g invite <player name> | Invite player to group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g join <group name> | Join a group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g leave | Leave your current group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g kick <player name> | Kick Player from group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g color <color name> | Change the color of your group.", ChatColor.RED));
        player.sendMessage(String.format("%s /g chat | Toggle on/off a chat only you and your group mates can see. ", ChatColor.RED));
    }

    private void createGroup(String name, Player player, SMPPlayer smpPlayer) {
        if (Main.getInstance().getConfigManager().isGroupInConfig(name) || Main.getInstance().getGroupManager().getGroups().containsValue(name)) {
            player.sendMessage(String.format("%s Group with name: %s%s%s already exists!", Main.getPrefix(), ChatColor.GREEN, name, ChatColor.WHITE));
            return;
        }

        for (Map.Entry<String, Group> entry : Main.getInstance().getGroupManager().getGroups().entrySet()) {
            Group existingGroup = entry.getValue();
            if (existingGroup.isLeader(smpPlayer)) {
                smpPlayer.getPlayer().sendMessage(String.format("%s You already own a group!", Main.getPrefix()));
                return;
            }
        }

        Group group = new Group(name, smpPlayer);
        player.sendMessage(String.format("%s Created group with name: %s%s%s!", Main.getPrefix(), ChatColor.GREEN, group.getName(), ChatColor.WHITE));
        player.setPlayerListName(String.format("%s[%s%s%s] %s%s",
                ChatColor.GRAY, group.getColor(), group.getName(), ChatColor.GRAY, ChatColor.YELLOW, player.getName()));
    }

    private void deleteGroup(String name, Player player) {
        Group group = Main.getInstance().getGroupManager().getGroupByName(name);
        if (group == null) {
            player.sendMessage(String.format("%s Group with name: %s%s%s does not exists!", Main.getPrefix(), ChatColor.GREEN, name, ChatColor.WHITE));
            return;
        }
        if (!group.isLeader(player)) {
            player.sendMessage(String.format("%s Only the leader can delete the group!", Main.getPrefix()));
            return;
        }

        player.sendMessage(String.format("%s Created group with name: %s%s%s!", Main.getPrefix(), ChatColor.GREEN, group.getName(), ChatColor.WHITE));
        for (SMPPlayer smpPlayer : group.getMembers()) {
            if (smpPlayer.getPlayer().isOnline()) smpPlayer.getPlayer().setPlayerListName(ChatUtil.setupTab(smpPlayer));
        }
        group.deleteGroup();
    }

    private void invitePlayer(String playerName, Player player) {
        Player invitedPlayer = Bukkit.getPlayer(playerName);
        if (invitedPlayer == null) {
            player.sendMessage(String.format("%s Player does not exist!", Main.getPrefix()));
            return;
        }
        SMPPlayer inviter = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (!inviter.getGroup().isLeader(inviter)) {
            player.sendMessage(String.format("%s Only the leader can invite players!", Main.getPrefix()));
            return;
        }
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(invitedPlayer);
        if (smpPlayer == null) {
            player.sendMessage(String.format("%s Player is not online!", Main.getPrefix()));
            return;
        }

        player.sendMessage(String.format("%s You have invited %s%s%s!", Main.getPrefix(), inviter.getGroup().getColor(), invitedPlayer.getDisplayName(), ChatColor.WHITE));
        smpPlayer.addInvite(inviter.getGroup());
        invitedPlayer.sendMessage(String.format("%s You have been invited to join group: %s%s%s! To join, type: /g join %s",
                Main.getPrefix(), inviter.getGroup().getColor(), inviter.getGroup().getName(), ChatColor.WHITE, inviter.getGroup().getName()));

    }

    private void leaveGroup(Player leavingPlayer) {
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(leavingPlayer);
        if (smpPlayer == null) return;
        if (smpPlayer.getGroup() == null) {
            leavingPlayer.sendMessage(String.format("%s You must be in a group to leave it!", Main.getPrefix()));
            return;
        }

        if (smpPlayer.getGroup().isLeader(leavingPlayer)) {
            if (smpPlayer.getGroup().getMembers().size() <= 1) {
                smpPlayer.getGroup().deleteGroup();
                return;
            }
            smpPlayer.getGroup().setLeader(smpPlayer.getGroup().getMembers().get(1).getPlayer().getUniqueId());
            smpPlayer.getGroup().removeMember(smpPlayer);
            return;
        }
        else smpPlayer.getGroup().removeMember(smpPlayer);

    }

    private void joinGroup(Player joiningPlayer, String groupName) {
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(joiningPlayer);
        Group group = Main.getInstance().getGroupManager().getGroupByName(groupName);
        if (smpPlayer == null) return;
        if (group == null) {
            joiningPlayer.sendMessage(String.format("%s Invalid group name!", Main.getPrefix()));
            return;
        }

        if (!smpPlayer.getInvites().contains(group)) {
            joiningPlayer.sendMessage(String.format("%s You must be invited to the group before joining!", Main.getPrefix()));
            return;
        }

        group.addMember(smpPlayer, false);
        smpPlayer.getPlayer().sendMessage(String.format("%s You joined group: %s%s%s!", Main.getPrefix(), group.getColor(), group.getName(), ChatColor.WHITE));
    }

    private void kickPlayer(Player leader, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            leader.sendMessage(String.format("%s Player does not exist!", Main.getPrefix()));
            return;
        }
        SMPPlayer smpKick = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        SMPPlayer smpLeader = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(leader);
        if (smpLeader == null) return;
        if (smpKick == null) {
            leader.sendMessage(String.format("%s Player does must be online to kick!", Main.getPrefix()));
            return;
        }

        smpLeader.getGroup().removeMember(smpKick);

    }

    private void setGroupColor(Player player, String colorName) {
        ChatColor color = ChatColor.valueOf(colorName);
        if (color == null) {
            player.sendMessage(String.format("%s Invalid color!", Main.getPrefix()));
            return;
        }
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) return;
        Group group = smpPlayer.getGroup();
        if (group == null) {
            player.sendMessage(String.format("%s You must be in a group to change it's color!", Main.getPrefix()));
            return;
        }

        if (!group.isLeader(smpPlayer)) {
            player.sendMessage(String.format("%s You must be the leader to change the group color!", Main.getPrefix()));
            return;
        }

        group.setColor(color);
        player.sendMessage(String.format("%s Group color has changed!", Main.getPrefix()));

    }

    private void toggleGroupChat(Player player) {
        SMPPlayer smpPlayer = Main.getInstance().getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer.getGroup() == null) {
            player.sendMessage(String.format("%s You must be in a group to toggle group chat!", Main.getPrefix()));
            return;
        }

        smpPlayer.toggleGroupChat();
        ChatColor color;
        String status;
        if (smpPlayer.isGroupChatting()) {
            color = ChatColor.GREEN;
            status = "ON";
        } else {
            color = ChatColor.RED;
            status = "OFF";
        }
        player.sendMessage(String.format("%s Group chat is %s%s%s!", Main.getPrefix(), color, status, ChatColor.WHITE));

    }

}
