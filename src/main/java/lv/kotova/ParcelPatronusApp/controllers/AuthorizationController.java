package lv.kotova.ParcelPatronusApp.controllers;

import jakarta.validation.Valid;
import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.security.UserDetails_;
import lv.kotova.ParcelPatronusApp.services.UserService;
import lv.kotova.ParcelPatronusApp.util.UserValidator;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthorizationController {

    private final UserValidator userValidator;
    private final UserService userService;

    public AuthorizationController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping
    public String home(@AuthenticationPrincipal UserDetails_ userDetails, Model model){
        model.addAttribute("role", userDetails.getAuthorities().toString());
        return "auth/home";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "auth/registration";

        userService.save(user);
        return "redirect:/login";
    }

}
