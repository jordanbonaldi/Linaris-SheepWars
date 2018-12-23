package net.neferett.linaris.sheepwars.handler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum Kit {
    MORE_HEALTH(ChatColor.GOLD + "Plus de vie", Material.APPLE),
    BETTER_BOW(ChatColor.GOLD + "Arc amélioré", Material.BOW),
    BETTER_SWORD(ChatColor.GOLD + "Epée améliorée", Material.STONE_SWORD),
    MORE_SHEEP(ChatColor.GOLD + "Plus de Moutons", Material.WOOL),
    MOBILITY(ChatColor.GOLD + "Mobilité", Material.LEATHER_BOOTS);

    private static Map<Player, Kit> playerKits = new HashMap<>();

    @Getter
    private String name;
    @Getter
    private Material material;
    @Getter
    private short durability;

    public static Kit getPlayerKit(final Player player) {
        return Kit.playerKits.get(player);
    }

    public static void setPlayerKit(final Player player, final Kit kit) {
        if (kit == null) {
            Kit.playerKits.remove(player);
        } else {
            Kit.playerKits.put(player, kit);
        }
    }

    private Kit(final String name, final Material material) {
        this(name, material, (short) 0);
    }

    private Kit(final String name, final Material material, final short durability) {
        this.name = name;
        this.material = material;
        this.durability = durability;
    }
}
