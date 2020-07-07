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

public class CmdWorld implements RTPCommand {

    //rtp world <world> <biome1, biome2...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 2) {
            Main.get().getCommands()
                .rtp(sender, Bukkit.getWorld(args[1]), Main.get().getCommands().getBiomes(args, 2, sender));
        } else {
            sendUsage(sender, label);
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            for (World world : Bukkit.getWorlds())
                if (world.getName().startsWith(args[1]) && !Main.get().getRTP().disabledWorlds().contains(world.getName()) &&
                        Main.get().getPerms().canTeleportInWorld(sender, world)) {
                    list.add(world.getName());
                }
        } else if (args.length >= 3) {
            if (CommandTypes.BIOME.getCommands().hasPermission(sender)) {
                getCommands().addBiomes(list, args);
            }
        }
        return list;
    }

    public boolean hasPermission(CommandSender sender) {
        return Main.get().getPerms().canTeleportInWorld(sender);
    }

    public void sendUsage(CommandSender sender, String label) {
        Main.get().getMessages().getUsageWorld(sender, label);
    }

    private Commands getCommands() {
        return Main.get().getCommands();
    }
}
