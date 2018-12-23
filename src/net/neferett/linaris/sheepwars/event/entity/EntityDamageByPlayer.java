package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class EntityDamageByPlayer extends SheepListener {
    public EntityDamageByPlayer(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByPlayer(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && (Step.isStep(Step.LOBBY) || Team.getPlayerTeam((Player) event.getDamager()) == Team.SPEC)) {
            event.setCancelled(true);
        }
    }
}
