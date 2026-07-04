package de.jaunikapauni.axjobs;

import de.jaunikapauni.axjobs.manager.DatabaseManager;import org.bukkit.Bukkit;import org.bukkit.plugin.java.JavaPlugin;

public final class AxJobs extends JavaPlugin {
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        try{
            databaseManager = new DatabaseManager(this);
            if(databaseManager.initDatabaseTable1() == false){
                getLogger().severe("Error creating table");
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
    }
}
