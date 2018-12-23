package net.neferett.linaris.sheepwars.sheep;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.handler.Sheep.SheepAction;

public class SeekerSheep implements SheepAction {

    @Override
    public void onSpawn(final Player player, final CustomSheep customSheep, final Sheep sheep) {}

    @Override
    public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final Sheep sheep) {
        if (ticks % 3 == 0) {
            if (ticks <= 60) {
                sheep.setColor(sheep.getColor() == DyeColor.WHITE ? DyeColor.LIME : DyeColor.WHITE);
            }
            final Team playerTeam = Team.getPlayerTeam(player);
            for (final Entity entity : sheep.getNearbyEntities(0.8D, 0.5D, 0.8D)) {
                if (entity instanceof Player) {
                    final Player nearby = (Player) entity;
                    final Team team = Team.getPlayerTeam(nearby);
                    if (team != playerTeam && team != Team.SPEC) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death) {
        if (!death) {
            customSheep.explode(3.5F);
        }
    }
}
