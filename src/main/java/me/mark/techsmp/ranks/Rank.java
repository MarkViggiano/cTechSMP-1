package me.mark.techsmp.ranks;

import org.bukkit.ChatColor;

public enum Rank {

    COUNCIL("Council", ChatColor.DARK_RED),
    NONE("none", ChatColor.WHITE);

    private final String name;
    private final ChatColor color;

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

    public static Rank getRankByName(String name) {
        if (name.equalsIgnoreCase("council")) return COUNCIL;
        return NONE;
    }

}
