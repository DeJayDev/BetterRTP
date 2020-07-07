package dev.dejay.evenbetterrtp.player.commands.types;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.RTPCommand;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CmdReload implements RTPCommand {

    public void execute(CommandSender sender, String label, String[] args) {
        Main.get().reload(sender);
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public boolean hasPermission(CommandSender sender) {
        return Main.get().getPerms().canReload(sender);
    }
}
