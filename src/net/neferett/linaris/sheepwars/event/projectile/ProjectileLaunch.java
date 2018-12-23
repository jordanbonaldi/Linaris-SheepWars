package net.neferett.linaris.sheepwars.event.projectile;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Booster;
import net.neferett.linaris.sheepwars.handler.Booster.TriggerAction;

public class ProjectileLaunch extends SheepListener {
    public ProjectileLaunch(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                final Player player = (Player) arrow.getShooter();
                for (final Booster booster : Booster.values()) {
                    if (booster.getTriggers().contains(TriggerAction.ARROW_LAUNCH)) {
                        booster.getAction().onEvent(player, event, TriggerAction.ARROW_LAUNCH);
                    }
                }
            }
        }
    }
}
