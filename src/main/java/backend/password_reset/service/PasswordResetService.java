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

    public PasswordResetService(PassWordResetTokenRepository tokenRepository, AppUserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.appUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(String email){
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

        if(appUserOptional.isEmpty()){
            throw new IllegalArgumentException("Email is not found: " + email);
        }

        AppUser appUser = appUserOptional.get();
        
        List<PassWordResetToken> existingTokens = tokenRepository.findByAppUser(appUser);
        if(!existingTokens.isEmpty()){
            tokenRepository.deleteAll(existingTokens);
        }

        String token = UUID.randomUUID().toString();
        
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        PassWordResetToken resetToken = new PassWordResetToken(null, token, appUser, expiryDate);

        tokenRepository.save(resetToken);

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
