package net.neferett.linaris.sheepwars.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import net.neferett.linaris.sheepwars.booster.ArrowBackBooster;
import net.neferett.linaris.sheepwars.booster.ArrowFireBooster;
import net.neferett.linaris.sheepwars.booster.MoreSheepBooster;
import net.neferett.linaris.sheepwars.booster.NauseaBooster;
import net.neferett.linaris.sheepwars.booster.PoisonBooster;
import net.neferett.linaris.sheepwars.booster.RegenerationBooster;
import net.neferett.linaris.sheepwars.booster.ResistanceBooster;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public enum Booster {
    // @formatter:off
    POISON(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Poison " + ChatColor.YELLOW + "(4 secondes)", new PoisonBooster(), new ArrayList<TriggerAction>()), NAUSEA(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Naus�e " + ChatColor.YELLOW + "(10 secondes)", new NauseaBooster(), new ArrayList<TriggerAction>()), MORE_SHEEP(ChatColor.AQUA + "" + ChatColor.BOLD + "Plus de moutons " + ChatColor.YELLOW + "(+1 mouton)", new MoreSheepBooster(), new ArrayList<TriggerAction>()), REGENERATION(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "R�g�n�ration " + ChatColor.YELLOW + "(6 secondes)", new RegenerationBooster(), new ArrayList<TriggerAction>()), RESISTANCE(ChatColor.WHITE + "" + ChatColor.BOLD + "R�sistance " + ChatColor.YELLOW + "(30 secondes)", new ResistanceBooster(), new ArrayList<TriggerAction>()), ARROW_FIRE(ChatColor.GOLD + "" + ChatColor.BOLD + "Fl�ches de feu " + ChatColor.YELLOW + "(15 secondes)", new ArrowFireBooster(), Arrays.asList(TriggerAction.ARROW_LAUNCH)), ARROW_BACK(ChatColor.GRAY + "" + ChatColor.BOLD + "Fl�ches de recul " + ChatColor.YELLOW + "(10 secondes)", new ArrowBackBooster(), Arrays.asList(TriggerAction.ARROW_LAUNCH));
    // @formatter:on

    public static interface BoosterAction {
        public boolean onStart(final Player player, final Team team, final Booster booster);

        public boolean onEvent(final Player player, final Event event, final TriggerAction trigger);
    }

    public static enum TriggerAction {
        ARROW_LAUNCH
    }

    @Getter
    private String name;
    @Getter
    private BoosterAction action;
    @Getter
    private List<TriggerAction> triggers;

    private Booster(final String name, final BoosterAction action, final List<TriggerAction> triggers) {
        this.name = name;
        this.action = action;
        this.triggers = triggers;
    }
}
