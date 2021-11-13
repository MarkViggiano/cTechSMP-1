package me.mark.techsmp.groups;

import me.mark.techsmp.Main;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {

    private UUID leaderUUID;
    private final String name;
    private ChatColor color;
    private final List<SMPPlayer> members;
    private int coins;

    public Group(String name, UUID uuid, ChatColor color, int coins) {
        this.name = name;
        this.leaderUUID = uuid;
        this.color = color;
        this.coins = coins;
        this.members = new ArrayList<>();

        Main.getInstance().getGroupManager().addGroup(this);
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeader(UUID uuid) {
        this.leaderUUID = uuid;
        Main.getInstance().getDatabaseManager().updateGroup(this);
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
        updateGroupMemberTabs();
        Main.getInstance().getDatabaseManager().updateGroup(this);
    }

    public List<SMPPlayer> getMembers() {
        return members;
    }

    public void addMember(SMPPlayer smpPlayer) {
        smpPlayer.setGroup(this);
        this.members.add(smpPlayer);
        Main.getInstance().getDatabaseManager().addGroupLog(smpPlayer.getPlayer().getUniqueId(), "None", getName());
        if (smpPlayer.getInvites().contains(this)) smpPlayer.removeInvite(this);
        for (SMPPlayer player : getMembers()) {
            if (smpPlayer.getPlayer() == null || player.getPlayer() == null) continue;
            if (player.getPlayer().isOnline())
                player.getPlayer().sendMessage(String.format("%s %s joined!", Main.getPrefix(), smpPlayer.getPlayer().getDisplayName()));
        }
        if (smpPlayer.getPlayer() != null) smpPlayer.getPlayer().setPlayerListName(ChatUtil.setupTab(smpPlayer, smpPlayer.getPlayer()));
    }

    public void removeMember(SMPPlayer smpPlayer) {
        this.members.remove(smpPlayer);
        Main.getInstance().getDatabaseManager().addGroupLog(smpPlayer.getPlayer().getUniqueId(), getName(), "None");
        for (SMPPlayer player : getMembers()) {
            if (player.getPlayer() == null || smpPlayer.getPlayer() == null) continue;
            if (player.getPlayer().isOnline()) player.getPlayer().sendMessage(String.format("%s %s has left the group!", Main.getPrefix(), smpPlayer.getPlayer().getDisplayName()));
        }
        smpPlayer.setGroup(null);
        if (smpPlayer.getPlayer() == null) return;
        smpPlayer.getPlayer().setPlayerListName(String.format("%s%s", ChatColor.YELLOW, smpPlayer.getPlayer().getDisplayName()));
        smpPlayer.getPlayer().sendMessage(String.format("%s You have left %s%s%s!", Main.getPrefix(), getColor(), getName(), ChatColor.WHITE));
    }

    public void deleteGroup() {
        Main.getInstance().getGroupManager().removeGroup(this);
        try {
            this.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isLeader(SMPPlayer smpPlayer) {
        if (getLeader() == null) return false;
        return (getLeader().toString().equals(smpPlayer.getPlayer().getUniqueId().toString()));
    }

    public boolean isLeader(Player player) {
        if (getLeader() == null) return false;
        return (getLeader().toString().equals(player.getUniqueId().toString()));
    }

    public void updateGroupMemberTabs() {
        for (SMPPlayer smpPlayer : getMembers()) {
            if (smpPlayer == null || smpPlayer.getPlayer() == null) continue;
            smpPlayer.getPlayer().setPlayerListName(ChatUtil.setupTab(smpPlayer, smpPlayer.getPlayer()));
        }
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
        Main.getInstance().getDatabaseManager().updateGroup(this);
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
        Main.getInstance().getDatabaseManager().updateGroup(this);
    }

}
