package dev.dejay.evenbetterrtp.player.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface RTPCommand {

    void execute(CommandSender sender, String label, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);

    boolean hasPermission(CommandSender sender);
}
