package net.neferett.linaris.sheepwars.event.server;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;

public class ServerCommand extends SheepListener {
    public ServerCommand(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerCommand(final ServerCommandEvent event) {
        if (event.getCommand().split(" ")[0].contains("reload")) {
            event.setCommand("/reload");
            event.getSender().sendMessage(ChatColor.RED + "Cette fonctionnalité est désactivée par le plugin SheepWars à cause de contraintes techniques (reset de map).");
        }
    }
}
