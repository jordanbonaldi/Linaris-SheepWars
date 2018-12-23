package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.util.MathUtils;

public class EntityChangeBlock extends SheepListener {
    public EntityChangeBlock(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock && MathUtils.randomBoolean(0.5F)) {
            event.setCancelled(true);
        }
    }
}
