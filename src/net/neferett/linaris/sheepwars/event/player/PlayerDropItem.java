package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class PlayerDropItem extends SheepListener {
    public PlayerDropItem(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
