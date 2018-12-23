package net.neferett.linaris.sheepwars.sheep;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.handler.Sheep.SheepAction;
import net.neferett.linaris.sheepwars.util.ParticleEffect;

public class HealerSheep implements SheepAction {

    @Override
    public void onSpawn(final Player player, final CustomSheep customSheep, final Sheep sheep) {}

    @Override
    public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final Sheep sheep) {
        if (ticks % 20 == 0) {
            final Team playerTeam = Team.getPlayerTeam(player);
            for (final Entity entity : sheep.getNearbyEntities(10, 5, 10)) {
                if (entity instanceof Player) {
                    final Player nearby = (Player) entity;
                    final Team team = Team.getPlayerTeam(nearby);
                    if (team == playerTeam) {
                        nearby.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1));
                    }
                }
            }
        } else if (ticks % 5 == 0) {
            ParticleEffect.HEART.display(sheep.getLocation().add(0, 1.5F, 0), 0, 0, 0, 0, 1);
        }
        return false;
    }

    @Override
    public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death) {}
}
