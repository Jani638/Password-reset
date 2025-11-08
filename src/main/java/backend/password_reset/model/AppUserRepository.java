package backend.password_reset.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByEnabled(boolean enabled);
    Optional<AppUser> findById(Long Id);
}
