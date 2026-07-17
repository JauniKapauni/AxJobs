package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.UUID;

public class EntityDeathListener implements Listener {

    AxJobs reference;
    public EntityDeathListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        Player p = e.getEntity().getKiller();
        if(p == null){
            return;
        }
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
            String path = job + "." + "kill" + "." + entity.getType().name();
            if (reference.getJobsConfig().contains(path)) {
                totalPay += reference.getJobsConfig().getDouble(path);
            }
        }
        if (totalPay > 0) {
            reference.getEconomyAPI().deposit(uuid, totalPay);
            p.sendMessage(ChatColor.GREEN + "+" + totalPay);
        }
    }
}
