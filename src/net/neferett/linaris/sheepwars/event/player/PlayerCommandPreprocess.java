package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class PlayerCommandPreprocess extends SheepListener {
    public PlayerCommandPreprocess(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (player.isOp() && event.getMessage().split(" ")[0].contains("reload")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Cette fonctionnalité est désactivée par le plugin SheepWars à cause de contraintes techniques (reset de map).");
        }
    }
}
