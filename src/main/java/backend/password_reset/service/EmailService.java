package backend.password_reset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String resetLink){
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("Tähän oma sähköposti");
            message.setTo(email);

            message.setSubject("Password reset request");

            String emailBody = "Hello there! Here is your password reset link:" + resetLink;
            message.setText(emailBody);


            mailSender.send(message);

            System.out.println("[EMAIL] Password reset link sent to: " + email);

        } catch (Exception e) {
            System.out.println("[EMAIL ERROR] Failed to send password reset link: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
