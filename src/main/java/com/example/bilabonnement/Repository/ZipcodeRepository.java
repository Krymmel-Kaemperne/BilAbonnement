package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Zipcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ZipcodeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Gemmer et nyt postnummer i databasen. Hvis et postnummer med samme kode allerede
     * eksisterer, returneres det eksisterende postnummer.
     */
    public Zipcode save(Zipcode zipcode) {
        Zipcode existingZipcode = findByZipcodeString(zipcode.getZipcode());
        if(existingZipcode != null) {
            return existingZipcode;
        }

        String sql = "INSERT INTO zipcode (zip_code, city_name) VALUES (?, ?)";
        KeyHolder keyholder = new GeneratedKeyHolder();

        // Udfører INSERT-forespørgslen og henter den genererede nøgle.
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, zipcode.getZipcode());
            ps.setString(2, zipcode.getCityName());
            return ps;
        }, keyholder);

        // Sæt det genereret id på objeketet
        if (keyholder.getKey() != null) {
            zipcode.setZipcodeId(keyholder.getKey().intValue());
        } else {
            throw new RuntimeException("Could not retrieve generated ID for zipcode");
        }
        return zipcode;
    }

    /**
     * Finder et postnummer baseret på dets ID.
     */
    public Zipcode findById(int id) {
        String sql = "SELECT zipcode_id, zip_code AS zipcode, city_name FROM zipcode WHERE zipcode_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Zipcode.class), id);
        } catch(EmptyResultDataAccessException e ) {
            return null;
        }
    }

    /**
     * Finder et postnummer baseret på postnummer strengen (f.eks. "4000").
     */
    public Zipcode findByZipcodeString(String zipcode) {
        String sql = "SELECT zipcode_id, zip_code AS zipcode, city_name FROM zipcode WHERE zip_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Zipcode.class), zipcode);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Finder alle postnumre i databasen.
     */
    public List<Zipcode> findAll() {
        // SQL forespørgsel til at vælge alle postnumre og sortere dem.
        String sql = "SELECT zipcode_id, zip_code AS zipcode, city_name FROM zipcode ORDER BY zip_code";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Zipcode.class));
    }
}
