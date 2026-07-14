package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class BrewListener implements Listener {

    AxJobs reference;
    public BrewListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBrew(BrewEvent e){
        Player p = (Player) e.getBlock().getLocation().getNearbyPlayers(5);
        UUID uuid = p.getUniqueId();
        BrewingStand brewingStand = (BrewingStand) e.getBlock().getState();
        List<String> jobs = reference.getPlayerManager().getJobs(uuid);
        if (jobs.isEmpty()) {
            return;
        }
        double totalPay = 0;
        for(ItemStack itemStack : brewingStand.getInventory().getContents()){
            if(itemStack == null){
                continue;
            }
            Material material = itemStack.getType();
            for (String job : jobs) {
                if (!reference.getJobsConfig().contains(job)) {
                    continue;
                }
                String path = job + "." + "craft" + "." + material.name();
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
}
