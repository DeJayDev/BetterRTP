package dev.dejay.evenbetterrtp.player.events;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.player.commands.Commands;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import dev.dejay.evenbetterrtp.references.file.FileBasics.FileType;
import java.util.Arrays;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class Interact {

    private boolean enabled;
    private String title, coloredTitle;

    public void load() {
        String pre = "Settings.";
        FileType file = Main.get().getFiles().getType(FileBasics.FileType.SIGNS);
        enabled = file.getBoolean(pre + "Enabled");
        title = file.getString(pre + "Title");
        coloredTitle = Main.get().getMessages().color(title);
    }

    public void playerInteractEvent(PlayerInteractEvent event) {
        if(!enabled) {
            return;
        }

        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && isSign(event.getClickedBlock())) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(0).equals(coloredTitle)) {
                String command = sign.getLine(1).split(" ")[0];
                if (cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("") || cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("rtp")) {
                    action(event.getPlayer(), null);
                    return;
                } else
                    for (String cmd : Commands.cmds)
                        if (command.equalsIgnoreCase(cmd)) {
                            action(event.getPlayer(), cmd(sign.getLines()).split(" "));
                            return;
                        }
                event.getPlayer().sendMessage(Main.get().getMessages().colorPre("&cError! &7Command &a"
                    + Arrays.toString(cmd(sign.getLines()).split(" ")) + "&7 does not exist! Defaulting command to /rtp!"));
            }
        }
    }

    public void createSign(SignChangeEvent event) {
        if(!enabled) {
            return;
        }

        if (Main.get().getPerms().canCreateSign(event.getPlayer())) {
            String line = event.getLine(0);
            if (line != null && (line.equalsIgnoreCase(title) ||
                    line.equalsIgnoreCase("[RTP]"))) {
                event.setLine(0, coloredTitle != null ? coloredTitle : "[RTP]");
                Main.get().getMessages().getSignCreated(event.getPlayer(), cmd(event.getLines()));
            }
        }
    }

    private void action(Player p, String[] line) {
        Main.get().getCommands().commandExecuted(p, "rtp", line);
    }

    private String cmd(String[] signArray) {
        String actions = "";
        for (int i = 1; i < signArray.length; i++) {
            String line = signArray[i];
            if (line != null && !line.equals(""))
                if (actions.equals(""))
                    actions = line;
                else
                    actions = actions.concat(" " + line);
        }
        return actions;
    }

    private boolean isSign(Block block) {
        return block.getState() instanceof Sign;
    }
}
