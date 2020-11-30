package me.mark.techsmp;

import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;
import me.mark.techsmp.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Config {

    private Main plugin;

     public Config(Main main) {
         this.plugin = main;
     }

    public FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }

    public void saveConfig() {
        this.plugin.saveConfig();
    }

    public void generatePlayersFromConfig() {
         FileConfiguration config = getConfig();
        List<String> playersData = config.getStringList("Players");
        if (playersData.isEmpty()) return;
        for (String playerData : playersData) {
            String[] playerInfo = playerData.split(",");
            Main.getInstance().getLogger().info(playerData);

            //PLAYER
            String uuid = playerInfo[0];
            Player player = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer();
            SMPPlayer smpPlayer = new SMPPlayer(UUID.fromString(uuid), player, true);

            //RANK
            String rankName = playerInfo[2];
            switch (rankName.toLowerCase()) {
                case "owner":
                    smpPlayer.setRank(Rank.OWNER, true);
                    break;
                case "developer":
                    smpPlayer.setRank(Rank.DEVELOPER, true);
                    break;
                case "admin":
                    smpPlayer.setRank(Rank.ADMIN, true);
                    break;
                case "mod":
                    smpPlayer.setRank(Rank.MOD, true);
                    break;
                default:
                    smpPlayer.setRank(Rank.NONE, true);
                    break;
            }

            //GROUP
            String groupName = playerInfo[1];
            Group group = this.plugin.getGroupManager().getGroupByName(groupName);
            if (group == null) {
                smpPlayer.setGroup(null, true);
                continue;
            }
            group.addMember(smpPlayer, true);
        }
    }


    public void generateGroupsFromConfig() {
        /*
        Groups:
         -name, uuid of owner, color
         */
        FileConfiguration config = getConfig();
        List<String> groupsData = config.getStringList("Groups");
        if (groupsData == null || groupsData.isEmpty()) return;

        for (String groupData : groupsData) {
            String[] data = groupData.split(",");
            String name = data[0];

            ChatColor color = ChatColor.valueOf(data[2]);
            Group group = new Group(name, UUID.fromString(data[1]));
            if (data[2].isEmpty() || data[2] == null) return;
            group.setColor(color);
            Main.getInstance().getLogger().info(String.format("[Groups] Created group with name: %s | Owner: %s | Color: %s", group.getName(), group.getLeader().toString(), group.getColor().name()));
        }
    }

    public boolean isPlayerInConfig(SMPPlayer smpPlayer) {
        FileConfiguration config = getConfig();
        if (smpPlayer == null) return false;
        String playerData = config.getString("Players");
        if (playerData == null || playerData.isEmpty()) return false;
        if (playerData.contains(smpPlayer.getPlayer().getUniqueId().toString())) return true;
        return false;
    }

    public boolean isGroupInConfig(String groupName) {
        FileConfiguration config = getConfig();
        List<String> groupData = config.getStringList("Groups");
        if (groupData == null || groupData.isEmpty()) return false;
        if (!groupData.contains(groupName)) return false;
        return true;
    }

    public void storePlayerIfNotExist(SMPPlayer smpPlayer) {
        FileConfiguration config = getConfig();
        List<String> playersData = config.getStringList("Players");
        if (isPlayerInConfig(smpPlayer)) return;
        playerDataEntry(config, smpPlayer, playersData);
    }

    public void storeGroupIfNotExist(Group group) {
        FileConfiguration config = getConfig();
        List<String> groupsData = config.getStringList("Groups");
        if (groupsData.contains(group.getName())) return;
        groupDataEntry(config, group, groupsData);

    }

    public void updateGroupStats(Group group) {
        if (!isGroupInConfig(group.getName())) storeGroupIfNotExist(group);
        FileConfiguration config = getConfig();
        List<String> groupsData = config.getStringList("Groups");

        groupDataEntry(config, group, groupsData);
    }

    public void updatePlayerStats(SMPPlayer smpPlayer) {
        FileConfiguration config = getConfig();
        List<String> playerData = config.getStringList("Players");
        playerDataEntry(config, smpPlayer, playerData);
    }

    public void deleteGroup(Group group) {
        FileConfiguration config = getConfig();
        if (!isGroupInConfig(group.getName())) return;
        List<String> groupData = config.getStringList("Groups");
        for (int i = 0; i < groupData.size(); i++) {
            if (groupData.get(i).contains(group.getName())) {
                groupData.remove(i);
            }
        }
        config.set("Groups", groupData);

        //Player Stuff
        List<String> playersData = config.getStringList("Players");

        for (int i = 0; i < playersData.size(); i++) {
            String playerData = playersData.get(i);
            String[] data = playerData.split(",");
            if (data[1] == group.getName()) {
                data[1] = null;
            }
        }
        config.set("Players", playersData);

        saveConfig();
    }

    private void groupDataEntry(FileConfiguration config, Group group, List<String> groupsData) {
        if (groupsData == null || groupsData.isEmpty()) {
            groupsData = new ArrayList<>();
            String name = group.getName();
            String owner = group.getLeader().toString();
            String color = group.getColor().name();
            String data = name + "," + owner + "," + color;
            groupsData.add(data);
            config.set("Groups", groupsData);
            saveConfig();
            return;
        }

        for (int i = 0; i < groupsData.size(); i++) {
            if (groupsData.get(i).contains(group.getName())) groupsData.remove(i);
        }
        String name = group.getName();
        String owner = group.getLeader().toString();
        String color = group.getColor().name();
        String data = name + "," + owner + "," + color;
        groupsData.add(data);
        config.set("Groups", groupsData);
        saveConfig();

    }

    private void playerDataEntry(FileConfiguration config, SMPPlayer player, List<String> playersData) {
        if (playersData == null || playersData.isEmpty()) {
            playersData = new ArrayList<>();
            String uuid = player.getPlayer().getUniqueId().toString();
            String group;
            if (player.getGroup() == null) group = "none";
            else group = player.getGroup().getName();

            String rank = player.getRank().getName().toLowerCase();
            String data = uuid + "," + group + "," + rank;
            playersData.add(data);
            config.set("Players", playersData);
            saveConfig();
            return;
        }

        for (int i = 0; i < playersData.size(); i++) {
            if (playersData.get(i).contains(player.getPlayer().getUniqueId().toString())) playersData.remove(i);
        }
        String uuid = player.getPlayer().getUniqueId().toString();
        String group;
        if (player.getGroup() == null) group = "none";
        else group = player.getGroup().getName();

        String rank = player.getRank().getName().toLowerCase();
        String data = uuid + "," + group + "," + rank;
        playersData.add(data);
        config.set("Players", playersData);
        saveConfig();

    }

}
