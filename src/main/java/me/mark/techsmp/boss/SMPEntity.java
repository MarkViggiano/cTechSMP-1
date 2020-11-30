package me.mark.techsmp.boss;

import me.mark.techsmp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class SMPEntity {

    private EntityType type;
    private Entity entity;
    private double MAX_HEALTH;
    private double health;
    private BossBar bossBar;

    public SMPEntity(EntityType type, double MAX_HEALTH) {
        this.type = type;
        this.MAX_HEALTH = MAX_HEALTH;
        this.health = MAX_HEALTH;
    }

    public abstract String getName();
    public abstract boolean isBoss();

    public void spawnEntity(Location location) {
        World world = location.getWorld();
        if (world == null) return; //should never happen but just in case
        Entity entity = world.spawnEntity(location, getType());
        setEntity(entity);
        if (!isBoss()) return;
        this.bossBar = Bukkit.createBossBar(getName(), BarColor.PURPLE, BarStyle.SOLID);
        updateBossBar();
        for (Player player : Bukkit.getOnlinePlayers()) {
            getBossBar().addPlayer(player);
        }

        Main.getInstance().getSmpEntityManager().addEntity(this);
    }

    public void killEntity() {
        getEntity().remove();
        Main.getInstance().getSmpEntityManager().removeEntity(getEntity().getUniqueId());
        if (isBoss()) removeBossBar();
    }

    public EntityType getType() {
        return type;
    }

    protected void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public double getHealth() {
        return health;
    }

    public void damage(double amount) {
        this.health -= amount;
        if (getHealth() <= 0) killEntity();
    }

    public void damage(int amount) {
        this.health -= amount;
        if (getHealth() <= 0) killEntity();
    }

    public void heal(double amount) {
        this.health += amount;
        if (getHealth() >= getMAX_HEALTH()) this.health = getMAX_HEALTH();
    }

    public void heal(int amount) {
        this.health += amount;
        if (getHealth() >= getMAX_HEALTH()) this.health = getMAX_HEALTH();
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    protected void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public void updateBossBar() {
        getBossBar().setProgress(getHealth()/getMAX_HEALTH());
    }

    public void removeBossBar() {
        getBossBar().removeAll();
    }

}
