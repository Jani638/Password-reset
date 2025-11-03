package backend.password_reset.controller;

import org.springframework.stereotype.Controller;

import backend.password_reset.model.AppUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegistrationController {

    private AppUserRepository appUserRepository;

    public RegistrationController(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    
}
