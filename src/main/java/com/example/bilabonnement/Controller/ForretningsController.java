package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.ForretningsModel;
import com.example.bilabonnement.Service.ForretningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForretningsController {

    @Autowired
    private ForretningsService forretningsService;

    @GetMapping("/forretningsdata/omsætning")
    public String showOmsaetningDashboard(Model model) {
        ForretningsModel metrics = forretningsService.getMetrics();
        model.addAttribute("metrics", metrics);
        return "businessData/omsætning";
    }

    @GetMapping("/forretningsdata/fleet")
    public String showFleetDashboard(Model model) {
        ForretningsModel metrics = forretningsService.getMetrics();
        model.addAttribute("metrics", metrics);
        return "businessData/fleet";
    }

    @GetMapping("/forretningsdata/andre")
    public String showAndreDashboard(Model model) {
        ForretningsModel metrics = forretningsService.getMetrics();
        model.addAttribute("metrics", metrics);
        return "businessData/andre";
    }
} 