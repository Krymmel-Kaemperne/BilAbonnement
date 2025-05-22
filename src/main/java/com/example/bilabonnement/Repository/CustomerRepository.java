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

    /**
     * Finder en kunde baseret på kundens ID og henter relaterede data
     * fra postnummer, private_customer og business_customer tabellerne.
     * Bruger en custom RowMapper til at håndtere forskellige kundetyper.
     */
    public Customer findById(int customerId) {
        String sqlBaseCustomer = "SELECT c.*, z.zip_code, z.city_name FROM customer c " +
                "LEFT JOIN zipcode z ON c.zipcode_id = z.zipcode_id WHERE c.customer_id = ?";
        try {
            // Henter basis kundedata og postnummer ved hjælp af en custom RowMapper.
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

            // Baseret på kundetypen, hentes de specifikke data fra den relevante tabel.
            if (baseCustomerData.getCustomerType() == CustomerType.PRIVATE) {
                String sqlPrivate = "SELECT * FROM private_customer WHERE customer_id = ?";
                // Henter private kundedata og opretter et PrivateCustomer objekt.
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
                // Henter business kundedata og opretter et BusinessCustomer objekt.
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
                    baseCustomerData.getCustomerType() + " for customer ID: " + customerId); // Håndterer ukendte kundetyper

        } catch (EmptyResultDataAccessException e) {
            // Håndterer tilfælde hvor kunden ikke findes i customer tabellen.
            System.err.println("Info: Customer with ID " + customerId + " not found.");
            return null; // Det er acceptabelt at returnere null, hvis kunden ikke findes.
        }
    }

    /**
     * Gemmer en ny kunde (både base kundedata og type-specifikke data) i databasen.
     * Indsætter først i customer tabellen og derefter i den type-specifikke tabel.
     */
    public Customer save(Customer customer) {
        // Validerer at postnummer ID er gyldigt og postnummeret eksisterer.
        if(customer.getZipcodeId() <= 0) {
            throw new IllegalArgumentException("Zipcode Id must be provided to save customer");
        }
        Zipcode zipcode = zipcodeRepository.findById(customer.getZipcodeId());
        if(zipcode == null) {
            throw new IllegalArgumentException("Invalid Zipcode ID" + customer.getZipcodeId() + "Zipcode does not exist");
        } customer.setZipcode(zipcode);

        // Indsætter data i customer tabellen.
        String sqlCustomer = "INSERT INTO customer (fname, lname, email, phone, address, zipcode_id, customer_type)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // Bruges til at hente den genererede kundens ID.

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
        // Sætter den genererede kundens ID på kundeobjektet.
        if(keyHolder.getKey() == null) {
            throw new RuntimeException("Failed to save customer, no ID obtained for customer table");
        }
        customer.setCustomerId(keyHolder.getKey().intValue());

        // Indsætter type-specifikke data i den relevante tabel.
        if(customer instanceof PrivateCustomer pc) {
            String sqlPrivate = "INSERT INTO private_customer (customer_id, cpr_number) VALUES (?, ?)";
            jdbcTemplate.update(sqlPrivate, pc.getCustomerId(), pc.getCprNumber());
        } else if (customer instanceof BusinessCustomer bc) {
            String sqlBusiness = "INSERT INTO business_customer (customer_id, cvr_number, company_name) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlBusiness, bc.getCustomerId(), bc.getCvrNumber(), bc.getCompanyName());
        }
        return customer; // Returnerer det opdaterede kundeobjekt med ID.
    }

    /**
     * Finder alle kunder (både private og business) med deres relaterede data.
     * Bruger JOINs til at hente data fra alle relevante tabeller i én forespørgsel.
     */
    public List<Customer> findAll() {
        // SQL forespørgsel der joiner customer, zipcode, private_customer og business_customer.
        String sql = "SELECT c.*, z.zip_code, z.city_name, " +
                "pc.cpr_number, bc.cvr_number, bc.company_name FROM customer c " +
                "LEFT JOIN zipcode z ON c.zipcode_id = z.zipcode_id " +
                "LEFT JOIN private_customer pc ON c.customer_id = pc.customer_id AND c.customer_type = 'PRIVATE' " +
                "LEFT JOIN business_customer bc ON c.customer_id = bc.customer_id AND c.customer_type = 'BUSINESS'";

        // Bruger en custom RowMapper til at mappe hver række til enten PrivateCustomer eller BusinessCustomer.
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

            Zipcode zipcode = new Zipcode(); // Opretter Zipcode objekt.
            zipcode.setZipcodeId(zipcodeId);
            zipcode.setZipcode(rs.getString("zip_code"));
            zipcode.setCityName(rs.getString("city_name"));

            // Opretter den korrekte subklasse baseret på kundetypen.
            if (type == CustomerType.PRIVATE) {
                PrivateCustomer pc = new PrivateCustomer(
                        customerId, fName, lName, email, phone, address, zipcodeId, zipcode,
                        rs.getString("cpr_number") // Henter CPR nummer fra private_customer (kan være null pga LEFT JOIN)
                );
                customer = pc;
            } else if (type == CustomerType.BUSINESS) {
                BusinessCustomer bc = new BusinessCustomer(
                        customerId, fName, lName, email, phone, address, zipcodeId, zipcode,
                        rs.getString("cvr_number"), // Henter CVR nummer fra business_customer (kan være null)
                        rs.getString("company_name") // Henter firmanavn fra business_customer (kan være null)
                );
                customer = bc;
            }

            // Fejlhåndtering for uventede kundetyper (skulle ikke ske hvis data er konsistente).
            if (customer == null) {
                System.err.println("Critical Error: Could not map row for customer_id: " + customerId + " " +
                        "with type: " + type + ". This should not happen.");
                throw new IllegalStateException("Failed to map customer with ID: " + customerId + " and type: " + type);
            }
            return customer; // Returnerer det mappede kundeobjekt.
        });
    }

    /**
     * Opdaterer en eksisterende kunde (både base kundedata og type-specifikke data) i databasen.
     * Opdaterer i customer tabellen og derefter i den type-specifikke tabel.
     */
    public Customer update(Customer customer) {
        // Validerer kundeobjektet og ID.
        if(customer == null || customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Customer to be updated must have a valid Id");
        }

        // Validering og håndtering af postnummer. Sikrer at postnummeret findes.
        if(customer.getZipcode() == null || customer.getZipcode().getZipcodeId() <= 0) {
            if (customer.getZipcodeId() > 0) {
                Zipcode zipcode = zipcodeRepository.findById(customer.getZipcodeId());
                if (zipcode == null) {
                    throw new IllegalArgumentException("Invalid Zipcode ID:" + customer.getZipcodeId() + ". Zipcode does not exist for update");
                }
                customer.setZipcode(zipcode);
            } else {
                throw new IllegalArgumentException("Zipcode ID or Zipcode object with valid ID must be provided for update.");
            }
        }
        if (customer.getZipcodeId() <= 0 && customer.getZipcode() != null) {
            customer.setZipcodeId(customer.getZipcode().getZipcodeId());
        }

        // Opdaterer data i customer tabellen.
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

        // Tjekker om opdateringen af base data lykkedes.
        if(rowsAffectedCustomer == 0) {
            throw new RuntimeException("Failed to update customer (base data). Customer with ID " +
                    customer.getCustomerId() + " not found or no data changed.");
        }

        // Opdaterer type-specifikke data i den relevante tabel.
        if (customer instanceof PrivateCustomer pc) {
            String sqlUpdatePrivate = "UPDATE private_customer SET cpr_number = ? WHERE customer_id = ?";
            int rowsAffectedPrivate = jdbcTemplate.update(sqlUpdatePrivate,
                    pc.getCprNumber(),
                    pc.getCustomerId());
            // Logger en advarsel hvis opdateringen af type-specifik data ikke påvirkede nogen rækker.
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
            // Logger en advarsel hvis opdateringen af type-specifik data ikke påvirkede nogen rækker.
            if (rowsAffectedBusiness == 0) {
                System.err.println("Warning: Update for business_customer with customer_id " + bc.getCustomerId() +
                        " affected 0 rows. Data might be unchanged or record missing.");
            }
        } else {
            // Kaster en fejl hvis kundeobjektet er af en ukendt type.
            throw new IllegalArgumentException("Customer object is not an instance of PrivateCustomer or BusinessCustomer.");
        }
        return customer; // Returnerer det opdaterede kundeobjekt.
    }
}
