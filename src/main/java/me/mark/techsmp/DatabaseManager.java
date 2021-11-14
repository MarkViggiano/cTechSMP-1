package me.mark.techsmp;

import me.mark.techsmp.groups.Group;
import me.mark.techsmp.player.SMPPlayer;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private Connection conn;

    public DatabaseManager(String username, String password) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ctechsmp?verifyServerCertificate=false&allowPublicKeyRetrieval=true&useSSL=false&requireSSL=false", username, password);
            generateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConn() {
        return conn;
    }

    private void generateTables() throws SQLException{
        Connection conn = getConn();
        Statement stmt = conn.createStatement();
        //Add 'Data' to the end just to make sure the name isn't some sql statement in future updates (this happened to me once I hit my head against the wall for an hour fixing it).
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS groupsData (name VARCHAR(255), ownerUuid VARCHAR(255), color VARCHAR(255), coins INT)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS playersData (uuid VARCHAR(255), groupName VARCHAR(255), rankName VARCHAR(255), coins INT)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS killsData (killer VARCHAR(255), dead VARCHAR(255))");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS groupLogData (uuid VARCHAR(255), oldGroup VARCHAR(255), newGroup VARCHAR(255))");
    }

    /*
    GROUPS
     */

    public ResultSet getGroupData(String groupName) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM groupsData WHERE name=?");
            query.setString(1, groupName);
            return query.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getAllGroupData() {
        Connection conn = getConn();
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("SELECT * FROM groupsData");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addGroup(Group group) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO groupsData (name, ownerUuid, color, coins) VALUES (?, ?, ?, ?)");
            query.setString(1, group.getName());
            query.setString(2, group.getLeader().toString());
            query.setString(3, group.getColor().name());
            query.setInt(4, group.getCoins());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroup(Group group) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("UPDATE groupsData SET ownerUuid=?, color=?, coins=? WHERE name=?");
            query.setString(1, group.getLeader().toString());
            query.setString(2, group.getColor().name());
            query.setInt(3, group.getCoins());
            query.setString(4, group.getName());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGroup(Group group) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("DELETE FROM groupsData WHERE ownerUuid=?");
            query.setString(1, group.getLeader().toString());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addGroupLog(UUID uuid, String oldName, String newName) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO groupLogData (uuid, oldGroup, newGroup) VALUES (?, ?, ?)");
            query.setString(1, uuid.toString());
            query.setString(2, oldName);
            query.setString(3, newName);
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    PLAYERS
     */

    public ResultSet getPlayerData(UUID uuid) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("SELECT * FROM playersData WHERE uuid=?");
            query.setString(1, uuid.toString());
            return query.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPlayer(SMPPlayer smpPlayer) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO playersData (uuid, groupName, rankName, coins) VALUES (?, ?, ?, ?)");
            query.setString(1, smpPlayer.getPlayer().getUniqueId().toString());
            query.setString(2, "None");
            query.setString(3, smpPlayer.getRank().getName());
            query.setInt(4, smpPlayer.getCoins());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePlayer(SMPPlayer smpPlayer) {
        Connection conn = getConn();
        String groupName;
        if (smpPlayer.getGroup() == null) groupName = "None";
        else groupName = smpPlayer.getGroup().getName();
        try {
            PreparedStatement query = conn.prepareStatement("UPDATE playersData SET groupName=?, rankName=?, coins=? WHERE uuid=?");
            query.setString(1, groupName);
            query.setString(2, smpPlayer.getRank().getName());
            query.setInt(3, smpPlayer.getCoins());
            query.setString(4, smpPlayer.getPlayer().getUniqueId().toString());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addKillLog(UUID killer, UUID victim) {
        Connection conn = getConn();
        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO killsData (killer, victim) VALUES (?, ?)");
            query.setString(1, killer.toString());
            query.setString(2, victim.toString());
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
