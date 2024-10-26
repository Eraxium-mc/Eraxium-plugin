package org.stiala.eraxium;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class CustomScoreboard {

    private final ScoreboardManager manager;
    private final Scoreboard board;
    private final Objective objective;

    public CustomScoreboard(String title) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("custom", "dummy", ChatColor.translateAlternateColorCodes('&', title));
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
    }

    public void updateScoreboard(Player player, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = PlaceholderAPI.setPlaceholders(player, lines.get(i));
            Score score = objective.getScore(ChatColor.translateAlternateColorCodes('&', line));
            score.setScore(lines.size() - i);
        }
        player.setScoreboard(board);
    }

    public void clearScoreboard(Player player) {
        player.setScoreboard(manager.getNewScoreboard());
    }
}