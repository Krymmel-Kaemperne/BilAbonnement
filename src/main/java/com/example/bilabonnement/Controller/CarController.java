package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.Car;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dataRegistrering")
public class CarController {

    @GetMapping("/opretbil")
    public String showCreateCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "dataregistrering/opretbil";
    }

    @PostMapping("/opretbil")
    public String createCar(@ModelAttribute Car car) {
        System.out.println("Modtaget bildata:");
        System.out.println("Registreringsnummer: " + car.getRegistrationNumber());
        System.out.println("Stelnummer: " + car.getChassisNumber());
        System.out.println("Stålpris: " + car.getSteelPrice());
        System.out.println("Farve: " + car.getColor());
        System.out.println("CO2-udledning: " + car.getCo2Emission());
        System.out.println("Køretøjsnummer: " + car.getVehicleNumber());
        return "redirect:/fleet";
    }
}
