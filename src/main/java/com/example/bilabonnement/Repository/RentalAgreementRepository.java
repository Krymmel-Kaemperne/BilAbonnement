package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.sql.PreparedStatement; // Behold denne til create for generated key

@Repository
public class RentalAgreementRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CarRepository carRepository; // Beholder denne, selvom den ikke bruges direkte i dette repo's metoder

    private final BeanPropertyRowMapper<RentalAgreement> rentalAgreementRowMapper = new BeanPropertyRowMapper<>(RentalAgreement.class);



    /**
     * Opretter en ny lejeaftale i databasen og henter den genererede ID.
     */
    public void create(RentalAgreement rentalAgreement) {
        String sql = """
                INSERT INTO rental_agreement (
                car_id, customer_id, start_date, end_date, monthly_price,
                kilometers_included, start_odometer, end_odometer,
                pickup_location_id, return_location_id, leasing_code)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        // Vi skal bruge KeyHolder for at hente den genererede ID, så vi beholder
        // PreparedStatementSetter til denne metode.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rentalAgreement.getCarId());
            ps.setInt(2, rentalAgreement.getCustomerId());
            ps.setObject(3, rentalAgreement.getStartDate());
            ps.setObject(4, rentalAgreement.getEndDate());
            ps.setBigDecimal(5, rentalAgreement.getMonthlyPrice());
            ps.setInt(6, rentalAgreement.getKilometersIncluded());
            ps.setInt(7, rentalAgreement.getStartOdometer());

            // JdbcTemplate håndterer automatisk null for objekter, men
            if (rentalAgreement.getEndOdometer() != null) {
                ps.setInt(8, rentalAgreement.getEndOdometer());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.setInt(9, rentalAgreement.getPickupLocationId());

            if (rentalAgreement.getReturnLocationId() != null) {
                ps.setInt(10, rentalAgreement.getReturnLocationId());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.setString(11, rentalAgreement.getLeasingCode());
            return ps;
        }, keyHolder);

        // Sætter den genererede nøgle (ID) på objektet.
        Number key = keyHolder.getKey();
        if (key != null) {
            rentalAgreement.setRentalAgreementId(key.intValue());
        } else {
            System.err.println("Warning: Could not retrieve generated key for RentalAgreement.");
        }
    }

    /**
     * Finder alle lejeaftaler i databasen.
     */
    public List<RentalAgreement> findAll() {
        String sql = "SELECT * FROM rental_agreement";
        return jdbcTemplate.query(sql, rentalAgreementRowMapper);
    }

    /**
     * Finder lejeaftaler, der er afsluttet (slutdato er passeret eller i dag).
     */
    public List<RentalAgreement> findFinishedRentalAgreements() {
        String sql = "SELECT * FROM rental_agreement WHERE end_date <= ?";
        return jdbcTemplate.query(sql, rentalAgreementRowMapper, LocalDate.now());
    }

    /**
     * Opdaterer en eksisterende lejeaftale i databasen.
     */
    public void update(RentalAgreement rentalAgreement) {
        String sql = """
                UPDATE rental_agreement SET
                car_id = ?, customer_id = ?, start_date = ?, end_date = ?,
                monthly_price = ?, kilometers_included = ?, start_odometer = ?,
                end_odometer = ?, pickup_location_id = ?, return_location_id = ?,
                leasing_code = ?
                WHERE rental_agreement_id = ?
                """;

        jdbcTemplate.update(sql,
                rentalAgreement.getCarId(),
                rentalAgreement.getCustomerId(),
                rentalAgreement.getStartDate(),
                rentalAgreement.getEndDate(),
                rentalAgreement.getMonthlyPrice(),
                rentalAgreement.getKilometersIncluded(),
                rentalAgreement.getStartOdometer(),
                rentalAgreement.getEndOdometer(),
                rentalAgreement.getPickupLocationId(),
                rentalAgreement.getReturnLocationId(),
                rentalAgreement.getLeasingCode(),
                rentalAgreement.getRentalAgreementId()
        );
    }

    /**
     * Finder en lejeaftale baseret på dens ID.
     */
    public RentalAgreement findById(int id) {
        String sql = "SELECT * FROM rental_agreement WHERE rental_agreement_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rentalAgreementRowMapper, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Sletter en lejeaftale fra databasen baseret på dens ID.
     */
    public void delete(int id) {
        String sql = "DELETE FROM rental_agreement WHERE rental_agreement_id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Tæller det totale antal lejeaftaler i databasen.
     */
    public int countAllRentalAgreements() {
        String sql = "SELECT COUNT(*) FROM rental_agreement";
        // Bruger queryForObject til at hente en enkelt værdi (antallet).
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result != null ? result : 0;
    }

    /**
     * Finder lejeaftaler tilknyttet en specifik bil ID.
     */
    public List<RentalAgreement> findAgreementsByCarId(int carId) {
        String sql = "SELECT * FROM rental_agreement WHERE car_id = ?";
        List<RentalAgreement> results = jdbcTemplate.query(sql, rentalAgreementRowMapper, carId);

        // Debug print statements
        System.out.println("📊 SQL executed: " + sql + " with carId=" + carId);
        System.out.println("📊 Found " + results.size() + " agreements");

        for (RentalAgreement ag : results) {
            System.out.println("📝 " + ag);
        }

        return results;
    }
}
