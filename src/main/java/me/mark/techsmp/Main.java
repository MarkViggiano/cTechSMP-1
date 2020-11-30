package me.mark.techsmp;

import me.mark.techsmp.boss.SMPEntityManager;
import me.mark.techsmp.commands.CordsCommand;
import me.mark.techsmp.commands.GCommand;
import me.mark.techsmp.commands.RoleCommand;
import me.mark.techsmp.groups.GroupManager;
import me.mark.techsmp.listeners.ChatListener;
import me.mark.techsmp.listeners.PlayerConnectionListener;
import me.mark.techsmp.listeners.SleepListener;
import me.mark.techsmp.player.SMPPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private SMPPlayerManager smpPlayerManager;
    private SMPEntityManager smpEntityManager;
    private GroupManager groupManager;
    private Config configManager;
    private static String prefix = String.format("%s[%scTech SMP%s]%s", ChatColor.GRAY, ChatColor.YELLOW, ChatColor.GRAY, ChatColor.WHITE);

    @Override
    public void onEnable() {
        INSTANCE = this;
        groupManager = new GroupManager(this);
        smpPlayerManager = new SMPPlayerManager(this);
        smpEntityManager = new SMPEntityManager(this);
        loadConfig();
        configManager = new Config(this);
        configManager.generateGroupsFromConfig();
        configManager.generatePlayersFromConfig();
        registerListeners();
        registerCommands();
        registerRecipes();
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

    private void registerRecipes() {
        //TODO: add recipes
    }

    private void registerCommands() {
        getCommand("g").setExecutor(new GCommand());
        getCommand("coords").setExecutor(new CordsCommand());
        getCommand("setrole").setExecutor(new RoleCommand());
    }

    private void registerListeners() {
        new PlayerConnectionListener(this);
        new ChatListener(this);
        new SleepListener(this);
    }

    public Config getConfigManager() {
        return configManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public SMPPlayerManager getSmpPlayerManager() {
        return smpPlayerManager;
    }

    public SMPEntityManager getSmpEntityManager() {
        return smpEntityManager;
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static String getPrefix() {
        return prefix;
    }
}
