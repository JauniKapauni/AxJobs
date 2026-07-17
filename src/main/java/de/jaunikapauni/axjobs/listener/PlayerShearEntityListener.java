package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.List;
import java.util.UUID;

public class PlayerShearEntityListener implements Listener {

    AxJobs reference;
    public PlayerShearEntityListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e){
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Entity entity = e.getEntity();
        List<String> jobs = reference.getPlayerManager().getJobs(uuid);
        if (jobs.isEmpty()) {
            return;
        }
        double totalPay = 0;
        for (String job : jobs) {
            if (!reference.getJobsConfig().contains(job)) {
                continue;
            }
            String path = job + "." + "shear" + "." + entity.getType().name();
            if (reference.getJobsConfig().contains(path)) {
                totalPay += reference.getJobsConfig().getDouble(path);
            }
        }
        if (totalPay > 0) {
            reference.getPlayerManager().addReward(uuid, totalPay);
            p.sendMessage(ChatColor.GREEN + "+" + totalPay);
        }
    }
}
