package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Model.Damage;
import com.example.bilabonnement.Service.ConditionReportService;
import com.example.bilabonnement.Service.DamageService;
import com.example.bilabonnement.Service.RentalAgreementService;
import com.example.bilabonnement.Service.CustomerService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.RentalAgreement; // Sørg for denne er importeret
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate; // Importer LocalDate
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

// Controller til håndtering af tilstandsrapporter og skader
@Controller
@RequestMapping("/conditionReport")
public class ConditionReportController {

    // BRUG KONSTRUKTØR-INJEKTION I STEDET FOR FELT-INJEKTION
    private final ConditionReportService conditionReportService;
    private final DamageService damageService;
    private final RentalAgreementService rentalAgreementService;
    private final CustomerService customerService;
    private final CarService carService;

    @Autowired
    public ConditionReportController(ConditionReportService conditionReportService,
                                     DamageService damageService,
                                     RentalAgreementService rentalAgreementService,
                                     CustomerService customerService,
                                     CarService carService) {
        this.conditionReportService = conditionReportService;
        this.damageService = damageService;
        this.rentalAgreementService = rentalAgreementService;
        this.customerService = customerService;
        this.carService = carService;
    }

    @GetMapping("/list")
    public String listAll(Model model) {
        // Overvej at flytte denne logik til en service metode som findAllWithDetails()
        List<ConditionReport> basicReports = conditionReportService.findAll();
        List<ConditionReport> detailedReports = new ArrayList<>();

        if (basicReports != null) {
            for (ConditionReport basicReport : basicReports) {
                ConditionReport detailedReport = conditionReportService.getConditionReportWithDetails(basicReport.getConditionReportId());
                if (detailedReport != null) {
                    detailedReports.add(detailedReport);
                } else {
                    detailedReports.add(basicReport); // Fallback
                }
            }
        }
        model.addAttribute("reports", detailedReports);
        return "damageRegistration/rapports";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable int id, Model model) {
        ConditionReport reportWithDetails = conditionReportService.getConditionReportWithDetails(id);
        if (reportWithDetails == null) {
            // Håndter hvis rapport ikke findes
            return "redirect:/conditionReport/list?error=notFound";
        }
        model.addAttribute("report", reportWithDetails);
        // Tilføj damages og totalDamagePrice til modellet, så template virker som før
        model.addAttribute("damages", reportWithDetails.getDamages() != null ? reportWithDetails.getDamages() : new ArrayList<>());
        model.addAttribute("totalDamagePrice", reportWithDetails.getTotalPrice() != null ? reportWithDetails.getTotalPrice() : java.math.BigDecimal.ZERO);
        return "damageRegistration/damageRegistration";
    }

