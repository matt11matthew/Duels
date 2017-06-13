package me.matt11matthew.mckit.listeners;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.utilities.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Matthew E on 6/12/2017.
 */
public class ArenaListeners implements Listener {
    private Location position1;
    private Location position2;
    private static ArenaListeners instance;

    public static ArenaListeners getInstance() {
        if (instance == null) {
            instance = new ArenaListeners();
        }
        return instance;
    }

    public ArenaListeners() {
        instance = this;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if ((block != null) && (block.getType() != Material.AIR)) {
            ItemStack itemStack = player.getItemInHand();
            if ((itemStack != null) && (itemStack.getType() == Material.STICK) && (itemStack.hasItemMeta()) && (itemStack.getItemMeta().hasDisplayName()) && (itemStack.getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Set positions " + ChatColor.GRAY + "(Right-Click for pos1 | Left-Click for pos2)"))) {
                if (player.hasPermission(McKitsDuels.getInstance().getConfig().getString("permissions.setupArena"))) {
                    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        this.position2 = block.getLocation();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', McKitsDuels.getInstance().getConfig().getString("messages.setPosition").replace("{number}", "2").replace("{location}", LocationUtil.getStringFromBlockLocation(block.getLocation()))));
                        return;
                    }
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        this.position1 = block.getLocation();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', McKitsDuels.getInstance().getConfig().getString("messages.setPosition").replace("{number}", "1").replace("{location}", LocationUtil.getStringFromBlockLocation(block.getLocation()))));
                        return;
                    }
                }
            }
        }
    }

    public ArenaListeners setPosition1(Location position1) {
        this.position1 = position1;
        return this;
    }

    public ArenaListeners setPosition2(Location position2) {
        this.position2 = position2;
        return this;
    }

    public Location getPosition1() {
        return position1;
    }

    public Location getPosition2() {
        return position2;
    }
}