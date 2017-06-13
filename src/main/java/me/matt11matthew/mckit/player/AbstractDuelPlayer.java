package me.matt11matthew.mckit.player;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.player.holder.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Matthew E on 6/12/2017.
 */
public abstract class AbstractDuelPlayer implements CreditHolder, DuelPlayer, EloHolder, RankedWinsHolder, UnRankedWinsHolder, DeathsHolder, KillsHolder {
    private UUID uniqueId;
    private String username;
    private File file;
    private FileConfiguration configFile;

    public AbstractDuelPlayer(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/",this.getUniqueId() + ".yml");
        if (!this.file.getParentFile().exists()) {
            this. file.getParentFile().mkdirs();
        }
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.configFile = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AbstractDuelPlayer{");
        sb.append("uniqueId=").append(uniqueId);
        sb.append(", username='").append(username).append('\'');
        sb.append(", file=").append(file);
        sb.append(", configFile=").append(configFile);
        sb.append('}');
        return sb.toString();
    }

    public static AbstractDuelPlayer get(UUID uniqueId) {
        return DuelPlayerManager.getInstance().getDuelPlayer(uniqueId);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void save() {
        try {
            this.configFile.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uniqueId);
        if ((player != null) && (player.isOnline())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    @Override
    public FileConfiguration getConfigFile() {
        return configFile;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }
}
