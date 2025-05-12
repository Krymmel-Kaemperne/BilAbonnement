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
    @Autowired
    private JdbcClient jdbcClient;

    // Gemmer et nyt postnummer. Returnerer det gemte postnummer med id
    public Zipcode save(Zipcode zipcode) {
        // Først tjek om postnummeret eksisterer baseret på zip_code streng
        Zipcode existingZipcode = findByZipcodeString(zipcode.getZipcode());
        if(existingZipcode != null) {
            return existingZipcode;
        }

        // Hvis ikke, opret nyt
        String sql = "INSERT INTO zipcode (zip_code, city_name) VALUES (?, ?)";
        // KeyHolder (i Spring JDBC): Er et værktøj til at fange og hente det auto-genererede
        // ID tilbage til din Java-kode i samme ombæring som INSERT-operationen,
        // så du kan opdatere dit Java-objekt med det korrekte ID.
        KeyHolder keyholder = new GeneratedKeyHolder();

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

    // Find et postnummer baseret på dets Id
    public Zipcode findById(int id) {
        String sql = "SELECT zipcode_id, zip_code AS zipcode, city_name FROM zipcode WHERE zipcode_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Zipcode.class), id);
        } catch(EmptyResultDataAccessException e ) {
            return null;
        }
    }

    // Finder postnummer baseret på postnummer streng
    public Zipcode findByZipcodeString(String zipcode) {
        String sql = "SELECT zipcode_id, zipcode AS zipcode, city_name FROM zipcode WHERE zip_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Zipcode.class), zipcode);
        } catch (EmptyResultDataAccessException e) {
            return null; // returnerer null hvis intet fundet
        }
    }

    // Hent alle zipcodes
    public List<Zipcode> findAll() {
        String sql = "SELECT zipcode_id, zip_code AS zipcode, city_name FROM zipcode ORDER BY zip_code";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Zipcode.class));
    }
}
