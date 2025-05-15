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
        List<ConditionReport> reports = conditionReportService.findAll();
        model.addAttribute("reports", reports);
        return "damageRegistration/rapports";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable int id, Model model) {
        ConditionReport report = conditionReportService.findById(id);
        List<Damage> damages = damageService.findByConditionReportId(id);
        model.addAttribute("report", report);
        model.addAttribute("damages", damages);

        // Beregn totalpris som summen af alle skader med for-each loop
        java.math.BigDecimal totalDamagePrice = java.math.BigDecimal.ZERO;
        for (Damage damage : damages) {
            if (damage.getDamagePrice() != null) {
                totalDamagePrice = totalDamagePrice.add(damage.getDamagePrice());
            }
        }
        model.addAttribute("totalDamagePrice", totalDamagePrice);

        return "damageRegistration/damageRegistration";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam int rentalAgreementId, Model model) {
        ConditionReport report = new ConditionReport();
        report.setRentalAgreementId(rentalAgreementId);
        model.addAttribute("report", report);
        return "damageRegistration/createConditionReport";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        ConditionReport created = conditionReportService.create(report);
        redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport oprettet!");
        return "redirect:/conditionReport/view/" + created.getConditionReportId();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        ConditionReport report = conditionReportService.findById(id);
        model.addAttribute("report", report);
        return "damageRegistration/editConditionReport";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute ConditionReport report, RedirectAttributes redirectAttributes) {
        conditionReportService.update(report);
        redirectAttributes.addFlashAttribute("successMessage", "Tilstandsrapport opdateret!");
        return "redirect:/conditionReport/view/" + report.getConditionReportId();
    }

    // Damage endpoints
    @GetMapping("/damage/create")
    public String showCreateDamageForm(@RequestParam int conditionReportId, Model model) {
        Damage damage = new Damage();
        damage.setConditionReportId(conditionReportId);
        model.addAttribute("damage", damage);
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
        return "damageRegistration/editDamage";
    }

    @PostMapping("/damage/edit")
    public String editDamage(@ModelAttribute Damage damage, RedirectAttributes redirectAttributes) {
        damageService.update(damage);
        redirectAttributes.addFlashAttribute("successMessage", "Skade opdateret!");
        return "redirect:/conditionReport/view/" + damage.getConditionReportId();
    }
} 