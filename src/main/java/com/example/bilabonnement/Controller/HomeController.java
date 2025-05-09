package com.example.bilabonnement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    //NAVIGATION Data-Registration

    @GetMapping("/dataRegistration")
    public String dataRegistration() {
        return "dataRegistration/dataRegistration";
    }

    @GetMapping("/dataRegistration/fleet")
    public String fleet() {
        return "dataRegistration/fleet";
    }

    @GetMapping("/dataRegistration/clients")
    public String clients() {
        return "dataRegistration/clients";
    }

    @GetMapping("/dataRegistration/rentalAgreements")
    public String rentalAgreements() {
        return "dataRegistration/rentalAgreements";
    }

    //NAVIGATION Damage-Registration

    @GetMapping("/damageRegistration")
    public String damageRegistration() {
        return "damageRegistration/damageRegistration";
    }

    @GetMapping("/damageRegistration/registerDamage")
    public String registerDamage() {
        return "damageRegistration/registerDamage";
    }

    @GetMapping("/damageRegistration/rapports")
    public String rapports() {
        return "damageRegistration/rapports";
    }

    @GetMapping("/damageRegistration/reparationStatus")
    public String reparationStatus() {
        return "damageRegistration/reparationStatus";
    }

    // NAVIGATION Business-Data

    @GetMapping("/businessData")
    public String businessData() {
        return "businessData/businessData";
    }

    @GetMapping("/businessData/fleetStatistics")
    public String fleetStatistics() {
        return "businessData/fleetStatistics";
    }

    @GetMapping("/businessData/paymentStatistics")
    public String paymentStatistics() {
        return "businessData/paymentStatistics";
    }

    @GetMapping("/businessData/revenue")
    public String revenue() {
        return "businessData/revenue";
    }
}