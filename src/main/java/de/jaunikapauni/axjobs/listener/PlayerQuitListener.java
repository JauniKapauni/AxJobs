package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    AxJobs reference;
    public PlayerQuitListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        reference.getPlayerManager().unloadPlayer(e.getPlayer().getUniqueId());
    }
}
