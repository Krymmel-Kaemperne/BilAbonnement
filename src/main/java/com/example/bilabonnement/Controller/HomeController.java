package com.example.bilabonnement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dataRegistration")
    public String dataRegistration() {
        return "dataRegistration/dataRegistration";
    }

    @GetMapping("/dataRegistration/fleet")
    public String fleet() {
        return "dataRegistration/fleet";
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
}