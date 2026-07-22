package de.jaunikapauni.axjobs.commands;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobsTabCompleter implements TabCompleter {

    AxJobs reference;
    public JobsTabCompleter(AxJobs reference){
        this.reference = reference;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            return List.of();
        }
        Player p = (Player) sender;
        if(args.length == 1){
            return List.of("join", "leave", "list");
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("join")){
            return getJobsPlayerDoesntHave(p);
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("leave")){
            return getJobsPlayerHas(p);
        }
        return List.of();
    }

    private List<String> getJobsPlayerHas(Player p){
        List<String> playerJobs = reference.getPlayerManager().getJobs(p.getUniqueId());
        return new ArrayList<>(playerJobs);
    }

    private List<String> getJobsPlayerDoesntHave(Player p){
        UUID uuid = p.getUniqueId();
        List<String> allJobs = reference.getJobs();
        List<String> playerJobs = reference.getPlayerManager().getJobs(uuid);
        List<String> available = new ArrayList<>();
        for(String job : allJobs){
            if(!playerJobs.contains(job)){
                available.add(job);
            }
        }
        return available;
    }
}
