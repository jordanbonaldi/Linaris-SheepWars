package net.neferett.linaris.sheepwars.event.projectile;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.EntityArrow;
import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Booster;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.util.MathUtils;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProjectileHit extends SheepListener {
    public ProjectileHit(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getEntity();
            arrow.remove();
            if (arrow.getShooter() instanceof Player) {
                final Player player = (Player) arrow.getShooter();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            final EntityArrow entityArrow = ((CraftArrow) arrow).getHandle();
                            final Field fieldX = EntityArrow.class.getDeclaredField("d");
                            final Field fieldY = EntityArrow.class.getDeclaredField("e");
                            final Field fieldZ = EntityArrow.class.getDeclaredField("f");
                            fieldX.setAccessible(true);
                            fieldY.setAccessible(true);
                            fieldZ.setAccessible(true);
                            final int x = fieldX.getInt(entityArrow);
                            final int y = fieldY.getInt(entityArrow);
                            final int z = fieldZ.getInt(entityArrow);
                            final Block block = arrow.getWorld().getBlockAt(x, y, z);
                            if (block.getType() == Material.WOOL && ProjectileHit.this.plugin.isBooster(block.getLocation())) {
                                final Team team = Team.getPlayerTeam(player);
                                if (team != null) {
                                    block.setType(Material.AIR);
                                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
                                    final Booster booster = Booster.values()[MathUtils.random.nextInt(Booster.values().length)];
                                    Bukkit.broadcastMessage(SheepWarsPlugin.prefix + team.getColor() + player.getName() + " active " + booster.getName());
                                    booster.getAction().onStart(player, team, booster);
                                }
                            }
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskLater(this.plugin, 1);
            }
        }
    }
}
