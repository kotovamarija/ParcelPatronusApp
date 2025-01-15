package lv.kotova.ParcelPatronusApp.controllers;

import jakarta.validation.Valid;
import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.services.ParcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parcels")
public class ParcelController {
    private final ParcelService parcelService;


    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("parcels", parcelService.findAll());
        return "parcels/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("parcel", parcelService.findById(id));
        return "parcels/show";
    }

    @GetMapping("/new")
    public String newParcel(@ModelAttribute("parcel") Parcel parcel) {
        return "parcels/new";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute("parcel") @Valid Parcel parcel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "parcels/new";
        }
        parcelService.save(parcel);
        return "redirect:/parcels";
    }

}
