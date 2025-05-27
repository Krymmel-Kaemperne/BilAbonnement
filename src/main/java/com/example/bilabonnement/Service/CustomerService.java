package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalAgreementService rentalAgreementService;

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findById(int customerId) {
        return customerRepository.findById(customerId);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        return customerRepository.update(customer);
    }

    // Finder kunder baseret på filtre som ID, type, by og aktiv lejeaftale.
    public List<Customer> findFilteredCustomers(String searchCustomerId, String customerType, Integer cityId, Boolean hasActiveRental) {
        List<Customer> customers = findAllCustomers();
        List<Customer> filteredCustomers = new ArrayList<>();

        if (searchCustomerId != null && !searchCustomerId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(searchCustomerId);
                Customer customer = findById(id);
                return customer != null ? List.of(customer) : List.of();
            } catch (NumberFormatException e) {
                return List.of();
            }
        }

        for (Customer customer : customers) {
            boolean matches = true;

            if (customerType != null && !customerType.isEmpty()) {
                if (customer.getCustomerType() == null || !customer.getCustomerType().name().equals(customerType)) {
                    matches = false;
                }
            }

            if (matches && cityId != null) {
                if (customer.getZipcodeId() != cityId) {
                    matches = false;
                }
            }

            if (matches && hasActiveRental != null) {
                if (hasActiveRental != rentalAgreementService.hasActiveRental(customer.getCustomerId())) {
                    matches = false;
                }
            }

            if (matches) {
                filteredCustomers.add(customer);
            }
        }

        return filteredCustomers;
    }
}
