package backend.password_reset.controller;

import backend.password_reset.model.AppUser;
import backend.password_reset.model.AppUserRepository;
import backend.password_reset.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class PasswordResetController {
    
   private final PasswordResetService passwordResetService;
   private final AppUserRepository appUserRepository;

   public PasswordResetController(PasswordResetService passwordResetService, AppUserRepository appUserRepository) {
    this.passwordResetService = passwordResetService;
    this.appUserRepository = appUserRepository;
   }

   @GetMapping("/home")
   public String userList(Model model) {
        model.addAttribute("appUsers", appUserRepository.findAll());
        return "home";
   }
   

   @GetMapping("/forgot-password")
   public String showForgotPasswordForm(Model model) {
       return "forgot-password";
   }

   @PostMapping("/forgot-password")
   public String handleForm(@RequestParam String email, Model model) {
        try {
            String token = passwordResetService.generateToken(email);
            model.addAttribute("successMessage", "Password reset link sent to your email!");
        } catch (IllegalArgumentException e) {
           model.addAttribute("errorMessage", "Email not found.");
        }
       return "forgot-password";
   }

   @GetMapping("/reset-password")
   public String showResetPasswordForm(@RequestParam String token, Model model) {
       if(passwordResetService.validToken(token)){
        model.addAttribute("token", token);
        return "reset-password";
       }else{
        model.addAttribute("errorMessage", "Invalid or expired token");
        return "forgot-password";
       }
   }

   @PostMapping("/reset-password")
   public String processResetPassword(@RequestParam String token, @RequestParam String newPassword, @RequestParam String confirmPassword, Model model) {
       
    if(!newPassword.equals(confirmPassword)){
        model.addAttribute("errorMessage", "Passwords do not match.");
        model.addAttribute("token", token);
        return "reset-password";
    }
    if(passwordResetService.resetPassword(token, newPassword)){
        model.addAttribute("successMessage", "Password changed successfully!");
        return "login";
    }else{
        model.addAttribute("errorMessage", "Failed to reset password");
        return "reset-password";
    }
    
   }

   @PostMapping("/save")
   public String save(@ModelAttribute AppUser appUser) {
       appUserRepository.save(appUser);
       return "redirect:/home";
   }

   @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId) {
    appUserRepository.deleteById(userId);
    return "redirect:../home";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long userId, Model model) {
        AppUser appUser = appUserRepository.findById(userId).orElse(null);
        model.addAttribute("appUser", appUser);
        return "editUser";
    }
}