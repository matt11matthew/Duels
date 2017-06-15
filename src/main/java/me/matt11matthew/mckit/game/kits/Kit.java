package me.matt11matthew.mckit.game.kits;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.game.GameType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 6/15/2017.
 */
public class Kit {
    private GameType gameType;
    private Map<Integer, ItemStack> armorMap = new HashMap<>();
    private Map<Integer, ItemStack> itemStackMap = new HashMap<>();

    public Kit(GameType gameType) {
        this.gameType = gameType;
        Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
        for (String item : duelsConfig.getStringList("kits." + gameType.toString() + ".items")) {
            KitItem kitItem = new KitItem(item);
            this.itemStackMap.put(kitItem.getSlot(), kitItem.getItemStack());
        }
        for (String item : duelsConfig.getStringList("kits." + gameType.toString() + ".armor")) {
            KitItem kitItem = new KitItem(item);
            this.armorMap.put(kitItem.getSlot(), kitItem.getItemStack());
        }
    }

    public void give(Player player) {
        itemStackMap.forEach((integer, itemStack) -> player.getInventory().setItem(integer, itemStack));
        ItemStack[] armorItemStacks = new ItemStack[armorMap.values().size()];
        for (int i = 0; i < armorMap.values().size(); i++) {
            armorItemStacks[i] = armorMap.get(i);
        }
        player.getInventory().setArmorContents(armorItemStacks);
    }

    public GameType getGameType() {
        return gameType;
    }

    public Map<Integer, ItemStack> getArmorMap() {
        return armorMap;
    }

    public Map<Integer, ItemStack> getItemStackMap() {
        return itemStackMap;
    }


    /*
    kits:
  UN_RANKED_REFILL:
    items:
    - "0:diamond_sword(damage_all:10,damage_all:10)"
    - "2:mushroom_soup"
    - "3:mushroom_soup"
    - "4:mushroom_soup"
    - "5:mushroom_soup"
    - "6:mushroom_soup"
    - "7:mushroom_soup"
    - "8:mushroom_soup"
    - "9:mushroom_soup"
    - "10:mushroom_soup"
    armor:
    - "0:diamond_helmet(protection:3)"
    - "1:diamond_chestplate(protection:3)"
    - "2:diamond_leggings(protection:3)"
    - "3:diamond_boots(protection:3)"
  RANKED_REFILL:
    items:
    - "0:diamond_sword(damage_all:10,damage_all:10)"
    - "2:mushroom_soup"
    - "3:mushroom_soup"
    - "4:mushroom_soup"
    - "5:mushroom_soup"
    - "6:mushroom_soup"
    - "7:mushroom_soup"
    - "8:mushroom_soup"
    - "9:mushroom_soup"
    - "10:mushroom_soup"
    armor:
    - "0:diamond_helmet(protection:3)"
    - "1:diamond_chestplate(protection:3)"
    - "2:diamond_leggings(protection:3)"
    - "3:diamond_boots(protection:3)"
  UN_RANKED_NO_REFILL:
    items:
    - "0:diamond_sword(damage_all:10,damage_all:10)"
    - "2:mushroom_soup"
    - "3:mushroom_soup"
    armor:
    - "0:diamond_helmet(protection:3)"
    - "1:diamond_chestplate(protection:3)"
    - "2:diamond_leggings(protection:3)"
    - "3:diamond_boots(protection:3)"
  RANKED_NO_REFILL:
    items:
    - "0:diamond_sword(damage_all:10,damage_all:10)"
    - "2:mushroom_soup"
    - "3:mushroom_soup"
    armor:
    - "0:diamond_helmet(protection:3)"
    - "1:diamond_chestplate(protection:3)"
    - "2:diamond_leggings(protection:3)"
    - "3:diamond_boots(protection:3)"
     */
}
