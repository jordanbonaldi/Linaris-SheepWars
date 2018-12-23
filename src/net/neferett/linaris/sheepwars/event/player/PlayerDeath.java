package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Kit;
import net.neferett.linaris.sheepwars.handler.PlayerData;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class PlayerDeath extends SheepListener {
    public PlayerDeath(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
        final Player player = event.getEntity();
        final Team playerTeam = Team.getPlayerTeam(player);
        if (Step.isStep(Step.LOBBY) || playerTeam == Team.SPEC) {
            event.setDeathMessage(null);
        } else {
            final Player killer = player.getKiller();
            final Team killerTeam = Team.getPlayerTeam(killer);
            if (killer != null) {
                killer.playSound(player.getLocation(), Sound.VILLAGER_HIT, 1, 1);
                final PlayerData killerData = this.plugin.getData(killer);
                killerData.addCoins(1.75, false);
                if (Kit.getPlayerKit(killer) == Kit.MORE_SHEEP) {
                    Sheep.giveRandomSheep(killer);
                    if (killerData.getMoreSheep() > 1) {
                        Sheep.giveRandomSheep(killer);
                    }
                }
                killer.sendMessage(ChatColor.GRAY + "Gain de FunCoins + " + ChatColor.GOLD + "1.75" + ChatColor.GRAY + " (" + ChatColor.YELLOW + "Un ennemi en moins !" + ChatColor.GRAY + ")");
                for (final Player online : killerTeam.getOnlinePlayers()) {
                    if (killer == online) {
                        continue;
                    }
                    this.plugin.getData(killer).addCoins(0.5, false);
                    online.sendMessage(ChatColor.GRAY + "Gain de FunCoins + " + ChatColor.GOLD + "0.5" + ChatColor.GRAY + " (" + ChatColor.YELLOW + "Un ennemi en moins !" + ChatColor.GRAY + ")");
                }
            }
            event.setDeathMessage(SheepWarsPlugin.prefix + playerTeam.getColor() + player.getName() + ChatColor.GRAY + " " + (killer == null ? "a succombé." : "a été tué par " + killerTeam.getColor() + killer.getName()));
            this.plugin.setSpectator(event.getEntity(), true);
        }
    }
}
