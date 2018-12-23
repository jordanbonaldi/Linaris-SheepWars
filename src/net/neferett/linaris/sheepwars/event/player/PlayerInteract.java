package net.neferett.linaris.sheepwars.event.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Kit;
import net.neferett.linaris.sheepwars.handler.PlayerData;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.util.ItemBuilder;
import net.neferett.linaris.sheepwars.util.MathUtils;

public class PlayerInteract extends SheepListener {
    public PlayerInteract(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (Step.isStep(Step.LOBBY) || Team.getPlayerTeam(player) == Team.SPEC) {
            event.setCancelled(true);
        }
        if (event.getAction().name().contains("RIGHT")) {
            if (!Step.isStep(Step.LOBBY)) {
                final Block block = event.getClickedBlock();
                if (event.hasItem()) {
                    final ItemStack item = event.getItem();
                    if (item.getType() == Material.BOW || item.getType() != Material.WOOL && item.getType() != Material.BED) return;
                    event.setCancelled(true);
                    if (Team.getPlayerTeam(player) == Team.SPEC) {
                        if (item.getType() == Material.BED) {
                            this.plugin.teleportToLobby(player);
                        }
                    } else {
                        for (final Sheep sheep : Sheep.values()) {
                            if (sheep.getIcon().isSimilar(item)) {
                                if (item.getAmount() == 1) {
                                    player.setItemInHand(null);
                                } else {
                                    item.setAmount(item.getAmount() - 1);
                                    player.setItemInHand(item);
                                }
                                player.updateInventory();
                                final Location playerLocation = player.getLocation();
                                final Location location = sheep.isFriendly() ? playerLocation.toVector().add(playerLocation.getDirection().multiply(2.0D)).toLocation(player.getWorld()) : player.getLocation();
                                final org.bukkit.entity.Sheep sheepEntity = sheep.spawnSheep(location, player);
                                sheepEntity.setCustomName(sheep.getName());
                                if (!sheep.isFriendly()) {
                                    sheepEntity.setVelocity(playerLocation.getDirection().multiply(4.4D));
                                }
                            }
                        }
                    }
                }
            } else {
                event.setCancelled(true);
                if (event.hasItem()) {
                    final ItemStack item = event.getItem();
                    if (item.getType() == Material.NAME_TAG && item.hasItemMeta()) {
                        final PlayerData data = this.plugin.getData(player);
                        final Kit playerKit = Kit.getPlayerKit(player);
                        final Inventory inventory = Bukkit.createInventory(player, 9, "Séléction : " + ChatColor.YELLOW + (playerKit == null ? "Aucune" : playerKit.getName()));
                        inventory.addItem(new ItemBuilder(Kit.MORE_HEALTH.getMaterial(), Kit.MORE_HEALTH.getDurability()).setTitle(Kit.MORE_HEALTH.getName()).addLores((data.getMoreHealth() == 0 ? ChatColor.GRAY + "Améliore votre santé\n\n" + ChatColor.RED + "Achetez le sur le Hub !" : ChatColor.GRAY + "Augmente les points de vie\n\n" + ChatColor.GRAY + "Augmente la santé de " + ChatColor.AQUA + (data.getMoreHealth() < 5 ? data.getMoreHealth() : 6) + ChatColor.RED + " ♥\n\n" + ChatColor.GOLD + "Niveau actuel : " + ChatColor.YELLOW + data.getMoreHealth()).split("\n")).build());
                        inventory.addItem(new ItemBuilder(Kit.BETTER_BOW.getMaterial(), Kit.BETTER_BOW.getDurability()).setTitle(Kit.BETTER_BOW.getName()).addLores((data.getMoreHealth() == 0 ? ChatColor.GRAY + "Améliore votre arc\n\n" + ChatColor.RED + "Achetez le sur le Hub !" : ChatColor.GRAY + "Améliore votre arc\n\n" + ChatColor.GRAY + "Punch " + (data.getBetterBow() <= 2 ? "I" : "II") + (data.getBetterBow() > 1 ? "\n" + ChatColor.GRAY + "Power " + (data.getBetterBow() > 3 ? "II" : "I") : "") + (data.getBetterBow() >= 5 ? "\n" + ChatColor.GRAY + "Flame I" : "") + "\n\n" + ChatColor.GOLD + "Niveau actuel : " + ChatColor.YELLOW + data.getBetterBow()).split("\n")).build());
                        inventory.addItem(new ItemBuilder(Kit.BETTER_SWORD.getMaterial(), Kit.BETTER_SWORD.getDurability()).setTitle(Kit.BETTER_SWORD.getName()).addLores((data.getMoreHealth() == 0 ? ChatColor.GRAY + "Améliore votre épée\n\n" + ChatColor.RED + "Achetez le sur le Hub !" : ChatColor.GRAY + "Améliore votre épée\n\n" + ChatColor.GRAY + "Epée en " + (data.getBetterSword() > 3 ? "fer" : "pierre") + (data.getBetterSword() > 1 && data.getBetterSword() != 4 ? "\n" + ChatColor.GRAY + "Knockback I" : "") + (data.getBetterSword() == 3 || data.getBetterSword() == 5 ? "\n" + ChatColor.GRAY + "Tranchant I" : "") + "\n\n" + ChatColor.GOLD + "Niveau actuel : " + ChatColor.YELLOW + data.getBetterSword()).split("\n")).build());
                        inventory.addItem(new ItemBuilder(Kit.MOBILITY.getMaterial(), Kit.MOBILITY.getDurability()).setTitle(Kit.MOBILITY.getName()).addLores((data.getMoreHealth() == 0 ? ChatColor.GRAY + "Améliore votre mobilité\n\n" + ChatColor.RED + "Achetez le sur le Hub !" : ChatColor.GRAY + "Améliore votre mobilité\n\n" + ChatColor.GRAY + "Effet de " + ChatColor.AQUA + "Speed " + (data.getMobility() < 3 ? data.getMobility() : "3") + "\n\n" + ChatColor.GOLD + "Niveau actuel : " + ChatColor.YELLOW + data.getMobility()).split("\n")).build());
                        inventory.addItem(new ItemBuilder(Kit.MORE_SHEEP.getMaterial(), Kit.MORE_SHEEP.getDurability()).setTitle(Kit.MORE_SHEEP.getName()).addLores((data.getMoreHealth() == 0 ? ChatColor.GRAY + "Augmente les chances d'obtenir\n" + ChatColor.GRAY + "un mouton supplémentaire\n\n" + ChatColor.RED + "Achetez le sur le Hub !" : ChatColor.GRAY + "Augmente les chances d'obtenir\n" + ChatColor.GRAY + "un mouton supplémentaire\n\n" + ChatColor.GRAY + "Vous obtenez " + ChatColor.AQUA + (data.getMoreSheep() < 2 ? data.getMoreSheep() : 2) + " mouton" + (data.getMoreSheep() > 1 ? "s" : "") + ChatColor.GRAY + " quand\n" + ChatColor.GRAY + "vous tuez quelqu'un\n\n" + ChatColor.GOLD + "Niveau actuel : " + ChatColor.YELLOW + data.getMoreSheep()).split("\n")).build());
                        player.openInventory(inventory);
                    } else if (item.getType() == Material.INK_SACK && item.hasItemMeta()) {
                        for (final Team team : Team.values()) {
                            if (item.isSimilar(team.getIcon())) {
                                final String displayName = team.getDisplayName();
                                final Team playerTeam = Team.getPlayerTeam(player);
                                if (playerTeam != team) {
                                    if (Bukkit.getOnlinePlayers().length > 1 && team.getOnlinePlayers().size() >= MathUtils.ceil(Bukkit.getOnlinePlayers().length / 2)) {
                                        player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Impossible de rejoindre cette équipe, trop de joueurs !");
                                    } else {
                                        if (playerTeam != Team.SPEC) {
                                            playerTeam.removePlayer(player);
                                        }
                                        team.addPlayer(player);
                                        player.sendMessage(SheepWarsPlugin.prefix + ChatColor.GRAY + "Vous rejoignez l'équipe " + team.getColor() + displayName);
                                    }
                                }
                                break;
                            }
                        }
                        player.updateInventory();
                        return;
                    }
                }
            }
        }
    }
}
