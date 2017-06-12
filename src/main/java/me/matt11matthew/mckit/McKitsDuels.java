package me.matt11matthew.mckit;

import me.matt11matthew.mckit.commands.DuelsReloadCommand;
import me.matt11matthew.mckit.commands.SetSpawnCommand;
import me.matt11matthew.mckit.config.DuelConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class McKitsDuels extends JavaPlugin {
    private static McKitsDuels instance;
    private DuelConfig duelConfig;

    public static McKitsDuels getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.duelConfig = new DuelConfig("config");
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("duelsreload").setExecutor(new DuelsReloadCommand());
    }

    @Override
    public void onDisable() {

    }

    public DuelConfig getDuelConfig() {
        return duelConfig;
    }
}
