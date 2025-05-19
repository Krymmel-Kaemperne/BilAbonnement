    package com.example.bilabonnement.Controller;
    import com.example.bilabonnement.Model.Car;
    import com.example.bilabonnement.Model.Customer;
    import com.example.bilabonnement.Model.Location;
    import com.example.bilabonnement.Model.RentalAgreement;
    import com.example.bilabonnement.Service.CarService;
    import com.example.bilabonnement.Service.CustomerService;
    import com.example.bilabonnement.Service.LocationService;
    import com.example.bilabonnement.Service.RentalAgreementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.Collections;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Controller
    @RequestMapping("/dataRegistration")
    public class RentalAgreementController {

        @Autowired
        private RentalAgreementService rentalAgreementService;

        @Autowired
        private CarService carService;

        @Autowired
        private CustomerService customerService;

        @Autowired
        private LocationService locationService;

        // OVERVIEW
        // OVERVIEW
        @GetMapping("/rental-agreements")
        public String showOverview(Model model) {
            try { // <-- Start af try blok
                List<RentalAgreement> agreements = rentalAgreementService.findAll();
                List<Car> allCars = carService.findAllCars();
                List<Customer> allCustomers = customerService.findAllCustomers();

                // Første definition af Maps - Jeg antager, du kun ønsker én version.
                // Jeg beholder denne version, da den ser mest detaljeret ud.
                Map <Integer, String>  carNamesById = allCars.stream()
                        .collect(Collectors.toMap(
                                Car::getCarId,
                                car -> (car.getBrandName() != null ? car.getBrandName() : "") + " " + (car.getModelName() != null ? car.getModelName() : "")
                        ));

                Map<Integer, String> customerNamesById = allCustomers.stream()
                        .collect(Collectors.toMap(
                                Customer::getCustomerId,
                                Customer::getDisplayName // Antager getDisplayName er en metode i Customer
                        ));

                model.addAttribute("agreements", agreements);
                model.addAttribute("carNames", carNamesById);
                model.addAttribute("customerNames", customerNamesById);
                model.addAttribute("searchRentalAgreementId", ""); // Beholder denne

                // Jeg fjerner den dobbelte definition af Maps og addAttribute kald herfra

            } catch (Exception e) { // <-- Slut på try blok, start på catch blok
                e.printStackTrace();
                model.addAttribute("agreements", Collections.emptyList());
                model.addAttribute("errorMessage", "Kunne ikke indlæse lejeaftaler.");
            }
            return "dataRegistration/rental/rental-agreements-overview";
        }


        //SHOW CREATE FORM
    @GetMapping("/rental-agreements/create")
    public String showCreateForm(Model model) {
        try {
            List<Car> allCars = carService.findAllCars(); // Eller findAll()
            List<Car> availableCars = allCars.stream()
                    .filter(car -> car.getCarStatusId() != null && car.getCarStatusId() == 1)
                    .collect(Collectors.toList());

            List<Customer> allCustomers = customerService.findAllCustomers();
            List<Location> allLocations = locationService.findAllLocations(); // Tilføjet for create form

            RentalAgreement rentalAgreement = new RentalAgreement();


            model.addAttribute("rentalAgreement", new RentalAgreement());
            model.addAttribute("availableCars", availableCars); // Ændret fra "cars"
            model.addAttribute("allCustomers", allCustomers);
            model.addAttribute("allLocations", allLocations); // Tilføjet
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Kunne ikke indlæse data til formularen.");
            model.addAttribute("rentalAgreement", new RentalAgreement());
            model.addAttribute("availableCars", Collections.emptyList());
            model.addAttribute("allCustomers", Collections.emptyList());
            model.addAttribute("allLocations", Collections.emptyList());
        }
        return "dataRegistration/rental/create-agreement";
    }

        // CREATE RENTAL AGREEMENT
        @PostMapping("/rental-agreements/create")
        public String createRentalAgreement(@ModelAttribute RentalAgreement rentalAgreement,
                                            Model model,
                                            RedirectAttributes redirectAttributes) {

            if (!rentalAgreement.isEndDateValid()) {
                List<Car> allCars = carService.findAllCars().stream()
                        .filter(car -> car.getCarStatusId() == 1 )
                        .toList();

                List<Customer> allCustomers = customerService.findAllCustomers();

                model.addAttribute("rentalAgreement", rentalAgreement);
                model.addAttribute("cars", allCars);
                model.addAttribute("customers", allCustomers);
                model.addAttribute("errorMessage", "Slutdato skal være mindst 3 måneder efter startdato.");
                return "dataRegistration/rental/create-agreement";
            }

            Car selectedCar = carService.findById(rentalAgreement.getCarId());
            if (selectedCar != null) {
                rentalAgreement.setStartOdometer(selectedCar.getCurrentOdometer());

                selectedCar.setCarStatusId(2);
                carService.update(selectedCar);
            }

            try {
                rentalAgreementService.create(rentalAgreement);
                redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");
            } catch (Exception e) {
                e.printStackTrace(); // Print error to console
                redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved oprettelse!");
        }
        return "redirect:/dataRegistration/rental-agreements"; // Redirect til oversigt
    }

    //SHOW EDIT FORM

    @GetMapping("/rental-agreements/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        RentalAgreement agreement = rentalAgreementService.findById(id);
        if (agreement == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale med ID " + id + " blev ikke fundet.");
            return "redirect:/dataRegistration/rental-agreements";
        }

        try {
            List<Car> allCars = carService.findAllCars();

            Car initialCurrentCarOnAgreement = null; // Midlertidig variabel
            if (agreement.getCarId() != null && agreement.getCarId() > 0) {
                initialCurrentCarOnAgreement = carService.findById(agreement.getCarId());
            }

            // Denne variabel er nu effektivt final for lambdaet nedenfor
            final Car currentCarForFilter = initialCurrentCarOnAgreement;

            List<Car> availableCarsForEdit = allCars.stream()
                    .filter(carInLoop -> {
                        boolean isGenerallyAvailable = (carInLoop.getCarStatusId() != null && carInLoop.getCarStatusId() == 1);
                        boolean isTheCurrentCar = false;
                        if (currentCarForFilter != null) { // Brug den effektivt finale variabel
                            // Antager carInLoop.getCarId() og currentCarForFilter.getCarId() returnerer int
                            isTheCurrentCar = (carInLoop.getCarId() == currentCarForFilter.getCarId());
                        }
                        return isGenerallyAvailable || isTheCurrentCar;
                    })
                    .distinct()
                    .collect(Collectors.toList());
            model.addAttribute("availableCars", availableCarsForEdit);

            List<Customer> allCustomers = customerService.findAllCustomers();
            model.addAttribute("allCustomers", allCustomers);

            List<Location> allLocations = locationService.findAllLocations();
            model.addAttribute("allLocations", allLocations);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved indlæsning af data til redigeringsformularen: " + e.getMessage());
            model.addAttribute("rentalAgreement", agreement);
            model.addAttribute("availableCars", Collections.emptyList());
            model.addAttribute("allCustomers", Collections.emptyList());
            model.addAttribute("allLocations", Collections.emptyList());
            return "dataRegistration/rental/editRentalAgreement";
        }

        model.addAttribute("rentalAgreement", agreement);
        return "dataRegistration/rental/editRentalAgreement";
    }



    // HANDLE EDIT SUBMISSION
    @PostMapping("/rental-agreements/update")
    public String updateRentalAgreement(@ModelAttribute("rentalAgreement") RentalAgreement rentalAgreement,
                                        RedirectAttributes redirectAttributes) {
        try {
            rentalAgreementService.update(rentalAgreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale med ID " + rentalAgreement.getRentalAgreementId() + " opdateret succesfuldt!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dataRegistration/rental-agreements/edit/" + rentalAgreement.getRentalAgreementId();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "En uventet fejl opstod under opdatering.");
        }
        return "redirect:/dataRegistration/rental-agreements";
    }

    // VIEW SINGLE AGREEMENT
    @GetMapping("/rental-agreements/{id}") // Fuld sti: /dataRegistration/rental-agreements/{id}
    public String viewRentalAgreementDetails(@PathVariable("id") int rentalAgreementId,
                                             Model model,
                                             RedirectAttributes redirectAttributes) {
        // logger.info("Viser detaljer for lejeaftale ID: {}", rentalAgreementId);
        RentalAgreement agreement = rentalAgreementService.findById(rentalAgreementId);

        if (agreement == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale med ID " + rentalAgreementId + " blev ikke fundet.");
            return "redirect:/dataRegistration/rental-agreements";
        }
        model.addAttribute("rentalAgreement", agreement);

        try {
            if (agreement.getCarId() != null && agreement.getCarId() > 0) {
                model.addAttribute("carDetails", carService.findById(agreement.getCarId()));
            }
            if (agreement.getCustomerId() != null && agreement.getCustomerId() > 0) {
                model.addAttribute("customerDetails", customerService.findById(agreement.getCustomerId()));
            }
            if (agreement.getPickupLocationId() != null && agreement.getPickupLocationId() > 0) {
                model.addAttribute("pickupLocationDetails", locationService.findLocationById(agreement.getPickupLocationId()));
            }
            if (agreement.getReturnLocationId() != null && agreement.getReturnLocationId() > 0) {
                model.addAttribute("returnLocationDetails", locationService.findLocationById(agreement.getReturnLocationId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Kunne ikke indlæse alle detaljer for lejeaftalen.");
        }
        return "dataRegistration/rental/view-rental-details";
    }

    // DELETE AGREEMENT
   /* @PostMapping("/rental-agreements/delete/{id}") // Fuld sti: /dataRegistration/rental-agreements/delete/{id}
    public String deleteRentalAgreement(@PathVariable int id,
                                        RedirectAttributes redirectAttributes) {
        // logger.info("Forsøger at slette lejeaftale ID: {}", id);
        try {
            rentalAgreementService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale slettet succesfuldt.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved sletning: " + e.getMessage());
        }
        return "redirect:/dataRegistration/rental-agreements";
    }*/
}
