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
    private String name;
    private ChatColor color;
    private List<SMPPlayer> members;

    public Group(String name, UUID uuid) {
        this.name = name;
        this.leaderUUID = uuid;
        this.color = ChatColor.GREEN;
        this.members = new ArrayList<>();

        Main.getInstance().getGroupManager().addGroup(this);
        Main.getInstance().getConfigManager().storeGroupIfNotExist(this);
    }

    public Group(String name, SMPPlayer player) {
        this.name = name;
        this.leaderUUID = player.getPlayer().getUniqueId();
        this.color = ChatColor.GREEN;
        this.members = new ArrayList<>();

        addMember(player, false);

        Main.getInstance().getGroupManager().addGroup(this);
        Main.getInstance().getConfigManager().storeGroupIfNotExist(this);
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeader(UUID uuid) {
        this.leaderUUID = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateGroupMemberTabs();
        Main.getInstance().getConfigManager().updateGroupStats(this);
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
        updateGroupMemberTabs();
        Main.getInstance().getConfigManager().updateGroupStats(this);
    }

    public List<SMPPlayer> getMembers() {
        return members;
    }

    public void addMember(SMPPlayer smpPlayer, boolean generated) {
        smpPlayer.setGroup(this, generated);
        this.members.add(smpPlayer);
        if (smpPlayer.getInvites().contains(this)) smpPlayer.removeInvite(this);
        for (SMPPlayer player : getMembers()) {
            if (smpPlayer.getPlayer() == null || player.getPlayer() == null) continue;
            if (player.getPlayer().isOnline())
                player.getPlayer().sendMessage(String.format("%s %s joined!", Main.getPrefix(), smpPlayer.getPlayer().getDisplayName()));
        }
        if (smpPlayer.getPlayer() != null) smpPlayer.getPlayer().setPlayerListName(ChatUtil.setupTab(smpPlayer));
    }

    public void removeMember(SMPPlayer smpPlayer) {
        this.members.remove(smpPlayer);
        for (SMPPlayer player : getMembers()) {
            if (player.getPlayer() == null || smpPlayer.getPlayer() == null) continue;
            if (player.getPlayer().isOnline()) player.getPlayer().sendMessage(String.format("%s %s has left the group!", Main.getPrefix(), smpPlayer.getPlayer().getDisplayName()));
        }
        smpPlayer.setGroup(null, false);
        if (smpPlayer.getPlayer() == null) return;
        smpPlayer.getPlayer().setPlayerListName(String.format("%s%s", ChatColor.YELLOW, smpPlayer.getPlayer().getDisplayName()));
        smpPlayer.getPlayer().sendMessage(String.format("%s You have left %s%s%s!", Main.getPrefix(), getColor(), getName(), ChatColor.WHITE));
    }

    public void deleteGroup() {
        Main.getInstance().getGroupManager().removeGroup(this);
        Main.getInstance().getConfigManager().deleteGroup(this);
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
            smpPlayer.getPlayer().setPlayerListName(ChatUtil.setupTab(smpPlayer));
        }
    }

}
