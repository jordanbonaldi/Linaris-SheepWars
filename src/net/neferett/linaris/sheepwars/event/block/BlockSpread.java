package net.neferett.linaris.sheepwars.event.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockSpreadEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class BlockSpread extends SheepListener {
    public BlockSpread(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent event) {
        event.setCancelled(true);
    }
}
