package net.neferett.linaris.sheepwars.booster;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import net.neferett.linaris.sheepwars.handler.Booster;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.handler.Booster.BoosterAction;
import net.neferett.linaris.sheepwars.handler.Booster.TriggerAction;

public class MoreSheepBooster implements BoosterAction {

    @Override
    public boolean onStart(final Player player, final Team team, final Booster booster) {
        for (final Player teamPlayer : team.getOnlinePlayers()) {
            Sheep.giveRandomSheep(teamPlayer);
        }
        return true;
    }

    @Override
    public boolean onEvent(final Player player, final Event event, final TriggerAction trigger) {
        return false;
    }
}
