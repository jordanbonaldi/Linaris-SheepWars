package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class PlayerPickupItem extends SheepListener {
    public PlayerPickupItem(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        if (!Step.isStep(Step.IN_GAME) || Team.getPlayerTeam(event.getPlayer()) == Team.SPEC) {
            event.setCancelled(true);
        } else {
            boolean cancel = true;
            for (final Sheep sheep : Sheep.values()) {
                if (sheep.getIcon().isSimilar(event.getItem().getItemStack())) {
                    cancel = false;
                    break;
                }
            }
            if (cancel) {
                event.setCancelled(true);
            }
        }
    }
}
