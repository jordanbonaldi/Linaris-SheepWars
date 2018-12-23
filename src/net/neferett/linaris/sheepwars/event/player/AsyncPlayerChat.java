package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class AsyncPlayerChat extends SheepListener {
    public AsyncPlayerChat(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Team playerTeam = Team.getPlayerTeam(player);
        event.setFormat((playerTeam != null ? playerTeam.getColor() : ChatColor.GRAY) + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
        if (Step.isStep(Step.IN_GAME) && playerTeam == Team.SPEC) {
            for (final Player online : Bukkit.getOnlinePlayers()) {
                if (Team.getPlayerTeam(online) != Team.SPEC) {
                    event.getRecipients().remove(online);
                }
            }
        }
    }
}
