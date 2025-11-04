package backend.password_reset.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.password_reset.model.AppUser;
import backend.password_reset.model.AppUserRepository;

@Service
public class AppUserService{
    
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder){
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(AppUser appUser){

        if(appUserRepository.findByUsername(appUser.getUsername()).isPresent()){
            return false;
        }

        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUser.setEnabled(true);
        appUserRepository.save(appUser);

        return true;
    }


}
