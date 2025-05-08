package com.example.bilabonnement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    //Data-registering Navigation
    @GetMapping("/dataRegistrering")
    public String dataRegistrering() {
        return "dataRegistrering/dataRegistrering";
    }

    @GetMapping("/dataRegistrering/fleet")
    public String fleet() {
        return "dataRegistrering/fleet";
    }

    @GetMapping("/dataRegistrering/kunder")
    public String kunder() {
        return "dataRegistrering/kunder";
    }

    @GetMapping("/dataRegistrering/lejeaftaler")
    public String lejeaftaler() {
        return "dataRegistrering/lejeaftaler";
    }

    //Skade-registering Navigation


    @GetMapping("/skadeRegistrering")
    public String skadeRegistrering() {
        return "skadeRegistrering";
    }

    @GetMapping("/forretningsData")
    public String forretningsData() {
        return "forretningsData";
    }
}
