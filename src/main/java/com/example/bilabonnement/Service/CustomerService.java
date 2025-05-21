package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Customer> findFilteredCustomers(String searchCustomerId, String customerType, Integer cityId, Boolean hasActiveRental) {
        List<Customer> customers = findAllCustomers();

        // Filter by ID if provided
        if (searchCustomerId != null && !searchCustomerId.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(searchCustomerId);
                Customer customer = findById(id);
                return customer != null ? List.of(customer) : List.of();
            } catch (NumberFormatException e) {
                return List.of(); // Invalid ID format, return empty list
            }
        }

        // Apply remaining filters if no ID search or if ID search returned no results
        if (customerType != null && !customerType.isEmpty()) {
            customers = customers.stream()
                    .filter(c -> c.getCustomerType().name().equals(customerType))
                    .collect(Collectors.toList());
        }

        if (cityId != null) {
            customers = customers.stream()
                    .filter(c -> c.getZipcodeId() == cityId)
                    .collect(Collectors.toList());
        }

        // Filter by active rental status if provided
        if (hasActiveRental != null) {
            customers = customers.stream()
                    .filter(c -> hasActiveRental == rentalAgreementService.hasActiveRental(c.getCustomerId()))
                    .collect(Collectors.toList());
        }

        return customers;
    }
}
