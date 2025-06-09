package gestao.quadrinhos.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import gestao.quadrinhos.dto.InventarioCreateDTO;
import gestao.quadrinhos.dto.InventarioResponseDTO;
import gestao.quadrinhos.entities.Inventario;
import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.InventarioRepository;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.repositories.QuadrinhoRepository;
import gestao.quadrinhos.repositories.UserRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private QuadrinhoRepository quadrinhoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public List<InventarioResponseDTO> getUserInventory() {
        User currentUser = getCurrentUser();
        var inventarios = inventarioRepository.findByUser(currentUser);

        return inventarios.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InventarioResponseDTO> getUserInventoryWithStock() {
        User currentUser = getCurrentUser();
        var inventarios = inventarioRepository.findByUserAndQuantidadeGreaterThan(currentUser, 0);

        return inventarios.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public InventarioResponseDTO addProductToInventory(InventarioCreateDTO dto) {
        User currentUser = getCurrentUser();

        Quadrinho quadrinho = quadrinhoRepository.findById(dto.getQuadrinhoId())
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));

        Optional<Inventario> existingInventario = inventarioRepository.findByUserAndQuadrinho(currentUser, quadrinho);

        Inventario inventario;
        if (existingInventario.isPresent()) {
            inventario = existingInventario.get();
            inventario.setQuantidade(inventario.getQuantidade() + dto.getQuantidade());
            if (dto.getPrecoUnitario() != null) {
                inventario.setPrecoUnitario(dto.getPrecoUnitario());
            }
            inventario.setUpdatedAt(new Date());
        } else {
            Float precoUnitario = dto.getPrecoUnitario() != null ? dto.getPrecoUnitario() : quadrinho.getPreco();
            inventario = new Inventario(currentUser, quadrinho, dto.getQuantidade(), precoUnitario);
        }

        inventario = inventarioRepository.save(inventario);

        var movimentacao = new Movimentacao(
                currentUser,
                quadrinho,
                StatusMovimentacao.ENTRADA,
                dto.getQuantidade(),
                dto.getRaridade()
        );
        movimentacaoRepository.save(movimentacao);

        return convertToResponseDto(inventario);
    }

    public void removeProductFromInventory(Long quadrinhoId, Integer quantity, String description) {
        User currentUser = getCurrentUser();

        Quadrinho quadrinho = quadrinhoRepository.findById(quadrinhoId)
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));

        Inventario inventario = inventarioRepository.findByUserAndQuadrinho(currentUser, quadrinho)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no inventário"));

        if (inventario.getQuantidade() < quantity) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }

        inventario.setQuantidade(inventario.getQuantidade() - quantity);
        inventario.setUpdatedAt(new Date());
        inventarioRepository.save(inventario);

        var movimentacao = new Movimentacao(
                currentUser,
                quadrinho,
                StatusMovimentacao.SAIDA,
                quantity,
                description
        );
        movimentacaoRepository.save(movimentacao);
    }

    public void adjustInventory(Long quadrinhoId, Integer newQuantity, String description) {
        User currentUser = getCurrentUser();

        Quadrinho quadrinho = quadrinhoRepository.findById(quadrinhoId)
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));

        Inventario inventario = inventarioRepository.findByUserAndQuadrinho(currentUser, quadrinho)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no inventário"));

        Integer oldQuantity = inventario.getQuantidade();
        inventario.setQuantidade(newQuantity);
        inventario.setUpdatedAt(new Date());
        inventarioRepository.save(inventario);

        var movimentacao = new Movimentacao(
                currentUser,
                quadrinho,
                StatusMovimentacao.AJUSTE,
                newQuantity - oldQuantity,
                description != null ? description : "Ajuste de estoque"
        );
        movimentacaoRepository.save(movimentacao);
    }

    public Double getTotalInventoryValue() {
        User currentUser = getCurrentUser();
        var inventarios = inventarioRepository.findByUser(currentUser);

        return inventarios.stream()
                .mapToDouble(inv -> inv.getQuantidade() * inv.getPrecoUnitario())
                .sum();
    }

    public Long getProductsInStockCount() {
        User currentUser = getCurrentUser();
        return inventarioRepository.countByUserAndQuantidadeGreaterThan(currentUser, 0);
    }

    /**
     * Remove completamente um produto do inventário do usuário.
     */
    public void deleteProductFromInventory(Long quadrinhoId) {
        User currentUser = getCurrentUser();

        Quadrinho quadrinho = quadrinhoRepository.findById(quadrinhoId)
                .orElseThrow(() -> new RuntimeException("Quadrinho não encontrado"));

        Inventario inventario = inventarioRepository.findByUserAndQuadrinho(currentUser, quadrinho)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no inventário"));

        inventarioRepository.delete(inventario);
        // (Opcional) Se quiser registrar a movimentação de exclusão, descomente abaixo:
        /*
        var movimentacao = new Movimentacao(
            currentUser,
            quadrinho,
            StatusMovimentacao.SAIDA, // ou crie um status específico para “delete” se preferir
            inventario.getQuantidade(),
            "Exclusão completa do inventário"
        );
        movimentacaoRepository.save(movimentacao);
        */
    }

    private InventarioResponseDTO convertToResponseDto(Inventario inventario) {
        Quadrinho quadrinho = inventario.getQuadrinho();
        Double valorTotal = (double) (inventario.getQuantidade() * inventario.getPrecoUnitario());

        return new InventarioResponseDTO(
                inventario.getId(),
                quadrinho.getId(),
                quadrinho.getNome(),
                quadrinho.getCodigo(),
                quadrinho.getRaridade(),
                quadrinho.getUrlImagem(),
                inventario.getQuantidade(),
                inventario.getPrecoUnitario(),
                valorTotal,
                inventario.getCreatedAt(),
                inventario.getUpdatedAt()
        );
    }
}
