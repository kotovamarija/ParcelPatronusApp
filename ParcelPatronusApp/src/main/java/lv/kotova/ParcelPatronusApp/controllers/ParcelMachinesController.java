package lv.kotova.ParcelPatronusApp.controllers;

import lv.kotova.ParcelPatronusApp.services.ParcelMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/parcelMachines")
public class ParcelMachinesController {
    private final ParcelMachineService parcelMachineService;

    public ParcelMachinesController(ParcelMachineService parcelMachineService) {
        this.parcelMachineService = parcelMachineService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("machines", parcelMachineService.findAll());
        return "parcelMachines/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("machine", parcelMachineService.findById(id));
        return "parcelMachines/show";
    }

    @GetMapping("/initialize")
    public String initialize() {
        String parcelMachinesFullList = "static/ParcelMachinesList.txt";
        parcelMachineService.saveFromFile(parcelMachinesFullList);
        return "redirect:/parcelMachines";
    }


}
