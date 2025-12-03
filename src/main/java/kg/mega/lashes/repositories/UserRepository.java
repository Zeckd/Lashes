package kg.mega.lashes.repositories;

import kg.mega.lashes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRole(User.Role role);
    Optional<User> findByResetToken(String resetToken);
}
