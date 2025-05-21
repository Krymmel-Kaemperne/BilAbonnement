package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.BusinessCustomer;
import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Model.CustomerType;
import com.example.bilabonnement.Model.PrivateCustomer;
import com.example.bilabonnement.Service.CustomerService;
import com.example.bilabonnement.Service.ZipcodeService;
import com.example.bilabonnement.Model.Zipcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ZipcodeService zipcodeService;

    @GetMapping("/dataRegistration/customers")
    public String showCustomerOverview(
            @RequestParam(required = false) String searchCustomerId,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Boolean hasActiveRental,
            Model model) {
        
        List<Customer> customers = customerService.findFilteredCustomers(searchCustomerId, customerType, cityId, hasActiveRental);
        model.addAttribute("customers", customers);
        model.addAttribute("availableZipcodes", zipcodeService.findAllZipcodes());

        return "dataRegistration/customers";
    }

    // --- Privatkunde Oprettelse ---
    @GetMapping("/customers/create/private")
    public String showCreatePrivateCustomerForm(Model model) {
        // Tilføj nødvendige data til modellen for den private formular
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes());
        // Initialiser tomme felter for th:value, hvis de ikke allerede er sat (f.eks. efter fejl)
        if (!model.containsAttribute("fName")) model.addAttribute("fName", "");
        // ... tilføj for lName, email, phone, address, zipcodeId, cprNumber
        return "dataRegistration/create-private-customer";
    }

    @PostMapping("/customers/create/private")
    public String createPrivateCustomer(@RequestParam String fName,
                                        @RequestParam String lName,
                                        @RequestParam String email,
                                        @RequestParam String phone,
                                        @RequestParam String address,
                                        @RequestParam int zipcodeId,
                                        @RequestParam String cprNumber, // Kun relevante felter
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        // Validering (f.eks. at cprNumber ikke er tom)
        if (cprNumber == null || cprNumber.trim().isEmpty()) {
            model.addAttribute("errorMessage", "CPR number is required.");
            repopulatePrivateFormForError(model, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/create-private-customer";
        }

        Zipcode zipcode = zipcodeService.findById(zipcodeId);
        if (zipcode == null) {
            model.addAttribute("errorMessage", "Invalid Zipcode selected.");
            repopulatePrivateFormForError(model, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/create-private-customer";
        }

        PrivateCustomer privateCustomer = new PrivateCustomer(fName, lName, email, phone, address, zipcodeId, zipcode, cprNumber);

        try {
            customerService.save(privateCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Private customer '" + fName + " " + lName + "' created successfully!");
            return "redirect:/dataRegistration/customers"; // Eller en anden relevant side
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating private customer: " + e.getMessage());
            e.printStackTrace();
            repopulatePrivateFormForError(model, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/create-private-customer";
        }
    }

    private void repopulatePrivateFormForError(Model model, String fName, String lName, String email, String phone, String address, int zipcodeId, String cprNumber) {
        model.addAttribute("fName", fName);
        model.addAttribute("lName", lName);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("address", address);
        model.addAttribute("zipcodeId", zipcodeId);
        model.addAttribute("cprNumber", cprNumber);
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes()); // Skal stadig med
    }


    // --- Erhvervskunde Oprettelse ---
    @GetMapping("/customers/create/business")
    public String showCreateBusinessCustomerForm(Model model) {
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes());
        // Initialiser tomme felter for th:value
        if (!model.containsAttribute("fName")) model.addAttribute("fName", "");
        // ... tilføj for lName, email, phone, address, zipcodeId, cvrNumber, companyName
        return "dataRegistration/create-business-customer";
    }

    @PostMapping("/customers/create/business")
    public String createBusinessCustomer(@RequestParam String fName, // Måske "contactFName"
                                         @RequestParam String lName, // Måske "contactLName"
                                         @RequestParam String email,
                                         @RequestParam String phone,
                                         @RequestParam String address, // Måske "companyAddress"
                                         @RequestParam int zipcodeId,
                                         @RequestParam String cvrNumber,
                                         @RequestParam String companyName,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        // Validering
        if (cvrNumber == null || cvrNumber.trim().isEmpty() || companyName == null || companyName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "CVR number and Company Name are required.");
            repopulateBusinessFormForError(model, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/create-business-customer";
        }

        Zipcode zipcode = zipcodeService.findById(zipcodeId);
        if (zipcode == null) {
            model.addAttribute("errorMessage", "Invalid Zipcode selected.");
            repopulateBusinessFormForError(model, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/create-business-customer";
        }

        BusinessCustomer businessCustomer = new BusinessCustomer(fName, lName, email, phone, address, zipcodeId, zipcode, cvrNumber, companyName);

        try {
            customerService.save(businessCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Business customer '" + companyName + "' created successfully!");
            return "redirect:/dataRegistration/customers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating business customer: " + e.getMessage());
            e.printStackTrace();
            repopulateBusinessFormForError(model, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/create-business-customer";
        }
    }

    // --- Rediger Kunde ---
    @GetMapping("/customers/edit/{id}")
    public String showEditCustomerForm(@PathVariable("id") int customerId, Model model, RedirectAttributes redirectAttributes) {
        Customer customer = customerService.findById(customerId);

        if (customer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kunde med ID " + customerId + " blev ikke fundet.");
            return "redirect:/kunder"; // Send tilbage til kundelisten (eller en fejlside)
        }

        model.addAttribute("customer", customer);
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes()); // Tilføj postnumre til dropdown

        if (customer.getCustomerType() == CustomerType.PRIVATE) {
            // Hvis det er en privatkunde, skal vi vise den private redigeringsformular
            return "dataRegistration/edit-private-customer";
        } else if (customer.getCustomerType() == CustomerType.BUSINESS) {
            // Hvis det er en erhvervskunde, skal vi vise den erhvervsmæssige redigeringsformular
            return "dataRegistration/edit-business-customer";
        } else {
            // Håndter uventede kundetyper
            redirectAttributes.addFlashAttribute("errorMessage", "Ukendt kundetype for kunde med ID " + customerId);
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/edit/private/{id}")
    public String updatePrivateCustomer(@PathVariable("id") int customerId,
                                        @RequestParam String fName,
                                        @RequestParam String lName,
                                        @RequestParam String email,
                                        @RequestParam String phone,
                                        @RequestParam String address,
                                        @RequestParam int zipcodeId,
                                        @RequestParam String cprNumber,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        if (cprNumber == null || cprNumber.trim().isEmpty()) {
            model.addAttribute("errorMessage", "CPR nummer skal angives.");
            repopulateEditPrivateFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/edit-private-customer";
        }

        Zipcode zipcode = zipcodeService.findById(zipcodeId);
        if (zipcode == null) {
            model.addAttribute("errorMessage", "Ugyldigt postnummer valgt.");
            repopulateEditPrivateFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/edit-private-customer";
        }

        PrivateCustomer updatedCustomer = new PrivateCustomer(customerId, fName, lName, email,
                phone, address, zipcodeId, zipcode, cprNumber);
        try {
            customerService.update(updatedCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Privatkunde " + fName + " " + lName + " Opdateret succesfuldt!");
            return "redirect:/dataRegistration/customers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved opdatering af privatkunde: " + e.getMessage());
            e.printStackTrace(); // Log fejlen for debugging
            repopulateEditPrivateFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cprNumber);
            return "dataRegistration/edit-private-customer";
        }
    }

    @PostMapping("/customers/edit/business/{id}")
    public String updateBusinessCustomer(@PathVariable("id") int customerId,
                                         @RequestParam String fName,
                                         @RequestParam String lName,
                                         @RequestParam String email,
                                         @RequestParam String phone,
                                         @RequestParam String address,
                                         @RequestParam int zipcodeId,
                                         @RequestParam String cvrNumber,
                                         @RequestParam String companyName,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        // Validering
        if (cvrNumber == null || cvrNumber.trim().isEmpty() || companyName == null || companyName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "CVR nummer og Firmanavn skal angives.");
            repopulateEditBusinessFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/edit-business-customer";
        }
        if (!cvrNumber.matches("\\d{8}")) {
            model.addAttribute("errorMessage", "CVR-nummer skal være præcis 8 cifre og kun indeholde tal.");
            repopulateEditBusinessFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/edit-business-customer";
        }

        Zipcode zipcode = zipcodeService.findById(zipcodeId);
        if (zipcode == null) {
            model.addAttribute("errorMessage", "Ugyldigt postnummer valgt.");
            repopulateEditBusinessFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/edit-business-customer";
        }

        // Opret et BusinessCustomer objekt med de opdaterede data
        BusinessCustomer updatedCustomer = new BusinessCustomer(customerId, fName, lName, email, phone, address, zipcodeId, zipcode, cvrNumber, companyName);

        try {
            customerService.update(updatedCustomer); // Kald din update metode i Service
            redirectAttributes.addFlashAttribute("successMessage", "Erhvervskunde '" + companyName + "' opdateret succesfuldt!");
            return "redirect:/dataRegistration/customers"; // Send tilbage til kundelisten
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fejl ved opdatering af erhvervskunde: " + e.getMessage());
            e.printStackTrace(); // Log fejlen for debugging
            repopulateEditBusinessFormForError(model, customerId, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/edit-business-customer";
        }
    }

    private void repopulateBusinessFormForError(Model model, String fName, String lName, String email, String phone, String address, int zipcodeId, String cvrNumber, String companyName) {
        model.addAttribute("fName", fName);
        model.addAttribute("lName", lName);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("address", address);
        model.addAttribute("zipcodeId", zipcodeId);
        model.addAttribute("cvrNumber", cvrNumber);
        model.addAttribute("companyName", companyName);
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes()); // Skal stadig med
    }

    private void repopulateEditPrivateFormForError(Model model, int customerId, String fName, String lName, String email, String phone, String address, int zipcodeId, String cprNumber) {
        // Opret et PrivateCustomer objekt med de indtastede data for at populere formularen
        PrivateCustomer customer = new PrivateCustomer(customerId, fName, lName, email, phone, address, zipcodeId, zipcodeService.findById(zipcodeId), cprNumber);
        model.addAttribute("customer", customer);
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes());
    }

    private void repopulateEditBusinessFormForError(Model model, int customerId, String fName, String lName, String email, String phone, String address, int zipcodeId, String cvrNumber, String companyName) {
        // Opret et BusinessCustomer objekt med de indtastede data for at populere formularen
        BusinessCustomer customer = new BusinessCustomer(customerId, fName, lName, email, phone, address, zipcodeId, zipcodeService.findById(zipcodeId), cvrNumber, companyName);
        model.addAttribute("customer", customer);
        model.addAttribute("zipcodes", zipcodeService.findAllZipcodes());
    }

}
