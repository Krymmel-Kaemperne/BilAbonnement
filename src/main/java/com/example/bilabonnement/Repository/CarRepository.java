package com.example.bilabonnement.Repository;
import com.example.bilabonnement.Model.Car;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.lang.StringBuilder;


@Repository
public class CarRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

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
                "transmission_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                "car_status_id = ?, fuel_type_id = ?, transmission_type_id = ? " +
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
                car.getCarId());

        if (rowsAffected > 0) {
            return car;
        } else {
            return null; // Ingen bil blev opdateret (f.eks. ID fandtes ikke)
        }
    }
    // SQL-sætning til at hente alle biler med JOINs til model og brand tabellerne.
    public List<Car> findAll() {
        String sql = "SELECT c.*, m.model_name, b.brand_id, b.brand_name, " +
                "cs.status_name AS car_status_name, " +
                "ft.fuel_type_name, " +
                "tt.transmission_type_name " +
                "FROM car c " +
                "JOIN model m ON c.model_id = m.model_id " +
                "JOIN brand b ON m.brand_id = b.brand_id " +
                "JOIN carstatus cs ON c.car_status_id = cs.car_status_id " +
                "JOIN fueltype ft ON c.fuel_type_id = ft.fuel_type_id " +
                "JOIN transmissiontype tt ON c.transmission_type_id = tt.transmission_type_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Car car = new Car();
            car.setCarId(rs.getInt("car_id"));
            car.setRegistrationNumber(rs.getString("registration_number"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setSteelPrice(rs.getDouble("steel_price"));
            car.setColor(rs.getString("color"));
            car.setCo2Emission(rs.getDouble("co2_emission"));
            car.setVehicleNumber(rs.getString("vehicle_number"));

            car.setModelId(rs.getInt("model_id"));
            car.setBrandId(rs.getInt("brand_id"));
            car.setBrandName(rs.getString("brand_name"));
            car.setCarStatusId(rs.getInt("car_status_id"));
            car.setFuelTypeId(rs.getInt("fuel_type_id"));
            car.setTransmissionTypeId(rs.getInt("transmission_type_id"));
            // Set the display names:
            car.setModelName(rs.getString("model_name"));
            car.setCarStatusName(rs.getString("car_status_name"));
            car.setFuelTypeName(rs.getString("fuel_type_name"));
            car.setTransmissionTypeName(rs.getString("transmission_type_name"));
            return car;
        });
    }

    public Car findById(int carId) {
        String sql = "SELECT c.*, m.model_name, b.brand_id, b.brand_name, " +
                "cs.status_name AS car_status_name, " +
                "ft.fuel_type_name, " +
                "tt.transmission_type_name " +
                "FROM car c " +
                "JOIN model m ON c.model_id = m.model_id " +
                "JOIN brand b ON m.brand_id = b.brand_id " +
                "JOIN carstatus cs ON c.car_status_id = cs.car_status_id " +
                "JOIN fueltype ft ON c.fuel_type_id = ft.fuel_type_id " +
                "JOIN transmissiontype tt ON c.transmission_type_id = tt.transmission_type_id " +
                "WHERE c.car_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Car car = new Car();
                car.setCarId(rs.getInt("car_id"));
                car.setRegistrationNumber(rs.getString("registration_number"));
                car.setChassisNumber(rs.getString("chassis_number"));
                car.setSteelPrice(rs.getDouble("steel_price"));
                car.setColor(rs.getString("color"));
                car.setCo2Emission(rs.getDouble("co2_emission"));
                car.setVehicleNumber(rs.getString("vehicle_number"));
                car.setModelId(rs.getInt("model_id"));
                car.setModelName(rs.getString("model_name"));
                car.setBrandId(rs.getInt("brand_id"));
                car.setBrandName(rs.getString("brand_name"));
                car.setCarStatusId(rs.getInt("car_status_id"));
                car.setCarStatusName(rs.getString("car_status_name"));
                car.setFuelTypeId(rs.getInt("fuel_type_id"));
                car.setFuelTypeName(rs.getString("fuel_type_name"));
                car.setTransmissionTypeId(rs.getInt("transmission_type_id"));
                car.setTransmissionTypeName(rs.getString("transmission_type_name"));
                return car;
            }, carId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Car> findByFilters(Integer brand, Integer status, Integer model, Integer fuelType, Integer transmissionType) {
        StringBuilder sql = new StringBuilder(
            "SELECT c.*, m.model_name, b.brand_id, b.brand_name, " +
            "cs.status_name AS car_status_name, " +
            "ft.fuel_type_name, " +
            "tt.transmission_type_name " +
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

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            Car car = new Car();
            car.setCarId(rs.getInt("car_id"));
            car.setRegistrationNumber(rs.getString("registration_number"));
            car.setChassisNumber(rs.getString("chassis_number"));
            car.setSteelPrice(rs.getDouble("steel_price"));
            car.setColor(rs.getString("color"));
            car.setCo2Emission(rs.getDouble("co2_emission"));
            car.setVehicleNumber(rs.getString("vehicle_number"));
            car.setModelId(rs.getInt("model_id"));
            car.setBrandId(rs.getInt("brand_id"));
            car.setBrandName(rs.getString("brand_name"));
            car.setCarStatusId(rs.getInt("car_status_id"));
            car.setFuelTypeId(rs.getInt("fuel_type_id"));
            car.setTransmissionTypeId(rs.getInt("transmission_type_id"));
            car.setModelName(rs.getString("model_name"));
            car.setCarStatusName(rs.getString("car_status_name"));
            car.setFuelTypeName(rs.getString("fuel_type_name"));
            car.setTransmissionTypeName(rs.getString("transmission_type_name"));
            return car;
        });
    }

}