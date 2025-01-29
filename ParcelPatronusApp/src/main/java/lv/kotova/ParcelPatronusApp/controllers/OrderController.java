package lv.kotova.ParcelPatronusApp.controllers;

import jakarta.validation.Valid;
import lv.kotova.ParcelPatronusApp.models.DeliveryDetails;
import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.models.ParcelMachine;
import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.security.UserDetails_;
import lv.kotova.ParcelPatronusApp.services.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"parcel", "details"})
@RequestMapping("/orderPlacement")
public class OrderController {
    private final OrderProcessingService orderProcessingService;
    private final ParcelMachineService parcelMachineService;
    private final List<ParcelMachine> parcelMachines;


    public OrderController(OrderProcessingService orderProcessingService, ParcelMachineService parcelMachineService) {
        this.orderProcessingService = orderProcessingService;
        this.parcelMachineService = parcelMachineService;
        this.parcelMachines = new ArrayList<>();
    }

    @GetMapping("/newParcel")
    public String newParcel(Model model)
    {
        model.addAttribute("parcel", new Parcel());
        return "parcels/new";
    }

    @PostMapping("/newDetails")
    public String newDetails(@ModelAttribute("parcel") @Valid Parcel parcel,
                             BindingResult bindingResult,
                             Model model)
    {
        if (bindingResult.hasErrors()) {
            return "parcels/new";
        }
        model.addAttribute("details", new DeliveryDetails());
        return "deliveryDetails/new";
    }

    @PostMapping("/newDispatchAddress")
    public String newDispatchAddress(@ModelAttribute("parcel") Parcel parcel,
                             @ModelAttribute("details") @Valid DeliveryDetails details,
                               BindingResult bindingResult,
                               Model model)
    {
        if (bindingResult.hasErrors()) {
            return "deliveryDetails/new";
        }
        model.addAttribute("dispatchMachine", new ParcelMachine());
        model.addAttribute("machines", parcelMachineService.findAll());
        return "parcelMachines/selectDispatch";
    }

    @PostMapping("/newDestinationAddress")
    public String newDestinationAddress(@ModelAttribute("parcel") Parcel parcel,
                                        @ModelAttribute("details") DeliveryDetails details,
                                        @ModelAttribute("dispatchMachine") ParcelMachine dispatchMachine,
                                        Model model)
    {
        parcelMachines.add(dispatchMachine);
        model.addAttribute("destinationMachine", new ParcelMachine());
        model.addAttribute("machines", parcelMachineService.findAll());
        return "parcelMachines/selectDestination";
    }

    @PostMapping("/confirmation")
    public String confirmation(@AuthenticationPrincipal UserDetails_ userDetails,
                               @ModelAttribute("parcel") Parcel parcel,
                               @ModelAttribute("details") DeliveryDetails details,
                               @ModelAttribute("destinationMachine") ParcelMachine destinationMachine, Model model)
    {
        parcelMachines.add(destinationMachine);
        if(parcelMachines.get(0).equals(parcelMachines.get(1))){
            model.addAttribute( "errorMessage", "The dispatch and destination parcel machines must be different. " +
                    "Please select another destination.");
            model.addAttribute("machines", parcelMachineService.findAll());
            parcelMachines.remove(1);

            return "parcelMachines/selectDestination";
        }
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("machines", parcelMachines);
        return "deliveryDetails/confirmation";
    }

    @PostMapping("/proceed")
    public String create(@AuthenticationPrincipal UserDetails_ userDetails,
                         @ModelAttribute("parcel") Parcel parcel,
                         @ModelAttribute("details") DeliveryDetails details,
                         @RequestParam(value = "terminate", required = false) String terminate, Model model, SessionStatus sessionStatus)
    {
        if(terminate.equals("yes")){
            parcelMachines.clear();
            sessionStatus.setComplete();
            return "redirect:/orderPlacement/newParcel";
        }
        User user = userDetails.getUser();
        orderProcessingService.create(user, parcel, details, parcelMachines.get(0).getAddress(), parcelMachines.get(1).getAddress());
        parcelMachines.clear();

        model.addAttribute("tranckingNumber", details.getTrackingNumber());

        sessionStatus.setComplete();
        return "deliveryDetails/printTracking";
    }




}
