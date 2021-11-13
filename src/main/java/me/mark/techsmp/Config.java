package me.mark.techsmp;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private final Main plugin;

     public Config(Main main) {
         this.plugin = main;
     }

    public FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }

    public void saveConfig() {
        this.plugin.saveConfig();
    }

    public String[] getDatabaseDetails() {
         FileConfiguration config = getConfig();
         return new String[] {config.getString("database.username"), config.getString("database.password")};
    }

}
