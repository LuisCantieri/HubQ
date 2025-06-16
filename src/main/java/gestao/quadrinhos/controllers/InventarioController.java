package gestao.quadrinhos.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestao.quadrinhos.dto.InventarioCreateDTO;
import gestao.quadrinhos.dto.InventarioResponseDTO;
import gestao.quadrinhos.services.InventarioService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/inventory")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventory() {
        return ResponseEntity.ok(inventarioService.getUserInventory());
    }

    @GetMapping("/stock")
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventoryWithStock() {
        return ResponseEntity.ok(inventarioService.getUserInventoryWithStock());
    }

    @PostMapping("/add")
    public ResponseEntity<InventarioResponseDTO> addProductToInventory(
            @Valid @RequestBody InventarioCreateDTO dto) {
        return ResponseEntity.ok(inventarioService.addProductToInventory(dto));
    }

    @PutMapping("/price/{quadrinhoId}")
    public ResponseEntity<InventarioResponseDTO> updateProductPrice(
            @PathVariable Long quadrinhoId,
            @RequestBody Map<String, Object> body) {

        if (!body.containsKey("novoPreco")) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal novoPreco;
        try {
            novoPreco = new BigDecimal(body.get("novoPreco").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(inventarioService.updateProductPrice(quadrinhoId, novoPreco));
    }

    @PostMapping("/remove/{quadrinhoId}")
    public ResponseEntity<String> removeProductFromInventory(
            @PathVariable Long quadrinhoId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String description) {

        inventarioService.removeProductFromInventory(quadrinhoId, quantity, description);
        return ResponseEntity.ok("Produto removido com sucesso.");
    }

    @PutMapping("/adjust/{quadrinhoId}")
    public ResponseEntity<String> adjustInventory(
            @PathVariable Long quadrinhoId,
            @RequestBody Map<String, Object> body) {

        Integer newQuantity;
        try {
            newQuantity = (Integer) body.get("newQuantity");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Quantidade inválida.");
        }

        String description = (String) body.getOrDefault("description", "Ajuste de estoque");
        inventarioService.adjustInventory(quadrinhoId, newQuantity, description);
        return ResponseEntity.ok("Inventário ajustado com sucesso.");
    }

    @GetMapping("/value")
    public ResponseEntity<Double> getTotalInventoryValue() {
        return ResponseEntity.ok(inventarioService.getTotalInventoryValue());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getProductsInStockCount() {
        return ResponseEntity.ok(inventarioService.getProductsInStockCount());
    }

    @DeleteMapping("/delete/{quadrinhoId}")
    public ResponseEntity<String> deleteProductFromInventory(@PathVariable Long quadrinhoId) {
        inventarioService.deleteProductFromInventory(quadrinhoId);
        return ResponseEntity.ok("Produto removido permanentemente do inventário.");
    }
}
