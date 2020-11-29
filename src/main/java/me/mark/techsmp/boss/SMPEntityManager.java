package me.mark.techsmp.boss;

import me.mark.techsmp.Main;

import java.util.HashMap;
import java.util.UUID;

public class SMPEntityManager {

    private Main plugin;
    private HashMap<UUID, SMPEntity> entities;

    public SMPEntityManager(Main plugin) {
        this.plugin = plugin;
        this.entities = new HashMap<>();
    }

    public HashMap<UUID, SMPEntity> getEntities() {
        return entities;
    }

    public void addEntity(SMPEntity entity) {
        this.entities.putIfAbsent(entity.getEntity().getUniqueId(), entity);
    }

    public SMPEntity getEntity(UUID uuid) {
        return this.entities.getOrDefault(uuid, null);
    }

    public void removeEntity(UUID uuid) {
        this.entities.remove(uuid);
    }
}
