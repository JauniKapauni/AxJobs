package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.UUID;

public class BlockBreakListener implements Listener {
    AxJobs reference;
    public BlockBreakListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Material material = e.getBlock().getType();

        List<String> jobs = reference.getPlayerManager().getJobs(uuid);
        if(jobs.isEmpty()){
            return;
        }
        double totalPay = 0;
        for(String job : jobs){
            if(!reference.getJobsConfig().contains(job)){
                continue;
            }
            for(String action : reference.getJobsConfig().getConfigurationSection(job).getKeys(false)){
                String path = job + "." + action + "." + material.name();
                if(reference.getJobsConfig().contains(path)){
                    totalPay += reference.getJobsConfig().getDouble(path);
                }
            }
        }
        if(totalPay > 0){
            reference.getEconomyAPI().deposit(uuid, totalPay);
            p.sendMessage(ChatColor.GREEN + "+" + totalPay);
        }
    }
}
