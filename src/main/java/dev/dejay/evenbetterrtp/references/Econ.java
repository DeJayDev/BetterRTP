package dev.dejay.evenbetterrtp.references;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Econ {

    private Economy economy;
    private boolean checked = false;

    public boolean charge(Player player, int price) {
        check();
        if (economy != null) {
            if (price != 0) {
                if (!Main.get().getPerms().canBypassEconomy(player)) {
                    return economy.withdrawPlayer(player, price).transactionSuccess();
                }
                return true;
            }
        }
        return true;
    }

    public void unCharge(Player p, int price) {
        if (economy != null) {
            if (price != 0) {
                economy.depositPlayer(p, price);
            }
        }
    }

    private void check() {
        if (!checked) {
            registerEconomy();
        }
    }

    private void registerEconomy() {
        try {
            if (Main.get().getFiles().getType(FileBasics.FileType.ECO).getBoolean("Economy.Enabled"))
                if (Main.get().getServer().getPluginManager().isPluginEnabled("Vault")) {
                    RegisteredServiceProvider<Economy> rsp = Main.get().getServer().getServicesManager().getRegistration(Economy.class);
                    economy = rsp.getProvider();
                }
        } catch (NullPointerException exception) {
            //
        }
        checked = true;
    }
}
