package gestao.quadrinhos.services;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.InventarioRepository;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.repositories.QuadrinhoRepository;
import gestao.quadrinhos.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class QuadrinhoService {

	private final QuadrinhoRepository repository;
    private final UserRepository userRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final InventarioRepository inventarioRepository;

    // Construtor corrigido
    public QuadrinhoService(
        QuadrinhoRepository repository,
        UserRepository userRepository,
        MovimentacaoRepository movimentacaoRepository,
        InventarioRepository inventarioRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public Quadrinho salvar(Quadrinho quadrinho) {
        User currentUser = getCurrentUser();
        
        // Se for um novo quadrinho
        if (quadrinho.getId() == null) {
            quadrinho.setUser(currentUser);
            quadrinho.setCreatedAt(new Date());
        } else {
            // Verificar se o quadrinho pertence ao usuário atual
            Quadrinho existingTenis = buscarPorId(quadrinho.getId());
            if (!existingTenis.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Você não tem permissão para editar este quadrinho");
            }
            quadrinho.setUser(currentUser);
            quadrinho.setUpdatedAt(new Date());
        }
        
        return repository.save(quadrinho);
    }

    public List<Quadrinho> listar() {
        User currentUser = getCurrentUser();
        // Retorna apenas os quadrinhos do usuário logado
        return repository.findByUser(currentUser);
    }

    public Quadrinho buscarPorId(Long id) {
        User currentUser = getCurrentUser();
        Quadrinho quadrinho = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));
        
        // Verificar se o quadrinhos pertence ao usuário atual
        if (!quadrinho.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar este quadrinho");
        }
        
        return quadrinho;
    }

    @Transactional
    public void deletar(Long id) {
        User currentUser = getCurrentUser();
        Quadrinho quadrinho = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));

        if (!quadrinho.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este quadrinho");
        }

        movimentacaoRepository.deleteAllByQuadrinho(quadrinho);
        inventarioRepository.deleteAllByQuadrinho(quadrinho);
        repository.delete(quadrinho);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = authentication.getName();
        
        if (email == null || email.equals("anonymousUser")) {
            throw new RuntimeException("Usuário não autenticado");
        }

        User user = (User) userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return user;
    }
}