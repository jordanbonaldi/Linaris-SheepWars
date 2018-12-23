package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.util.EntityUtils;
import net.neferett.linaris.sheepwars.util.MathUtils;

public class EntityExplode extends SheepListener {
    public EntityExplode(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (Step.isStep(Step.LOBBY)) {
            event.blockList().clear();
        } else {
            final Location center = event.getLocation();
            for (final Block block : event.blockList()) {
                if (MathUtils.random.nextBoolean()) {
                    EntityUtils.spawnFallingBlock(block, center.getWorld(), 0.3F, 1.2F, 0.3F);
                }
                block.setType(Material.AIR);
            }
        }
    }
}
