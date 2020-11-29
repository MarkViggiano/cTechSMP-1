package me.mark.techsmp.boss;

import me.mark.techsmp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class Boss extends SMPEntity {

    public Boss(EntityType type, double MAX_HEALTH) {
        super(type, MAX_HEALTH);
    }

    @Override
    public void spawnEntity(Location location) {
        World world = location.getWorld();
        if (world == null) return; //should never happen but just in case
        Entity entity = world.spawnEntity(location, getType());
        setEntity(entity);
        setBossBar(Bukkit.createBossBar(getName(), BarColor.PURPLE, BarStyle.SOLID));
        updateBossBar();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 3, 2);
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 4, 1);
            player.sendMessage(getSpawnMessage());
            getBossBar().addPlayer(player);
        }
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    public String getSpawnMessage() {
        return String.format("%s The %s has spawned!", Main.getPrefix(), getName());
    }
}
