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
        String sql = "SELECT COUNT(*) FROM rental_agreement WHERE CURDATE() BETWEEN start_date AND end_date";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public BigDecimal sumCurrentRevenue() {
        String sql = "SELECT IFNULL(SUM(monthly_price),0) FROM rental_agreement WHERE CURDATE() BETWEEN start_date AND end_date";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal sumTotalRevenue() {
        String sql = "SELECT IFNULL(SUM(monthly_price * (TIMESTAMPDIFF(MONTH, start_date, end_date))),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal avgRentalIncome() {
        String sql = "SELECT IFNULL(AVG(monthly_price),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    // --- Omsætning ---
    public BigDecimal sumCurrentMonthRevenue() {
        String sql = "SELECT IFNULL(SUM(monthly_price),0) FROM rental_agreement WHERE (YEAR(start_date) = YEAR(CURDATE()) AND MONTH(start_date) = MONTH(CURDATE())) OR (YEAR(end_date) = YEAR(CURDATE()) AND MONTH(end_date) = MONTH(CURDATE())) OR (start_date < CURDATE() AND end_date > CURDATE())";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal avgRevenuePerCustomer() {
        String sql = "SELECT IFNULL(AVG(total),0) FROM (SELECT SUM(monthly_price * TIMESTAMPDIFF(MONTH, start_date, end_date)) AS total FROM rental_agreement GROUP BY customer_id) t";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }

    // --- Fleet ---
    public int countTotalCars() {
        String sql = "SELECT COUNT(*) FROM car";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public int countAvailableCars() {
        String sql = "SELECT COUNT(*) FROM car WHERE car_status_id = (SELECT car_status_id FROM carstatus WHERE status_name = 'Tilgængelig' LIMIT 1)";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public int countCarsWithDamages() {
        String sql = "SELECT COUNT(DISTINCT ra.car_id) FROM rental_agreement ra JOIN condition_report cr ON ra.rental_agreement_id = cr.rental_agreement_id JOIN damage d ON cr.condition_report_id = d.condition_report_id";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    // --- Customers & Rentals ---
    public int countTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM customer";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public int countNewCustomersThisMonth() {
        // Only works if you have a created_at column. If not, this will need to be adjusted.
        String sql = "SELECT COUNT(*) FROM customer WHERE YEAR(CURDATE()) = YEAR(created_at) AND MONTH(CURDATE()) = MONTH(created_at)";
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
            return result != null ? result : 0;
        } catch (Exception e) {
            return 0; // If created_at does not exist, return 0
        }
    }

    public int countCompletedRentalAgreements() {
        String sql = "SELECT COUNT(*) FROM rental_agreement WHERE end_date < CURDATE()";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    public BigDecimal avgRentalPeriodMonths() {
        String sql = "SELECT IFNULL(AVG(TIMESTAMPDIFF(MONTH, start_date, end_date)),0) FROM rental_agreement";
        BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return result != null ? result : BigDecimal.ZERO;
    }
} 