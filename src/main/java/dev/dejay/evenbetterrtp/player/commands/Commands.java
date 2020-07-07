package dev.dejay.evenbetterrtp.player.commands;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import dev.dejay.evenbetterrtp.references.file.FileBasics.FileType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

    private Main pl;
    public HashMap<UUID, Long> cooldowns = new HashMap<>();
    public HashMap<UUID, Boolean> rtping = new HashMap<>();
    private boolean cooldownTimer;
    private int timer, cooldown;
    public static String[] cmds = {"help", "player", "world", "version", "reload", "biome"};

    public Commands(Main pl) {
        this.pl = pl;
    }

    public void load() {
        FileType config = pl.getFiles().getType(FileBasics.FileType.CONFIG);
        timer = config.getInt("Settings.Delay.Time");
        cooldownTimer = config.getBoolean("Settings.Cooldown.Enabled");
        cooldown = config.getInt("Settings.Cooldown.Time");
        cooldowns.clear();
    }

    public void commandExecuted(CommandSender sender, String label, String[] args) {
        if (pl.getPerms().canRun(sender)) {
            if (args.length > 0) {
                for (CommandTypes cmd : CommandTypes.values()) {
                    if (cmd.name().equalsIgnoreCase(args[0])) {
                        if (cmd.getCommands().hasPermission(sender))
                            cmd.getCommands().execute(sender, label, args);
                        else
                            noPerm(sender);
                        return;
                    }
                }
                invalid(sender, label);
            } else
                rtp(sender, null, null);
        } else {
            noPerm(sender);
        }
    }

    private void invalid(CommandSender sender, String cmd) {
        pl.getMessages().getInvalid(sender, cmd);
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (CommandTypes cmd : CommandTypes.values()) {
                if (cmd.name().toLowerCase().startsWith(args[0].toLowerCase()) && cmd.getCommands().hasPermission(sender)) {
                    list.add(cmd.name().toLowerCase());
                }
            }
        } else if (args.length > 1) {
            for (CommandTypes cmd : CommandTypes.values()) {
                if (cmd.name().equalsIgnoreCase(args[0]) && cmd.getCommands().hasPermission(sender)) {
                    List<String> _cmdlist = cmd.getCommands().tabComplete(sender, args);
                    if (_cmdlist != null) {
                        list.addAll(_cmdlist);
                    }
                }
            }
        }
        return list;
    }

    public void addBiomes(List<String> list, String[] args) {
        try {
            for (Biome b : Biome.values())
                if (b.name().toUpperCase().replaceAll("minecraft:", "").startsWith(args[args.length - 1].toUpperCase()))
                    list.add(b.name().replaceAll("minecraft:", ""));
        } catch (NoSuchMethodError e) {
            //Not in 1.14.X
        }
    }

    public void rtp(CommandSender sender, World world, List<String> biomes) {
        if (sender instanceof Player) {
            tp((Player) sender, sender, world, biomes);
        } else {
            sender.sendMessage(pl.getMessages().colorPre("Only players can run this command."));
        }
    }

    //Custom biomes
    public List<String> getBiomes(String[] args, int start, CommandSender sender) {
        List<String> biomes = new ArrayList<>();
        boolean error_sent = false;
        if (Main.get().getPerms().canTeleportInBiome(sender))
            for (int i = start; i < args.length; i++) {
                String str = args[i];
                try {
                    biomes.add(Biome.valueOf(str.replaceAll(",", "").toUpperCase()).name());
                } catch (Exception e) {
                    if (!error_sent) {
                        pl.getMessages().getOtherBiome(sender, str);
                        error_sent = true;
                    }
                }
            }
        return biomes;
    }

    public void playerNotOnline(CommandSender sender, String player) {
        pl.getMessages().getNotOnline(sender, player);
    }

    private void noPerm(CommandSender sender) {
        pl.getMessages().getNoPermission(sender);
    }

    public void tp(Player player, CommandSender sender, World world, List<String> biomes) {
        if (cooldown(sender, player)) {
            boolean delay = false;
            if (!pl.getPerms().canBypassDelay(player)) {
                if (timer != 0) {
                    if (sender == player) {
                        delay = true;
                    }
                }
            }
            pl.getRTP().start(player, sender, world, biomes, delay);
        }
    }

    private boolean cooldown(CommandSender sender, Player player) {
        if (sender != player || pl.getPerms().canBypassCooldown(player))
            return true;
        else if (rtping.containsKey(player.getUniqueId()))
            if (rtping.get(player.getUniqueId())) {
                pl.getMessages().getAlready(player);
                return false;
            }
        if (cooldownTimer) {
            Player p = (Player) sender;
            if (cooldowns.containsKey(p.getUniqueId())) {
                long Left = ((cooldowns.get(p.getUniqueId()) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                if (!pl.getPerms().canBypassDelay(p))
                    Left = Left + timer;
                if (Left > 0) {
                    // Still cooling down
                    pl.getMessages().getCooldown(sender, String.valueOf(Left));
                    return false;
                } else {
                    cooldowns.remove(p.getUniqueId());
                    return true;
                }
            } else
                cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
        }
        return true;
    }
}
