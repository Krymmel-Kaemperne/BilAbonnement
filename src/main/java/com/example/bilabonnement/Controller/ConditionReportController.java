package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Model.Damage;
import com.example.bilabonnement.Service.ConditionReportService;
import com.example.bilabonnement.Service.DamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

// Controller til håndtering af tilstandsrapporter og skader
@Controller
@RequestMapping("/conditionReport")
public class ConditionReportController {
    @Autowired
    private ConditionReportService conditionReportService;
    @Autowired
    private DamageService damageService;

    @GetMapping("/list")
    public String listAll(Model model) {
        List<ConditionReport> basicReports = conditionReportService.findAll();
        List<ConditionReport> detailedReports = new ArrayList<>();

        if (basicReports != null) {
            for (ConditionReport basicReport : basicReports) {
                // Fetch and populate details for each report
                ConditionReport detailedReport = conditionReportService.getConditionReportWithDetails(basicReport.getConditionReportId());
                if (detailedReport != null) {
                    detailedReports.add(detailedReport);
                } else {
                    // Fallback: add the basic report if details couldn't be fetched (should be logged)
                    detailedReports.add(basicReport);
                }
            }
        }
        model.addAttribute("reports", detailedReports);
        return "damageRegistration/rapports";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable int id, Model model) {
        // Use the new service method to get all details
        ConditionReport reportWithDetails = conditionReportService.getConditionReportWithDetails(id);
        
        model.addAttribute("report", reportWithDetails);
        // The damages and total price are now part of reportWithDetails if correctly populated by the service
        // So, explicitly adding damages and totalDamagePrice might be redundant if the template uses report.damages and report.totalPrice
        // However, if damageRegistration.html specifically expects 'damages' and 'totalDamagePrice' as separate attributes, keep them.
        if (reportWithDetails != null && reportWithDetails.getDamages() != null) {
            model.addAttribute("damages", reportWithDetails.getDamages());
            model.addAttribute("totalDamagePrice", reportWithDetails.getTotalPrice()); // Already calculated by getConditionReportWithDetails
        } else {
            model.addAttribute("damages", new ArrayList<Damage>());
            model.addAttribute("totalDamagePrice", java.math.BigDecimal.ZERO);
        }

        return "damageRegistration/damageRegistration";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) Integer rentalAgreementId, Model model) {
        ConditionReport report = new ConditionReport();
        if (rentalAgreementId != null) {
            report.setRentalAgreementId(rentalAgreementId);
            // Optionally, pre-populate car/customer details if rentalAgreementId is present
            // RentalAgreement agreement = rentalAgreementService.findById(rentalAgreementId);
            // if (agreement != null) { ... set car and customer on report ... }
        }
        model.addAttribute("report", report);
        return "damageRegistration/createConditionReport";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        ConditionReport created = conditionReportService.create(report);
        // After creating, you might want to fetch with details if redirecting to a view page that needs them
        redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport oprettet!");
        return "redirect:/conditionReport/view/" + created.getConditionReportId(); // viewReport should now fetch details
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        // IMPORTANT: Use getConditionReportWithDetails here as well for the edit page
        ConditionReport reportWithDetails = conditionReportService.getConditionReportWithDetails(id);
        model.addAttribute("report", reportWithDetails);
        return "damageRegistration/editConditionReport";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        conditionReportService.update(report); // The update service only needs the core fields
        redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport opdateret!");
        // Redirect to the view page, which will fetch details again
        return "redirect:/conditionReport/view/" + report.getConditionReportId();
    }

    // Damage endpoints
    @GetMapping("/damage/create")
    public String showCreateDamageForm(@RequestParam int conditionReportId, Model model) {
        Damage damage = new Damage();
        damage.setConditionReportId(conditionReportId);
        // Optionally, add the condition report itself to the model for context
        // ConditionReport conditionReport = conditionReportService.getConditionReportWithDetails(conditionReportId);
        // model.addAttribute("conditionReport", conditionReport);
        return "damageRegistration/registerDamage";
    }

    @PostMapping("/damage/create")
    public String createDamage(@ModelAttribute Damage damage, RedirectAttributes redirectAttributes) {
        damageService.create(damage);
        redirectAttributes.addFlashAttribute("successMessage", "Skade oprettet!");
        return "redirect:/conditionReport/view/" + damage.getConditionReportId();
    }

    @GetMapping("/damage/edit/{id}")
    public String showEditDamageForm(@PathVariable int id, Model model) {
        Damage damage = damageService.findById(id);
        model.addAttribute("damage", damage);
        // Optionally, add the condition report for context
        // if (damage != null) {
        //    ConditionReport conditionReport = conditionReportService.getConditionReportWithDetails(damage.getConditionReportId());
        //    model.addAttribute("conditionReport", conditionReport);
        // }
        return "damageRegistration/editDamage";
    }

    @PostMapping("/damage/edit")
    public String editDamage(@ModelAttribute Damage damage, RedirectAttributes redirectAttributes) {
        damageService.update(damage);
        redirectAttributes.addFlashAttribute("successMessage", "Skade opdateret!");
        return "redirect:/conditionReport/view/" + damage.getConditionReportId();
    }
} 