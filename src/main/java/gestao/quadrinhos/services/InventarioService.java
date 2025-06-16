// src/main/java/gestao/quadrinhos/services/InventarioService.java
package gestao.quadrinhos.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import gestao.quadrinhos.dto.InventarioCreateDTO;
import gestao.quadrinhos.dto.InventarioResponseDTO;
import gestao.quadrinhos.dto.QuadrinhoDTO;
import gestao.quadrinhos.entities.Inventario;
import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.InventarioRepository;
import gestao.quadrinhos.repositories.QuadrinhoRepository;
import gestao.quadrinhos.repositories.UserRepository;
import gestao.quadrinhos.repositories.MovimentacaoRepository;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.entities.Movimentacao;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final QuadrinhoRepository quadrinhoRepository;
    private final UserRepository userRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public InventarioService(InventarioRepository inventarioRepository,
                             QuadrinhoRepository quadrinhoRepository,
                             UserRepository userRepository,
                             MovimentacaoRepository movimentacaoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.quadrinhoRepository = quadrinhoRepository;
        this.userRepository = userRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado"));
    }

    public List<InventarioResponseDTO> getUserInventory() {
        User user = getCurrentUser();
        return inventarioRepository.findByUser(user).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<InventarioResponseDTO> getUserInventoryWithStock() {
        User user = getCurrentUser();
        return inventarioRepository.findByUserAndQuantidadeGreaterThan(user, 0).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public InventarioResponseDTO addProductToInventory(InventarioCreateDTO dto) {
        User user = getCurrentUser();
        Quadrinho q = quadrinhoRepository.findById(dto.getQuadrinhoId())
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        Inventario inv = inventarioRepository.findByUserAndQuadrinho(user, q)
            .orElseGet(() -> new Inventario(user, q, dto.getQuantidade(),
                dto.getPrecoUnitario() != null ? dto.getPrecoUnitario() : q.getPreco()));
        inv.setQuantidade(inv.getQuantidade() + dto.getQuantidade());
        if (dto.getPrecoUnitario() != null) inv.setPrecoUnitario(dto.getPrecoUnitario());
        inv.setUpdatedAt(new Date());
        inv = inventarioRepository.save(inv);
        movimentacaoRepository.save(new Movimentacao(
            user, q, StatusMovimentacao.ENTRADA, dto.getQuantidade(), dto.getRaridade()
        ));
        return toDto(inv);
    }

    @Transactional
    public InventarioResponseDTO updateProductPrice(Long quadrinhoId, BigDecimal novoPreco) {
        User user = getCurrentUser();
        Quadrinho q = quadrinhoRepository.findById(quadrinhoId)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        Inventario inv = inventarioRepository.findByUserAndQuadrinho(user, q)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado no inventário"));
        inv.setPrecoUnitario(novoPreco);
        inv.setUpdatedAt(new Date());
        inv = inventarioRepository.save(inv);
        return toDto(inv);
    }

    @Transactional
    public void removeProductFromInventory(Long quadrinhoId, Integer quantity, String description) {
        User user = getCurrentUser();
        Quadrinho q = quadrinhoRepository.findById(quadrinhoId)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        Inventario inv = inventarioRepository.findByUserAndQuadrinho(user, q)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado no inventário"));
        if (inv.getQuantidade() < quantity) {
            throw new IllegalArgumentException("Quantidade insuficiente");
        }
        inv.setQuantidade(inv.getQuantidade() - quantity);
        inv.setUpdatedAt(new Date());
        inventarioRepository.save(inv);
        movimentacaoRepository.save(new Movimentacao(
            user, q, StatusMovimentacao.SAIDA, quantity, description
        ));
    }

    @Transactional
    public void adjustInventory(Long quadrinhoId, Integer newQuantity, String description) {
        User user = getCurrentUser();
        Quadrinho q = quadrinhoRepository.findById(quadrinhoId)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        Inventario inv = inventarioRepository.findByUserAndQuadrinho(user, q)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado no inventário"));
        int oldQty = inv.getQuantidade();
        inv.setQuantidade(newQuantity);
        inv.setUpdatedAt(new Date());
        inventarioRepository.save(inv);
        movimentacaoRepository.save(new Movimentacao(
            user, q, StatusMovimentacao.AJUSTE, newQuantity - oldQty,
            description != null ? description : "Ajuste de estoque"
        ));
    }

    public double getTotalInventoryValue() {
        User user = getCurrentUser();
        return inventarioRepository.findByUser(user).stream()
            .map(inv -> inv.getPrecoUnitario().multiply(BigDecimal.valueOf(inv.getQuantidade())))
            .mapToDouble(BigDecimal::doubleValue)
            .sum();
    }

    public long getProductsInStockCount() {
        return inventarioRepository.countByUserAndQuantidadeGreaterThan(getCurrentUser(), 0);
    }

    @Transactional
    public void deleteProductFromInventory(Long quadrinhoId) {
        User user = getCurrentUser();
        Quadrinho q = quadrinhoRepository.findById(quadrinhoId)
            .orElseThrow(() -> new EntityNotFoundException("Quadrinho não encontrado"));
        Inventario inv = inventarioRepository.findByUserAndQuadrinho(user, q)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado no inventário"));
        int qty = inv.getQuantidade();
        inventarioRepository.delete(inv);
        movimentacaoRepository.save(new Movimentacao(
            user, q, StatusMovimentacao.SAIDA, qty, "Remoção permanente do inventário"
        ));
    }

    private InventarioResponseDTO toDto(Inventario inv) {
        QuadrinhoDTO dto = new QuadrinhoDTO(inv.getQuadrinho());
        BigDecimal total = inv.getPrecoUnitario().multiply(BigDecimal.valueOf(inv.getQuantidade()));
        return new InventarioResponseDTO(
            inv.getId(),
            inv.getQuantidade(),
            dto,
            total,
            inv.getCreatedAt(),
            inv.getUpdatedAt()
        );
    }
}
