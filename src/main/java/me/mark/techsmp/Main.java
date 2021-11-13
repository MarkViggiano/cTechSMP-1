package me.mark.techsmp;

import me.mark.techsmp.commands.*;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.groups.GroupManager;
import me.mark.techsmp.listeners.ChatListener;
import me.mark.techsmp.listeners.PlayerConnectionListener;
import me.mark.techsmp.listeners.SleepListener;
import me.mark.techsmp.player.SMPPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private SMPPlayerManager smpPlayerManager;
    private GroupManager groupManager;
    private Config configManager;
    private DatabaseManager databaseManager;
    private static final String prefix = String.format("%s[%scTech SMP%s]%s", ChatColor.GRAY, ChatColor.YELLOW, ChatColor.GRAY, ChatColor.WHITE);

    @Override
    public void onEnable() {
        INSTANCE = this;
        groupManager = new GroupManager(this);
        smpPlayerManager = new SMPPlayerManager(this);
        loadConfig();
        configManager = new Config(this);
        registerDatabaseConnection();
        createGroupsFromDatabase();
        registerListeners();
        registerCommands();
        getLogger().info("Loaded plugin!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unloaded plugin!");
        saveConfig();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands() {
        getCommand("g").setExecutor(new GCommand());
        getCommand("coords").setExecutor(new CordsCommand());
        getCommand("setrole").setExecutor(new RoleCommand());
        getCommand("bal").setExecutor(new BalCommand());
        getCommand("pay").setExecutor(new PayCommand());
    }

    private void registerListeners() {
        new PlayerConnectionListener(this);
        new ChatListener(this);
        new SleepListener(this);
    }

    private void registerDatabaseConnection() {
        String[] details = getConfigManager().getDatabaseDetails();
        this.databaseManager = new DatabaseManager(details[0], details[1]);
    }

    private void createGroupsFromDatabase() {
        try {
            ResultSet result = getDatabaseManager().getAllGroupData();
            String name;
            UUID ownerUuid;
            ChatColor colorName;
            int coins;
            while (result.next()) {
                //name VARCHAR(255), ownerUuid VARCHAR(255), color VARCHAR(255), coins INT
                name = result.getString("name");
                ownerUuid = UUID.fromString(result.getString("ownerUuid"));
                colorName = ChatColor.valueOf(result.getString("color"));
                coins = result.getInt("coins");
                new Group(name, ownerUuid, colorName, coins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Config getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public SMPPlayerManager getSmpPlayerManager() {
        return smpPlayerManager;
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static String getPrefix() {
        return prefix;
    }
}
