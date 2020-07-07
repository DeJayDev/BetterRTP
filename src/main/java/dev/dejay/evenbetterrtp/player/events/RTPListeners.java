package dev.dejay.evenbetterrtp.player.events;

import dev.dejay.evenbetterrtp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RTPListeners implements Listener {

    private LeaveListener leaveListener = new LeaveListener();
    private Interact interact = new Interact();

    public void registerEvents(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void load() {
        interact.load();
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        leaveListener.event(e);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        interact.playerInteractEvent(e);
    }

    @EventHandler
    private void onSignChange(SignChangeEvent e) {
        interact.createSign(e);
    }

}