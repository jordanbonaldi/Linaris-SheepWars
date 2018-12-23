package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class PlayerAchievementAwarded extends SheepListener {
    public PlayerAchievementAwarded(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerAchievementArwarded(final PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }
}
