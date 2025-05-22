package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.TransmissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransmissionTypeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finder alle transmissionstyper i databasen.
     */
    public List<TransmissionType> findAllTransmissionTypes() {
        String sql = "SELECT transmission_type_id, transmission_type_name FROM transmissiontype";
        // Udfører forespørgslen og bruger en custom RowMapper til at mappe resultaterne.
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TransmissionType type = new TransmissionType();
            type.setTransmissionTypeId(rs.getInt("transmission_type_id"));
            type.setTransmissionTypeName(rs.getString("transmission_type_name"));
            return type;
        });
    }
}
