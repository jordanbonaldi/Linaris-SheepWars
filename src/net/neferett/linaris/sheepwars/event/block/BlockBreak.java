package net.neferett.linaris.sheepwars.event.block;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class BlockBreak extends SheepListener {
    public BlockBreak(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!Step.isStep(Step.LOBBY) && Team.getPlayerTeam(event.getPlayer()) != Team.SPEC) {
            event.getBlock().setType(Material.AIR);
        }
        event.setCancelled(true);
    }
}
