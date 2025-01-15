package lv.kotova.ParcelPatronusApp.controllers;

import jakarta.validation.Valid;
import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.services.DeliveryDetailsService;
import lv.kotova.ParcelPatronusApp.services.UserService;
import lv.kotova.ParcelPatronusApp.util.UserNotCreatedException;
import lv.kotova.ParcelPatronusApp.util.UserNotFoundException;
import lv.kotova.ParcelPatronusApp.util.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("parcels", userService.findActiveParcelsByUserId(id));
        return "users/show";
    }
}
