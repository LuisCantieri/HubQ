package gestao.quadrinhos.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestao.quadrinhos.dto.InventarioCreateDTO;
import gestao.quadrinhos.dto.InventarioResponseDTO;
import gestao.quadrinhos.services.InventarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")

public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventory() {
        List<InventarioResponseDTO> inventory = inventarioService.getUserInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/stock")
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventoryWithStock() {
        List<InventarioResponseDTO> inventory = inventarioService.getUserInventoryWithStock();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToInventory(@Valid @RequestBody InventarioCreateDTO dto) {
        try {
            InventarioResponseDTO response = inventarioService.addProductToInventory(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    @PostMapping("/remove/{quadrinhoId}")
    public ResponseEntity<?> removeProductFromInventory(
            @PathVariable Long quadrinhoId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String description) {
        try {
            inventarioService.removeProductFromInventory(quadrinhoId, quantity, description);
            return ResponseEntity.ok("Produto removido com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao remover produto: " + e.getMessage());
        }
    }

    @PutMapping("/adjust/{quadrinhoId}")
    public ResponseEntity<?> adjustInventory(
            @PathVariable Long quadrinhoId,
            @RequestBody Map<String, Object> request) {

        try {
            if (!request.containsKey("newQuantity")) {
                return ResponseEntity.badRequest().body("Campo 'newQuantity' é obrigatório");
            }

            Integer newQuantity;
            try {
                newQuantity = (Integer) request.get("newQuantity");
            } catch (ClassCastException e) {
                return ResponseEntity.badRequest().body("Campo 'newQuantity' deve ser um número inteiro");
            }

            if (newQuantity < 0) {
                return ResponseEntity.badRequest().body("Quantidade não pode ser negativa");
            }

            String description = (String) request.get("description");

            inventarioService.adjustInventory(quadrinhoId, newQuantity, description);
            return ResponseEntity.ok("Inventário ajustado com sucesso");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao ajustar inventário: " + e.getMessage());
        }
    }

    @GetMapping("/value")
    public ResponseEntity<Double> getTotalInventoryValue() {
        Double totalValue = inventarioService.getTotalInventoryValue();
        return ResponseEntity.ok(totalValue);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getProductsInStockCount() {
        Long count = inventarioService.getProductsInStockCount();
        return ResponseEntity.ok(count);
    }

    /**
     * DELETE: remove completamente o produto do inventário do usuário autenticado
     */
    @DeleteMapping("/delete/{quadrinhoId}")
    public ResponseEntity<?> deleteProductFromInventory(@PathVariable Long quadrinhoId) {
        try {
            inventarioService.deleteProductFromInventory(quadrinhoId);
            return ResponseEntity.ok("Produto removido permanentemente do inventário.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao excluir produto: " + e.getMessage());
        }
    }
}
