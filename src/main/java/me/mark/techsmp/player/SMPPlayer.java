package me.mark.techsmp.player;

import me.mark.techsmp.DatabaseManager;
import me.mark.techsmp.Main;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.ranks.Rank;
import me.mark.techsmp.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SMPPlayer {

    private Player player;
    private Group group;
    List<Group> invites;
    private boolean groupChat;
    private Rank rank;
    private int coins;

    public SMPPlayer(Player player, Rank rank, Group group, int coins) {
        this.player = player;
        this.rank = rank;
        this.group = group;
        this.coins = coins;
        this.invites = new ArrayList<>();
        this.groupChat = false;
        Main.getInstance().getSmpPlayerManager().addSMPPlayer(player.getUniqueId(), this);
        player.setPlayerListHeader(String.format("%s%scTech SMP", ChatColor.RED, ChatColor.BOLD));
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

    public void setGroup(Group group) {
        this.group = group;
        Main.getInstance().getDatabaseManager().updatePlayer(this);
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

    public void setRank(Rank rank) {
        this.rank = rank;
        if (getPlayer() != null)
            getPlayer().setPlayerListName(ChatUtil.setupTab(this, getPlayer()));

        Main.getInstance().getDatabaseManager().updatePlayer(this);
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
        Main.getInstance().getDatabaseManager().updatePlayer(this);
    }

    public void removeCoins(int amount) {
        this.coins -= amount;
        Main.getInstance().getDatabaseManager().updatePlayer(this);
    }

    public static SMPPlayer createSMPPlayer(Player player) {
        Main main = Main.getInstance();
        SMPPlayer smpPlayer = main.getSmpPlayerManager().getSMPPlayerFromPlayer(player);
        if (smpPlayer == null) {
            DatabaseManager db = main.getDatabaseManager();
            ResultSet rs = db.getPlayerData(player.getUniqueId());
            try {
                while (rs.next()) {
                    String groupName = rs.getString("groupName");
                    String rankName = rs.getString("rankName");
                    int coins = rs.getInt("coins");
                    Group group = main.getGroupManager().getGroupByName(groupName);
                    Rank rank = Rank.getRankByName(rankName);
                    smpPlayer = new SMPPlayer(player, rank, group, coins);
                    group.addMember(smpPlayer, false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (smpPlayer == null) {
                smpPlayer = new SMPPlayer(player, Rank.NONE, null, 0);
                db.addPlayer(smpPlayer);
            }

        } else {
            smpPlayer.setPlayer(player);
        }

        return smpPlayer;
    }

}
