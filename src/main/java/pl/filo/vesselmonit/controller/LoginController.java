package pl.filo.vesselmonit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import pl.filo.vesselmonit.service.AuthenticationUserService;

@Controller
public class LoginController {
    private final AuthenticationUserService authenticationUserService;

    public LoginController(final AuthenticationUserService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    @GetMapping("/login")
    public String login(final ModelMap modelMap, @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser) {
        authenticationUserService.setAuthorizationInfo(modelMap, authenticationUser);
        modelMap.addAttribute("pageName", "login");
        return "login";
    }

}