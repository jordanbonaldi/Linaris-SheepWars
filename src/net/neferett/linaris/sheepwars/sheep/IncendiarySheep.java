package net.neferett.linaris.sheepwars.sheep;

import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.handler.Sheep.SheepAction;

public class IncendiarySheep implements SheepAction {

    @Override
    public void onSpawn(final Player player, final CustomSheep customSheep, final Sheep sheep) {}

    @Override
    public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final Sheep sheep) {
        if (ticks <= 60 && ticks % 3 == 0) {
            if (ticks == 60) {
                sheep.getWorld().playSound(sheep.getLocation(), Sound.FUSE, 1, 1);
            }
            sheep.setColor(sheep.getColor() == DyeColor.WHITE ? DyeColor.ORANGE : DyeColor.WHITE);
        }
        return false;
    }

    @Override
    public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death) {
        if (!death) {
            customSheep.explode(4.7F, true);
        }
    }
}
