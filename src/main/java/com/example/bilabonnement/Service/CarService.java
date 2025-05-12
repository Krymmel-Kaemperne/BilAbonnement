package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Repository.CarRepository;
import com.example.bilabonnement.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public Car findById(int carId) {
        return carRepository.findById(carId);
    }

    public Car create(Car car) {
        return carRepository.create(car);
    }

    public Car update(Car car) {
        return carRepository.update(car);
    }
}