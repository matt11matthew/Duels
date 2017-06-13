package me.matt11matthew.mckit.player;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.game.GameType;
import me.matt11matthew.mckit.player.value.Value;
import me.matt11matthew.mckit.player.value.ValueType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 6/12/2017.
 */
public class DuelPlayerManager {
    private static DuelPlayerManager instance;
    private Map<UUID, AbstractDuelPlayer> duelPlayerMap;

    public static DuelPlayerManager getInstance() {
        if (instance == null) {
            instance = new DuelPlayerManager();
        }
        return instance;
    }

    public DuelPlayerManager() {
        instance = this;
        this.duelPlayerMap = new HashMap<>();
    }

    public boolean isPlayer(UUID uniqueId) {
        if (this.duelPlayerMap.containsKey(uniqueId)) {
            System.out.println("Found duel player " + uniqueId.toString() + "...");
            return true;
        }
        File file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/", uniqueId.toString() + ".yml");
        if (file.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if (configuration.getString("uniqueId").equalsIgnoreCase(uniqueId.toString())) {
                System.out.println("Found duel player " + uniqueId.toString() + "...");
                return true;
            }
        }
        System.out.println("Could not find duel player " + uniqueId.toString() + " returning false");
        return false;
    }

    public AbstractDuelPlayer getDuelPlayer(UUID uniqueId) {
        if (this.duelPlayerMap.containsKey(uniqueId)) {
            System.out.println("Found duel player " + uniqueId.toString() + "...");
            return duelPlayerMap.get(uniqueId);
        }
        File file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/", uniqueId.toString() + ".yml");
        if (file.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if (configuration.getString("uniqueId").equalsIgnoreCase(uniqueId.toString())) {
                System.out.println("Found duel player " + uniqueId.toString() + "...");
                return new AbstractDuelPlayerImpl(uniqueId, configuration.getString("name"));
            }
        }
        System.out.println("Could not find duel player " + uniqueId.toString() + " returning null");
        return null;
    }

    public AbstractDuelPlayer getDuelPlayer(String name) {
        for (AbstractDuelPlayer abstractDuelPlayer : this.duelPlayerMap.values()) {
            if (abstractDuelPlayer.getName().equalsIgnoreCase(name)) {
                return abstractDuelPlayer;
            }
        }
        File file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/");
        if ((file.exists()) && (file.listFiles()!=null)&&(file.listFiles().length>0)) {
            for (File file1 : file.listFiles()) {
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file1);
                if (configuration.getString("name").equalsIgnoreCase(name)) {
                    System.out.println("Found duel player " + configuration.getString("uniqueId") + "...");
                    return new AbstractDuelPlayerImpl(UUID.fromString(configuration.getString("uniqueId")), configuration.getString("name"));
                }
            }
        }
        System.out.println("Could not find duel player " + name + " returning null");
        return null;
    }

    public AbstractDuelPlayer create(UUID uuid, String username) {
        if (duelPlayerMap.containsKey(uuid)) {
            System.out.println("Found duel player " + username + " skipped creation...");
            return duelPlayerMap.get(uuid);
        }
        File file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/", uuid.toString() + ".yml");
        if (file.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if (configuration.getString("uniqueId").equalsIgnoreCase(uuid.toString())) {
                System.out.println("Found duel player " + username + " skipped creation...");
                AbstractDuelPlayer duelPlayer = new AbstractDuelPlayerImpl(uuid, configuration.getString("name"));
                this.duelPlayerMap.put(uuid, duelPlayer);
                return duelPlayer;
            }
        }
        AbstractDuelPlayerImpl abstractDuelPlayer = new AbstractDuelPlayerImpl(uuid, username);
        abstractDuelPlayer.setCredits(new Value<>(0, ValueType.SET));
        abstractDuelPlayer.setRankedWins(new Value<>(0, ValueType.SET));
        abstractDuelPlayer.setUnRankedWins(new Value<>(0, ValueType.SET));
        for (GameType gameType : GameType.values()) {
            abstractDuelPlayer.setElo(gameType, new Value<>(McKitsDuels.getInstance().getDuelsConfig().getInteger("defaultElo"), ValueType.SET));
            abstractDuelPlayer.setKills(gameType, new Value<>(0, ValueType.SET));
            abstractDuelPlayer.setDeaths(gameType, new Value<>(0, ValueType.SET));
        }
        FileConfiguration configFile = abstractDuelPlayer.getConfigFile();
        configFile.set("uniqueId", uuid.toString());
        configFile.set("name", username);
        try {
            configFile.save(abstractDuelPlayer.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.duelPlayerMap.put(uuid, abstractDuelPlayer);
        System.out.println("Created duel player " + abstractDuelPlayer.toString());
        return abstractDuelPlayer;
    }

    public AbstractDuelPlayer getOnlineDuelPlayer(UUID uniqueId) {
        if (duelPlayerMap.containsKey(uniqueId)) {
            return duelPlayerMap.get(uniqueId);
        }
        AbstractDuelPlayer duelPlayer = getDuelPlayer(uniqueId);
        if (duelPlayer == null) {
            return null;
        }
        this.duelPlayerMap.put(uniqueId, duelPlayer);
        return duelPlayer;
    }

    public boolean isPlayer(String name) {
        for (AbstractDuelPlayer abstractDuelPlayer : this.duelPlayerMap.values()) {
            if (abstractDuelPlayer.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        File file = new File(McKitsDuels.getInstance().getDataFolder() + "/playerdata/");
        if ((file.exists()) && (file.listFiles()!=null)&&(file.listFiles().length>0)) {
            for (File file1 : file.listFiles()) {
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file1);
                if (configuration.getString("name").equalsIgnoreCase(name)) {
                    System.out.println("Found duel player " + configuration.getString("uniqueId") + "...");
                    return true;
                }
            }
        }
        System.out.println("Could not find duel player " + name+ " returning false");
        return false;
    }

    public UUID getUniqueId(String name) {
        if (isPlayer(name)) {
            AbstractDuelPlayer duelPlayer = getDuelPlayer(name);
            if (duelPlayer != null) {
                return duelPlayer.getUniqueId();
            }
        }
        return null;
    }
}

