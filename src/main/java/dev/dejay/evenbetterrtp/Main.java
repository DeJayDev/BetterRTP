package dev.dejay.evenbetterrtp;

import dev.dejay.evenbetterrtp.player.RTP;
import dev.dejay.evenbetterrtp.player.commands.Commands;
import dev.dejay.evenbetterrtp.player.events.RTPListeners;
import dev.dejay.evenbetterrtp.references.Econ;
import dev.dejay.evenbetterrtp.references.Permissions;
import dev.dejay.evenbetterrtp.references.file.Files;
import dev.dejay.evenbetterrtp.references.file.Messages;
import dev.dejay.evenbetterrtp.references.settings.Settings;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private Permissions permissions = new Permissions();
    private Messages messages = new Messages(this);
    private Econ econ = new Econ();
    private Commands commands = new Commands(this);
    private RTP rtp = new RTP(this);
    private RTPListeners RTPListeners = new RTPListeners();
    private Files files = new Files();
    private Settings settings = new Settings();

    @Override
    public void onEnable() {
        loadAll();
        RTPListeners.registerEvents(this);
    }

    @Override
    public void onDisable() {
        // nothing.
    }

    public Files getFiles() {
        return files;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            commands.commandExecuted(sender, label, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
            messages.error(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return commands.onTabComplete(sender, args);
    }

    public Permissions getPerms() {
        return permissions;
    }

    public Messages getMessages() {
        return messages;
    }

    public Econ getEcon() {
        return econ;
    }

    public Commands getCommands() {
        return commands;
    }

    public RTP getRTP() {
        return rtp;
    }

    public Settings getSettings() {
        return settings;
    }

    public void reload(CommandSender sender) {
        loadAll();
        messages.getReload(sender);
    }

    //Load
    private void loadAll() {
        files.loadAll();
        settings.load();
        rtp.load();
        commands.load();
        RTPListeners.load();
    }

    public static Main get() {
        return JavaPlugin.getPlugin(Main.class);
    }

}
