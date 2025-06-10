package gestao.quadrinhos.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.User;
import jakarta.transaction.Transactional;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
	@Modifying
	@Transactional
	void deleteAllByQuadrinho(Quadrinho quadrinho);
	
    List<Movimentacao> findByUser(User user);

    List<Movimentacao> findByUserAndQuadrinho(User user, Quadrinho quadrinho);

    List<Movimentacao> findByUserAndTipoOrderByDataMovimentacaoDesc(User user, StatusMovimentacao tipo);

    List<Movimentacao> findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
        User user, LocalDateTime inicio, LocalDateTime fim);

    List<Movimentacao> findByUserOrderByDataMovimentacaoDesc(User user);

    /**
     * Busca todas as movimentações associadas a um dado inventário (pelo ID do inventário). 
     * A entidade Movimentacao tem apenas um atributo "quadrinho", então fazemos JOIN 
     * com Inventario i ON m.quadrinho = i.quadrinho.
     */
    @Query("""
        SELECT m 
          FROM Movimentacao m 
          JOIN Inventario i ON m.quadrinho = i.quadrinho 
         WHERE i.id = :inventarioId 
      ORDER BY m.dataMovimentacao DESC
        """)
    List<Movimentacao> findByInventarioId(@Param("inventarioId") Long inventarioId);

}
