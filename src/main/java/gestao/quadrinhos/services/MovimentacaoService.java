// src/main/java/gestao/quadrinhos/services/MovimentacaoService.java
package gestao.quadrinhos.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gestao.quadrinhos.dto.MovimentacaoDTO;
import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.repositories.UserRepository;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final UserRepository userRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
                               UserRepository userRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));
    }

    public List<MovimentacaoDTO> getAllMovimentacoes() {
        User user = getCurrentUser();
        return movimentacaoRepository
            .findByUserOrderByDataMovimentacaoDesc(user)
            .stream()
            .map(MovimentacaoDTO::new)
            .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByTipo(StatusMovimentacao tipo) {
        User user = getCurrentUser();
        return movimentacaoRepository
            .findByUserAndTipoOrderByDataMovimentacaoDesc(user, tipo)
            .stream()
            .map(MovimentacaoDTO::new)
            .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByPeriodo(Date inicio, Date fim) {
        User user = getCurrentUser();
        return movimentacaoRepository
            .findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(user, inicio, fim)
            .stream()
            .map(MovimentacaoDTO::new)
            .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByQuadrinhoId(Long quadrinhoId) {
        return movimentacaoRepository
            .findByQuadrinhoId(quadrinhoId)
            .stream()
            .map(MovimentacaoDTO::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteMovimentacao(UUID id) {
        User user = getCurrentUser();
        return movimentacaoRepository
            .findByIdAndUser(id, user)
            .map(m -> {
                movimentacaoRepository.delete(m);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public void desvincularQuadrinho(gestao.quadrinhos.entities.Quadrinho quadrinho) {
        movimentacaoRepository.desvincularQuadrinhoDasMovimentacoes(quadrinho);
    }
}
