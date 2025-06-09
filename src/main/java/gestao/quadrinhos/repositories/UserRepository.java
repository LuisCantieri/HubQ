package gestao.quadrinhos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import gestao.quadrinhos.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Retorna diretamente User (não precisa UserDetails aqui)
    User findByEmail(String email);
}
