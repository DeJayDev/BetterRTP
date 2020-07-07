package dev.dejay.evenbetterrtp.player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import dev.dejay.evenbetterrtp.references.file.FileBasics.FileType;
import dev.dejay.evenbetterrtp.references.worlds.Custom;
import dev.dejay.evenbetterrtp.references.worlds.DefaultWorld;
import dev.dejay.evenbetterrtp.references.worlds.PlayerWorld;
import dev.dejay.evenbetterrtp.references.worlds.RTPWorld;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTP {

    private Main plugin = Main.get();

    //Cache
    public HashMap<String, RTPWorld> customWorlds = new HashMap<>();
    public HashMap<String, World> overriden = new HashMap<>();
    public DefaultWorld DefaultWorld = new DefaultWorld();
    private Random rn = new Random();
    private List<String> disabledWorlds, blockList;
    private int maxAttempts, delayTime;
    private boolean cancelOnMove;

    public RTP(Main plugin) {
        this.plugin = plugin;
    }

    public void load() {
        DefaultWorld.setup();
        FileType config = plugin.getFiles().getType(FileBasics.FileType.CONFIG);
        disabledWorlds = config.getStringList("DisabledWorlds");
        maxAttempts = config.getInt("Settings.MaxAttempts");
        delayTime = config.getInt("Settings.Delay.Time");
        cancelOnMove = config.getBoolean("Settings.Delay.CancelOnMove");
        blockList = config.getStringList("BlacklistedBlocks");
        try {
            for (String s : config.getConfigurationSection("Override").getKeys(false))
                overriden.put(s, plugin.getServer().getWorld(config.getString("Override." + s)));
        } catch (Exception e) {
            //No Overrides
        }
        customWorlds.clear();

        List<Map<?, ?>> map = config.getMapList("CustomWorlds");

        //Find Custom World and cache values
        for (Map<?, ?> m : map)
            for (Map.Entry<?, ?> entry : m.entrySet())
                customWorlds.put(entry.getKey().toString(), new Custom(entry.getKey().toString()));
    }

    public List<String> disabledWorlds() {
        return disabledWorlds;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public void start(Player player, CommandSender sender, World world, List<String> biomes, boolean delay) {
        // Check overrides
        if (world == null) {
            world = player.getWorld();
        }
        if (overriden.containsKey(world)) {
            world = overriden.get(world);
        }
        if (sender == player && !Main.get().getPerms().canTeleportInWorld(sender, world)) {
            plugin.getCommands().cooldowns.remove(player.getUniqueId());
            plugin.getMessages().getNoPermissionWorld(player, world.getName());
            return;
        }
        // Check disabled worlds
        if (disabledWorlds.contains(world)) {
            plugin.getMessages().getDisabledWorld(player, world.getName());
            plugin.getCommands().cooldowns.remove(player.getUniqueId());
            return;
        }
        // Check if nulled
        if (world == null) {
            plugin.getMessages().getNotExist(sender, world.getName());
            plugin.getCommands().cooldowns.remove(player.getUniqueId());
            return;
        }
        PlayerWorld pWorld = new PlayerWorld(player, world.getName());
        //Set all methods
        if (customWorlds.containsKey(world)) {
            RTPWorld cWorld = customWorlds.get(pWorld.getWorld());
            pWorld.setup(cWorld, cWorld.getPrice(), biomes);
        } else {
            pWorld.setup(DefaultWorld, DefaultWorld.getPrice(), biomes);
        }
        // Check world price
        if (!plugin.getEcon().charge(player, pWorld.getPrice())) {
            plugin.getMessages().getFailedPrice(player, pWorld.getPrice());
            plugin.getCommands().cooldowns.remove(player.getUniqueId());
            return;
        }

        if (delay) {
            plugin.getCommands().rtping.put(player.getUniqueId(), true);
            new Delay(player, pWorld, delayTime, cancelOnMove);
        } else {
            tp(player, pWorld);
        }

    }

    void tp(CommandSender sender, PlayerWorld pWorld) {
        Location loc = randomLoc(pWorld);
        if (loc != null)
            sendPlayer(sender, pWorld.getPlayer(), loc, pWorld.getPrice(), pWorld.getAttempts());
        else
            metMax(sender, pWorld.getPlayer(), pWorld.getPrice());
    }

    private void sendPlayer(CommandSender sender, Player player, Location loc, int price, int attempts) throws NullPointerException {
        if (sender != player) {
            checkPH(sender, player, loc, price, false, attempts);
        }

        if (plugin.getMessages().getTitleSuccessChat()) {
            checkPH(sender, player, loc, price, true, attempts);
        }

        if (plugin.getMessages().getTitleEnabled()) {
            showTitle(player, loc, attempts);
        }

        try {
            player.teleport(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (plugin.getMessages().getSoundsEnabled()) {
            playSuccessSound(player);
        }
    }

    private void checkPH(CommandSender sender, Player player, Location loc, int price, boolean sameAsPlayer, int attempts) {
        String x = Integer.toString(loc.getBlockX());
        String y = Integer.toString(loc.getBlockY());
        String z = Integer.toString(loc.getBlockZ());
        String world = loc.getWorld().getName();
        if (sameAsPlayer) {
            if (price == 0) {
                plugin.getMessages().getSuccessBypass(sender, x, y, z, world, attempts);
            } else {
                plugin.getMessages().getSuccessPaid(sender, price, x, y, z, world, attempts);
            }
        } else {
            plugin.getMessages().getOtherSuccess(sender, player.getDisplayName(), x, y, z, world, attempts);
        }
    }

    private void showTitle(Player player, Location loc, int attempts) {
        String x = String.valueOf(loc.getBlockX());
        String y = String.valueOf(loc.getBlockY());
        String z = String.valueOf(loc.getBlockZ());
        String title = plugin.getMessages().getTitleSuccess(player.getName(), x, y, z, attempts);
        String subTitle = plugin.getMessages().getSubTitleSuccess(player.getName(), x, y, z, attempts);
        player.sendTitle(title, subTitle);
    }

    private void playSuccessSound(Player p) {
        Sound sound = plugin.getMessages().getSoundsSuccess();
        if (sound != null) {
            p.playSound(p.getLocation(), sound, 1F, 1F);
        }
    }

    // Compressed code for MaxAttempts being meet
    private void metMax(CommandSender sender, Player p, int price) {
        if (p == sender)
            plugin.getMessages().getFailedNotSafe(sender, maxAttempts);
        else
            plugin.getMessages().getOtherNotSafe(sender, maxAttempts, p.getDisplayName());
        plugin.getCommands().cooldowns.remove(p.getUniqueId());
        plugin.getEcon().unCharge(p, price);
    }

    private Location randomLoc(PlayerWorld pWorld) {
        int borderRad = pWorld.getMaxRad();
        int minVal = pWorld.getMinRad();
        int centerX = pWorld.getCenterX();
        int centerZ = pWorld.getCenterZ();
        int posOrNeg = rn.nextInt(4);
        Player p = pWorld.getPlayer();
        World world = Bukkit.getWorld(pWorld.getWorld());
        if (pWorld.getUseWorldBorder()) {
            WorldBorder border = world.getWorldBorder();
            borderRad = (int) border.getSize() / 2;
            centerX = border.getCenter().getBlockX();
            centerZ = border.getCenter().getBlockZ();
        }
        float yaw = p.getLocation().getYaw(), pitch = p.getLocation().getPitch();
        boolean normal = !world.getBiome(0, 0).equals(Biome.valueOf("NETHER"));
        for (int i = 0; i <= maxAttempts; i++) {
            // Get the y-coords from up top, then check if it's SAFE!
            Location loc;
            if (borderRad <= minVal) {
                minVal = DefaultWorld.getMinRad();
                if (borderRad <= minVal)
                    minVal = 0;
            }
            if (normal)
                loc = normal(borderRad, minVal, centerX, centerZ, posOrNeg, world, pWorld, yaw, pitch);
            else
                loc = nether(borderRad, minVal, centerX, centerZ, posOrNeg, world, pWorld, yaw, pitch);
            pWorld.addAttempt();
            if (loc != null && isTeleportAllowed(loc))
                return loc;
            posOrNeg = rn.nextInt(4);
        }
        return null;
    }

    private Location normal(int borderRad, int minVal, int centerX, int centerZ, int posOrNeg, World world,
                            PlayerWorld pWorld, Float yaw, Float pitch) {
        int x, x2, z, z2;
        Location loc;
        // Will Check is centerZ is negative or positive, then set 2 x's
        // up for choosing up next
        z = rn.nextInt(borderRad - minVal) + centerZ + minVal;
        z2 = -(rn.nextInt(borderRad - minVal) - centerZ - minVal);
        // Will Check is centerZ is negative or positive, then set 2 z's
        // up for choosing up next
        x = rn.nextInt(borderRad - minVal) + centerX + minVal;
        x2 = -rn.nextInt(borderRad - minVal) + centerX - minVal;
        if (posOrNeg == 0)
            // Positive X and Z
            loc = getLocAtNormal(x, z, world, yaw, pitch, pWorld);
        else if (posOrNeg == 1)
            // Negative X and Z
            loc = getLocAtNormal(x2, z2, world, yaw, pitch, pWorld);
        else if (posOrNeg == 2)
            // Negative X and Positive Z
            loc = getLocAtNormal(x2, z, world, yaw, pitch, pWorld);
        else
            // Positive X and Negative Z
            loc = getLocAtNormal(x, z2, world, yaw, pitch, pWorld);
        return loc;
    }

    private Location getLocAtNormal(int x, int z, World world, Float yaw, Float pitch, PlayerWorld pWorld) {
        Block block = world.getHighestBlockAt(x, z);
        if (block.getType() == Material.AIR || !block.getType().isSolid()) { //1.15.1 or less
            int y = world.getHighestBlockYAt(x, z);
            block = world.getBlockAt(x, y - 1, z);
        }
        if (!isBadLocation(block, block.getLocation(), pWorld.getWorld(), pWorld.getBiomes())) {
            return new Location(world, (x + 0.5), block.getY() + 1, (z + 0.5), yaw, pitch);
        }
        return null;
    }

    private Location nether(int borderRad, int minVal, int centerX, int centerZ, int posOrNeg, World world,
                            PlayerWorld pWorld, Float yaw, Float pitch) {
        int x, x2, z, z2;
        Location loc;
        // Will Check is centerZ is negative or positive, then set 2 x's
        // up for choosing up next
        z = rn.nextInt((borderRad) - minVal) + centerZ + minVal;
        z2 = -(rn.nextInt(borderRad - minVal) - centerZ - minVal);
        // Will Check is centerZ is negative or positive, then set 2 z's
        // up for choosing up next
        x = rn.nextInt(borderRad - minVal) + centerX + minVal;
        x2 = -rn.nextInt(borderRad - minVal) + centerX - minVal;
        if (posOrNeg == 0)
            // Positive X and Z
            loc = getLocAtNether(x, z, world, yaw, pitch, pWorld);
        else if (posOrNeg == 1)
            // Negative X and Z
            loc = getLocAtNether(x2, z2, world, yaw, pitch, pWorld);
        else if (posOrNeg == 2)
            // Negative X and Positive Z
            loc = getLocAtNether(x2, z, world, yaw, pitch, pWorld);
        else
            // Positive X and Negative Z
            loc = getLocAtNether(x, z2, world, yaw, pitch, pWorld);
        return loc;
    }

    private Location getLocAtNether(int x, int z, World world, Float yaw, Float pitch, PlayerWorld pWorld) {
        for (int y = 0; y < world.getMaxHeight(); y++)
            if (world.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
                Block block = world.getBlockAt(x, y - 1, z);
                if (!isBadLocation(block, block.getLocation(), pWorld.getWorld(), pWorld.getBiomes())) {
                    return new Location(world, (x + 0.5), y, (z + 0.5), yaw, pitch);
                }
            }
        return null;
    }

    private boolean isTeleportAllowed(Location loc) {
        if (plugin.getSettings().getsDepends().usingWorldGuard()) {
            WorldGuard worldGuard = WorldGuard.getInstance();
            RegionContainer container = worldGuard.getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            // Check to make sure that "regions" is not null
            return query.getApplicableRegions(BukkitAdapter.adapt(loc)).size() == 0;
        }
        return !plugin.getSettings().getsDepends().usingGriefPrevention() || GriefPrevention.instance.dataStore.getClaimAt(loc, true, null) == null;
    }

    // Bad blocks, or good block and bad biome
    private boolean isBadLocation(Block block, Location location, String world, List<String> biomes) {
        for (String currentBlock : blockList) {//Check Block
            if (currentBlock.toUpperCase().equals(block.getType().name().toUpperCase())) {
                return true;
            }
        }
        //Check Biomes
        if (biomes == null || biomes.isEmpty()) {
            return false;
        }
        String biomeCurrent = Bukkit.getWorld(world).getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ()).name();
        for (String biome : biomes) {
            if (biomeCurrent.toUpperCase().contains(biome.toUpperCase())) {
                return false;
            }
        }
        return true;
        //FALSE MEANS NO BAD BLOCKS WHERE FOUND!
    }
}