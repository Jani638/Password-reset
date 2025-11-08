package backend.password_reset.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import backend.password_reset.model.AppUser;
import backend.password_reset.model.AppUserRepository;

@Service
public class EmailService {

    private final AppUserRepository appUserRepository;

    public EmailService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String email, String resetLink, String username){
        try {
            System.out.println("[DEBUG] fromEmail: " + fromEmail);
            System.out.println("[DEBUG] Email to send to: " + email);
            
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(email);

            message.setSubject("Password reset request");

            appUserRepository.findByUsername(username);

            String emailBody = "Hello " + username + "! \n\nHere is your password reset link: " + resetLink;
            message.setText(emailBody);


            mailSender.send(message);

            System.out.println("[EMAIL] Password reset link sent to: " + email);

        } catch (Exception e) {
            System.out.println("[EMAIL ERROR] Failed to send password reset link: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
