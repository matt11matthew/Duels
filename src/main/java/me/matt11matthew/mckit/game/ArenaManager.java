package me.matt11matthew.mckit.game;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.utilities.LocationUtil;
import org.bukkit.Location;

import java.io.IOException;


/**
 * Created by Matthew E on 6/13/2017.
 */
public class ArenaManager {
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
}
