package net.neferett.linaris.sheepwars.event.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Step;

public class ServerListPing extends SheepListener {
    public ServerListPing(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerListPing(final ServerListPingEvent event) {
        event.setMotd(Step.getMOTD());
    }
}