    // OPDATERET showCreateForm METODE
    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) Integer rentalAgreementId, Model model) {
        ConditionReport report = new ConditionReport();
        if (rentalAgreementId != null) {
            report.setRentalAgreementId(rentalAgreementId);
        }
        // Hent lejeaftaler (overvej en metode som findAllActiveOrRelevant() i din service)
        List<RentalAgreement> rentalAgreements = rentalAgreementService.findAll(); // Eller findAllActiveOrRelevant()

        // Kald den nye private hjælpemetode
        model.addAttribute("rentalAgreementOptions", formatRentalAgreementsForDropdown(rentalAgreements));

        model.addAttribute("report", report);
        return "damageRegistration/createConditionReport";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        // Sæt dato hvis den ikke er sat
        if (report.getReportDate() == null) {
            report.setReportDate(LocalDate.now());
        }
        ConditionReport created = conditionReportService.create(report);
        if (created == null || created.getConditionReportId() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl: Kunne ikke oprette tilstandsrapport.");
            return "redirect:/conditionReport/create";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport oprettet!");
        return "redirect:/conditionReport/view/" + created.getConditionReportId();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        ConditionReport reportWithDetails = conditionReportService.getConditionReportWithDetails(id);
        if (reportWithDetails == null) {
            return "redirect:/conditionReport/list?error=notFound";
        }
        if (reportWithDetails.getReportDate() == null) {
            reportWithDetails.setReportDate(java.time.LocalDate.now());
        }
        model.addAttribute("report", reportWithDetails);
        return "damageRegistration/editConditionReport";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        if (report.getReportDate() == null) {
            report.setReportDate(LocalDate.now());
        }

        int rowsAffected = conditionReportService.update(report); // Modtag int

        if (rowsAffected > 0) { // Tjek om mindst én række blev opdateret
            redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport opdateret succesfuldt!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl: Kunne ikke opdatere tilstandsrapport (rapport ikke fundet eller ingen ændringer).");
            //redirecte tilbage til edit-siden med fejlen og de indtastede data,
             redirectAttributes.addFlashAttribute("report", report); // Send rapporten tilbage
             return "redirect:/conditionReport/edit/" + report.getConditionReportId();
        }
        return "redirect:/conditionReport/list"; // Eller redirect til view-siden for den opdaterede rapport
    }


    // Damage endpoints (forbliver som de var, med mindre du også vil refaktorere dem)
    @GetMapping("/damage/create")
    public String showCreateDamageForm(@RequestParam int conditionReportId, Model model) {
        Damage damage = new Damage();
        damage.setConditionReportId(conditionReportId);
        model.addAttribute("damage", damage);
        model.addAttribute("report", conditionReportService.findById(conditionReportId));
        return "damageRegistration/registerDamage";
    }

    @PostMapping("/damage/create")
    public String createDamage(@ModelAttribute Damage damage, RedirectAttributes redirectAttributes) {
        damageService.create(damage);
        redirectAttributes.addFlashAttribute("successMessage", "Skade oprettet!");
        return "redirect:/conditionReport/view/" + damage.getConditionReportId();
    }

    @GetMapping("/damage/edit/{id}") // Ændret til {id} for at matche path variable navnet
    public String showEditDamageForm(@PathVariable int id, Model model) { // Ændret parameter til id
        Damage damage = damageService.findById(id);
        if (damage == null) {
            return "redirect:/conditionReport/list?error=damageNotFound";
        }
        model.addAttribute("damage", damage);
        model.addAttribute("report", conditionReportService.findById(damage.getConditionReportId()));
        return "damageRegistration/editDamage";
    }

    @PostMapping("/damage/edit")
    public String editDamage(@ModelAttribute Damage damage, RedirectAttributes redirectAttributes) {
        damageService.update(damage);
        redirectAttributes.addFlashAttribute("successMessage", "Skade opdateret!");
        return "redirect:/conditionReport/view/" + damage.getConditionReportId();
    }

    @PostMapping("/damage/delete/{id}")
    public String deleteDamage(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Damage damage = damageService.findById(id);
        if (damage != null) {
            int conditionReportId = damage.getConditionReportId();
            damageService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Skade slettet!");
            return "redirect:/conditionReport/view/" + conditionReportId;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Skade ikke fundet.");
            return "redirect:/conditionReport/list";
        }
    }

    @GetMapping("/reparationStatus")
    public String showReparationStatus(Model model) {
        double averageDamagesPerRental = conditionReportService.getAverageDamagesPerRental();
        double averageDamagePrice = conditionReportService.getAverageDamagePrice();
        model.addAttribute("averageDamagesPerRental", averageDamagesPerRental);
        model.addAttribute("averageDamagePrice", averageDamagePrice);
        return "damageRegistration/reparationStatus";
    }

    // ========================================================================
    // HER INDSÆTTES DEN PRIVATE HJÆLPEMETODE
    // ========================================================================
    private List<String[]> formatRentalAgreementsForDropdown(List<RentalAgreement> agreements) {
        if (agreements == null) {
            return new ArrayList<>();
        }

        List<String[]> dropdownOptions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (RentalAgreement agreement : agreements) {
            Customer customer = customerService.findById(agreement.getCustomerId());
            String customerName = (customer != null) ? customer.getFname() + " " + customer.getLname() : "Ukendt Kunde";

            Car car = carService.findById(agreement.getCarId());
            String carIdentifier = "Ukendt Bil";
            if (car != null) {
                carIdentifier = (car.getRegistrationNumber() != null && !car.getRegistrationNumber().isEmpty()) ?
                        car.getRegistrationNumber() :
                        ((car.getChassisNumber() != null && !car.getChassisNumber().isEmpty()) ?
                                "Stel: " + car.getChassisNumber() : "Ukendt Bil");
            }


            String startDateString = (agreement.getStartDate() != null) ? agreement.getStartDate().format(formatter) : "N/A";
            String endDateString = (agreement.getEndDate() != null) ? agreement.getEndDate().format(formatter) : "N/A";

            String displayText = String.format("ID: %d | %s | Bil: %s | %s - %s",
                    agreement.getRentalAgreementId(),
                    customerName,
                    carIdentifier,
                    startDateString,
                    endDateString);

            dropdownOptions.add(new String[]{String.valueOf(agreement.getRentalAgreementId()), displayText});
        }
        return dropdownOptions;
    }
    // ========================================================================
    // SLUT PÅ HJÆLPEMETODE
    // ========================================================================

} // SLUT PÅ ConditionReportController KLASSEN
