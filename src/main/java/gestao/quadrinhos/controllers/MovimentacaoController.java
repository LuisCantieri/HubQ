// src/main/java/gestao/quadrinhos/controllers/MovimentacaoController.java
package gestao.quadrinhos.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gestao.quadrinhos.dto.MovimentacaoDTO;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.services.MovimentacaoService;

@RestController
@RequestMapping("/api/movimentacoes")
@CrossOrigin(
  origins = "http://localhost:5173",
  allowedHeaders = {"Authorization", "Content-Type"},
  exposedHeaders = "Authorization",
  allowCredentials = "true"
)
public class MovimentacaoController {

    private final MovimentacaoService service;
    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoDTO>> getAll(
      @RequestParam(value="inicio", required=false)
      @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date inicio,
      @RequestParam(value="fim", required=false)
      @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date fim
    ) {
      List<MovimentacaoDTO> list = (inicio!=null||fim!=null)
        ? service.getMovimentacoesByPeriodo(inicio,fim)
        : service.getAllMovimentacoes();
      return ResponseEntity.ok(list);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoDTO>> byTipo(
      @PathVariable StatusMovimentacao tipo
    ) {
      return ResponseEntity.ok(service.getMovimentacoesByTipo(tipo));
    }

    @GetMapping("/quadrinho/{id}")
    public ResponseEntity<List<MovimentacaoDTO>> byQuadrinho(
      @PathVariable Long id
    ) {
      return ResponseEntity.ok(service.getMovimentacoesByQuadrinhoId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
      return service.deleteMovimentacao(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
    }
}
