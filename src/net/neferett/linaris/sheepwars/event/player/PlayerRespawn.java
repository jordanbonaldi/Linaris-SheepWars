package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.util.ItemBuilder;

public class PlayerRespawn extends SheepListener {
    public PlayerRespawn(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final Team playerTeam = Team.getPlayerTeam(player);
        if (Step.isStep(Step.LOBBY) || playerTeam != Team.SPEC) {
            event.setRespawnLocation(this.plugin.lobbyLocation);
        } else {
            final Location spawn = Team.SPEC.getNextSpawn();
            event.setRespawnLocation(spawn == null ? this.plugin.lobbyLocation : spawn);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setFlying(true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
                    player.getInventory().setItem(8, new ItemBuilder(Material.BED).setTitle(ChatColor.GREEN + "Retour au hub").build());
                }
            }.runTaskLater(this.plugin, 1);
            player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Vous êtes maintenant un " + ChatColor.WHITE + "Fantôme" + ChatColor.GRAY + ", pour quitter " + ChatColor.YELLOW + "/hub");
            player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Seul les " + ChatColor.WHITE + "Fantômes" + ChatColor.GRAY + " peuvent vous entendre parler !");
        }
    }
}
