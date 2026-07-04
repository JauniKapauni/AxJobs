package de.jaunikapauni.axjobs;

import de.jaunikapauni.axjobs.manager.DatabaseManager;import de.jaunikapauni.axjobs.manager.PlayerManager;import org.bukkit.Bukkit;import org.bukkit.plugin.java.JavaPlugin;

public final class AxJobs extends JavaPlugin {
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    PlayerManager playerManager;
    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
    }
}
