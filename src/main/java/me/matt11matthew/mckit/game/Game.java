package me.matt11matthew.mckit.game;

import me.matt11matthew.mckit.McKitsDuels;
import me.matt11matthew.mckit.config.Config;
import me.matt11matthew.mckit.player.AbstractDuelPlayer;
import me.matt11matthew.mckit.player.value.Value;
import me.matt11matthew.mckit.player.value.ValueType;
import me.matt11matthew.mckit.utilities.EloUtil;
import me.matt11matthew.mckit.utilities.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Matthew E on 6/13/2017.
 */
public class Game {
    private String name;
    private GameType gameType;
    private boolean isRanked;
    private AbstractDuelPlayer player1;
    private AbstractDuelPlayer player2;
    private GameState gameState;
    private int countDownTaskId;
    private boolean player1Left;
    private boolean player2Left;
    private int countDownTime;
    private int tickTaskId;

    public Game(String name, GameType gameType, boolean isRanked, AbstractDuelPlayer player1, AbstractDuelPlayer player2) {
        this.name = name;
        this.gameType = gameType;
        this.isRanked = isRanked;
        this.player1 = player1;
        this.player2 = player2;
        this.gameState = GameState.BUILDING;

    }

    public void sendMessage(String message) {
        if (player1 != null) {
            player1.sendMessage(message);
        }
        if (player2 != null) {
            player2.sendMessage(message);
        }
    }

    public void tick() {
        hidePlayers();

    }
    public void build() {
       hidePlayers();
       this.gameState = GameState.TELEPORTING;
       teleportPlayersToArena();

    }

    private void teleportPlayersToArena() {
        Location spawnPointTwo = LocationUtil.getPlayerLocationFromString(McKitsDuels.getInstance().getArenaConfig().getString("spawnPointTwo"));
        Location spawnPointOne = LocationUtil.getPlayerLocationFromString(McKitsDuels.getInstance().getArenaConfig().getString("spawnPointOne"));
        Player player2Player = player2.getPlayer();
        Player player1Player = player1.getPlayer();
        if (player1Player.isOnline() && (player2Player.isOnline())) {
            player1Player.teleport(spawnPointOne);
            player2Player.teleport(spawnPointTwo);
        }
        this.gameState = GameState.COUNT_DOWN;
        startCountDown();
    }

    private void startCountDown() {
        this.countDownTime = 10;
        Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
        this.gameState = GameState.COUNT_DOWN;
        this.countDownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(McKitsDuels.getInstance(), () -> {
            if (this.countDownTime > 0) {
                this.countDownTime--;
                sendMessage(duelsConfig.getString("messages.arenaCountDown").replace("{time}", countDownTime + ""));
            } else {
                startGame();
            }
        }, 20L, 20L);
    }

    private void startGame() {
        Bukkit.getScheduler().cancelTask(this.countDownTaskId);
        this.tickTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(McKitsDuels.getInstance(), this::tick, 20L, 20L);
    }

    public void hidePlayers() {
        Player player2Player = player2.getPlayer();
        Player player1Player = player1.getPlayer();
        if (player1Player.isOnline() && (player2Player.isOnline())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if ((player.getUniqueId().equals(player1.getUniqueId())) || (player.getUniqueId().equals(player2.getUniqueId()))) {
                    continue;
                }
                player2Player.hidePlayer(player);
                player1Player.hidePlayer(player);
            }
        }
    }

    public void leave(Player player) {
        Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
        if (player.getUniqueId().equals(player1.getUniqueId())) {
            player1Left = true;
        }
        if (player.getUniqueId().equals(player2.getUniqueId())) {
            player2Left = true;
        }
        sendMessage(duelsConfig.getString("messages.playerLeaveGame").replace("{player}", player.getName()));
        endGame();
    }

    private void endGame() {
        this.gameState = GameState.ENDED;
        Bukkit.getScheduler().cancelTask(this.tickTaskId);
        handleWinner();
    }

    private int getAverageElo(AbstractDuelPlayer duelPlayer, GameType gameType) {
        return duelPlayer.getElo(gameType);
    }

    private void handleWinner() {
        Config duelsConfig = McKitsDuels.getInstance().getDuelsConfig();
        int eloSwitch = 0;
        if (player1Left) {
            int wonElo = getAverageElo(player2, gameType);
            int lostElo = getAverageElo(player1, gameType);
            int newElo = newElo = EloUtil.newRating(player2.getElo(gameType), lostElo, 1);
            int switchedElo = (newElo - player2.getElo(gameType));
            player1.setElo(gameType, new Value<>(EloUtil.newRating(player1.getElo(gameType), wonElo, 0), ValueType.SET));
            player2.setElo(gameType, new Value<>(newElo, ValueType.SET));
            eloSwitch = switchedElo;
        } else if (player2Left) {
            int wonElo = getAverageElo(player1, gameType);
            int lostElo = getAverageElo(player2, gameType);
            int newElo = newElo = EloUtil.newRating(player1.getElo(gameType), lostElo, 1);
            int switchedElo = (newElo - player1.getElo(gameType));
            player2.setElo(gameType, new Value<>(EloUtil.newRating(player2.getElo(gameType), wonElo, 0), ValueType.SET));
            player1.setElo(gameType, new Value<>(newElo, ValueType.SET));
            eloSwitch = switchedElo;
        }
        List<String> winMessageStringList;
        if (isRanked) {
            winMessageStringList = duelsConfig.getStringList("messages.rankedDuelWinnerMessages");
        } else {
            winMessageStringList = duelsConfig.getStringList("messages.unRankedDuelWinnerMessages");
        }
        String winnerName = "";
        String loserName = "";
        double loserHealth = 0;
        double winnerHealth = 0;
        if (player2Left) {
            winnerName = player1.getName();
            loserName = player2.getName();
            loserHealth = player2.getPlayer().getHealth();
            winnerHealth = player1.getPlayer().getHealth();
        }
        if (player1Left) {
            winnerName = player2.getName();
            loserName = player1.getName();
            loserHealth = player1.getPlayer().getHealth();
            winnerHealth = player2.getPlayer().getHealth();
        }
        for (String line : winMessageStringList) {
            line = ChatColor
                    .translateAlternateColorCodes('&', line)
                    .replace("{winner}", winnerName)
                    .replace("{loser}", loserName)
                    .replace("{elo_loss}", ChatColor.RED + "-" + eloSwitch)
                    .replace("{loser_health}", (int)loserHealth+"")
                    .replace("{winner_health}", (int)winnerHealth+"")
                    .replace("{elo_gain}", ChatColor.GREEN+ "+" + eloSwitch);
            sendMessage(line);

        }
    }
    /*
    rankedDuelWinnerMessages:
            - "&8&m---------------------------------"
                    - "&e{winner} has won the duel"
                    - "&e{winner} had {winner_health} health left"
                    - "&e{loser} had {loser_health} health left"
                    - "&e{loser} &c{elo_loss}"
                    - "&e{winner} &c{elo_gain}"
                    - "&8&m---------------------------------"
     */

    public String getName() {
        return name;
    }
}
