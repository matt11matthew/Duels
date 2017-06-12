package me.matt11matthew.mckit.player.holder;

import me.matt11matthew.mckit.player.DuelPlayer;
import me.matt11matthew.mckit.player.value.Value;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Matthew E on 6/12/2017.
 */
public interface CreditHolder extends DuelPlayer {

    default Integer getCredits() {
        FileConfiguration configFile = getConfigFile();
        return (configFile.isSet("credits")) ? configFile.getInt("credits") : 0;
    }

    default void setCredits(Value<Integer> value) {
        FileConfiguration configFile = getConfigFile();
        int credits = getCredits();
        switch (value.getValueType()) {
            case ADD:
                configFile.set("credits", credits + value.getValue());
                break;
            case SET:
                configFile.set("credits", value.getValue());
                break;
            case TAKE:
                if (value.getValue() > credits) {
                    configFile.set("credits", 0);
                    break;
                }
                configFile.set("credits", (credits - value.getValue()));
                break;
        }
        this.save();
    }
}
