package me.matt11matthew.mckit.game;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.utilities.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;


/**
 * Created by Matthew E on 6/13/2017.
 */
public class ArenaManager implements Listener {
    private static ArenaManager instance;

    public static ArenaManager getInstance() {
        if (instance == null) {
            instance = new ArenaManager();
        }
        return instance;
    }

    public ArenaManager() {
        instance = this;
    }

    public void completeSetup() {
        Config arenaConfig = McKitsDuels.getInstance().getArenaConfig();
        arenaConfig.set("setup", true);
        try {
            arenaConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSetupComplete() {
        return  McKitsDuels.getInstance().getArenaConfig().getBoolean("setup");
    }

    public Location getPosition(int position) {
        Config arenaConfig = McKitsDuels.getInstance().getArenaConfig();
        if (position == 1) {
            return LocationUtil.getBlockLocationFromString(arenaConfig.getString("positionOne"));
        } else if (position == 2) {
            return LocationUtil.getBlockLocationFromString(arenaConfig.getString("positionTwo"));
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    public void setPosition(int position, Location location) {
        Config arenaConfig = McKitsDuels.getInstance().getArenaConfig();
        if (position == 1) {
            arenaConfig.set("positionOne", LocationUtil.getStringFromBlockLocation(location));
        } else if (position == 2) {
            arenaConfig.set("positionTwo", LocationUtil.getStringFromBlockLocation(location));
        }
        try {
            arenaConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void giveSpawnItems(Player player) {
        player.getInventory().setItem(0, getRankedSword());
        player.getInventory().setItem(1, getUnRankedSword());
    }

    private ItemStack getRankedSword() {
        Config config = McKitsDuels.getInstance().getDuelsConfig();
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("rankedSwordName")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getUnRankedSword() {
        Config config = McKitsDuels.getInstance().getDuelsConfig();
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("unRankedSwordName")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}