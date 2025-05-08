package com.example.bilabonnement.Repository;

import com.example.bilabonnement.Model.*;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ZipcodeRepository zipcodeRepository;

    public Customer findById(int customerId) {
        String sqlCustomer = "SELECT * FROM customer WHERE customer_id = ?";
        try {
            Customer customer = jdbcTemplate.queryForObject(
                    sqlCustomer, new BeanPropertyRowMapper<>(Customer.class), customerId);

            Zipcode zipcode = zipcodeRepository.findById(customer.getZipcodeId());
            if(zipcode != null) {
                customer.setZipcode(zipcode);
            } else {
                System.err.println("Warning: Zipcode with ID" +
                        customer.getZipcodeId() + "not found for customer" + customerId);
            }
            if(customer.getCustomerType() == CustomerType.PRIVATE) {
                String sqlPrivate = "SELECT * FROM private_customer WHERE customer_id = ?";
                try {
                    PrivateCustomer privateDetails = jdbcTemplate.queryForObject(
                            sqlPrivate, new BeanPropertyRowMapper<>(PrivateCustomer.class), customerId);
                    customer.setPrivateCustomerDetails(privateDetails);
                } catch (EmptyResultDataAccessException e) {
                    System.err.println("Warning: Private customer details not found for customer" + customerId);
                }
            } else if (customer.getCustomerType() == CustomerType.BUSINESS) {
                String sqlBusiness = "SELECT * FROM business_customer WHERE customer_id = ?";
                try {
                    BusinessCustomer businessDetails = jdbcTemplate.queryForObject(
                            sqlBusiness, new BeanPropertyRowMapper<>(BusinessCustomer.class), customerId);
                    customer.setBusinessCustomerDetails(businessDetails);
                } catch (EmptyResultDataAccessException e) {
                    System.err.println("Warning: Business customer details not found for customer" + customerId);
                }
            }
            return customer;
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Customer save(Customer customer) {
        if(customer.getZipcodeId() <= 0) {
            throw new IllegalArgumentException("Zipcode Id must be provided to save customer");
        }
        Zipcode zipcode = zipcodeRepository.findById(customer.getZipcodeId());
        if(zipcode == null) {
            throw new IllegalArgumentException("Invalid Zipcode ID" + customer.getZipcodeId() + "Zipcode does not exist");
        } customer.setZipcode(zipcode);

        String sqlCustomer = "INSERT INTO customer (fname, lname, email, phone, address, zipcode_id, customer_type)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlCustomer, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getFname());
            ps.setString(2, customer.getLname());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getAddress());
            ps.setInt(6, customer.getZipcodeId());
            ps.setString(7, customer.getCustomerType().name());
            return ps;
        }, keyHolder);
        if(keyHolder.getKey() == null) {
            throw new RuntimeException("Failed to save customer, no ID obtained for customer table");
        }
        customer.setCustomerId(keyHolder.getKey().intValue());

        if(customer.getCustomerType() == CustomerType.PRIVATE) {
            if(customer.getPrivateCustomerDetails() == null) {
                throw new IllegalArgumentException("Private customer details are required for PRIVATE types");
            }
            PrivateCustomer pc = customer.getPrivateCustomerDetails();
            pc.setPrivateCustomerId(customer.getCustomerId());

            String sqlPrivate = "INSERT INTO private_customer (customer_id, cpr_number) VALUES (?, ?)";
            KeyHolder privateKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlPrivate, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, pc.getCustomerId());
                ps.setString(2, pc.getCprNumber());
                return ps;
            }, privateKeyHolder);
            if(privateKeyHolder.getKey() != null) {
                pc.setPrivateCustomerId(privateKeyHolder.getKey().intValue());
            } else {
                System.err.println("Warning: Could not retrieve generated ID for private_customer details.");
            }
        } else if (customer.getCustomerType() == CustomerType.BUSINESS) {
            if(customer.getBusinessCustomerDetails() == null) {
                throw new IllegalArgumentException("Business customer details are required for BUSINESS types");
            }
            BusinessCustomer bc = customer.getBusinessCustomerDetails();
            bc.setBusinessCustomerId(customer.getCustomerId());

            String sqlBusiness = "INSERT INTO business_customer (customer_id, cvr_number, company_name) VALUES (?, ?, ?)";
            KeyHolder businessKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlBusiness, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, bc.getCustomerId());
                ps.setString(2, bc.getCvrNumber());
                ps.setString(3, bc.getCompanyName());
                return ps;
            }, businessKeyHolder);
            if(businessKeyHolder.getKey() != null) {
                bc.setBusinessCustomerId(businessKeyHolder.getKey().intValue());
            } else {
                System.err.println("Warning: Could not retrieve generated ID for business_customer details.");
            }
        }
        return customer;
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";

        List<Customer> customers = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setFname(rs.getString("fname"));
            customer.setLname(rs.getString("lname"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));
            customer.setZipcodeId(rs.getInt("zipcode_id"));
            try {
                customer.setCustomerType(CustomerType.valueOf(rs.getString("customer_type")));
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Invalid customer_type in database for customer_id: " + customer.getCustomerId());
            }
            return customer;
        });
    }
}
