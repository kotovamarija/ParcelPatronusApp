package lv.kotova.ParcelPatronusApp.util;

import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try{
            userService.findByUsername(user.getUsername());
            errors.rejectValue("username", "", "Username already exists");
        }
            catch(UserNotFoundException ignored){
        }

        try{
            userService.findByEmail(user.getEmail());
            errors.rejectValue("email", "", "Email already exists");
        }
            catch(UserNotFoundException ignored){
        }

    }
}
