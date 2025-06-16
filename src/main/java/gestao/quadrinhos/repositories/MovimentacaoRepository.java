package gestao.quadrinhos.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.User;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {

    List<Movimentacao> findByUserOrderByDataMovimentacaoDesc(User user);

    List<Movimentacao> findByUserAndTipoOrderByDataMovimentacaoDesc(User user, StatusMovimentacao tipo);

    List<Movimentacao> findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
        User user, Date inicio, Date fim);

    /**
     * Retorna todas movimentações de um quadrinho específico (por entidade).
     */
    List<Movimentacao> findByQuadrinho(Quadrinho quadrinho);

    /**
     * Retorna todas movimentações de um quadrinho específico (por id).
     */
    List<Movimentacao> findByQuadrinhoId(Long quadrinhoId);

    Optional<Movimentacao> findByIdAndUser(UUID id, User user);

    /**
     * Desassocia o quadrinho de todas as movimentações, definindo a FK como null.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Movimentacao m SET m.quadrinho = null WHERE m.quadrinho = :quadrinho")
    void desvincularQuadrinhoDasMovimentacoes(Quadrinho quadrinho);
}
