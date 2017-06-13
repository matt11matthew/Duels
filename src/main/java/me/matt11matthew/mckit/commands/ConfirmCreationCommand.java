package me.matt11matthew.mckit.commands;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.game.ArenaManager;
import me.matt11matthew.mckit.listeners.ArenaListeners;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew E on 6/13/2017.
 */
public class ConfirmCreationCommand implements CommandExecutor {
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
            Config config = McKitsDuels.getInstance().getDuelsConfig();
            Player player = (Player) sender;
            String permission = config.getString("permissions.setupArena");
            if (!player.hasPermission(permission)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.errorNoPermission").replace("{permission}", permission)));
                return true;
            }
            if (ArenaListeners.getInstance().getPosition1() == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.positionNotSet").replace("{position}", "1")));
                return true;
            }
            if (ArenaListeners.getInstance().getPosition2() == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.positionNotSet").replace("{position}", "2")));
                return true;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.createArena")));
            ArenaManager.getInstance().completeSetup();
            return true;
        }
        return true;
    }
}
