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
        String sqlBaseCustomer = "SELECT c.*, z.zip_code, z.city_name FROM customer c " +
                "LEFT JOIN zipcode z ON c.zipcode_id = z.zipcode_id WHERE c.customer_id = ?";
        try {
            Customer baseCustomerData = jdbcTemplate.queryForObject(sqlBaseCustomer, (rs, rowNum) -> {
                Zipcode zipcode = new Zipcode();
                zipcode.setZipcodeId(rs.getInt("zipcode_id"));

                if(rs.getObject("zipcode_id") != null) {
                    zipcode.setZipcode(rs.getString("zip_code"));
                    zipcode.setCityName(rs.getString("city_name"));
                }

                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("zipcode_id"),
                        CustomerType.valueOf(rs.getString("customer_type")),
                        zipcode
                        ) {};
            }, customerId);

            if (baseCustomerData.getCustomerType() == CustomerType.PRIVATE) {
                String sqlPrivate = "SELECT * FROM private_customer WHERE customer_id = ?";
                PrivateCustomer privateCustomer = jdbcTemplate.queryForObject(sqlPrivate, (rs,
                                                                                               rowNum) ->
                        new PrivateCustomer(
                                baseCustomerData.getCustomerId(),
                                baseCustomerData.getFname(), // Brug getter fra baseCustomerData
                                baseCustomerData.getLname(), // Brug getter fra baseCustomerData
                                baseCustomerData.getEmail(),
                                baseCustomerData.getPhone(),
                                baseCustomerData.getAddress(),
                                baseCustomerData.getZipcodeId(),
                                baseCustomerData.getZipcode(), // Send det Zipcode objekt vi byggede
                                rs.getString("cpr_number")
                        ), baseCustomerData.getCustomerId());
                return privateCustomer;

            } else if (baseCustomerData.getCustomerType() == CustomerType.BUSINESS) {
                    String sqlBusiness = "SELECT * FROM business_customer WHERE customer_id = ?";
                    BusinessCustomer businessCustomer = jdbcTemplate.queryForObject(sqlBusiness, (rs,
                                                                                                  rowNum) ->
                            new BusinessCustomer(
                                    baseCustomerData.getCustomerId(),
                                    baseCustomerData.getFname(),
                                    baseCustomerData.getLname(),
                                    baseCustomerData.getEmail(),
                                    baseCustomerData.getPhone(),
                                    baseCustomerData.getAddress(),
                                    baseCustomerData.getZipcodeId(),
                                    baseCustomerData.getZipcode(),
                                    rs.getString("cvr_number"),
                                    rs.getString("company_name")
                            ), baseCustomerData.getCustomerId());
                    return businessCustomer;
                } throw new IllegalStateException("Unknown or unhandled customer type: " +
                    baseCustomerData.getCustomerType() + " for customer ID: " + customerId);

        } catch (EmptyResultDataAccessException e) {
            System.err.println("Info: Customer with ID " + customerId + " not found.");
            return null; // Det er acceptabelt at returnere null, hvis kunden ikke findes.
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

        if(customer instanceof PrivateCustomer pc) {

            String sqlPrivate = "INSERT INTO private_customer (customer_id, cpr_number) VALUES (?, ?)";
            KeyHolder privateKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlPrivate, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, pc.getCustomerId());
                ps.setString(2, pc.getCprNumber());
                return ps;
            }, privateKeyHolder);
            //if(privateKeyHolder.getKey() != null) {
              //  pc.setPrivateSpecificId(privateKeyHolder.getKey().intValue());
            //} else {
              //  System.err.println("Warning: Could not retrieve generated ID for private_customer details.");
            //}
        } else if (customer instanceof BusinessCustomer bc) {
            String sqlBusiness = "INSERT INTO business_customer (customer_id, cvr_number, company_name) VALUES (?, ?, ?)";
            KeyHolder businessKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlBusiness, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, bc.getCustomerId());
                ps.setString(2, bc.getCvrNumber());
                ps.setString(3, bc.getCompanyName());
                return ps;
            }, businessKeyHolder);
            //if(businessKeyHolder.getKey() != null) {
              //  bc.setBusinessCustomerId(businessKeyHolder.getKey().intValue());
            //} else {
              //  System.err.println("Warning: Could not retrieve generated ID for business_customer details.");
            //}
        }
        return customer;
    }

    public List<Customer> findAll() {
        String sql = "SELECT c.*, z.zip_code, z.city_name, " +
                "pc.cpr_number, bc.cvr_number, bc.company_name FROM customer c " +
                "LEFT JOIN zipcode z ON c.zipcode_id = z.zipcode_id " +
                "LEFT JOIN private_customer pc ON c.customer_id = pc.customer_id AND c.customer_type = 'PRIVATE' " +
                "LEFT JOIN business_customer bc ON c.customer_id = bc.customer_id AND c.customer_type = 'BUSINESS'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Customer customer = null;
            CustomerType type = CustomerType.valueOf(rs.getString("customer_type"));

            int customerId = rs.getInt("customer_id");
            String fName = rs.getString("fname");
            String lName = rs.getString("lname");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            int zipcodeId = rs.getInt("zipcode_id");

            Zipcode zipcode = new Zipcode();
            zipcode.setZipcodeId(zipcodeId);
            zipcode.setZipcode(rs.getString("zip_code"));
            zipcode.setCityName(rs.getString("city_name"));

            if (type == CustomerType.PRIVATE) {
                PrivateCustomer pc = new PrivateCustomer(
                        customerId, fName, lName, email, phone, address, zipcodeId, zipcode,
                        rs.getString("cpr_number")
                );
                customer = pc;
            } else if (type == CustomerType.BUSINESS) {
                BusinessCustomer bc = new BusinessCustomer(
                        customerId, fName, lName, email, phone, address, zipcodeId, zipcode,
                        rs.getString("cvr_number"),
                        rs.getString("company_name")
                );
                customer = bc;
            }

            if (customer == null) {
                System.err.println("Critical Error: Could not map row for customer_id: " + customerId + " " +
                        "with type: " + type + ". This should not happen.");
                throw new IllegalStateException("Failed to map customer with ID: " + customerId + " and type: " + type);
            }
            return customer;
        });
    }

    public Customer update(Customer customer) {
        if(customer == null || customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Customer to be updated must have a valid Id");
        }

        if(customer.getZipcode() == null && customer.getZipcodeId() > 0) {
            Zipcode zipcode = zipcodeRepository.findById(customer.getZipcodeId());
            if(zipcode == null) {
                throw new IllegalArgumentException("Invalid Zipcode ID:" + customer.getZipcodeId() + ". " +
                        "Zipcode does not exist for update");
            }
            customer.setZipcode(zipcode);
        } else if (customer.getZipcode() != null && customer.getZipcode().getZipcodeId() <= 0) {
            throw new IllegalArgumentException("Zipcode object provided for update but its ID is invalid or not set.");
        } else if (customer.getZipcode() == null && customer.getZipcodeId() <= 0) {
            throw new IllegalArgumentException("Zipcode ID or Zipcode object must be provided for update.");
        }
        if (customer.getZipcodeId() <= 0 && customer.getZipcode() != null) {
            customer.setZipcodeId(customer.getZipcode().getZipcodeId());
        }
        String sqlUpdateCustomer = "UPDATE customer SET fname = ?, lname = ?, email = ?, phone = ?, " +
                "address = ?, zipcode_id = ? WHERE customer_id = ?";
        int rowsAffectedCustomer = jdbcTemplate.update(sqlUpdateCustomer,
                customer.getFname(),
                customer.getLname(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getZipcodeId(),
                customer.getCustomerId());

        if(rowsAffectedCustomer == 0) {
            throw new RuntimeException("Failed to update customer (base data). Customer with ID " +
                    customer.getCustomerId() + " not found or no data changed.");
        }
        if (customer instanceof PrivateCustomer pc) {
            String sqlUpdatePrivate = "UPDATE private_customer SET cpr_number = ? WHERE customer_id = ?";
            int rowsAffectedPrivate = jdbcTemplate.update(sqlUpdatePrivate,
                    pc.getCprNumber(),
                    pc.getCustomerId());
            if (rowsAffectedPrivate == 0) {
                System.err.println("Warning: Update for private_customer with customer_id " + pc.getCustomerId() +
                        " affected 0 rows. Data might be unchanged or record missing.");
            }
        } else if (customer instanceof BusinessCustomer bc) {
            String sqlUpdateBusiness = "UPDATE business_customer SET cvr_number = ?, company_name = ? WHERE customer_id = ?";
            int rowsAffectedBusiness = jdbcTemplate.update(sqlUpdateBusiness,
                    bc.getCvrNumber(),
                    bc.getCompanyName(),
                    bc.getCustomerId());
            if (rowsAffectedBusiness == 0) {
                System.err.println("Warning: Update for business_customer with customer_id " + bc.getCustomerId() +
                        " affected 0 rows. Data might be unchanged or record missing.");
            }
        } else {
            throw new IllegalArgumentException("Customer object is not an instance of PrivateCustomer or BusinessCustomer.");
        }
        return customer;
    }
}
