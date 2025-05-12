package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ModelRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ModelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Model findByBrandIdAndModelName(Integer brandId, String modelName) {
        String sql = "SELECT * FROM model WHERE brand_id = ? AND model_name = ?";
        List<Model> models = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Model.class), brandId, modelName);
        return models.isEmpty() ? null : models.get(0);
    }

    public Model create(Model model) {
        String sql = "INSERT INTO model (model_name, brand_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, model.getModelName());
            ps.setInt(2, model.getBrandId());
            return ps;
        }, keyHolder);
        Number generatedKey = keyHolder.getKey();
        if (generatedKey != null) {
            model.setModelId(generatedKey.intValue());
        }
        return model;
    }
} 