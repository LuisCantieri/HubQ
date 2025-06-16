package gestao.quadrinhos.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gestao.quadrinhos.entities.Inventario;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, UUID> {

    List<Inventario> findByUser(User user);

    List<Inventario> findByUserAndQuantidadeGreaterThan(User user, Integer quantidade);

    Optional<Inventario> findByUserAndQuadrinho(User user, Quadrinho quadrinho);

    Long countByUserAndQuantidadeGreaterThan(User user, Integer quantidade);

    List<Inventario> findByQuadrinho(Quadrinho quadrinho);
}
