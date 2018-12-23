package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class FoodLevelChange extends SheepListener {
    public FoodLevelChange(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
