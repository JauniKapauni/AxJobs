package de.jaunikapauni.axjobs.manager;

import de.jaunikapauni.axjobs.AxJobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayerManager {
    AxJobs reference;
    public PlayerManager(AxJobs reference){
        this.reference = reference;
    }

    Map<UUID, List<String>> cache = new HashMap<>();

    public void loadPlayer(UUID uuid){
        List<String> jobs = new ArrayList<>();
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT job FROM jobs WHERE uuid = ?")){
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    jobs.add(rs.getString("job"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cache.put(uuid, jobs);
    }

    public boolean hasJob(UUID uuid, String job){
        return cache.getOrDefault(uuid, new ArrayList<>()).contains(job);
    }

    public void addJob(UUID uuid, String job){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO jobs(uuid, job) VALUES (?, ?)")){
                ps.setString(1, uuid.toString());
                ps.setString(2, job);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(!cache.containsKey(uuid)){
            cache.put(uuid, new ArrayList<>());
        }
        cache.get(uuid).add(job);
    }

    public void removeJob(UUID uuid, String job){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM jobs WHERE uuid = ? AND job = ?")){
                ps.setString(1, uuid.toString());
                ps.setString(2, job);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cache.getOrDefault(uuid, new ArrayList<>()).remove(job);
    }

    public List<String> getJobs(UUID uuid){
        return cache.getOrDefault(uuid, new ArrayList<>());
    }

    public void unloadPlayer(UUID uuid){
        cache.remove(uuid);
    }
}
