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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ZipcodeService zipcodeService;

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
            return "redirect:/kunder"; // Eller en anden relevant side
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
            return "redirect:/kunder";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating business customer: " + e.getMessage());
            e.printStackTrace();
            repopulateBusinessFormForError(model, fName, lName, email, phone, address, zipcodeId, cvrNumber, companyName);
            return "dataRegistration/create-business-customer";
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

}
