package dev.dejay.evenbetterrtp.player.commands.types;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.RTPCommand;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHelp implements RTPCommand {

    public void execute(CommandSender sender, String label, String[] args) {
        Main pl = Main.get();
        pl.getMessages().getHelpList(sender, label);
        if (pl.getPerms().canTeleportOthers(sender)) {
            pl.getMessages().getHelpPlayer(sender, label);
        }
        if (sender instanceof Player) {
            if (pl.getPerms().canTeleportInWorld(sender, null))
                pl.getMessages().getHelpWorld(sender, label);
        } else {
            pl.getMessages().getHelpWorld(sender, label);
        }
        if (pl.getPerms().canReload(sender)) {
            pl.getMessages().getHelpReload(sender, label);
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }


    public boolean hasPermission(CommandSender sender) {
        return true;
    }
}
