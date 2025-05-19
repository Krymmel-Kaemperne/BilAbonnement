
package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class RentalAgreementRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper<RentalAgreement> rentalAgreementRowMapper = new BeanPropertyRowMapper<>(RentalAgreement.class);

    public RentalAgreementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(RentalAgreement rentalAgreement) {
        String sql = """
                INSERT INTO rental_agreement (
                car_id,
                customer_id, 
                start_date, 
                end_date, 
                monthly_price, 
                kilometers_included,
                start_odometer,
                end_odometer,
                pickup_location_id,
                return_location_id,
                leasing_code)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql);
            ps.setInt(1, rentalAgreement.getCarId());
            ps.setInt(2, rentalAgreement.getCustomerId());
            ps.setObject(3, rentalAgreement.getStartDate());
            ps.setObject(4, rentalAgreement.getEndDate());
            ps.setBigDecimal(5, rentalAgreement.getMonthlyPrice());
            ps.setInt(6, rentalAgreement.getKilometersIncluded());
            ps.setInt(7, rentalAgreement.getStartOdometer());


            if (rentalAgreement.getEndOdometer() != null)
            {
                ps.setInt(8, rentalAgreement.getEndOdometer());
            } else
            {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.setInt(9, rentalAgreement.getPickupLocation());

            if (rentalAgreement.getReturnLocation() != null)
            {
                ps.setInt(10, rentalAgreement.getReturnLocation());
            } else
            {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.setString(11, rentalAgreement.getLeasingCode());
            return ps;
        });
    }

    public List<RentalAgreement> findAll() {
        String sql = "SELECT * FROM rental_agreement";
        List<RentalAgreement> agreements = jdbcTemplate.query(sql, rentalAgreementRowMapper);
        return agreements;
    }

    public void update(RentalAgreement rentalAgreement) {
        String sql = """
                UPDATE rental_agreement SET
                car_id = ?,
                customer_id = ?,
                start_date = ?,
                end_date = ?,
                monthly_price = ?,
                kilometers_included = ?,
                start_odometer = ?,
                end_odometer = ?,
                pickup_location_id = ?,
                return_location_id = ?,
                leasing_code = ?
                WHERE rental_agreement_id = ?
                """;

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql);
            ps.setInt(1, rentalAgreement.getCarId());
            ps.setInt(2, rentalAgreement.getCustomerId());
            ps.setObject(3, rentalAgreement.getStartDate());
            ps.setObject(4, rentalAgreement.getEndDate());
            ps.setBigDecimal(5, rentalAgreement.getMonthlyPrice());
            ps.setInt(6, rentalAgreement.getKilometersIncluded());
            ps.setInt(7, rentalAgreement.getStartOdometer());

            //NULL-SAFE
            if (rentalAgreement.getEndOdometer() != null)
            {
                ps.setInt(8, rentalAgreement.getEndOdometer());
            } else
            {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.setInt(9, rentalAgreement.getPickupLocation());

            if (rentalAgreement.getReturnLocation() != null)
            {
                ps.setInt(10, rentalAgreement.getReturnLocation());
            } else
            {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.setString(11, rentalAgreement.getLeasingCode());
            ps.setInt(12, rentalAgreement.getRentalAgreementId());

            return ps;
        });
    }

    public RentalAgreement findById(int id) {
        String sql = "SELECT * FROM rental_agreement WHERE rental_agreement_id = ?";

        try
        {
            return jdbcTemplate.queryForObject(sql, rentalAgreementRowMapper, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM rental_agreement WHERE rental_agreement_id = ?";
        jdbcTemplate.update(sql, id);
    }

}

