package me.matt11matthew.mckit.listeners;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.player.AbstractDuelPlayer;
import me.matt11matthew.mckit.player.DuelPlayerManager;
import me.matt11matthew.mckit.utilities.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Matthew E on 6/12/2017.
 */
public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Config config = McKitsDuels.getInstance().getDuelsConfig();
        if (config.isSet("joinMessageList")) {
            for (String messageString : config.getStringList("joinMessageList")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageString.replaceAll("\\{player}", player.getName())));
            }
        }
        DuelPlayerManager playerManager = DuelPlayerManager.getInstance();
        if (!playerManager.isPlayer(player.getUniqueId())) {
            AbstractDuelPlayer abstractDuelPlayer = playerManager.create(player.getUniqueId(), player.getName());
            //new player
        }  else {
            AbstractDuelPlayer duelPlayer = playerManager.getOnlineDuelPlayer(player.getUniqueId());
            if (duelPlayer != null) {
                // not new player
            }
        }
        if (config.isSet("locations.spawnPoint")) {
            Location location = LocationUtil.getPlayerLocationFromString(config.getString("locations.spawnPoint"));
            player.teleport(location);
        }
    }
}
