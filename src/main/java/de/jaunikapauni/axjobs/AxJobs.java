package de.jaunikapauni.axjobs;

import de.jaunikapauni.axeconomy.AxEconomy;
import de.jaunikapauni.axeconomy.api.EconomyAPI;
import de.jaunikapauni.axjobs.commands.JobsCommand;
import de.jaunikapauni.axjobs.commands.JobsTabCompleter;
import de.jaunikapauni.axjobs.listener.BlockBreakListener;
import de.jaunikapauni.axjobs.manager.DatabaseManager;
import de.jaunikapauni.axjobs.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class AxJobs extends JavaPlugin {
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    PlayerManager playerManager;
    public PlayerManager getPlayerManager(){
        return playerManager;
    }
    EconomyAPI economyAPI;
    public EconomyAPI getEconomyAPI(){
        return economyAPI;
    }
    File jobsFile;
    FileConfiguration jobsConfig;
    public FileConfiguration getJobsConfig(){
        return jobsConfig;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        saveResource("jobs.yml", false);
        jobsFile = new File(getDataFolder(), "jobs.yml");
        jobsConfig = YamlConfiguration.loadConfiguration(jobsFile);
        try{
            playerManager = new PlayerManager(this);
            databaseManager = new DatabaseManager(this);
            if(databaseManager.initDatabaseTable1() == false){
                getLogger().severe("Error creating table");
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        getCommand("jobs").setExecutor(new JobsCommand(this));
        getCommand("jobs").setTabCompleter(new JobsTabCompleter(this));
        if(Bukkit.getPluginManager().getPlugin("AxEconomy") != null){
            AxEconomy axEconomy = (AxEconomy) Bukkit.getPluginManager().getPlugin("AxEconomy");
            if(axEconomy == null){
                throw new IllegalStateException("AxEconomy is missing!");
            }
            economyAPI = axEconomy.getEconomyAPI();
        }
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
    }
}
