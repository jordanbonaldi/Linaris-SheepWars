package net.neferett.linaris.sheepwars.event.entity;

import net.minecraft.server.v1_7_R4.Entity;
import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.handler.Team;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget extends SheepListener {
    public EntityTarget(final SheepWarsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent event) {
        if (event.getEntity() instanceof Sheep && event.getTarget() instanceof Player) {
            final Entity entity = ((CraftEntity) event.getEntity()).getHandle();
            if (entity instanceof CustomSheep) {
                final CustomSheep sheep = (CustomSheep) entity;
                final Team team = Team.getPlayerTeam((Player) event.getTarget());
                if (sheep.getColor() != DyeColor.LIME.ordinal() || team == Team.SPEC || team == Team.getPlayerTeam(sheep.getPlayer())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
