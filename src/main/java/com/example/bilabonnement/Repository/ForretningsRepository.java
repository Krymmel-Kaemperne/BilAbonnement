package com.example.bilabonnement.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public class ForretningsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countActiveRentals() {
        // Tæller antallet af aktive lejeaftaler baseret på dags dato.
        String sql = "SELECT COUNT(*) FROM rental_agreement WHERE CURDATE() BETWEEN start_date AND end_date";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    public BigDecimal sumCurrentRevenue() {
        // Summerer den månedlige pris for aktive lejeaftaler for at få aktuel
        // omsætning.
        String sql = "SELECT IFNULL(SUM(monthly_price),0) FROM rental_agreement WHERE CURDATE() BETWEEN start_date AND end_date";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal sumTotalRevenue() {
        // Beregner den totale forventede omsætning fra alle lejeaftaler.
        String sql = "SELECT IFNULL(SUM(monthly_price * (TIMESTAMPDIFF(MONTH, start_date, end_date))),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal avgRentalIncome() {
        // Beregner den gennemsnitlige månedlige indkomst pr. lejeaftale.
        String sql = "SELECT IFNULL(AVG(monthly_price),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    // --- Omsætning ---
    public BigDecimal sumCurrentMonthRevenue() {
        // Summerer omsætningen fra lejeaftaler, der er aktive i den nuværende måned.
        String sql = "SELECT IFNULL(SUM(monthly_price),0) FROM rental_agreement WHERE (YEAR(start_date) = YEAR(CURDATE()) AND MONTH(start_date) = MONTH(CURDATE())) OR (YEAR(end_date) = YEAR(CURDATE()) AND MONTH(end_date) = MONTH(CURDATE())) OR (start_date < CURDATE() AND end_date > CURDATE())";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal avgRevenuePerCustomer() {
        // Beregner den gennemsnitlige totale omsætning pr. kunde.
        String sql = "SELECT IFNULL(AVG(total),0) FROM (SELECT SUM(monthly_price * TIMESTAMPDIFF(MONTH, start_date, end_date)) AS total FROM rental_agreement GROUP BY customer_id) t";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    // --- Fleet ---
    public int countTotalCars() {
        // Tæller det samlede antal biler i bilparken.
        String sql = "SELECT COUNT(*) FROM car";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    public int countAvailableCars() {
        // Tæller antallet af biler med status 'Tilgængelig'.
        String sql = "SELECT COUNT(*) FROM car WHERE car_status_id = (SELECT car_status_id FROM carstatus WHERE status_name = 'Tilgængelig' LIMIT 1)";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    public int countCarsWithDamages() {
        // Tæller antallet af unikke biler, der har registrerede skader via en
        // tilstandsrapport.
        String sql = "SELECT COUNT(DISTINCT ra.car_id) FROM rental_agreement ra JOIN condition_report cr ON ra.rental_agreement_id = cr.rental_agreement_id JOIN damage d ON cr.condition_report_id = d.condition_report_id";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    // --- Kunder & Lejeaftaler ---
    public int countTotalCustomers() {
        // Tæller det samlede antal kunder.
        String sql = "SELECT COUNT(*) FROM customer";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    public int countCompletedRentalAgreements() {
        // Tæller antallet af lejeaftaler, hvis slutdato er passeret.
        String sql = "SELECT COUNT(*) FROM rental_agreement WHERE end_date < CURDATE()";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    public BigDecimal avgRentalPeriodMonths() {
        // Beregner den gennemsnitlige længde af lejeperioder i måneder.
        String sql = "SELECT IFNULL(AVG(TIMESTAMPDIFF(MONTH, start_date, end_date)),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        if (result != null) {
            return result;
        } else {
            return BigDecimal.ZERO;
        }
    }

    public int countNewRentalsThisMonth() {
        // Tæller antallet af nye lejeaftaler, der er startet i den nuværende måned.
        String sql = "SELECT COUNT(*) FROM rental_agreement WHERE YEAR(start_date) = YEAR(CURDATE()) AND MONTH(start_date) = MONTH(CURDATE())";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }
}
