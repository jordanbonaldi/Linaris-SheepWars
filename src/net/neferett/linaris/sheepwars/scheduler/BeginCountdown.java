package net.neferett.linaris.sheepwars.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.handler.Kit;
import net.neferett.linaris.sheepwars.handler.PlayerData;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.util.ItemBuilder;

public class BeginCountdown extends BukkitRunnable {
    public static boolean started = false;
    public static int timeUntilStart = 60;
    private final SheepWarsPlugin plugin;

    public BeginCountdown(final SheepWarsPlugin plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 0, 20);
        BeginCountdown.started = true;
    }

    @Override
    public void run() {
        if (BeginCountdown.timeUntilStart == 0) {
            this.cancel();
            if (Bukkit.getOnlinePlayers().length < 2) {
                Bukkit.broadcastMessage(SheepWarsPlugin.prefix + ChatColor.RED + "Il n'y a pas assez de joueurs !");
                BeginCountdown.timeUntilStart = 120;
                BeginCountdown.started = false;
            } else {
                Bukkit.broadcastMessage(SheepWarsPlugin.prefix + ChatColor.AQUA + "La partie commence, " + ChatColor.GOLD + "un mouton toutes les " + ChatColor.GREEN + "40 " + ChatColor.GOLD + "secondes.");
                final Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
                Objective obj = board.getObjective("health");
                if (obj != null) {
                    obj.unregister();
                }
                obj = board.registerNewObjective("health", "health");
                obj.setDisplayName(ChatColor.RED + "♥");
                obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    Team team = Team.getPlayerTeam(player);
                    if (team == Team.SPEC) {
                        team = Team.getRandomTeam();
                        team.addPlayer(player);
                    }
                    BeginCountdown.resetPlayer(player, GameMode.SURVIVAL);
                    final Color color = team.getLeatherColor();
                    final PlayerData data = this.plugin.getData(player);
                    final Kit kit = Kit.getPlayerKit(player);
                    if (kit == Kit.BETTER_BOW) {
                        final ItemStack item = new ItemStack(Material.BOW);
                        item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, data.getBetterBow() < 3 ? 1 : 2);
                        if (data.getBetterSword() > 1) {
                            item.addEnchantment(Enchantment.ARROW_DAMAGE, data.getBetterSword() < 4 ? 1 : 2);
                            if (data.getBetterBow() >= 5) {
                                item.addEnchantment(Enchantment.ARROW_FIRE, 1);
                            }
                        }
                        player.getInventory().addItem(item);
                    } else {
                        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                    }
                    if (kit == Kit.BETTER_SWORD) {
                        final ItemStack item = new ItemStack(data.getBetterSword() < 4 ? Material.STONE_SWORD : Material.IRON_SWORD);
                        if (data.getBetterSword() > 1 && data.getBetterSword() != 4) {
                            item.addEnchantment(Enchantment.KNOCKBACK, 1);
                        }
                        if (data.getBetterSword() == 3 || data.getBetterSword() == 5) {
                            item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        }
                        player.getInventory().addItem(item);
                    } else {
                        player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
                    }
                    player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherColor(color).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                    player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(color).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                    player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(color).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                    if (kit == Kit.MOBILITY) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, data.getMobility() < 3 ? data.getMobility() : 3));
                    }
                    player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(color).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                    player.getInventory().setItem(9, new ItemStack(Material.ARROW));
                    final int healthBoost = 2 * (data.getMoreHealth() < 5 ? data.getMoreHealth() : 6);
                    if (kit == Kit.MORE_HEALTH) {
                        player.setMaxHealth(20 + healthBoost);
                        player.setHealthScaled(false);
                        player.setHealth(20 + healthBoost);
                    }
                    obj.getScore(player).setScore(kit == Kit.MORE_HEALTH ? 20 + healthBoost : 20);
                    player.teleport(team.getNextSpawn());
                }
                final Objective kills = board.getObjective("kills");
                if (kills != null) {
                    kills.unregister();
                }
                board.registerNewObjective("kills", "playerKillCount").setDisplaySlot(DisplaySlot.PLAYER_LIST);
                Step.setCurrentStep(Step.IN_GAME);
                new GameTask(this.plugin);
            }
            return;
        }
        final int remainingMins = BeginCountdown.timeUntilStart / 60 % 60;
        final int remainingSecs = BeginCountdown.timeUntilStart % 60;
        if (BeginCountdown.timeUntilStart % 30 == 0 || remainingMins == 0 && (remainingSecs % 10 == 0 || remainingSecs < 10)) {
            Bukkit.broadcastMessage(SheepWarsPlugin.prefix + ChatColor.GOLD + "Démarrage du jeu dans " + ChatColor.YELLOW + (remainingMins > 0 ? remainingMins + " minute" + (remainingMins > 1 ? "s" : "") : "") + (remainingSecs > 0 ? (remainingMins > 0 ? " " : "") + remainingSecs + " seconde" + (remainingSecs > 1 ? "s" : "") : "") + ".");
            if (remainingMins == 0 && remainingSecs <= 10) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                }
            }
        }
        BeginCountdown.timeUntilStart--;
    }

    public static void resetPlayer(final Player player, final GameMode gameMode) {
        player.setFireTicks(0);
        player.setMaxHealth(20.0D);
        player.setHealthScaled(true);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setExhaustion(5);
        player.setFallDistance(0);
        player.setExp(0);
        player.setLevel(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(gameMode);
        player.closeInventory();
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
