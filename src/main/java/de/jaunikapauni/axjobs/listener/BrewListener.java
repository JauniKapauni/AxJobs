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

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BrewListener implements Listener {

    AxJobs reference;
    public BrewListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBrew(BrewEvent e){
        BrewingStand brewingStand = (BrewingStand) e.getBlock().getState();
        Collection<Player> nearbyPlayers = e.getBlock().getLocation().getNearbyPlayers(5);
        if(nearbyPlayers.isEmpty()){
            return;
        }
        for(Player p : nearbyPlayers){
            UUID uuid = p.getUniqueId();
            List<String> jobs = reference.getPlayerManager().getJobs(uuid);
            if(jobs.isEmpty()){
                continue;
            }
            double totalPay = 0;
            for(ItemStack itemStack : brewingStand.getInventory().getContents()){
                if(itemStack == null){
                    continue;
                }
                Material material = itemStack.getType();
                for(String job : jobs){
                    if(!reference.getJobsConfig().contains(job)){
                        continue;
                    }
                    String path = job + ".craft." + material.name();
                    if(reference.getJobsConfig().contains(path)){
                        totalPay += reference.getJobsConfig().getDouble(path);
                    }
                }
            }
            if(totalPay > 0){
                reference.getPlayerManager().addReward(uuid, totalPay);
                p.sendActionBar(ChatColor.GREEN + "+" + totalPay);
            }
        }
    }
}
