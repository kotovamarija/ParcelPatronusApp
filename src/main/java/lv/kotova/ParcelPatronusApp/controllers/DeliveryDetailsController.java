package lv.kotova.ParcelPatronusApp.controllers;

import lv.kotova.ParcelPatronusApp.services.DeliveryDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deliveryDetails")
public class DeliveryDetailsController {
    private final DeliveryDetailsService deliveryDetailsService;

    public DeliveryDetailsController(DeliveryDetailsService deliveryDetailsService) {
        this.deliveryDetailsService = deliveryDetailsService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("deliveryDetails", deliveryDetailsService.findAll());
        return "deliveryDetails/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("deliveryDetails", deliveryDetailsService.findById(id));
        return "deliveryDetails/show";
    }

    // See OrderController for new DeliveryDetails creation logic
}
