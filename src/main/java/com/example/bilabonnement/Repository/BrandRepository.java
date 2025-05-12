// I en ny fil, f.eks. BrandRepository.java
package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BrandRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Brand> findAllBrands() {
        String sql = "SELECT brand_id, brand_name FROM brand ORDER BY brand_name";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Brand.class));
    }
}
