package me.matt11matthew.mckit.player;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.game.GameType;
import me.matt11matthew.mckit.menu.Menu;
import me.matt11matthew.mckit.menu.MenuBuilder;
import me.matt11matthew.mckit.menu.item.MenuItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 6/15/2017.
 */
public class DuelSettings {
    private AbstractDuelPlayer sender;
    private AbstractDuelPlayer player;
    private GameType gameType;

    public DuelSettings(AbstractDuelPlayer sender, AbstractDuelPlayer player) {
        this.sender = sender;
        this.player = player;
    }

    public void openMainMenu() {
        Menu menu = MenuBuilder.fastMenu("Duel " + player.getName(), 9, McKitsDuels.getInstance());
        int slot =1;
        for (GameType gameType : GameType.getUnRankedGames()) {
            Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
            List<String> loreStringList = new ArrayList<>();
            for (String lore : duelsConfig.getStringList("unRankedDuelMenu." + gameType.toString() + ".loreList")) {
                loreStringList.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            menu.setItem(slot, new MenuItemBuilder()
                    .type(getType(gameType))
                    .amount(1)
                    .data(0)
                    .lore(loreStringList)
                    .named(ChatColor.translateAlternateColorCodes('&', duelsConfig.getString("unRankedDuelMenu." + gameType.toString() + ".name")))
                    .click((player1, clickType) -> {
                        player1.closeInventory();
                        this.gameType = gameType;
                        sender.sendDuelRequest(player, this);
                    }));
            slot++;
        }
        menu.open(sender.getPlayer());
    }

    private Material getType(GameType gameType) {
        Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
        try {
            Material material = Material.getMaterial(duelsConfig.getString("unRankedDuelMenu." + gameType.toString() + ".type").toUpperCase());
            return material;
        } catch (Exception e)  {
            return Material.MUSHROOM_SOUP;
        }
    }

    public GameType getDuelType() {
        return gameType;
    }
}
