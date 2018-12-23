package net.neferett.linaris.sheepwars.event.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class BlockPlace extends SheepListener {
    public BlockPlace(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (Step.isStep(Step.LOBBY) || Team.getPlayerTeam(event.getPlayer()) == Team.SPEC) {
            event.setCancelled(true);
        }
    }
}
