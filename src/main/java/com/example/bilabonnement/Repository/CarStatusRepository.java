package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.CarStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarStatusRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finder alle bilstatusser i databasen.
     */
    public List<CarStatus> findAllStatuses() {
        String sql = "SELECT car_status_id, status_name FROM carstatus";
        // Udfører forespørgslen og bruger en custom RowMapper til at mappe resultaterne.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CarStatus status = new CarStatus();
            status.setCarStatusId(rs.getInt("car_status_id"));
            status.setStatusName(rs.getString("status_name"));
            return status;
        });
    }

    /**
     * Finder en specifik bilstatus baseret på dens ID.
     */
    public CarStatus findCarStatusById(int carStatusId) {
        String sql = "SELECT status_name FROM carstatus WHERE car_status_id = ?";
        try {
            // Udfører forespørgslen for et enkelt objekt og bruger BeanPropertyRowMapper til at mappe.
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CarStatus.class), carStatusId);
        } catch (EmptyResultDataAccessException e) {
            // Håndterer tilfælde hvor ingen bilstatus med det givne ID blev fundet.
            System.err.println("Ingen carStatus fundet med ID: " + carStatusId);
            return null;
        }
    }
}
