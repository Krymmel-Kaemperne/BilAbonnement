package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.CarStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarStatusRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CarStatus> findAllStatuses() {
        String sql = "SELECT car_status_id, status_name FROM carstatus";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CarStatus status = new CarStatus();
            status.setCarStatusId(rs.getInt("car_status_id"));
            status.setStatusName(rs.getString("status_name"));
            return status;
        });
    }
} 