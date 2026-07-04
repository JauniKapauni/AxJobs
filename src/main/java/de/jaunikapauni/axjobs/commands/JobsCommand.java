package de.jaunikapauni.axjobs.commands;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class JobsCommand implements CommandExecutor {
    AxJobs reference;
    public JobsCommand(AxJobs reference){
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0){
            return false;
        }
        String sub = args[0].toLowerCase();
        switch (sub){
            case "join": {
                if(args.length < 2){
                    return false;
                }
                String job = args[1].toLowerCase();
                if(!reference.getConfig().contains("jobs." + job)){
                    p.sendMessage(ChatColor.RED + "This job does not exist!");
                    return true;
                }
                UUID uuid = p.getUniqueId();
                if(reference.getPlayerManager().hasJob(uuid, job)){
                    p.sendMessage(ChatColor.RED + "You already have this job!");
                    return true;
                }
                reference.getPlayerManager().addJob(uuid, job);
                p.sendMessage(ChatColor.GREEN + "You joined the job: " + job);
                break;
            }
            case "leave": {
                if(args.length < 2){
                    return false;
                }
                String job = args[1].toLowerCase();
                UUID uuid = p.getUniqueId();
                if(!reference.getPlayerManager().hasJob(uuid, job)){
                    p.sendMessage(ChatColor.RED + "You don't have this job.");
                    return true;
                }
                reference.getPlayerManager().removeJob(uuid, job);
                p.sendMessage(ChatColor.GREEN + "You left the job: " + job);
                break;
            }
            case "list": {
                UUID uuid = p.getUniqueId();
                List<String> jobs = reference.getPlayerManager().getJobs(uuid);
                if(jobs.isEmpty()){
                    p.sendMessage(ChatColor.RED + "You have no job.");
                    return true;
                }
                p.sendMessage("Your jobs:");
                for(String job : jobs){
                    p.sendMessage(job);
                }
                break;
            }
            default:
                return false;
        }
        return true;
    }
}
