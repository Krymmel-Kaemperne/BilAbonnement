package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.*;
import com.example.bilabonnement.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
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

    @Autowired
    private ConditionReportService conditionReportService;

    // vis liste af lejafatler
    @GetMapping("/rental-agreements")
    public String showOverview(@RequestParam(value = "searchRentalAgreementId", required = false) Integer searchId,
                               @RequestParam(value = "statusFilter", required = false) String statusFilter,
                               Model model) {

        LocalDate today = LocalDate.now();

        List<RentalAgreement> agreements = rentalAgreementService.findAll();
        List<Car> allCars = carService.findAllCars();
        List<Customer> allCustomers = customerService.findAllCustomers();


        //search filter
        if (searchId != null)
        {
            RentalAgreement agreement = rentalAgreementService.findById(searchId);
            if (agreement != null)
            {
                agreements = List.of(agreement);
            } else
            {
                agreements = List.of();
            }
            model.addAttribute("errorMessage", "Lejeaftale med ID " + searchId + " blev ikke fundet.");
        } else
        {
            agreements = rentalAgreementService.findAll();
        }

        //status filter
        if (statusFilter != null && !statusFilter.isEmpty())
        {
            agreements = agreements.stream()
                    .filter(agreement -> {
                        LocalDate start = agreement.getStartDate();
                        LocalDate end = agreement.getEndDate();
                        return switch (statusFilter)
                        {
                            case "upcoming" -> start != null && start.isAfter(today);
                            case "active" ->
                                    start != null && !start.isAfter(today) && (end == null || end.isAfter(today));
                            case "completed" -> end != null && end.isBefore(today);
                            default -> true;
                        };
                    })
                    .toList();
        }

        Map<Integer, String> carNamesById = allCars.stream()
                .collect(Collectors.toMap(
                        Car::getCarId,

                        car -> car.getBrandName() + " " + car.getModelName()
                ));



        Map<Integer, String> customerNamesById = allCustomers.stream()
                .collect(Collectors.toMap(
                        Customer::getCustomerId,
                        c -> c.getFname() + " " + c.getLname()
                ));



        model.addAttribute("agreements", agreements);
        model.addAttribute("carNames", carNamesById);
        model.addAttribute("customerNames", customerNamesById);
        model.addAttribute("searchRentalAgreementId", "");
        model.addAttribute("searchRentalAgreementId", searchId != null ? searchId : "");

        return "dataRegistration/rental/rental-agreements-overview";
    }



    // Vis en opret lejeaftale formular
    @GetMapping("/rental-agreements/create")
    public String showCreateForm(Model model) {
        List<Car> allCars = carService.findAllCars();
        List<Location> allLocations = locationService.findAllLocations();
        List<Customer> allCustomers = customerService.findAllCustomers();

        // Filter cars with car_status_id == 1 (Tilgængelig)
        List<Car> availableCars = allCars.stream()
                .filter(car -> car.getCarStatusId() == 1)
                .toList();

        RentalAgreement rentalAgreement = new RentalAgreement();

        // Generate a new leasing code (e.g., "LEASING-0010")
        int nextId = rentalAgreementService.findNextId(); // You need to implement this
        String generatedCode = "LA" + String.format("%04d", nextId);

        rentalAgreement.setLeasingCode(generatedCode);

        model.addAttribute("rentalAgreement", rentalAgreement);
        model.addAttribute("cars", availableCars);
        model.addAttribute("customers", allCustomers);
        model.addAttribute("locations", allLocations);
        return "dataRegistration/rental/create-agreement";
    }


    // Opret en rental agreement
    @PostMapping("/rental-agreements/create")
    public String createRentalAgreement(@ModelAttribute RentalAgreement rentalAgreement,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {

        if (!rentalAgreement.isEndDateValid())
        {
            List<Car> allCars = carService.findAllCars().stream()
                    .filter(car -> car.getCarStatusId() == 1)
                    .toList();

            List<Customer> allCustomers = customerService.findAllCustomers();
            List<Location> allLocations = locationService.findAllLocations();

            model.addAttribute("rentalAgreement", rentalAgreement);
            model.addAttribute("cars", allCars);
            model.addAttribute("customers", allCustomers);
            model.addAttribute("locations", allLocations);
            model.addAttribute("errorMessage", "Slutdato skal være mindst 3 måneder efter startdato.");
            return "dataRegistration/rental/create-agreement";
        }

        Car selectedCar = carService.findById(rentalAgreement.getCarId());
        if (selectedCar != null)
        {

            if (selectedCar.getCarStatusId() != 1) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bilen er ikke tilgængelig og kan ikke udlejes.");
                return "redirect:/dataRegistration/rental-agreements";
            }
            rentalAgreement.setStartOdometer(selectedCar.getCurrentOdometer());
            selectedCar.setCarStatusId(2);
            carService.update(selectedCar);
        }



        try
        {
            rentalAgreementService.create(rentalAgreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");
        } catch (Exception e)
        {
            e.printStackTrace(); // Print error to console
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved oprettelse!");
        }
        return "redirect:/dataRegistration/rental-agreements";
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
    @GetMapping("/rental-agreements/{id}")
    public String viewRentalAgreementDetails(@PathVariable("id") int rentalAgreementId,
                                             Model model,
                                             RedirectAttributes redirectAttributes) {
        RentalAgreement agreement = rentalAgreementService.findById(rentalAgreementId);

        if (agreement == null)
        {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lejeaftale med ID " + rentalAgreementId + " blev ikke fundet.");
            return "redirect:/dataRegistration/rental-agreements";
        }

        model.addAttribute("rentalAgreement", agreement);

        if (agreement.getCarId() > 0)
        {
            Car car = carService.findById(agreement.getCarId());
            model.addAttribute("carDetails", car);
        } else
        {
            model.addAttribute("carDetails", null);
        }

        if (agreement.getCustomerId() > 0)
        {
            Customer customer = customerService.findById(agreement.getCustomerId());
            model.addAttribute("customerDetails", customer);
        } else
        {
            model.addAttribute("customerDetails", null);
        }

        if (agreement.getPickupLocationId() > 0)
        {
            System.out.println("Pickup Location ID: " + agreement.getPickupLocationId()); // Se hvad ID'et faktisk er
            Location pickupLoc = locationService.findLocationById(agreement.getPickupLocationId());
            System.out.println("Pickup Location object: " + (pickupLoc != null ? "Found" : "Not found")); // Se om objektet findes
            model.addAttribute("pickupLocationDetails", pickupLoc);
        } else
        {
            System.out.println("Pickup Location ID er 0 eller mindre"); // Bekræft at dette er tilfældet
            model.addAttribute("pickupLocationDetails", null);
        }

        if (agreement.getReturnLocationId() != null && agreement.getReturnLocationId() > 0)
        {
            Location returnLoc = locationService.findLocationById(agreement.getReturnLocationId());
            model.addAttribute("returnLocationDetails", returnLoc);
        } else
        {
            model.addAttribute("returnLocationDetails", null);
        }
        List<ConditionReport> conditionReports = conditionReportService.findByRentalAgreementId(rentalAgreementId);

        if (conditionReports != null && !conditionReports.isEmpty()) {
            model.addAttribute("conditionReportId", conditionReports.getFirst().getConditionReportId());
        } else {
            model.addAttribute("conditionReportId", null);
        }
        return "dataRegistration/rental/view-rental-details";
    }

    // DELETE AGREEMENT
    @PostMapping("/rental-agreements/delete/{id}")
    public String deleteRentalAgreement(@PathVariable int id,
                                        RedirectAttributes redirectAttributes) {
        rentalAgreementService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale slettet.");
        return "redirect:/dataRegistration/rental-agreements";
    }


    // Konstruktør af controlleren, så den kan anvendes i Test klassen
    public RentalAgreementController(RentalAgreementService rentalAgreementService,
                                     CarService carService,
                                     CustomerService customerService,
                                     LocationService locationService,
                                     ConditionReportService conditionReportService) {
        this.rentalAgreementService = rentalAgreementService;
        this.carService = carService;
        this.customerService = customerService;
        this.locationService = locationService;
        this.conditionReportService = conditionReportService;
    }


}
