package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.scheduler.BeginCountdown;
import net.neferett.linaris.sheepwars.util.ItemBuilder;

public class PlayerJoin extends SheepListener {
    public PlayerJoin(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.getInventory().clear();
        if (!Step.isStep(Step.LOBBY) && player.hasPermission("games.join")) {
            event.setJoinMessage(null);
            this.plugin.setSpectator(player, false);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
            final Location spawn = Team.SPEC.getNextSpawn();
            player.teleport(spawn == null ? this.plugin.lobbyLocation : spawn);
            player.setFlying(true);
        } else if (Step.isStep(Step.LOBBY)) {
            event.setJoinMessage(SheepWarsPlugin.prefix + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " a rejoint la partie " + ChatColor.GREEN + "(" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + ")");
            for (final Team team : Team.values()) {
                if (team.getSpawns() != null && !team.getSpawns().isEmpty() && team != Team.SPEC) {
                    player.getInventory().addItem(team.getIcon());
                }
            }
            this.plugin.loadData(player);
            player.getInventory().setItem(8, new ItemBuilder(Material.NAME_TAG).setTitle(ChatColor.GOLD + "Kits " + ChatColor.GRAY + "(Clic-droit)").build());
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(this.plugin.lobbyLocation);
            if (Bukkit.getOnlinePlayers().length >= 2 && !BeginCountdown.started && !SheepWarsPlugin.i.boosters.isEmpty()) {
                for (final Team team : Team.values()) {
                    if (team != Team.SPEC && (team.getSpawns() == null || team.getSpawns().isEmpty())) {
                        BeginCountdown.started = true;
                        return;
                    }
                }
                new BeginCountdown(this.plugin);
            }
        }
    }
}
