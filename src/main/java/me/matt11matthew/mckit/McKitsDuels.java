package me.matt11matthew.mckit;

import me.matt11matthew.mckit.commands.*;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.game.ArenaManager;
import me.matt11matthew.mckit.listeners.ArenaListeners;
import me.matt11matthew.mckit.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class McKitsDuels extends JavaPlugin {
    private static McKitsDuels instance;
    private Config duelsConfig;
    private Config arenaConfig;


    public static McKitsDuels getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.duelsConfig = new Config("config");
        this.arenaConfig = new Config("arena");
        this.registerCommands();
        this.registerListeners();
        ArenaListeners instance = ArenaListeners.getInstance();
        ArenaManager instance1 = ArenaManager.getInstance();
        instance.setPosition1(instance1.getPosition(1));
        instance.setPosition2(instance1.getPosition(2));
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ArenaListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
    }

    private void registerCommands() {
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("setuparena").setExecutor(new SetUpArenaCommand());
        this.getCommand("duelsreload").setExecutor(new DuelsReloadCommand());
        this.getCommand("confirmcreation").setExecutor(new ConfirmCreationCommand());
        this.getCommand("stats").setExecutor(new StatsCommand());
        this.getCommand("duel").setExecutor(new DuelCommand());
        this.getCommand("setarenaspawnpoint").setExecutor(new SetArenaSpawnPointCommand());
        this.getCommand("rankedmenu").setExecutor(new RankedMenuCommand());
        this.getCommand("unrankedmenu").setExecutor(new UnRankedMenuCommand());
        this.getCommand("dev").setExecutor(new DevCommand());
        this.getCommand("elotop").setExecutor(new EloTopCommand());
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', duelsConfig.getString("messages.kickMessage")));
        }
        ArenaListeners instance = ArenaListeners.getInstance();
        ArenaManager instance1 = ArenaManager.getInstance();
        if (instance.getPosition1() != null) {
            instance1.setPosition(1, instance.getPosition1());
        }
        if (instance.getPosition2() != null) {
            instance1.setPosition(2, instance.getPosition2());
        }
    }

    public Config getArenaConfig() {
        return arenaConfig;
    }

    public Config getDuelsConfig() {
        return duelsConfig;
    }
}
