package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class CreatureSpawn extends SheepListener {
    public CreatureSpawn(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Sheep)) {
            event.setCancelled(true);
        }
    }
}
