package org.stiala.eraxium;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class plugin extends JavaPlugin implements CommandExecutor {

    private ResourceBundle messages;
    private CustomScoreboard customScoreboard;
    private List<String> scoreboardLines;

    @Override
    public void onEnable() {
        // Load default config
        saveDefaultConfig();
        loadConfig();

        // Register the era command
        if (getCommand("era") != null) {
            getCommand("era").setExecutor(new EraCommand());
        } else {
            getLogger().severe("Failed to register command 'era'. Command not found in paper-plugin.yml.");
        }

        // Update scoreboard periodically
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    customScoreboard.updateScoreboard(player, scoreboardLines);
                });
            }
        }.runTaskTimer(this, 0L, 20L * 5); // Update every 5 seconds

        // Plugin startup logic
        getLogger().info("The plugin is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(messages.getString("plugin.disabled"));
    }

    private void loadConfig() {
        String language = getConfig().getString("language", "en");
        Locale locale = new Locale(language);
        messages = ResourceBundle.getBundle("messages", locale);

        // Load scoreboard settings from config
        String title = getConfig().getString("scoreboard.title", "&aMy Server");
        scoreboardLines = getConfig().getStringList("scoreboard.lines");
        customScoreboard = new CustomScoreboard(title);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("era") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
            return true;
        }
        return false;
    }
}