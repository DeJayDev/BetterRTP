package dev.dejay.evenbetterrtp.references.settings;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import org.bukkit.Bukkit;

public class SoftDepends {

    private boolean useWorldGuard = false, useGriefPrevention = false, useSavageFactions = false;

    public boolean usingWorldGuard() {
        return useWorldGuard;
    }

    public boolean usingGriefPrevention() {
        return useGriefPrevention;
    }

    void load() {
        FileBasics.FileType config = Main.get().getFiles().getType(FileBasics.FileType.CONFIG);
        if (config.getBoolean("Settings.RespectWorldGuard")) {
            loadWorldGuard();
        } else if (useWorldGuard) {
            useWorldGuard = false;
        }

        if (config.getBoolean("Settings.RespectGriefPrevention")) {
            loadGriefPrevention();
        } else if (useGriefPrevention) {
            useGriefPrevention = false;
        }

        if (config.getBoolean("Settings.RespectSavageFactions")) {
            loadSavageFactions();
        } else if (useSavageFactions) {
            useSavageFactions = false;
        }
    }

    private void loadWorldGuard() {
        useWorldGuard = Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
    }

    private void loadGriefPrevention() {
        useGriefPrevention = Bukkit.getPluginManager().isPluginEnabled("GriefPrevention");
    }

    private void loadSavageFactions() {
        useSavageFactions = Bukkit.getPluginManager().isPluginEnabled("Factions");
    }
}
