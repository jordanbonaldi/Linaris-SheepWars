package net.neferett.linaris.sheepwars.sheep;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.handler.Sheep.SheepAction;
import net.neferett.linaris.sheepwars.util.MathUtils;

public class RandomSheep implements SheepAction {

    @Override
    public void onSpawn(final Player player, final CustomSheep customSheep, final Sheep sheep) {}

    @Override
    public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final Sheep sheep) {
        if (ticks % 5 == 0) {
            DyeColor randomColor = null;
            while (randomColor == null || randomColor == sheep.getColor()) {
                randomColor = DyeColor.values()[MathUtils.random.nextInt(DyeColor.values().length)];
            }
            sheep.setColor(randomColor);
        }
        return false;
    }

    @Override
    public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death) {
        if (!death) {
            customSheep.explode(3.7F);
        }
    }
}
