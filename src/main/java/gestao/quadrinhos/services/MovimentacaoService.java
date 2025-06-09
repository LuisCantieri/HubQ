package gestao.quadrinhos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import gestao.quadrinhos.dto.MovimentacaoDTO;
import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.repositories.UserRepository;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public List<MovimentacaoDTO> getAllMovimentacoes() {
        User user = getCurrentUser();
        List<Movimentacao> movimentacoes = movimentacaoRepository
            .findByUserOrderByDataMovimentacaoDesc(user);

        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByTipo(StatusMovimentacao status) {
        User user = getCurrentUser();
        List<Movimentacao> movimentacoes = movimentacaoRepository
            .findByUserAndTipoOrderByDataMovimentacaoDesc(user, status);

        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        User user = getCurrentUser();

        List<Movimentacao> movimentacoes = movimentacaoRepository
            .findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(user, inicio, fim);

        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByInventario(Long inventarioId) {
        // Não precisamos converter tipos — o repositório já aceita Long.
        List<Movimentacao> movimentacoes = movimentacaoRepository
            .findByInventarioId(inventarioId);

        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }
}
