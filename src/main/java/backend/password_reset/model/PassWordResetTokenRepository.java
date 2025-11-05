package backend.password_reset.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassWordResetTokenRepository extends JpaRepository<PassWordResetToken, Long>{
    Optional<PassWordResetToken> findByToken(String token);
    Optional<PassWordResetToken> findByAppUser(AppUser appUser);
    void deleteByAppUser(AppUser appUser);
}
