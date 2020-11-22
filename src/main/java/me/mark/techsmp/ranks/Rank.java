package me.mark.techsmp.ranks;

import org.bukkit.ChatColor;

public enum Rank {

    OWNER("Owner", ChatColor.DARK_RED),
    DEVELOPER("Developer", ChatColor.RED),
    ADMIN("Admin", ChatColor.GOLD),
    MOD("Mod", ChatColor.YELLOW),
    NONE("none", ChatColor.WHITE);

    private String name;
    private ChatColor color;

    Rank(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
}
