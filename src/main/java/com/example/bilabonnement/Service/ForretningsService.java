package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.ForretningsModel;
import com.example.bilabonnement.Repository.ForretningsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForretningsService {
    @Autowired
    private ForretningsRepository forretningsRepository;

    public ForretningsModel getMetrics() {
        int activeRentals = forretningsRepository.countActiveRentals();
        java.math.BigDecimal currentRevenue = forretningsRepository.sumCurrentRevenue();
        java.math.BigDecimal totalRevenue = forretningsRepository.sumTotalRevenue();
        java.math.BigDecimal averageRentalIncome = forretningsRepository.avgRentalIncome();
        int totalCars = forretningsRepository.countTotalCars();
        int availableCars = forretningsRepository.countAvailableCars();
        int carsWithDamages = forretningsRepository.countCarsWithDamages();
        int totalCustomers = forretningsRepository.countTotalCustomers();
        int newCustomersThisMonth = forretningsRepository.countNewCustomersThisMonth();
        int completedRentalAgreements = forretningsRepository.countCompletedRentalAgreements();
        java.math.BigDecimal avgRentalPeriodMonths = forretningsRepository.avgRentalPeriodMonths();
        java.math.BigDecimal currentMonthRevenue = forretningsRepository.sumCurrentMonthRevenue();
        java.math.BigDecimal avgRevenuePerCustomer = forretningsRepository.avgRevenuePerCustomer();
        return new ForretningsModel(
            activeRentals, currentRevenue, totalRevenue, averageRentalIncome,
            totalCars, availableCars, carsWithDamages, totalCustomers, newCustomersThisMonth,
            completedRentalAgreements, avgRentalPeriodMonths, currentMonthRevenue, avgRevenuePerCustomer
        );
    }
} 