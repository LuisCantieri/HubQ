package gestao.quadrinhos.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestao.quadrinhos.dto.QuadrinhoDTO;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.InventarioRepository;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.repositories.QuadrinhoRepository;
import gestao.quadrinhos.repositories.UserRepository;

@Service
public class QuadrinhoService {

    private final QuadrinhoRepository quadrinhoRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final UserRepository userRepository;

    public QuadrinhoService(
        QuadrinhoRepository quadrinhoRepository,
        InventarioRepository inventarioRepository,
        MovimentacaoRepository movimentacaoRepository,
        UserRepository userRepository
    ) {
        this.quadrinhoRepository = quadrinhoRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
            .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));
    }

    public List<QuadrinhoDTO> listar() {
        return quadrinhoRepository.findAll().stream()
            .map(QuadrinhoDTO::new)
            .collect(Collectors.toList());
    }

    public QuadrinhoDTO buscarPorId(Long id) {
        Quadrinho q = quadrinhoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        return new QuadrinhoDTO(q);
    }

    @Transactional
    public QuadrinhoDTO salvar(QuadrinhoDTO dto) {
        User user = getCurrentUser();
        Quadrinho q = dto.toEntity(user);
        quadrinhoRepository.save(q);
        return new QuadrinhoDTO(q);
    }

    @Transactional
    public QuadrinhoDTO atualizar(Long id, QuadrinhoDTO dto) {
        Quadrinho q = quadrinhoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));

        if (dto.getCodigo() != null)      q.setCodigo(dto.getCodigo());
        if (dto.getNome() != null)        q.setNome(dto.getNome());
        if (dto.getPreco() != null)       q.setPreco(dto.getPreco());
        if (dto.getRaridade() != null)    q.setRaridade(dto.getRaridade());
        if (dto.getUrlImagem() != null)   q.setUrlImagem(dto.getUrlImagem());

        q.setUser(getCurrentUser());

        quadrinhoRepository.save(q);
        return new QuadrinhoDTO(q);
    }

    @Transactional
    public void deletar(Long id) {
        Quadrinho q = quadrinhoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));

        movimentacaoRepository.findByQuadrinho(q)
            .forEach(m -> m.setQuadrinho(null));
        movimentacaoRepository.saveAll(movimentacaoRepository.findByQuadrinho(q));

        inventarioRepository.deleteAll(inventarioRepository.findByQuadrinho(q));

        quadrinhoRepository.delete(q);
    }

    @Transactional
    public QuadrinhoDTO updatePreco(Long quadrinhoId, BigDecimal novoPreco) {
        Quadrinho q = quadrinhoRepository.findById(quadrinhoId)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));

        if (novoPreco == null) {
            throw new IllegalArgumentException("Preço não pode ser nulo");
        }
        if (!novoPreco.equals(q.getPreco())) {
            q.setPreco(novoPreco);
            quadrinhoRepository.save(q);

            Date agora = new Date();
            var invs = inventarioRepository.findByQuadrinho(q);
            invs.forEach(inv -> {
                inv.setPrecoUnitario(novoPreco);
                inv.setUpdatedAt(agora);
            });
            inventarioRepository.saveAll(invs);
        }

        return new QuadrinhoDTO(q);
    }
}
