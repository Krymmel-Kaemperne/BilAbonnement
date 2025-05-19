package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Damage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

// Repository til databaseadgang for skader
@Repository
public class DamageRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Damage> rowMapper = new BeanPropertyRowMapper<>(Damage.class);

    public Damage create(Damage damage) {
        String sql = "INSERT INTO damage (condition_report_id, damage_description, damage_price) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, damage.getConditionReportId());
            ps.setString(2, damage.getDamageDescription());
            ps.setBigDecimal(3, damage.getDamagePrice());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) damage.setDamageId(key.intValue());
        return damage;
    }

    public Damage findById(int id) {
        String sql = "SELECT * FROM damage WHERE damage_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Damage> findByConditionReportId(int conditionReportId) {
        String sql = "SELECT * FROM damage WHERE condition_report_id = ?";
        return jdbcTemplate.query(sql, rowMapper, conditionReportId);
    }

    public List<Damage> findAll() {
        String sql = "SELECT * FROM damage";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int update(Damage damage) {
        String sql = "UPDATE damage SET damage_description = ?, damage_price = ? WHERE damage_id = ?";
        return jdbcTemplate.update(sql,
                damage.getDamageDescription(),
                damage.getDamagePrice(),
                damage.getDamageId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM damage WHERE damage_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int countAllDamages() {
        String sql = "SELECT COUNT(*) FROM damage";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
} 