package me.mark.techsmp;

import me.mark.techsmp.commands.*;
import me.mark.techsmp.groups.Group;
import me.mark.techsmp.groups.GroupManager;
import me.mark.techsmp.listeners.*;
import me.mark.techsmp.player.SMPPlayerManager;
import me.mark.techsmp.shop.Shop;
import me.mark.techsmp.shop.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private SMPPlayerManager smpPlayerManager;
    private GroupManager groupManager;
    private Config configManager;
    private DatabaseManager databaseManager;
    private Shop itemShop;
    private Shop groupShop;
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
        registerItemShop();
        registerGroupShop();
        removeRecipes();
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
        getCommand("shop").setExecutor(new ShopCommand());
    }

    private void registerListeners() {
        new PlayerConnectionListener(this);
        new ChatListener(this);
        new SleepListener(this);
        new ShopInventoryListener(this);
        new CreatePortalListener(this);
        new PlayerKillPlayerListener(this);
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

    private void registerItemShop() {
        Shop shop = new Shop("Item Shop", 5);
        shop.addItem(new ShopItem(new ItemStack(Material.COOKED_BEEF, 16), 65, 100));
        shop.addItem(new ShopItem(new ItemStack(Material.COOKED_CHICKEN, 16), 60, 85));
        shop.addItem(new ShopItem(new ItemStack(Material.COOKED_PORKCHOP, 16), 55, 80));
        shop.addItem(new ShopItem(new ItemStack(Material.GOLDEN_CARROT, 8), 35, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.BAKED_POTATO, 8), 25, 40));
        shop.addItem(new ShopItem(new ItemStack(Material.WHEAT_SEEDS, 32), 35, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.CARROT, 16), 35, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.POTATO, 8), 35, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.MELON_SEEDS, 4), 20, 30));
        shop.addItem(new ShopItem(new ItemStack(Material.PUMPKIN_SEEDS, 4), 20, 30));
        shop.addItem(new ShopItem(new ItemStack(Material.SUGAR_CANE, 16), 55, 80));
        shop.addItem(new ShopItem(new ItemStack(Material.RED_MUSHROOM, 4), 15, 25));
        shop.addItem(new ShopItem(new ItemStack(Material.BROWN_MUSHROOM, 4), 15, 25));
        shop.addItem(new ShopItem(new ItemStack(Material.DIAMOND, 1), 850, 1500));
        shop.addItem(new ShopItem(new ItemStack(Material.IRON_INGOT, 1), 150, 250));
        shop.addItem(new ShopItem(new ItemStack(Material.GOLD_INGOT, 1), 225, 350));
        shop.addItem(new ShopItem(new ItemStack(Material.EMERALD, 1), 500, 750));
        shop.addItem(new ShopItem(new ItemStack(Material.REDSTONE, 16), 60, 100));
        shop.addItem(new ShopItem(new ItemStack(Material.LAPIS_LAZULI, 8), 135, 200));
        shop.addItem(new ShopItem(new ItemStack(Material.QUARTZ, 4), 35, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.GLOWSTONE, 16), 200, 300));
        shop.addItem(new ShopItem(new ItemStack(Material.COBBLESTONE, 16), 16, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.STONE, 16), 32, 75));
        shop.addItem(new ShopItem(new ItemStack(Material.OAK_LOG, 8), 10, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.DIRT, 16), 10, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.GRASS_BLOCK, 16), 15, 80));
        shop.addItem(new ShopItem(new ItemStack(Material.GRAVEL, 8), 25, 100));
        shop.addItem(new ShopItem(new ItemStack(Material.SAND, 16), 10, 50));
        shop.addItem(new ShopItem(new ItemStack(Material.GLASS, 16), 35, 75));
        shop.addItem(new ShopItem(new ItemStack(Material.BLAZE_ROD, 3), 100, 500));
        this.itemShop = shop;
    }

    private void registerGroupShop() {
        Shop shop = new Shop("Group Shop", 1);
        shop.addItem(new ShopItem(new ItemStack(Material.BEACON), 50000, 100000));
        shop.addItem(new ShopItem(new ItemStack(Material.ENCHANTING_TABLE), 5000, 10000));
        shop.addItem(new ShopItem(new ItemStack(Material.ANVIL), 5000, 10000));
        shop.addItem(new ShopItem(new ItemStack(Material.LECTERN), 10000, 20000));
        this.groupShop = shop;
    }

    private void removeRecipes() {
        Iterator<Recipe> it = getServer().recipeIterator();
        List<Material> removeMaterials = Arrays.asList(Material.BEACON, Material.ENCHANTING_TABLE, Material.ANVIL, Material.LECTERN);
        Recipe recipe;
        while (it.hasNext()) {
            recipe = it.next();
            if (recipe != null && removeMaterials.contains(recipe.getResult().getType())) it.remove();
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


    public Shop getItemShop() {
        return itemShop;
    }

    public Shop getGroupShop() {
        return groupShop;
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static String getPrefix() {
        return prefix;
    }
}
