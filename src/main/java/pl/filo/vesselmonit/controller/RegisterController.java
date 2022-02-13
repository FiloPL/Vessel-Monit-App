package pl.filo.vesselmonit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.filo.vesselmonit.service.UserService;
import pl.filo.vesselmonit.entity.User;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showAddUser(final ModelMap modelMap) {
        modelMap.addAttribute("isUserLogged", false);
        modelMap.addAttribute("isAdminAuthorized", false);
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("pageName", "register");
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") final User user, final Errors errors, final ModelMap modelMap) {
        modelMap.addAttribute("isUserLogged", false);
        modelMap.addAttribute("isAdminAuthorized", false);
        modelMap.addAttribute("pageName", "register");
        if(errors.hasErrors()) {
            return "register";
        }
        final User createdUser = userService.createNewUser(user);
        if(createdUser == null) {
            modelMap.addAttribute("userExistsError",
                    "Can't create new user, because that username or email already exist.");
            return "register";
        }
        return "redirect:/login";
    }

}

