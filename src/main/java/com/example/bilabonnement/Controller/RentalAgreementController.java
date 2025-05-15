    package com.example.bilabonnement.Controller;

    import com.example.bilabonnement.Model.RentalAgreement;
    import com.example.bilabonnement.Service.RentalAgreementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.List;

    @Controller
    @RequestMapping("/dataRegistration")
    public class RentalAgreementController {

        @Autowired
        private RentalAgreementService rentalAgreementService;

        // OVERVIEW
        @GetMapping("/rental-agreements")
        public String showOverview(Model model) {
            List<RentalAgreement> agreements = rentalAgreementService.findAll();
            model.addAttribute("agreements", agreements);
            return "dataRegistration/rental/rental-agreements-overview";
        }

        //SHOW CREATE FORM
        @GetMapping("/rental-agreements/create")
        public String showCreateForm(Model model) {
            model.addAttribute("rentalAgreement", new RentalAgreement());
            return "dataRegistration/rental/create-agreement";
        }

        // HANDLE CREATE FORM SUBMISSION
        @PostMapping("/rental-agreements/create")
        public String createRentalAgreement(@ModelAttribute RentalAgreement rentalAgreement,
                                            RedirectAttributes redirectAttributes) {
            rentalAgreementService.create(rentalAgreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");
            return "redirect:/dataRegistration/rental-agreements";
        }

        //SHOW EDIT FORM
        @GetMapping("/rental-agreements/edit/{id}")
        public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
            RentalAgreement agreement = rentalAgreementService.findById(id);
            if (agreement == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale ikke fundet.");
                return "redirect:/dataRegistration/rental-agreements";
            }
            model.addAttribute("rentalAgreement", agreement);
            return "dataRegistration/rental/editRentalAgreement";
        }

        // HANDLE EDIT SUBMISSION
        @PostMapping("/rental-agreements/update")
        public String updateRentalAgreement(@ModelAttribute RentalAgreement rentalAgreement,
                                            RedirectAttributes redirectAttributes) {
            rentalAgreementService.update(rentalAgreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale opdateret!");
            return "redirect:/dataRegistration/rental-agreements";
        }

        // VIEW SINGLE AGREEMENT
        @GetMapping("/rental-agreements/{id}")
        public String viewRentalAgreement(@PathVariable int id, Model model) {
            RentalAgreement agreement = rentalAgreementService.findById(id);
            if (agreement == null) {
                return "redirect:/dataRegistration/rental-agreements";
            }
            model.addAttribute("rentalAgreement", agreement);
            return "dataRegistration/rental/viewRentalAgreement";
        }

        // DELETE AGREEMENT
        @PostMapping("/rental-agreements/delete/{id}")
        public String deleteRentalAgreement(@PathVariable int id,
                                            RedirectAttributes redirectAttributes) {
            rentalAgreementService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale slettet.");
            return "redirect:/dataRegistration/rental-agreements";
        }
    }
