package backend.password_reset.service;

import backend.password_reset.model.AppUser;
import backend.password_reset.model.AppUserRepository;
import backend.password_reset.model.PassWordResetToken;
import backend.password_reset.model.PassWordResetTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    
    private final PassWordResetTokenRepository tokenRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(PassWordResetTokenRepository tokenRepository, AppUserRepository userRepository,PasswordEncoder passwordEncoder, EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.appUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String generateToken(String email){
        System.out.println("[DEBUG] generateToken called with email: " + email);

        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

        if(appUserOptional.isEmpty()){
            System.out.println("[DEBUG] User not found with email: " + email);
            throw new IllegalArgumentException("Email is not found: " + email);
        }

        AppUser appUser = appUserOptional.get();
        System.out.println("[DEBUG] User found: " + appUser.getUsername());
        
        List<PassWordResetToken> existingTokens = tokenRepository.findByAppUser(appUser);
        System.out.println("[DEBUG] Found " + existingTokens.size() + " existing tokens");
        if(!existingTokens.isEmpty()){
            System.out.println("[DEBUG] Deleting " + existingTokens.size() + " old tokens");
            tokenRepository.deleteAll(existingTokens);
            System.out.println("[DEBUG] Old tokens deleted");
        }

        String token = UUID.randomUUID().toString();
        System.out.println("[DEBUG] Generated new token " + token);

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        PassWordResetToken resetToken = new PassWordResetToken(null, token, appUser, expiryDate);

        tokenRepository.save(resetToken);
        System.out.println("[DEBUG] Token saved succesfully");

        /*String resetLink = "http://localhost:8081/reset-password?token=" + token; TÄÄ LINKKI PAIKALLISEEN!!!!*/
        String resetLink = "https://password-reset-app-a15eeefe2c4e.herokuapp.com/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink, appUser.getUsername());
        System.out.println("[EMAIL] Password reset email sent to: " + email);

        return token;

    }

    public boolean validToken(String token){
        Optional<PassWordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if(tokenOptional.isEmpty()){
            return false;
        }

        PassWordResetToken resetToken = tokenOptional.get();

        if(resetToken.isUsed()){
            return false;
        }
        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }

    public boolean resetPassword(String token, String newPassword){
        if(!validToken(token)){
            return false;
        }
        Optional<PassWordResetToken> tokenOptional = tokenRepository.findByToken(token);

        if(tokenOptional.isEmpty()){
            return false;
        }

        PassWordResetToken resetToken = tokenOptional.get();
        AppUser appUser = resetToken.getAppUser();

        String encodedPassword = passwordEncoder.encode(newPassword);
        appUser.setPassword(encodedPassword);

        appUser.setEnabled(true);
        appUserRepository.save(appUser);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return true;

    }
    public void cleanExpiredTokens(){

        List<PassWordResetToken>allTokens = tokenRepository.findAll();

        for(PassWordResetToken token : allTokens){

            if(token.getExpiryDate().isBefore(LocalDateTime.now())){
                tokenRepository.deleteById(token.getId());
            }
        }
        
    }

    
}
