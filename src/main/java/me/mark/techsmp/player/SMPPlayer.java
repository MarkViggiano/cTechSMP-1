package me.mark.techsmp.player;

import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.ranks.Rank;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SMPPlayer {

    private Player player;
    private Group group;
    List<Group> invites;
    private boolean groupChat;
    private Rank rank;

    public SMPPlayer(Player player, boolean generated) {
        this.player = player;
        this.invites = new ArrayList<>();
        this.groupChat = false;
        Main.getInstance().getSmpPlayerManager().addSMPPlayer(this);
        if (generated) return;
        player.setPlayerListHeader(String.format("%s%scTech SMP", ChatColor.RED, ChatColor.BOLD));
        setRank(Rank.NONE, true);
        Main.getInstance().getConfigManager().storePlayerIfNotExist(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group, boolean generated) {
        this.group = group;
        if (generated) return;
        Main.getInstance().getConfigManager().updatePlayerStats(this);
    }

    public void toggleGroupChat() {
        if (isGroupChatting()) this.groupChat = false;
        else this.groupChat = true;
    }

    public boolean isGroupChatting() {
        return groupChat;
    }

    public List<Group> getInvites() {
        return invites;
    }

    public void addInvite(Group group) {
        this.invites.add(group);
    }

    public void removeInvite(Group group) {
        this.invites.remove(group);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank, boolean fromConfig) {
        this.rank = rank;
        if (getPlayer() != null)
            getPlayer().setPlayerListName(ChatUtil.setupTab(this));
        if (fromConfig) return;
        Main.getInstance().getConfigManager().updatePlayerStats(this);
    }
}
