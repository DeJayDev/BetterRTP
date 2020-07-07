package dev.dejay.evenbetterrtp.references;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class Permissions {

    private String prefix = "evenbetterrtp.";

    public boolean canRun(CommandSender sender) {
        return perm(prefix + "use", sender);
    }

    boolean canBypassEconomy(CommandSender sender) {
        return perm(prefix + "bypass.economy", sender);
    }

    public boolean canBypassCooldown(CommandSender sender) {
        return perm(prefix + "bypass.cooldown", sender);
    }

    public boolean canBypassDelay(CommandSender sender) {
        return perm(prefix + "bypass.delay", sender);
    }

    public boolean canReload(CommandSender sender) {
        return perm(prefix + "reload", sender);
    }

    public boolean canGetSettings(CommandSender sender) {
        return perm(prefix + "settings", sender);
    }

    public boolean canGetInfo(CommandSender sender) {
        return perm(prefix + "info", sender);
    }

    public boolean canTeleportOthers(CommandSender sender) {
        return perm(prefix + "player", sender);
    }

    public boolean canTeleportInBiome(CommandSender sender) {
        return (perm(prefix + "biome", sender));
    }

    public boolean canTeleportInWorld(CommandSender sender) {
        return (perm(prefix + "world", sender));
    }

    public boolean canCreateSign(CommandSender sender) {
        return (perm(prefix + "sign", sender));
    }

    public boolean canTeleportInWorld(CommandSender sender, World world) {
        if (perm(prefix + "world.*", sender))
            return true;
        else if (world == null) {
            for (World w : Bukkit.getWorlds())
                if (perm(prefix + "world." + w.getName(), sender))
                    return true;
        } else
            return perm(prefix + "world." + world, sender);
        return false;
    }

    private boolean perm(String str, CommandSender sender) {
        return sender.hasPermission(str);
    }
}
