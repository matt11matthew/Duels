package me.matt11matthew.mckit.commands;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.DuelConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 6/12/2017.
 */
public class DuelsReloadCommand implements CommandExecutor {

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            DuelConfig duelConfig = McKitsDuels.getInstance().getDuelConfig();
            Player player = (Player) sender;
            String permission = duelConfig.getString("permissions.reloadDuels");
            if (!player.hasPermission(permission)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', duelConfig.getString("messages.errorNoPermission").replace("{permission}", permission)));
                return true;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', duelConfig.getString("messages.reloadConfig")));
            return true;
        }
        return true;
    }
}
