package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.FuelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FuelTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finder alle brændstoftyper i databasen.
     */
    public List<FuelType> findAllFuelTypes() {
        String sql = "SELECT fuel_type_id, fuel_type_name FROM fueltype ORDER BY fuel_type_name";
        // Udfører forespørgslen og mapper resultatrækkerne til FuelType-objekter.
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FuelType.class));
    }
}
