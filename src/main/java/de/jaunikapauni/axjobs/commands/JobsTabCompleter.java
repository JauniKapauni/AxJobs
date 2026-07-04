package de.jaunikapauni.axjobs.commands;

import de.jaunikapauni.axjobs.AxJobs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JobsTabCompleter implements TabCompleter {
    AxJobs reference;
    public JobsTabCompleter(AxJobs reference){
        this.reference = reference;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return List.of("join", "leave", "list");
        }
        if(args.length == 2 && (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave"))){
            if(reference.getJobsConfig().getKeys(false).isEmpty()){
                return List.of();
            }
            return new ArrayList<>(reference.getJobsConfig().getKeys(false));
        }
        return List.of();
    }
}
