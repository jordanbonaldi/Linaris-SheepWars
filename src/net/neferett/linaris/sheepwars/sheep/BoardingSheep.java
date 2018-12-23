package net.neferett.linaris.sheepwars.sheep;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.handler.Sheep.SheepAction;

public class BoardingSheep implements SheepAction {

    @Override
    public void onSpawn(final Player player, final CustomSheep customSheep, final Sheep sheep) {
        sheep.setPassenger(player);
    }

    @Override
    public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final Sheep sheep) {
        return sheep.getPassenger() == null || sheep.isOnGround();
    }

    @Override
    public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death) {}
}
