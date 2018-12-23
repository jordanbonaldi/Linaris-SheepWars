package net.neferett.linaris.sheepwars.booster;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.neferett.linaris.sheepwars.handler.Booster;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.handler.Booster.BoosterAction;
import net.neferett.linaris.sheepwars.handler.Booster.TriggerAction;

public class PoisonBooster implements BoosterAction {

    @Override
    public boolean onStart(final Player player, final Team team, final Booster booster) {
        final Team opponents = team == Team.BLUE ? Team.RED : Team.BLUE;
        for (final Player opponent : opponents.getOnlinePlayers()) {
            opponent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }
        return true;
    }

    @Override
    public boolean onEvent(final Player player, final Event event, final TriggerAction trigger) {
        return false;
    }
}
