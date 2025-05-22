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

    // Custom RowMapper til at mappe resultatrækker til Location objekter,
    // inklusive relateret Zipcode information.
    private RowMapper<Location> locationRowMapper = (rs, rowNum) -> {
        Zipcode zipcode = new Zipcode();
        // Sæt zipcode_id fra location tabellen.
        zipcode.setZipcodeId(rs.getInt("zipcode_id"));
        // Tjek om JOIN'et til zipcode tabellen gav resultat (hvis zipcode_id var gyldigt).
        if (rs.getObject("z_zipcode_id") != null) { // Tjek om join gav resultat
            // Sæt postnummer og bynavn fra den joinerede zipcode tabel.
            zipcode.setZipcode(rs.getString("zip_code"));
            zipcode.setCityName(rs.getString("city_name"));
        } else {
            // Hvis postnummeret ikke blev fundet (LEFT JOIN), sæt standardværdier.
            zipcode.setZipcode("N/A");
            zipcode.setCityName("N/A");
        }
        Location location = new Location();
        // Sæt lokationsspecifikke felter fra location tabellen.
        location.setLocationId(rs.getInt("location_id"));
        location.setZipcodeId(rs.getInt("zipcode_id")); // Dette er FK i location tabellen
        location.setAddress(rs.getString("address"));
        location.setPhone(rs.getString("phone"));
        location.setEmail(rs.getString("email"));
        location.setOpeningHours(rs.getString("opening_hours"));
        // Sæt det opbyggede Zipcode objekt på Location objektet.
        location.setZipcode(zipcode); // Sæt det opbyggede Zipcode objekt
        return location;

    };

    /**
     * Finder alle lokationer i databasen med tilhørende postnummerinformation.
     */
    public List<Location> findAll() {
        String sql =
                "SELECT l.*, z.zipcode_id as z_zipcode_id, z.zip_code, z.city_name " +
                        "FROM location l LEFT JOIN zipcode z ON l.zipcode_id = z.zipcode_id";
        // Udfører forespørgslen og bruger den custom LocationRowMapper.
        return jdbcTemplate.query(sql, locationRowMapper);
    }

    /**
     * Finder en specifik lokation baseret på dens ID med tilhørende postnummerinformation.
     */
    public Location findById(int locationId) {
        String sql =
                "SELECT l.*, z.zipcode_id as z_zipcode_id, z.zip_code, z.city_name " +
                        "FROM location l LEFT JOIN zipcode z ON l.zipcode_id = z.zipcode_id WHERE l.location_id = ?";
        try {
            // Udfører forespørgslen for et enkelt objekt og bruger den custom LocationRowMapper.
            return jdbcTemplate.queryForObject(sql, locationRowMapper, locationId);
        } catch (EmptyResultDataAccessException e) {
            // Håndterer tilfælde hvor lokationen ikke findes.
            System.err.println(
                    "Info: Location with ID " + locationId + " not found."
            );
            return null;
        }
    }
}
