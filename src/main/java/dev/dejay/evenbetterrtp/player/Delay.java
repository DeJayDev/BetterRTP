package dev.dejay.evenbetterrtp.player;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.worlds.PlayerWorld;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

class Delay implements Listener {
    private int run;
    private PlayerWorld playerWorld;
    private Main pl = Main.get();

    public Delay(CommandSender sender, PlayerWorld playerWorld, int delay, boolean cancelOnMove) {
        this.playerWorld = playerWorld;
        delay(sender, delay, cancelOnMove);
    }

    private void delay(CommandSender sender, int delay, boolean cancelOnMove) {
        Main plugin = Main.get();
        if (sender.equals(playerWorld.getPlayer()) && delay != 0 && pl.getMessages().getTitleDelayChat())
            pl.getMessages().getDelay(sender, String.valueOf(delay));
        if (pl.getMessages().getSoundsEnabled()) {
            Sound sound = pl.getMessages().getSoundsDelay();
            if (sound != null)
                playerWorld.getPlayer().playSound(playerWorld.getPlayer().getLocation(), sound, 1F, 1F);
        }
        if (pl.getMessages().getTitleEnabled()) {
            String title = pl.getMessages().getTitleDelay(playerWorld.getPlayer().getName(), String.valueOf(delay));
            String subTitle = pl.getMessages().getSubTitleDelay(playerWorld.getPlayer().getName(), String.valueOf(delay));
            playerWorld.getPlayer().sendTitle(title, subTitle);
            // int fadeIn = text.getFadeIn();
            // int stay = text.getStay();
            // int fadeOut = text.getFadeOut();
            // player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
            // pWorld.getPlayer().sendTitle(title, subTitle);
        }
        run = Bukkit.getScheduler().scheduleSyncDelayedTask(pl, run(sender, this), delay * 2 * 10);
        if (cancelOnMove) {
            Bukkit.getPluginManager().registerEvents(this, Main.get());
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().equals(playerWorld.getPlayer())) {
            Bukkit.getScheduler().cancelTask(run);
            if (!Bukkit.getScheduler().isCurrentlyRunning(run)) {
                HandlerList.unregisterAll(this);
                pl.getMessages().getMoved(playerWorld.getPlayer());
                pl.getEcon().unCharge(playerWorld.getPlayer(), playerWorld.getPrice());
                pl.getCommands().cooldowns.remove(playerWorld.getPlayer().getUniqueId());
                pl.getCommands().rtping.put(playerWorld.getPlayer().getUniqueId(), false);
            }
        }
    }

    private Runnable run(final CommandSender sender, final Delay cls) {
        return () -> {
                HandlerList.unregisterAll(cls);
                if (pl.getCommands().rtping.containsKey(playerWorld.getPlayer().getUniqueId())) {
                    try {
                        pl.getRTP().tp(sender, playerWorld);
                    } catch (NullPointerException e) {
                        if (playerWorld.getPrice() > 0)
                            pl.getEcon().unCharge(playerWorld.getPlayer(), playerWorld.getPrice());
                    }
                    pl.getCommands().rtping.put(playerWorld.getPlayer().getUniqueId(), false);
                } else if (playerWorld.getPrice() > 0) {
                    pl.getEcon().unCharge(playerWorld.getPlayer(), playerWorld.getPrice());
                }
                Bukkit.getScheduler().cancelTask(run);
        };
    }
}
