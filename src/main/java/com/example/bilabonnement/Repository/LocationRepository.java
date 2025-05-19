package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Location;
import com.example.bilabonnement.Model.Zipcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Location> locationRowMapper = (rs, rowNum) -> {
        Zipcode zipcode = new Zipcode();
        zipcode.setZipcodeId(rs.getInt("zipcode_id"));
        if (rs.getObject("z_zipcode_id") != null) { // Tjek om join gav resultat
            zipcode.setZipcode(rs.getString("zip_code"));
            zipcode.setCityName(rs.getString("city_name"));
        } else {
            zipcode.setZipcode("N/A");
            zipcode.setCityName("N/A");
        }
        Location location = new Location();
        location.setLocationId(rs.getInt("location_id"));
        location.setZipcodeId(rs.getInt("zipcode_id")); // Dette er FK i location tabellen
        location.setAddress(rs.getString("address"));
        location.setPhone(rs.getString("phone"));
        location.setEmail(rs.getString("email"));
        location.setOpeningHours(rs.getString("opening_hours"));
        location.setZipcode(zipcode); // Sæt det opbyggede Zipcode objekt
        return location;

    };

    public List<Location> findAll() {
        String sql =
                "SELECT l.*, z.zipcode_id as z_zipcode_id, z.zip_code, z.city_name " +
                        "FROM location l LEFT JOIN zipcode z ON l.zipcode_id = z.zipcode_id";
        return jdbcTemplate.query(sql, locationRowMapper);
    }

    public Location findById(int locationId) {
        String sql =
                "SELECT l.*, z.zipcode_id as z_zipcode_id, z.zip_code, z.city_name " +
                        "FROM location l LEFT JOIN zipcode z ON l.zipcode_id = z.zipcode_id WHERE l.location_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, locationRowMapper, locationId);
        } catch (EmptyResultDataAccessException e) {
            System.err.println(
                    "Info: Location with ID " + locationId + " not found."
            );
            return null;
        }
    }
}
