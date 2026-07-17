package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    AxJobs reference;
    public PlayerJoinListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        reference.getPlayerManager().loadPlayer(e.getPlayer().getUniqueId());
    }
}
