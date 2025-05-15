package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.Car;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper; // Import BeanPropertyRowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.lang.StringBuilder;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class CarRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    // Use BeanPropertyRowMapper, specifying the target class
    private final BeanPropertyRowMapper<Car> carRowMapper = new BeanPropertyRowMapper<>(Car.class);

    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Opretter en ny bil i databasen.
     * @param car Car objektet der skal oprettes.
     * @return Car objektet med det tildelte car_id.
     */
    public Car create(Car car) {
        String sqlInsert = "INSERT INTO car (registration_number, chassis_number, steel_price, color, " +
                "co2_emission, vehicle_number, model_id, car_status_id, fuel_type_id, " +
                "transmission_type_id, current_odometer, irk_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, car.getRegistrationNumber());
            ps.setString(2, car.getChassisNumber());
            ps.setDouble(3, car.getSteelPrice());
            ps.setString(4, car.getColor());
            ps.setDouble(5, car.getCo2Emission());
            ps.setString(6, car.getVehicleNumber());
            ps.setInt(7, car.getModelId());
            ps.setInt(8, car.getCarStatusId());
            ps.setInt(9, car.getFuelTypeId());
            ps.setInt(10, car.getTransmissionTypeId());
            ps.setInt(11, car.getCurrentOdometer());
            ps.setString(12, car.getIrkCode());
            return ps;
        }, keyHolder);

        Number generatedKey = keyHolder.getKey();
        if (generatedKey != null) {
            car.setCarId(generatedKey.intValue());
        } else {
            System.err.println("Fejl: Kunne ikke hente genereret nøgle for ny bil.");
        }
        return car;
    }

    /**
     * Opdaterer en eksisterende bil i databasen.
     * @param car Car objektet med de opdaterede informationer.
     * @return Det opdaterede Car objekt, eller null hvis ingen rækker blev påvirket.
     */
    public Car update(Car car) {
        if (car.getCarId() <= 0) {
            throw new IllegalArgumentException("Car ID skal være positivt for at kunne opdatere.");
        }
        String sqlUpdate = "UPDATE car SET registration_number = ?, chassis_number = ?, steel_price = ?, " +
                "color = ?, co2_emission = ?, vehicle_number = ?, model_id = ?, " +
                "car_status_id = ?, fuel_type_id = ?, transmission_type_id = ?, current_odometer = ?, irk_code = ? " +
                "WHERE car_id = ?";
        int rowsAffected = jdbcTemplate.update(sqlUpdate,
                car.getRegistrationNumber(),
                car.getChassisNumber(),
                car.getSteelPrice(),
                car.getColor(),
                car.getCo2Emission(),
                car.getVehicleNumber(),
                car.getModelId(),
                car.getCarStatusId(),
                car.getFuelTypeId(),
                car.getTransmissionTypeId(),
                car.getCurrentOdometer(),
                car.getIrkCode(),
                car.getCarId());

        if (rowsAffected > 0) {
            return car;
        } else {
            return null; // Ingen bil blev opdateret (f.eks. ID fandtes ikke)
        }
    }

    public List<Car> findAll() {
        String sql = "SELECT c.car_id, c.registration_number, c.chassis_number, c.steel_price, " +
                "c.color, c.co2_emission, c.vehicle_number, c.model_id, c.car_status_id, " +
                "c.fuel_type_id, c.transmission_type_id, c.current_odometer, c.irk_code, " +
                "m.model_name AS modelName, b.brand_id AS brandId, b.brand_name AS brandName, " +
                "cs.status_name AS carStatusName, " +
                "ft.fuel_type_name AS fuelTypeName, " +
                "tt.transmission_type_name AS transmissionTypeName " +
                "FROM car c " +
                "JOIN model m ON c.model_id = m.model_id " +
                "JOIN brand b ON m.brand_id = b.brand_id " +
                "JOIN carstatus cs ON c.car_status_id = cs.car_status_id " +
                "JOIN fueltype ft ON c.fuel_type_id = ft.fuel_type_id " +
                "JOIN transmissiontype tt ON c.transmission_type_id = tt.transmission_type_id";

        return jdbcTemplate.query(sql, carRowMapper);
    }

    public Car findById(int carId) {
        String sql = "SELECT c.car_id, c.registration_number, c.chassis_number, c.steel_price, " +
                "c.color, c.co2_emission, c.vehicle_number, c.model_id, c.car_status_id, " +
                "c.fuel_type_id, c.transmission_type_id, c.current_odometer, c.irk_code, " +
                "m.model_name AS modelName, b.brand_id AS brandId, b.brand_name AS brandName, " +
                "cs.status_name AS carStatusName, " +
                "ft.fuel_type_name AS fuelTypeName, " +
                "tt.transmission_type_name AS transmissionTypeName " +
                "FROM car c " +
                "JOIN model m ON c.model_id = m.model_id " +
                "JOIN brand b ON m.brand_id = b.brand_id " +
                "JOIN carstatus cs ON c.car_status_id = cs.car_status_id " +
                "JOIN fueltype ft ON c.fuel_type_id = ft.fuel_type_id " +
                "JOIN transmissiontype tt ON c.transmission_type_id = tt.transmission_type_id " +
                "WHERE c.car_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, carRowMapper, carId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Car> findByFilters(Integer brand, Integer status, Integer model, Integer fuelType, Integer transmissionType) {
        StringBuilder sql = new StringBuilder(
                "SELECT c.car_id, c.registration_number, c.chassis_number, c.steel_price, " +
                        "c.color, c.co2_emission, c.vehicle_number, c.model_id, c.car_status_id, " +
                        "c.fuel_type_id, c.transmission_type_id, c.current_odometer, c.irk_code, " +
                        "m.model_name AS modelName, b.brand_id AS brandId, b.brand_name AS brandName, " +
                        "cs.status_name AS carStatusName, " +
                        "ft.fuel_type_name AS fuelTypeName, " +
                        "tt.transmission_type_name AS transmissionTypeName " +
                        "FROM car c " +
                        "JOIN model m ON c.model_id = m.model_id " +
                        "JOIN brand b ON m.brand_id = b.brand_id " +
                        "JOIN carstatus cs ON c.car_status_id = cs.car_status_id " +
                        "JOIN fueltype ft ON c.fuel_type_id = ft.fuel_type_id " +
                        "JOIN transmissiontype tt ON c.transmission_type_id = tt.transmission_type_id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (brand != null) {
            sql.append(" AND b.brand_id = ?");
            params.add(brand);
        }
        if (status != null) {
            sql.append(" AND cs.car_status_id = ?");
            params.add(status);
        }
        if (model != null) {
            sql.append(" AND m.model_id = ?");
            params.add(model);
        }
        if (fuelType != null) {
            sql.append(" AND ft.fuel_type_id = ?");
            params.add(fuelType);
        }
        if (transmissionType != null) {
            sql.append(" AND tt.transmission_type_id = ?");
            params.add(transmissionType);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), carRowMapper);
    }
}
