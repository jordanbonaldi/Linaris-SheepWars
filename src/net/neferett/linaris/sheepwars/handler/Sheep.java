package net.neferett.linaris.sheepwars.handler;

import lombok.Getter;
import net.neferett.linaris.sheepwars.SheepWarsPlugin;
import net.neferett.linaris.sheepwars.entity.CustomSheep;
import net.neferett.linaris.sheepwars.sheep.BoardingSheep;
import net.neferett.linaris.sheepwars.sheep.DarkSheep;
import net.neferett.linaris.sheepwars.sheep.DistorsionSheep;
import net.neferett.linaris.sheepwars.sheep.EarthQuakeSheep;
import net.neferett.linaris.sheepwars.sheep.ExplosiveSheep;
import net.neferett.linaris.sheepwars.sheep.FragmentationSheep;
import net.neferett.linaris.sheepwars.sheep.FrozenSheepAction;
import net.neferett.linaris.sheepwars.sheep.HealerSheep;
import net.neferett.linaris.sheepwars.sheep.IncendiarySheep;
import net.neferett.linaris.sheepwars.sheep.IntergalacticSheep;
import net.neferett.linaris.sheepwars.sheep.LightningSheep;
import net.neferett.linaris.sheepwars.sheep.SeekerSheep;
import net.neferett.linaris.sheepwars.util.ItemBuilder;
import net.neferett.linaris.sheepwars.util.MathUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public enum Sheep implements Listener {
    BOARDING(ChatColor.WHITE + "Mouton d'abordage", ChatColor.GRAY + "Permet de voyager � dos de\n" + ChatColor.GRAY + "mouton l� ou vous le lancez.", new BoardingSheep(), DyeColor.WHITE, -1, false, false, false, 0.25F),
    EXPLOSIVE(ChatColor.RED + "Mouton explosif", ChatColor.GRAY + "Cr�e une explosion mod�r�e.", new ExplosiveSheep(), DyeColor.RED, 5, true, false, true),
    HEALER(ChatColor.LIGHT_PURPLE + "Mouton soigneur", ChatColor.GRAY + "Soigne les joueurs les plus proches.", new HealerSheep(), DyeColor.PINK, 10, true, true, false),
    INCENDIARY(ChatColor.GOLD + "Mouton incendiaire", ChatColor.GRAY + "Cr�e une explosion de grande taille\n" + ChatColor.GRAY + "mettant le feu dans la zone.", new IncendiarySheep(), DyeColor.ORANGE, 5, true, false, true, 0.5F),
    SEEKER(ChatColor.GREEN + "Mouton chercheur", ChatColor.GRAY + "Cours vers la cible la plus proche\n" + ChatColor.GRAY + "et explose � son contact.", new SeekerSheep(), DyeColor.LIME, 10, true, false, true, 0.5F),
    DARK(ChatColor.DARK_GRAY + "Mouton t�n�breux", ChatColor.GRAY + "Aveugle les ennemis les plus proches.", new DarkSheep(), DyeColor.BLACK, 10, true, false, true),
    FROZEN(ChatColor.AQUA + "Mouton glac�", ChatColor.GRAY + "Ralentit les ennemis et couvre la\n" + ChatColor.GRAY + "zone de neige.", new FrozenSheepAction(), DyeColor.CYAN, 10, true, false, true, 0.5F),
    EARTHQUAKE(ChatColor.GOLD + "Mouton tremblement de terre", ChatColor.GRAY + "Fait trembler la zone et\n" + ChatColor.GRAY + "projete les ennemis\n" + ChatColor.GRAY + "en l'air.", new EarthQuakeSheep(), DyeColor.BROWN, 10, true, false, true),
    DISTORSION(ChatColor.DARK_PURPLE + "Mouton distorsion", ChatColor.GRAY + "Cr�e de petites distorsions qui attirent\n" + ChatColor.GRAY + "tout ce qui se trouve � proximit� !", new DistorsionSheep(), DyeColor.PURPLE, 5, true, false, true),
    FRAGMENTATION(ChatColor.DARK_GRAY + "Mouton � fragmentation", ChatColor.GRAY + "Explose une premi�re fois\n" + ChatColor.GRAY + "et cr�e des petits moutons\n" + ChatColor.GRAY + "qui explosent � leur tour.", new FragmentationSheep(), DyeColor.GRAY, 5, true, false, true),
    LIGHTNING(ChatColor.YELLOW + "Mouton invocateur de foudre", ChatColor.GRAY + "Cr�e une temp�te et invoque la\n" + ChatColor.GRAY + "foudre dans la zone.", new LightningSheep(), DyeColor.YELLOW, 5, true, false, true, 0.35F),
    INTERGALACTIC(ChatColor.DARK_BLUE + "Mouton intergalactique", ChatColor.GRAY + "Ce mouton vient des confins de\n" + ChatColor.GRAY + "l'espace et invoque une nu�e\n" + ChatColor.GRAY + "de m�t�orites... !", new IntergalacticSheep(), DyeColor.BLUE, -1, true, false, false, 0.15F);

    public static interface SheepAction {
        public void onSpawn(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep);

        public boolean onTicking(final Player player, final long ticks, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep);

        public void onFinish(final Player player, final CustomSheep customSheep, final org.bukkit.entity.Sheep sheep, final boolean death);
    }

    public static org.bukkit.entity.Sheep spawnSheepStatic(final Location location, final Player player) {
        final CustomSheep customSheep = new CustomSheep(((CraftWorld) location.getWorld()).getHandle(), player);
        customSheep.setPosition(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(customSheep);
        return (org.bukkit.entity.Sheep) customSheep.getBukkitEntity();
    }

    public static org.bukkit.entity.Sheep spawnSheep(final Location location, final Player player, final Sheep sheep) {
        final CustomSheep customSheep = new CustomSheep(((CraftWorld) location.getWorld()).getHandle(), player, sheep);
        customSheep.setPosition(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(customSheep);
        return (org.bukkit.entity.Sheep) customSheep.getBukkitEntity();
    }

    public static Sheep giveRandomSheep(final Player player) {
        Sheep sheep = null;
        while (sheep == null) {
            final Sheep temp = Sheep.values()[MathUtils.random.nextInt(Sheep.values().length)];
            if (temp.random >= 1 || MathUtils.randomBoolean(temp.random)) {
                sheep = temp;
            }
        }
        Sheep.giveSheep(player, sheep);
        return sheep;
    }

    public static void giveSheep(final Player player, final Sheep sheep) {
        if (Team.getPlayerTeam(player) != Team.SPEC) {
            player.getInventory().addItem(sheep.getIcon());
            player.playSound(player.getLocation(), Sound.SHEEP_IDLE, 1, 1);
        }
    }

    @Getter
    private String name;
    private String description;
    @Getter
    private SheepAction action;
    @Getter
    private DyeColor color;
    @Getter
    private ItemStack icon;
    @Getter
    private int duration;
    @Getter
    private boolean onGround;
    @Getter
    private boolean friendly;
    @Getter
    private boolean drop;
    @Getter
    private float random;

    private Sheep(final String name, final String description, final SheepAction action, final DyeColor color, final int duration, final boolean onGround, final boolean friendly, final boolean drop) {
        this(name, description, action, color, duration, onGround, friendly, drop, 1);
    }

    private Sheep(final String name, final String description, final SheepAction action, final DyeColor color, final int duration, final boolean onGround, final boolean friendly, final boolean drop, final float random) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.color = color;
        this.icon = new ItemBuilder(Material.WOOL, color.getWoolData()).setTitle(name).addLores(description.split("\n")).build();
        this.duration = duration;
        this.onGround = onGround;
        this.friendly = friendly;
        this.drop = drop;
        this.random = random;
        Bukkit.getPluginManager().registerEvents(this, SheepWarsPlugin.i);
    }

    public org.bukkit.entity.Sheep spawnSheep(final Location location, final Player player) {
        return Sheep.spawnSheep(location, player, this);
    }
}
