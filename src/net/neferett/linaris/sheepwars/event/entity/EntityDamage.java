package net.neferett.linaris.sheepwars.event.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Step;

public class EntityDamage extends SheepListener {
    public EntityDamage(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (Step.isStep(Step.LOBBY)) {
            event.setCancelled(true);
        } else if (event.getEntity() instanceof org.bukkit.entity.Sheep) {
            if (event.getCause() == DamageCause.FALL) {
                event.setCancelled(true);
            } else {
                final org.bukkit.entity.Sheep entity = (org.bukkit.entity.Sheep) event.getEntity();
                // Custom Sheeps
                if (Sheep.INTERGALACTIC.getColor() == entity.getColor() && Sheep.INTERGALACTIC.getName().equals(entity.getCustomName()) && event.getCause().name().contains("_EXPLOSION")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
