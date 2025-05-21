package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.ConditionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;
import java.util.List;

// Repository til databaseadgang for tilstandsrapporter
@Repository
public class ConditionReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper<ConditionReport> rowMapper = new BeanPropertyRowMapper<>(ConditionReport.class);

    public ConditionReport create(ConditionReport report) {
        String sql = "INSERT INTO condition_report (rental_agreement_id, condition_notes, report_date, total_price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, report.getRentalAgreementId());
            ps.setString(2, report.getConditionNotes());
            ps.setDate(3, Date.valueOf(report.getReportDate()));
            ps.setBigDecimal(4, report.getTotalPrice());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) report.setConditionReportId(key.intValue());
        return report;
    }

    public ConditionReport findById(int id) {
        String sql = "SELECT * FROM condition_report WHERE condition_report_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<ConditionReport> findByRentalAgreementId(int rentalAgreementId) {
        String sql = "SELECT * FROM condition_report WHERE rental_agreement_id = ?";
        return jdbcTemplate.query(sql, rowMapper, rentalAgreementId);
    }

    public List<ConditionReport> findAll() {
        String sql = "SELECT * FROM condition_report";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int update(ConditionReport report) {
        String sql = "UPDATE condition_report SET condition_notes = ?, report_date = ?, total_price = ? WHERE condition_report_id = ?";
        return jdbcTemplate.update(sql,
                report.getConditionNotes(),
                Date.valueOf(report.getReportDate()),
                report.getTotalPrice(),
                report.getConditionReportId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM condition_report WHERE condition_report_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int updateTotalDamagePrice(int conditionReportId, BigDecimal totalDamagePrice) {
        String sql = "UPDATE condition_report SET total_price = ? WHERE condition_report_id = ?";
        return jdbcTemplate.update(sql, totalDamagePrice, conditionReportId);
    }


} 