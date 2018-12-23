package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class PlayerDamage extends SheepListener {
    public PlayerDamage(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Team playerTeam = Team.getPlayerTeam(player);
            if (Step.isStep(Step.LOBBY) || playerTeam == Team.SPEC || event.getCause() == DamageCause.FALL && player.getVehicle() instanceof Sheep) {
                event.setCancelled(true);
            }
        }
    }
}
