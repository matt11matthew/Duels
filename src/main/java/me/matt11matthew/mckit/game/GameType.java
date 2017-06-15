package me.matt11matthew.mckit.game;

/**
 * Created by Matthew E on 6/12/2017.
 */
public enum GameType {
    RANKED_REFILL, RANKED_NO_REFILL,
    UN_RANKED_REFILL, UN_RANKED_NO_REFILL;

    public static GameType[] getUnRankedGames() {
        return new GameType[] {UN_RANKED_NO_REFILL, UN_RANKED_REFILL};
    }

    public static GameType[] getRankedGames() {
        return new GameType[] {RANKED_NO_REFILL, RANKED_REFILL};
    }
}
