package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class PlayerDamageByPlayer extends SheepListener {
    public PlayerDamageByPlayer(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByPlayer(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            Entity damagerEntity = event.getDamager();
            if (damagerEntity instanceof Projectile) {
                damagerEntity = ((Projectile) damagerEntity).getShooter();
            }
            if (damagerEntity instanceof Player) {
                final Player damager = (Player) damagerEntity;
                if (Step.isStep(Step.LOBBY) || Team.getPlayerTeam(player) == Team.SPEC) {
                    event.setCancelled(true);
                    if (!Step.isStep(Step.LOBBY) && event.getCause() == DamageCause.PROJECTILE) {
                        player.teleport(player.getLocation().add(0, 5, 0));
                        player.setFlying(true);
                        final Arrow arrow = damager.launchProjectile(Arrow.class);
                        arrow.setVelocity(event.getDamager().getVelocity());
                    }
                }
            }
        }
    }
}
