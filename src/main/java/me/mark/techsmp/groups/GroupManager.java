package me.mark.techsmp.groups;

import me.mark.techsmp.Main;

import java.util.HashMap;

public class GroupManager {

    private Main plugin;
    private HashMap<String, Group> groups;

    public GroupManager(Main plugin) {
        this.plugin = plugin;
        this.groups = new HashMap<>();
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        this.groups.putIfAbsent(group.getName(), group);
    }

    public void removeGroup(Group group) {
        if (group == null) return; //should never happen
        this.groups.remove(group.getName());
    }

    public Group getGroupByName(String name) {
        return this.groups.getOrDefault(name, null);
    }
}
