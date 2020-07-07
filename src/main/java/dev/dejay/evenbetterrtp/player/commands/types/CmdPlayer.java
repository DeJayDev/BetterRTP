package dev.dejay.evenbetterrtp.player.commands.types;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.CommandTypes;
import dev.dejay.evenbetterrtp.player.commands.Commands;
import dev.dejay.evenbetterrtp.player.commands.RTPCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPlayer implements RTPCommand {

    //rtp player <world> <biome1> <biome2...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                getCommands().tp(Bukkit.getPlayer(args[1]), sender,
                    Bukkit.getPlayer(args[1]).getWorld(), null);
            } else if (Bukkit.getPlayer(args[1]) != null) {
                getCommands().playerNotOnline(sender, args[1]);
            } else {
                usage(sender, label);
            }
        } else if (args.length >= 3) {
            if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline())
                getCommands()
                    .tp(Bukkit.getPlayer(args[1]), sender, Bukkit.getWorld(args[2]),
                        getCommands().getBiomes(args, 3, sender));
            else if (Bukkit.getPlayer(args[1]) != null)
                getCommands().playerNotOnline(sender, args[1]);
            else
                usage(sender, label);
        } else {
            usage(sender, label);
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        System.out.println(this.toString());
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    list.add(p.getDisplayName());
                }
            }
        } else if (args.length == 3) {
            for (World w : Bukkit.getWorlds()) {
                if (w.getName().startsWith(args[2]) && !Main.get().getRTP().disabledWorlds()
                    .contains(w.getName())) {
                    list.add(w.getName());
                }
            }
        } else if (args.length > 3) {
            if (CommandTypes.BIOME.getCommands().hasPermission(sender)) {
                getCommands().addBiomes(list, args);
            }
        }
        return list;
    }

    public boolean hasPermission(CommandSender sender) {
        return Main.get().getPerms().canTeleportOthers(sender);
    }

    public void usage(CommandSender sender, String label) {
        Main.get().getMessages().getUsageRTPOther(sender, label);
    }

    private Commands getCommands() {
        return Main.get().getCommands();
    }
}
