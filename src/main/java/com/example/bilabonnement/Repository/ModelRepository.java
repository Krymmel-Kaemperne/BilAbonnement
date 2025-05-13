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

    // *** NY METODE TIL AT HENTE ALLE MODELLER ***
    public List<Model> findAllModels() {
        String sql = "SELECT m.model_id, m.model_name, m.brand_id, b.brand_name " +
                "FROM model m " +
                "JOIN brand b ON m.brand_id = b.brand_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Model model = new Model();
            model.setModelId(rs.getInt("model_id"));
            model.setModelName(rs.getString("model_name"));
            model.setBrandId(rs.getInt("brand_id"));
            model.setBrandName(rs.getString("brand_name")); // <-- Set this
            return model;
        });
    }
} 