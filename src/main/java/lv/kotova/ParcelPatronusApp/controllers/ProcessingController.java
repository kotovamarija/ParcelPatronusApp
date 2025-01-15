package lv.kotova.ParcelPatronusApp.controllers;

import lv.kotova.ParcelPatronusApp.models.*;
import lv.kotova.ParcelPatronusApp.models.enums.Status;
import lv.kotova.ParcelPatronusApp.security.UserDetails_;
import lv.kotova.ParcelPatronusApp.services.DeliveryDetailsService;
import lv.kotova.ParcelPatronusApp.services.EmployeeService;
import lv.kotova.ParcelPatronusApp.services.ParcelMachineService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/processing")
public class ProcessingController {

    private final EmployeeService employeeService;
    private final ParcelMachineService parcelMachineService;
    private final DeliveryDetailsService deliveryDetailsService;

    public ProcessingController(EmployeeService employeeService, ParcelMachineService parcelMachineService, DeliveryDetailsService deliveryDetailsService) {
        this.employeeService = employeeService;
        this.parcelMachineService = parcelMachineService;
        this.deliveryDetailsService = deliveryDetailsService;
    }

    @GetMapping
    public String dailyTasks(){
        return "/processing/tasks";
    }

    @GetMapping("/ribaolga")
    public String testing(){
        System.out.println("TESTINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        return "/processing/tasks";
    }

    @GetMapping("/showIndividualTasks") // Accessed in courier's own profile only
    public String showIndividualTasks(@AuthenticationPrincipal UserDetails_ userDetails, Model model){
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("tasks", deliveryDetailsService.findByEmployee(userDetails.getUser().getEmployee()));

        // FOR COURIERS
        model.addAttribute("tasks_ASSIGNED_TO_COURIER_TO_PICKUP", deliveryDetailsService.findByEmployeeIdAndStatusOrderByDispatch(userDetails.getUser().getEmployee().getId(), Status.ASSIGNED_TO_COURIER_FOR_PICKUP));
        model.addAttribute("tasks_IN_TRANSIT_TO_SORTING_POINT", deliveryDetailsService.findByEmployeeIdAndStatusOrderByDispatch(userDetails.getUser().getEmployee().getId(), Status.IN_TRANSIT_TO_SORTING_FACILITY));
        model.addAttribute("tasks_ASSIGNED_TO_COURIER_TO_DELIVER", deliveryDetailsService.findByEmployeeIdAndStatusOrderByDestination(userDetails.getUser().getEmployee().getId(), Status.ASSIGNED_TO_COURIER_FOR_DELIVERY));
        model.addAttribute("tasks_IN_TRANSIT_TO_DESTINATION", deliveryDetailsService.findByEmployeeIdAndStatusOrderByDestination(userDetails.getUser().getEmployee().getId(), Status.IN_TRANSIT_TO_DESTINATION));

        // FOR WAREHOUSE MANAGERS
        model.addAttribute("tasks_UNLOADED_AT_SORTING_POINT", deliveryDetailsService.findByEmployeeIdAndStatusOrderByDispatch(userDetails.getUser().getEmployee().getId(), Status.UNLOADED_AT_SORTING_FACILITY));
  //      model.addAttribute("tasks_ACCEPTED_AT_SORTING_POINT", deliveryDetailsService.findByEmployeeIdAndStatus(userDetails.getUser().getEmployee().getId(), Status.ACCEPTED_AT_SORTING_POINT));

        return "/processing/individualTasks";
    }

    @GetMapping("/showAllTasks")
    public String showAllTasks(){
        return "/processing/allTasks";
    }


    @GetMapping("/setPickUpTasks")
    public String setPickUpTasks(Model model){
        model.addAttribute("machines", parcelMachineService.findAll());
        model.addAttribute("dd", deliveryDetailsService.findParcelsToPickup());
        model.addAttribute("couriers", employeeService.findCouriers());
        model.addAttribute("courier", new Employee());
//        model.addAttribute("address", new ParcelMachine());

        return "/processing/setPickUpTasks";
    }


    @PostMapping("/setCourierToPickUp")
    public String setCourierToPickUp(@ModelAttribute("courier") Employee courier,
                                     @RequestParam("address") String address){
        deliveryDetailsService.assignCourierToPickUp(address, courier);
        return "redirect:/processing/setPickUpTasks";
    }


    @GetMapping("/setPostSortingDeliveries")
    public String setPostSortingDeliveries(Model model){
        model.addAttribute("machines", parcelMachineService.findAll());
        model.addAttribute("dd", deliveryDetailsService.findParcelsToDeliver());
        model.addAttribute("couriers", employeeService.findCouriers());
        model.addAttribute("courier", new Employee());

        for(DeliveryDetails d : deliveryDetailsService.findParcelsToDeliver()) {
            System.out.println("STATUS EXPECTED: ACCEPTED_AT_SORTING_POINT -->  ID: " + d.getId());
        }
        return "/processing/setPostSortingDeliveries";
    }

    @PostMapping("/setCourierToDeliver")
    public String setCourierToDeliver(@ModelAttribute("courier") Employee courier,
                                      @RequestParam("address") String address){
        deliveryDetailsService.assignCourierToDeliver(address, courier);
        return "redirect:/processing/setPostSortingDeliveries";
    }


    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam("delivery_id") int deliveryId,
                               @RequestParam("delivery_status") Status deliveryStatus,
                                       Model model){

        deliveryDetailsService.moveToNextStatus(deliveryId, deliveryStatus);
        return "redirect:/processing/showIndividualTasks";
    }

    @GetMapping("/collect")
    public String collect(Model model){
//        model.addAttribute("ddID", "");
        return "/processing/collect";
    }

    @PostMapping("/confirm")
    public String confirm(@RequestParam("code") String doorCode, Model model){
        model.addAttribute("delivery", deliveryDetailsService.findByDoorCode(doorCode));
        return "/processing/confirm";
    }

    @PostMapping("/collect")
    public String collectById(@RequestParam(value = "terminate") String terminate,
                              @RequestParam(value = "doorCode") String doorCode)
    {
        System.out.println(terminate);
        System.out.println(doorCode);
        if(terminate.equals("no")) {
            deliveryDetailsService.finalizeDelivery(doorCode);
        }

        return "redirect:/";
    }

    @GetMapping("/tracking")
    public String track(Model model){
//        model.addAttribute("trackingNumber", "");
        return "/processing/tracking";
    }

    @PostMapping("/getStatus")
    public String getStatus(@RequestParam("trackingNumber") String tracking_number, Model model){
        System.out.println(tracking_number);
        model.addAttribute("delivery", deliveryDetailsService.findByTrackingNumber(tracking_number));
        return "/processing/checkStatus";
    }

}
