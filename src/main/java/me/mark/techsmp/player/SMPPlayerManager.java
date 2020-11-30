package me.mark.techsmp.player;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class SMPPlayerManager {

    private JavaPlugin plugin;
    private HashMap<UUID, SMPPlayer> smpPlayers;

    public SMPPlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.smpPlayers = new HashMap<>();
    }

    public void addSMPPlayer(UUID uuid, SMPPlayer smpPlayer) {
        if (smpPlayer == null) return;
        this.smpPlayers.putIfAbsent(uuid, smpPlayer);
    }

    public void removeSMPPlayer(UUID uuid) {
        if (!smpPlayers.containsValue(uuid)) return;
        this.smpPlayers.remove(uuid);
    }

    public SMPPlayer getSMPPlayerFromPlayer(Player player) {
        return this.smpPlayers.getOrDefault(player.getUniqueId(), null);
    }

    public SMPPlayer getSMPPlayerFromUUID(UUID uuid) {
        return this.smpPlayers.getOrDefault(uuid, null);
    }

}
