package gestao.quadrinhos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;


@Repository
public interface QuadrinhoRepository extends JpaRepository<Quadrinho, Long> {
    
    List<Quadrinho> findByUser(User user);
    
    List<Quadrinho> findByUserOrderByNome(User user);
    
    Long countByUser(User user);
}