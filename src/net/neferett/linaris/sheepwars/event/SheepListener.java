package net.neferett.linaris.sheepwars.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.neferett.linaris.sheepwars.SheepWarsPlugin;

import org.bukkit.event.Listener;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SheepListener implements Listener {
    protected SheepWarsPlugin plugin;
}
