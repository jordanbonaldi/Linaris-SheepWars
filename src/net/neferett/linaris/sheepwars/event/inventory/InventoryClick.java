package net.neferett.linaris.sheepwars.event.inventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Kit;
import net.neferett.linaris.sheepwars.handler.PlayerData;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;

public class InventoryClick extends SheepListener {
    public InventoryClick(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final ItemStack current = event.getCurrentItem();
        if (!Step.isStep(Step.LOBBY) && Team.getPlayerTeam((Player) event.getWhoClicked()) == Team.SPEC) {
            event.setCancelled(true);
        } else if (Step.isStep(Step.LOBBY)) {
            event.setCancelled(true);
            if (event.getInventory().getTitle().contains("Séléction :") && event.getSlot() == event.getRawSlot() && current != null && current.hasItemMeta()) {
                final Player player = (Player) event.getWhoClicked();
                final PlayerData data = this.plugin.getData(player);
                final String display = current.getItemMeta().getDisplayName();
                final Kit clickedKit = Kit.MORE_HEALTH.getName().equals(display) ? Kit.MORE_HEALTH : Kit.BETTER_BOW.getName().equals(display) ? Kit.BETTER_BOW : Kit.BETTER_SWORD.getName().equals(display) ? Kit.BETTER_SWORD : Kit.MOBILITY.getName().equals(display) ? Kit.MOBILITY : Kit.MORE_SHEEP.getName().equals(display) ? Kit.MORE_SHEEP : null;
                if (clickedKit == null) return;
                if (clickedKit == Kit.MORE_HEALTH && data.getMoreHealth() <= 0 || clickedKit == Kit.BETTER_BOW && data.getBetterBow() <= 0 || clickedKit == Kit.BETTER_SWORD && data.getBetterSword() <= 0 || clickedKit == Kit.MOBILITY && data.getMobility() <= 0 || clickedKit == Kit.MORE_SHEEP && data.getMoreSheep() <= 0) {
                    player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Vous ne possédez pas ce kit !");
                } else {
                    Kit.setPlayerKit(player, clickedKit);
                    player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Kit séléctionné : " + ChatColor.GOLD + clickedKit.getName());
                    player.closeInventory();
                }
            }
        }
    }
}
