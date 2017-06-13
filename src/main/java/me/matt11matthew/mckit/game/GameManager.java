package me.matt11matthew.mckit.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 6/13/2017.
 */
public class GameManager {
    private static GameManager instance;
    private Map<String, Game> gameMap;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    public GameManager() {
        instance = this;
        this.gameMap = new HashMap<>();
    }

    public void setupGame(Game game) {
        if (!this.gameMap.containsKey(game.getName())) {
            this.gameMap.put(game.getName(), game);
        }
    }

    public Map<String, Game> getGameMap() {
        return gameMap;
    }
}
