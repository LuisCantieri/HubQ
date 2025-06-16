// src/main/java/gestao/quadrinhos/controllers/QuadrinhoController.java
package gestao.quadrinhos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestao.quadrinhos.dto.PrecoDTO;
import gestao.quadrinhos.dto.QuadrinhoDTO;
import gestao.quadrinhos.services.QuadrinhoService;

@RestController
@RequestMapping("/api/quadrinhos")
@CrossOrigin(origins = "https://hub-q.vercel.app",
             allowedHeaders = {"Authorization","Content-Type"},
             exposedHeaders = "Authorization",
             allowCredentials = "true")
public class QuadrinhoController {

    private final QuadrinhoService service;
    public QuadrinhoController(QuadrinhoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<QuadrinhoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuadrinhoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<QuadrinhoDTO> criar(@RequestBody QuadrinhoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuadrinhoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody QuadrinhoDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/preco")
    public ResponseEntity<QuadrinhoDTO> atualizarPreco(
            @PathVariable Long id,
            @RequestBody PrecoDTO dto
    ) {
        return ResponseEntity.ok(
            service.updatePreco(id, dto.getNovoPreco())
        );
    }
}
