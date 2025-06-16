// src/main/java/gestao/quadrinhos/controllers/DashboardController.java
package gestao.quadrinhos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestao.quadrinhos.dto.MovimentacaoDTO;
import gestao.quadrinhos.services.InventarioService;
import gestao.quadrinhos.services.MovimentacaoService;

@CrossOrigin(origins = "https://hub-q.vercel.app")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private InventarioService inventoryService;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalValue", inventoryService.getTotalInventoryValue());
        stats.put("productsInStock", inventoryService.getProductsInStockCount());

        List<MovimentacaoDTO> recentMovements = movimentacaoService.getAllMovimentacoes()
                .stream()
                .limit(10)
                .toList();
        stats.put("recentMovements", recentMovements);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getInventorySummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("inventory", inventoryService.getUserInventoryWithStock());
        summary.put("totalValue", inventoryService.getTotalInventoryValue());
        summary.put("totalProducts", inventoryService.getProductsInStockCount());
        return ResponseEntity.ok(summary);
    }
}
