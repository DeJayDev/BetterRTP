package dev.dejay.evenbetterrtp.player.commands.types;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.RTPCommand;
import dev.dejay.evenbetterrtp.references.worlds.RTPWorld;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;

public class CmdInfo implements RTPCommand {

    public void execute(CommandSender sender, String label, String[] args) {
        List<String> info = new ArrayList<>();
        info.add("&6EvenBetterRTP Info");
        for (World w : Bukkit.getWorlds()) {
            info.add("&aWorld: &7" + w.getName());
            if (Main.get().getRTP().getDisabledWorlds().contains(w.getName())) {
                info.add("&7- &6Disabled: &bTrue");
            } else {
                info.add("&7- &6Disabled: &cFalse");
                if (Main.get().getRTP().overriden.containsKey(w.getName())) {
                    info.add("&7- &6Overriden: &bTrue");
                } else {
                    info.add("&7- &6Overriden: &cFalse");
                    RTPWorld rtpWorld = Main.get().getRTP().DefaultWorld;
                    for (RTPWorld world : Main.get().getRTP().customWorlds.values()) {
                        if (world.getWorld().equals(w.getName())) {
                            rtpWorld = world;
                            break;
                        }
                    }
                    if (rtpWorld == Main.get().getRTP().DefaultWorld) {
                        info.add("&7- &6Custom: &cFalse");
                    } else {
                        info.add("&7- &6Custom: &bTrue");
                    }
                    if (rtpWorld.getUseWorldBorder()) {
                        info.add("&7- &6Using WorldBorder");
                        WorldBorder border = w.getWorldBorder();
                        info.add("&7- &6Center X: &7" + border.getCenter().getBlockX());
                        info.add("&7- &6Center Z: &7" + border.getCenter().getBlockZ());
                        info.add("&7- &6MaxRad: &7" + (border.getSize() / 2));
                    } else {
                        info.add("&7- &6Not Using WorldBorder");
                        info.add("&7- &6Center X: &7" + rtpWorld.getCenterX());
                        info.add("&7- &6Center Z: &7" + rtpWorld.getCenterZ());
                        info.add("&7- &6MaxRad: &7" + rtpWorld.getMaxRad());
                    }
                    info.add("&7- &6MinRad: &7" + rtpWorld.getMinRad());
                }
            }
        }
        info.forEach(str ->
                info.set(info.indexOf(str), Main.get().getMessages().color(str)));
        sender.sendMessage(info.toArray(new String[0]));
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    public boolean hasPermission(CommandSender sender) {
        return Main.get().getPerms().canGetInfo(sender);
    }
}
