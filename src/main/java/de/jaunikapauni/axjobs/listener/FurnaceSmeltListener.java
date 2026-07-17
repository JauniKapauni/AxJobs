package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class FurnaceSmeltListener implements Listener {

    AxJobs reference;
    public FurnaceSmeltListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent e){
        Player p = (Player) e.getBlock().getLocation().getNearbyPlayers(5);
        UUID uuid = p.getUniqueId();
        ItemStack result = e.getResult();
        Material material = result.getType();
        List<String> jobs = reference.getPlayerManager().getJobs(uuid);
        if (jobs.isEmpty()) {
            return;
        }
        double totalPay = 0;
        for (String job : jobs) {
            if (!reference.getJobsConfig().contains(job)) {
                continue;
            }
            String path = job + "." + "melt" + "." + material.name();
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
