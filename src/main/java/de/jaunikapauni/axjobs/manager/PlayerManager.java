package de.jaunikapauni.axjobs.manager;

import de.jaunikapauni.axjobs.AxJobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    AxJobs reference;
    public PlayerManager(AxJobs reference){
        this.reference = reference;
    }

    public boolean hasJob(UUID uuid, String job){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM jobs WHERE uuid = ? AND job = ?")){
                ps.setString(1, uuid.toString());
                ps.setString(2, job);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return  true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
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
    }

    public List<String> getJobs(UUID uuid){
        List<String> jobs = new ArrayList<>();
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT job FROM jobs WHERE uuid = ?")){
                ps.setString(1, uuid.toString());
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        jobs.add(rs.getString("job"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jobs;
    }
}
