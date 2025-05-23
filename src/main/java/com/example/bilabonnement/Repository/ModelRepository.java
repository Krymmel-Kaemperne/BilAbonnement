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

    /**
     * Finder en model baseret på dens mærke ID og modelnavn.
     */
    public Model findByBrandIdAndModelName(Integer brandId, String modelName) {
        String sql = "SELECT * FROM model WHERE brand_id = ? AND model_name = ?";
        List<Model> models = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Model.class), brandId, modelName);
        return models.isEmpty() ? null : models.get(0);
    }

    /**
     * Opretter en ny model i databasen og sætter den genererede ID.
     */
    public Model create(Model model) {
        String sql = "INSERT INTO model (model_name, brand_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // Udfører INSERT-forespørgslen og henter den automatisk genererede nøgle (ID).
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, model.getModelName());
            ps.setInt(2, model.getBrandId());
            return ps;
        }, keyHolder);
        // Sætter den genererede nøgle (ID) på modelobjektet.
        Number generatedKey = keyHolder.getKey();
        if (generatedKey != null) {
            model.setModelId(generatedKey.intValue());
        }
        return model;
    }

    /**
     * Opdaterer en eksisterende model i databasen.
     */
    public Model update(Model model) {
        String sql = "UPDATE model SET model_name = ?, brand_id = ? WHERE model_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                model.getModelName(),
                model.getBrandId(),
                model.getModelId());
        return rowsAffected > 0 ? model : null;
    }

    /**
     * Finder alle modeller i databasen med tilhørende mærkenavn.
     */
    public List<Model> findAllModels() {
        String sql = "SELECT m.model_id, m.model_name, m.brand_id, b.brand_name " +
                "FROM model m " +
                "JOIN brand b ON m.brand_id = b.brand_id";
        // Udfører forespørgslen og bruger en custom RowMapper til at mappe resultaterne,
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Model model = new Model();
            model.setModelId(rs.getInt("model_id"));
            model.setModelName(rs.getString("model_name"));
            model.setBrandId(rs.getInt("brand_id"));
            model.setBrandName(rs.getString("brand_name"));
            return model;
        });
    }
}
