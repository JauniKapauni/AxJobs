package de.jaunikapauni.axjobs.listener;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class CraftItemListener implements Listener {

    AxJobs reference;
    public CraftItemListener(AxJobs reference){
        this.reference = reference;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        ItemStack itemStack = e.getCurrentItem();
        if(itemStack == null){
            return;
        }
        Material material = itemStack.getType();
        List<String> jobs = reference.getPlayerManager().getJobs(uuid);
        if (jobs.isEmpty()) {
            return;
        }
        double totalPay = 0;
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
