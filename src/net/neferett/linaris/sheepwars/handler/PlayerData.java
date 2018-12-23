package net.neferett.linaris.sheepwars.handler;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class PlayerData {
    private final UUID uuid;
    private final String name;
    private final int moreHealth;
    private final int betterBow;
    private final int betterSword;
    private final int moreSheep;
    private final int mobility;
    private double coins;

    public void addCoins(final double coins) {
        this.addCoins(coins, true);
    }

    public void addCoins(final double coins, final boolean msg) {
        final Player player = Bukkit.getPlayer(this.name);
        if (player != null && player.isOnline()) {
            this.coins += player.hasPermission("funcoins.mvpplus") ? coins * 4 : player.hasPermission("funcoins.mvp") ? coins * 3 : player.hasPermission("funcoins.vip") ? coins * 2 : coins;
            if (msg) {
                Bukkit.getPlayer(this.name).sendMessage(ChatColor.GRAY + "Gain de FunCoins + " + ChatColor.GOLD + String.valueOf(coins).replace(".", ","));
            }
        }
    }
}
