package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

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
}
