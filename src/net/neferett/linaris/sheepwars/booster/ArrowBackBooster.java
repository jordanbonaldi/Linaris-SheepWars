package net.neferett.linaris.sheepwars.booster;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import net.neferett.linaris.sheepwars.handler.Booster;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.handler.Booster.BoosterAction;
import net.neferett.linaris.sheepwars.handler.Booster.TriggerAction;

public class ArrowBackBooster implements BoosterAction {
    private final Map<Team, Long> teams = new HashMap<>();

    @Override
    public boolean onStart(final Player player, final Team team, final Booster booster) {
        this.teams.put(team, System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean onEvent(final Player player, final Event event, final TriggerAction trigger) {
        if (trigger == TriggerAction.ARROW_LAUNCH) {
            final ProjectileLaunchEvent launchEvent = (ProjectileLaunchEvent) event;
            final Team team = Team.getPlayerTeam(player);
            if (team != null && this.teams.containsKey(team)) {
                final long time = this.teams.get(team);
                if (System.currentTimeMillis() - time > 15000) {
                    this.teams.remove(team);
                } else {
                    ((Arrow) launchEvent.getEntity()).setKnockbackStrength(2);
                    return true;
                }
            }
        }
        return false;
    }
}
