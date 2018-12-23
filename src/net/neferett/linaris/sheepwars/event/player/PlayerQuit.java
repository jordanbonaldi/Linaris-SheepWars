package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.scheduler.BeginCountdown;

public class PlayerQuit extends SheepListener {
    public PlayerQuit(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final Player player = event.getPlayer();
        BeginCountdown.resetPlayer(player, GameMode.ADVENTURE);
        this.plugin.removePlayer(player);
    }
}
