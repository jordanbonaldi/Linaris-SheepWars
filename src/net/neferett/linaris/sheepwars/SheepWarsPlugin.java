package net.neferett.linaris.sheepwars;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import lombok.SneakyThrows;
import net.neferett.linaris.sheepwars.entity.EntityFirework;
import net.neferett.linaris.sheepwars.event.SheepListener;
import net.neferett.linaris.sheepwars.event.block.BlockBreak;
import net.neferett.linaris.sheepwars.event.block.BlockPlace;
import net.neferett.linaris.sheepwars.event.block.BlockSpread;
import net.neferett.linaris.sheepwars.event.entity.CreatureSpawn;
import net.neferett.linaris.sheepwars.event.entity.EntityChangeBlock;
import net.neferett.linaris.sheepwars.event.entity.EntityDamage;
import net.neferett.linaris.sheepwars.event.entity.EntityDamageByPlayer;
import net.neferett.linaris.sheepwars.event.entity.EntityDeath;
import net.neferett.linaris.sheepwars.event.entity.EntityExplode;
import net.neferett.linaris.sheepwars.event.entity.EntityTarget;
import net.neferett.linaris.sheepwars.event.entity.FoodLevelChange;
import net.neferett.linaris.sheepwars.event.inventory.InventoryClick;
import net.neferett.linaris.sheepwars.event.player.AsyncPlayerChat;
import net.neferett.linaris.sheepwars.event.player.PlayerAchievementAwarded;
import net.neferett.linaris.sheepwars.event.player.PlayerCommandPreprocess;
import net.neferett.linaris.sheepwars.event.player.PlayerDamage;
import net.neferett.linaris.sheepwars.event.player.PlayerDamageByPlayer;
import net.neferett.linaris.sheepwars.event.player.PlayerDeath;
import net.neferett.linaris.sheepwars.event.player.PlayerDropItem;
import net.neferett.linaris.sheepwars.event.player.PlayerInteract;
import net.neferett.linaris.sheepwars.event.player.PlayerJoin;
import net.neferett.linaris.sheepwars.event.player.PlayerKick;
import net.neferett.linaris.sheepwars.event.player.PlayerLogin;
import net.neferett.linaris.sheepwars.event.player.PlayerMove;
import net.neferett.linaris.sheepwars.event.player.PlayerPickupItem;
import net.neferett.linaris.sheepwars.event.player.PlayerQuit;
import net.neferett.linaris.sheepwars.event.player.PlayerRespawn;
import net.neferett.linaris.sheepwars.event.projectile.ProjectileHit;
import net.neferett.linaris.sheepwars.event.projectile.ProjectileLaunch;
import net.neferett.linaris.sheepwars.event.server.ServerCommand;
import net.neferett.linaris.sheepwars.event.server.ServerListPing;
import net.neferett.linaris.sheepwars.handler.Entity;
import net.neferett.linaris.sheepwars.handler.Kit;
import net.neferett.linaris.sheepwars.handler.MySQL;
import net.neferett.linaris.sheepwars.handler.PlayerData;
import net.neferett.linaris.sheepwars.handler.Sheep;
import net.neferett.linaris.sheepwars.handler.Step;
import net.neferett.linaris.sheepwars.handler.Team;
import net.neferett.linaris.sheepwars.util.FileUtils;
import net.neferett.linaris.sheepwars.util.MathUtils;
import net.neferett.linaris.sheepwars.util.ReflectionHandler;
import net.neferett.linaris.sheepwars.util.ReflectionHandler.PackageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SheepWarsPlugin extends JavaPlugin {
    public static SheepWarsPlugin i;
    public static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "SW" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " ";

    private World world;
    public MySQL database;
    public Location lobbyLocation;
    public List<Location> boosters;
    private final Map<UUID, PlayerData> data = new HashMap<>();

    @SneakyThrows
    @Override
    public void onLoad() {
        Bukkit.unloadWorld("world", false);
        final File worldContainer = this.getServer().getWorldContainer();
        final File worldFolder = new File(worldContainer, "world");
        final File copyFolder = new File(worldContainer, "sheepwars");
        if (copyFolder.exists()) {
            ReflectionHandler.getClass("RegionFileCache", PackageType.MINECRAFT_SERVER).getMethod("a").invoke(null);
            FileUtils.delete(worldFolder);
        }
    }

    @Override
    public void onEnable() {
        SheepWarsPlugin.i = this;
        Entity.registerEntities();
        Step.setCurrentStep(Step.LOBBY);
        this.world = Bukkit.getWorlds().get(0);
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setTime(6000);
        this.world.setStorm(false);
        this.world.setThundering(false);
        this.load();
        this.database = new MySQL(this, this.getConfig().getString("mysql.host"), this.getConfig().getString("mysql.port"), this.getConfig().getString("mysql.database"), this.getConfig().getString("mysql.user"), this.getConfig().getString("mysql.pass"));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.register(BlockBreak.class, BlockPlace.class, BlockSpread.class, CreatureSpawn.class, EntityChangeBlock.class, EntityDamage.class, EntityDamageByPlayer.class, EntityDeath.class, EntityExplode.class, EntityTarget.class, FoodLevelChange.class, InventoryClick.class, AsyncPlayerChat.class, PlayerAchievementAwarded.class, PlayerCommandPreprocess.class, PlayerDamage.class, PlayerDamageByPlayer.class, PlayerDeath.class, PlayerDropItem.class, PlayerInteract.class, PlayerJoin.class, PlayerKick.class, PlayerLogin.class, PlayerMove.class, PlayerPickupItem.class, PlayerQuit.class, PlayerRespawn.class, ProjectileHit.class, ProjectileLaunch.class, ServerCommand.class, ServerListPing.class);
    }

    @Override
    public void onDisable() {
        Entity.unregisterEntities();
        this.save();
    }

    @SneakyThrows
    private void register(final Class<? extends SheepListener>... classes) {
        for (final Class<? extends SheepListener> clazz : classes) {
            final Constructor<? extends SheepListener> constructor = clazz.getConstructor(SheepWarsPlugin.class);
            Bukkit.getPluginManager().registerEvents(constructor.newInstance(this), this);
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (command.getName().equalsIgnoreCase("sheepwars")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Vous devez être un joueur.");
                return true;
            }
            final Player player = (Player) sender;
            if (args.length != 0) {
                final String sub = args[0];
                if (sub.equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GOLD + "Aide du plugin SheepWars :");
                    player.sendMessage("/sw setlobby" + ChatColor.YELLOW + " - définit le lobby du jeu");
                    player.sendMessage("/sw addbooster" + ChatColor.YELLOW + " - ajoute une laine magique");
                    player.sendMessage("/sw addspawn <couleur>" + ChatColor.YELLOW + " - ajoute un spawn pour l'équipe <couleur>");
                } else if (sub.equalsIgnoreCase("setlobby")) {
                    this.lobbyLocation = player.getLocation();
                    player.sendMessage(ChatColor.GREEN + "Vous avez défini le lobby avec succès.");
                    this.getConfig().set("lobby", this.toString(player.getLocation()));
                    this.saveConfig();
                } else if (sub.equalsIgnoreCase("addspawn") && args.length == 2) {
                    if (!args[1].equalsIgnoreCase("red") && !args[1].equalsIgnoreCase("blue") && !args[1].equalsIgnoreCase("spec")) {
                        player.sendMessage(ChatColor.RED + "La couleur " + ChatColor.DARK_RED + args[1] + ChatColor.RED + " n'existe pas.");
                    } else {
                        final Team team = Team.getTeam(args[1]);
                        team.getSpawns().add(player.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Vous avez ajouté avec succès un spawn à l'équipe des " + team.getColor() + team.getDisplayName());
                    }
                } else if (sub.equalsIgnoreCase("addbooster")) {
                    final Location location = player.getLocation().getBlock().getLocation().subtract(0, 1, 0);
                    if (location.getBlock().getType() != Material.AIR) {
                        player.sendMessage(ChatColor.RED + "Vous devez vous placer sur un bloc vide.");
                    } else {
                        this.boosters.add(location);
                        player.sendMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "| " + ChatColor.AQUA + ChatColor.BOLD + "Vous avez ajouté une laine magique" + ChatColor.YELLOW + ChatColor.MAGIC + " |" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|");
                    }
                } else if (sub.equalsIgnoreCase("give") && args.length == 2) {
                    final Sheep sheep = Sheep.valueOf(args[1].toUpperCase());
                    if (sheep == null) {
                        player.sendMessage(ChatColor.RED + "Le mouton n'existe pas.");
                    } else {
                        player.getInventory().addItem(sheep.getIcon());
                        player.sendMessage(ChatColor.GREEN + "Vous avez reçu le mouton.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Mauvais arguments ou commande inexistante. Tapez " + ChatColor.DARK_RED + "/sw help" + ChatColor.RED + " pour de l'aide.");
                }
                return true;
            }
        }
        return false;
    }

    public PlayerData getData(final Player player) {
        final PlayerData data = this.data.get(player.getUniqueId());
        if (data == null) {
            this.loadData(player);
            return new PlayerData(player.getUniqueId(), player.getName(), 0, 0, 0, 0, 0, 0);
        }
        return data;
    }

    public void loadData(final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    final ResultSet res = SheepWarsPlugin.this.database.querySQL("SELECT * FROM players WHERE uuid=UNHEX('" + player.getUniqueId().toString().replaceAll("-", "") + "')");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerData data = null;
                            try {
                                if (res.first()) {
                                    data = new PlayerData(player.getUniqueId(), res.getString("name"), res.getInt("sw_more_health"), res.getInt("sw_better_bow"), res.getInt("sw_better_sword"), res.getInt("sw_more_sheep"), res.getInt("sw_mobility"), 0);
                                } else {
                                    data = new PlayerData(player.getUniqueId(), player.getName(), 0, 0, 0, 0, 0, 0);
                                }
                                SheepWarsPlugin.this.data.put(player.getUniqueId(), data);
                            } catch (final SQLException e) {
                                player.kickPlayer(ChatColor.RED + "Impossible de charger vos statistiques... :(");
                            }
                        }
                    }.runTask(SheepWarsPlugin.this);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }

    private void load() {
        this.saveDefaultConfig();
        this.lobbyLocation = this.toLocation(this.getConfig().getString("lobby", this.toString(this.world.getSpawnLocation())));
        this.boosters = new ArrayList<>();
        final ConfigurationSection boosters = this.getConfig().getConfigurationSection("boosters");
        if (boosters != null) {
            for (final String key : boosters.getKeys(false)) {
                this.boosters.add(this.toLocation(boosters.getString(key)));
            }
        }
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        final ConfigurationSection teams = this.getConfig().getConfigurationSection("teams");
        if (teams != null) {
            Objective objective = scoreboard.getObjective("teams");
            if (objective == null) {
                objective = scoreboard.registerNewObjective("teams", "dummy");
            }
            objective.setDisplayName(ChatColor.DARK_GRAY + "-" + ChatColor.YELLOW + "SheepWars " + ChatColor.GREEN + "20:00" + ChatColor.DARK_GRAY + "-");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (final String key : teams.getKeys(false)) {
                final Team team = Team.getTeam(key);
                final ConfigurationSection spawns = teams.getConfigurationSection(key + ".spawns");
                if (spawns != null) {
                    for (final String spawn : spawns.getKeys(false)) {
                        team.getSpawns().add(this.toLocation(spawns.getString(spawn)));
                    }
                }
            }
        }
    }

    private void save() {
        this.getConfig().set("lobby", this.toString(this.lobbyLocation));
        for (int i = 0; i < this.boosters.size(); i++) {
            this.getConfig().set("boosters." + i, this.toString(this.boosters.get(i)));
        }
        for (final Team team : Team.values()) {
            final String name = team.getCraftTeam().getName();
            for (int i = 0; i < team.getSpawns().size(); i++) {
                this.getConfig().set("teams." + name + ".spawns." + i, this.toString(team.getSpawns().get(i)));
            }
        }
        this.saveConfig();
    }

    private Location toLocation(final String string) {
        final String[] splitted = string.split("_");
        World world = Bukkit.getWorld(splitted[0]);
        if (world == null || splitted.length < 6) {
            world = this.world;
        }
        return new Location(world, Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]), Float.parseFloat(splitted[4]), Float.parseFloat(splitted[5]));
    }

    private String toString(final Location location) {
        final World world = location.getWorld();
        return world.getName() + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "_" + location.getYaw() + "_" + location.getPitch();
    }

    public void setSpectator(final Player player, final boolean lose) {
        player.setAllowFlight(true);
        if (lose && Team.getPlayerTeam(player) != Team.SPEC) {
            this.removePlayer(player);
        }
        Team.SPEC.addPlayer(player);
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (player != online) {
                player.showPlayer(online);
                if (Team.getPlayerTeam(online) != Team.SPEC) {
                    online.hidePlayer(player);
                }
            }
        }
    }

    public void removePlayer(final Player player) {
        final Team team = Team.getPlayerTeam(player);
        if (team != Team.SPEC) {
            team.removePlayer(player);
            Kit.setPlayerKit(player, null);
            if (Step.isStep(Step.LOBBY)) {
                this.data.remove(player.getUniqueId());
            } else if (Step.isStep(Step.IN_GAME) && team.getOnlinePlayers().size() == 0) {
                final Team winnerTeam = Team.BLUE == team ? Team.RED : Team.BLUE;
                Step.setCurrentStep(Step.POST_GAME);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(SheepWarsPlugin.prefix + ChatColor.GOLD + ChatColor.BOLD + "Victoire de l'équipe " + winnerTeam.getColor() + ChatColor.BOLD + winnerTeam.getDisplayName() + " " + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.BOLD + " Félicitations " + ChatColor.YELLOW + ChatColor.MAGIC + " |" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|" + ChatColor.YELLOW + ChatColor.MAGIC + "|" + ChatColor.AQUA + ChatColor.MAGIC + "|" + ChatColor.GREEN + ChatColor.MAGIC + "|" + ChatColor.RED + ChatColor.MAGIC + "|" + ChatColor.LIGHT_PURPLE + ChatColor.MAGIC + "|");
                        new BukkitRunnable() {
                            private int ticks = 300;

                            @Override
                            public void run() {
                                if (this.ticks == 0) {
                                    this.cancel();
                                    return;
                                }
                                final Location location = SheepWarsPlugin.i.boosters.get(MathUtils.random.nextInt(SheepWarsPlugin.i.boosters.size()));
                                EntityFirework.spawn(location, FireworkEffect.builder().withColor(Color.fromRGB(MathUtils.random(255), MathUtils.random(255), MathUtils.random(255))).build(), Bukkit.getOnlinePlayers());
                                this.ticks -= 10;
                            }
                        }.runTaskTimer(SheepWarsPlugin.this, 0, 10);
                        SheepWarsPlugin.this.stopGame(winnerTeam);
                    }
                }.runTaskLater(this, 1);
            }
        }
    }

    public boolean isBooster(final Location location) {
        for (final Location booster : this.boosters) {
            if (booster.getBlockX() == location.getBlockX() && booster.getBlockY() == location.getBlockY() && booster.getBlockZ() == location.getBlockZ()) return true;
        }
        return false;
    }

    public void stopGame(final Team winnerTeam) {
        for (final Player online : Bukkit.getOnlinePlayers()) {
            online.setAllowFlight(true);
        }
        for (final Entry<UUID, PlayerData> entry : this.data.entrySet()) {
            final String uuid = entry.getKey().toString().replaceAll("-", "");
            final PlayerData data = entry.getValue();
            if (winnerTeam != null) {
                final Player online = Bukkit.getPlayer(entry.getKey());
                if (online != null && online.isOnline()) {
                    if (Team.getPlayerTeam(online) == winnerTeam) {
                        data.addCoins(10, false);
                    } else {
                        data.addCoins(2.5, false);
                    }
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        final ResultSet res = SheepWarsPlugin.this.database.querySQL("SELECT name FROM players WHERE uuid=UNHEX('" + uuid + "')");
                        if (res.first()) {
                            SheepWarsPlugin.this.database.updateSQL("UPDATE players SET name='" + data.getName() + "', coins=coins+" + data.getCoins() + ", updated_at=NOW() WHERE uuid=UNHEX('" + uuid + "')");
                        } else {
                            SheepWarsPlugin.this.database.updateSQL("INSERT INTO players(name, uuid, coins, created_at, updated_at) VALUES('" + data.getName() + "', UNHEX('" + uuid + "'), " + data.getCoins() + ", NOW(), NOW())");
                        }
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(this);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (final Player online : Bukkit.getOnlinePlayers()) {
                    SheepWarsPlugin.this.teleportToLobby(online);
                }
            }
        }.runTaskLater(this, 300);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(this, 400);
    }

    public void teleportToLobby(final Player player) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("lobby");
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
}
