package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class EntityDeath extends SheepListener {
    public EntityDeath(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(final EntityDeathEvent event) {
        event.setDroppedExp(0);
    }
}
