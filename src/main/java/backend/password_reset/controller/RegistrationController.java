package backend.password_reset.controller;

import org.springframework.stereotype.Controller;

import backend.password_reset.model.AppUser;
import backend.password_reset.service.AppUserService;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private AppUserService appUserService;

    public RegistrationController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public String home() {
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute AppUser appUser, 
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!appUser.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "registration";
        }
        
        boolean success = appUserService.registerUser(appUser);

        if(!success){
            model.addAttribute("error", "Username is reserved.");
            return "registration";
        }

        return "redirect:/login?registered";
    }
    
    
    
}
