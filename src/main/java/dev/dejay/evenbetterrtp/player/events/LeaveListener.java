package dev.dejay.evenbetterrtp.player.events;

import dev.dejay.evenbetterrtp.Main;
import org.bukkit.event.player.PlayerQuitEvent;

class LeaveListener {

    public void event(PlayerQuitEvent e) {
        Main.get().getCommands().rtping.remove(e.getPlayer().getUniqueId());
    }

}
