    package com.example.bilabonnement.Controller;

    import com.example.bilabonnement.Model.Car;
    import com.example.bilabonnement.Model.Customer;
    import com.example.bilabonnement.Model.RentalAgreement;
    import com.example.bilabonnement.Service.CarService;
    import com.example.bilabonnement.Service.CustomerService;
    import com.example.bilabonnement.Service.RentalAgreementService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        // OVERVIEW
        @GetMapping("/rental-agreements")
        public String showOverview(Model model) {
            List<RentalAgreement> agreements = rentalAgreementService.findAll();
            List<Car> allCars = carService.findAllCars();
            List<Customer> allCustomers = customerService.findAllCustomers();

            Map <Integer, String>  carNamesById = allCars.stream()
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

            return "dataRegistration/rental/rental-agreements-overview";
        }

        //SHOW CREATE FORM
        @GetMapping("/rental-agreements/create")
        public String showCreateForm(Model model) {
            List<Car> allCars = carService.findAllCars();

            // Filter cars with car_status_id == 1 (Tilgængelig)
            List<Car> availableCars = allCars.stream()
                    .filter(car -> car.getCarStatusId() == 1)
                    .toList();

            List<Customer> allCustomers = customerService.findAllCustomers();

            RentalAgreement rentalAgreement = new RentalAgreement();


            model.addAttribute("rentalAgreement", new RentalAgreement());
            model.addAttribute("cars", availableCars);
            model.addAttribute("customers", allCustomers);
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
            return "redirect:/dataRegistration/rental-agreements";

        }



        //SHOW EDIT FORM
        @GetMapping("/rental-agreements/edit/{id}")
        public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
            RentalAgreement agreement = rentalAgreementService.findById(id);
            if (agreement == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale ikke fundet.");
                return "redirect:/dataRegistration/rental-agreements";
            }
            model.addAttribute("rentalAgreement", agreement);
            return "dataRegistration/rental/editRentalAgreement";
        }

        // HANDLE EDIT SUBMISSION
        @PostMapping("/rental-agreements/update")
        public String updateRentalAgreement(@ModelAttribute RentalAgreement rentalAgreement,
                                            RedirectAttributes redirectAttributes) {
            rentalAgreementService.update(rentalAgreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale opdateret!");
            return "redirect:/dataRegistration/rental-agreements";
        }

        // VIEW SINGLE AGREEMENT
        @GetMapping("/rental-agreements/{id}")
        public String viewRentalAgreement(@PathVariable int id, Model model) {
            RentalAgreement agreement = rentalAgreementService.findById(id);
            if (agreement == null) {
                return "redirect:/dataRegistration/rental-agreements";
            }
            model.addAttribute("rentalAgreement", agreement);
            return "dataRegistration/rental/viewRentalAgreement";
        }

        // DELETE AGREEMENT
        @PostMapping("/rental-agreements/delete/{id}")
        public String deleteRentalAgreement(@PathVariable int id,
                                            RedirectAttributes redirectAttributes) {
            rentalAgreementService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale slettet.");
            return "redirect:/dataRegistration/rental-agreements";
        }
    }
