package dev.dejay.evenbetterrtp.player.commands.types;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.RTPCommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdBiome implements RTPCommand {

    //rtp biome <biome1, biome2...>
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length >= 2) {
            Main.get().getCommands()
                .rtp(sender, null, Main.get().getCommands().getBiomes(args, 1, sender));
        } else {
            sendUsage(sender, label);
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public boolean hasPermission(CommandSender sender) {
        return Main.get().getPerms().canTeleportInBiome(sender);
    }

    public void sendUsage(CommandSender sender, String label) {
        Main.get().getMessages().getUsageBiome(sender, label);
    }
}
