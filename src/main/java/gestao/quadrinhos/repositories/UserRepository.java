// src/main/java/gestao/quadrinhos/repositories/UserRepository.java
package gestao.quadrinhos.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import gestao.quadrinhos.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
