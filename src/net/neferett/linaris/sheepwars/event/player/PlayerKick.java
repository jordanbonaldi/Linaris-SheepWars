package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.scheduler.BeginCountdown;

public class PlayerKick extends SheepListener {
    public PlayerKick(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        event.setLeaveMessage(null);
        final Player player = event.getPlayer();
        BeginCountdown.resetPlayer(player, GameMode.ADVENTURE);
        this.plugin.removePlayer(player);
    }
}
